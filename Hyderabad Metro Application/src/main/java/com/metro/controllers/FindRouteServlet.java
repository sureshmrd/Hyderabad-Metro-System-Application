package com.metro.controllers;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/FindRouteServlet")
public class FindRouteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Edge class to represent graph edges
    class Edge {
        int destination;
        double distance;

        Edge(int destination, double distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startStationId = request.getParameter("sourceStationId");
        String endStationId = request.getParameter("destinationStationId");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (startStationId == null || endStationId == null) {
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Invalid request: Start and End stations are required.");
            response.getWriter().write(errorJson.toString());
            return;
        }

        try {
            int source = Integer.parseInt(startStationId);
            int destination = Integer.parseInt(endStationId);

            System.out.println("Source ID: " + source);
            System.out.println("Destination ID: " + destination);

            // Fetch Metro Data
            Map<Integer, String> stationNames = new HashMap<>();
            Map<Integer, List<Edge>> graph = new HashMap<>();
            fetchMetroData(stationNames, graph);

            // 🚨 **Check if Source and Destination Exist in the Graph**
            if (!graph.containsKey(source)) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Source station ID " + source + " not found in database.");
                response.getWriter().write(errorJson.toString());
                return;
            }
            if (!graph.containsKey(destination)) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Destination station ID " + destination + " not found in database.");
                response.getWriter().write(errorJson.toString());
                return;
            }

            // Find the shortest path
            List<Integer> shortestPath = dijkstra(source, destination, graph);
            if (shortestPath.isEmpty()) {
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "No route found between the selected stations.");
                response.getWriter().write(errorJson.toString());
                return;
            }

            // Convert the shortest path to station names
            List<String> stationRoute = new ArrayList<>();
            for (int station : shortestPath) {
                if (!stationNames.containsKey(station)) {
                    JSONObject errorJson = new JSONObject();
                    errorJson.put("error", "Station ID " + station + " found in route but missing from database.");
                    response.getWriter().write(errorJson.toString());
                    return;
                }
                stationRoute.add(stationNames.get(station));
            }

            // Create JSON response
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("route", stationRoute);
            response.getWriter().write(jsonResponse.toString());

        } catch (NumberFormatException e) {
            e.printStackTrace();
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Invalid station ID format. Please select valid stations.");
            response.getWriter().write(errorJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Internal server error: " + e.getMessage());
            response.getWriter().write(errorJson.toString());
        }
    }



    private void fetchMetroData(Map<Integer, String> stationNames, Map<Integer, List<Edge>> graph) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to Database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "c##project", "project");

            // Fetch all stations
            stmt = conn.prepareStatement("SELECT station_id, station_name FROM metro_stations");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int stationId = rs.getInt("station_id");
                stationNames.put(stationId, rs.getString("station_name"));

                // 🔥 Ensure every station is initialized in the graph
                graph.putIfAbsent(stationId, new ArrayList<>());
            }
            rs.close();
            stmt.close();

            // Fetch all routes based on Distance
            stmt = conn.prepareStatement("SELECT from_station, to_station, distance_km FROM metro_routes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int from = rs.getInt("from_station");
                int to = rs.getInt("to_station");
                double distance = rs.getDouble("distance_km");

                // 🚨 Avoid NullPointerException by ensuring stations exist
                graph.putIfAbsent(from, new ArrayList<>());
                graph.putIfAbsent(to, new ArrayList<>());

                graph.get(from).add(new Edge(to, distance));
                graph.get(to).add(new Edge(from, distance)); // Bidirectional graph
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }


    private List<Integer> dijkstra(int source, int destination, Map<Integer, List<Edge>> graph) {
        if (!graph.containsKey(source) || !graph.containsKey(destination)) {
            return Collections.emptyList(); // No path if source or destination is missing
        }

        Map<Integer, Double> distance = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));

        // 🔥 Initialize distances
        for (int station : graph.keySet()) {
            distance.put(station, Double.MAX_VALUE);
        }
        distance.put(source, 0.0);
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int station = current[0];

            if (station == destination) break;

            // 🚨 Avoid NullPointerException
            if (!graph.containsKey(station) || graph.get(station) == null) continue;

            for (Edge edge : graph.get(station)) {
                if (!distance.containsKey(edge.destination)) {
                    distance.put(edge.destination, Double.MAX_VALUE);
                }

                double newDist = distance.get(station) + edge.distance;
                if (newDist < distance.get(edge.destination)) {
                    distance.put(edge.destination, newDist);
                    previous.put(edge.destination, station);
                    pq.add(new int[]{edge.destination, (int) newDist});
                }
            }
        }

        // Construct path
        List<Integer> path = new ArrayList<>();
        for (Integer at = destination; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        if (path.size() == 1 && !path.contains(source)) {
            return Collections.emptyList(); // No valid route found
        }

        return path;
    }


}

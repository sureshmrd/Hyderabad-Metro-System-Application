package com.metro.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.metro.utils.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MetroStationServlet
 */
@WebServlet("/metroStation")
public class MetroStationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MetroStationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();
        JSONArray stationsArray = new JSONArray();

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT station_id, station_name, latitude, longitude, line_id FROM metro_stations";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            System.out.println(rs.getFetchSize());

            while (rs.next()) {
                JSONObject station = new JSONObject();
                station.put("station_id", rs.getInt("station_id"));
                station.put("station_name", rs.getString("station_name"));
                station.put("latitude", rs.getDouble("latitude"));
                station.put("longitude", rs.getDouble("longitude"));
                station.put("line_id", rs.getInt("line_id"));
                stationsArray.put(station);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pw.print(stationsArray.toString()); // Ensure JSON is printed properly
        pw.flush();
    }
}

# Hyderabad Metro System Application

## 📌 Idea of the Application
The **Hyderabad Metro System Application** is a web-based project that provides users with essential metro-related information, including station details, metro line categorization, and the shortest route between two selected stations. The application dynamically calculates the shortest path between metro stations using **Dijkstra's Algorithm**, helping users navigate efficiently.

## 🚀 Implementation Tech Stack

- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Servlets, JSP
- **Database**: Oracle DB Server
- **Server**: In-built Tomcat Server
- **IDE**: Eclipse IDE
- **Algorithm for Finding Shortest Path**: Dijkstra's Algorithm
- **Map Visualization**: OpenStreetMap and Leaflet.js

## 🎯 Features

- 📍 Users can view the list of **all metro stations** in Hyderabad.
- 🌈 Metro stations are **categorized based on their respective lines** (Red, Green, and Blue lines).
- 🔍 Users can select **source and destination stations** to find the **shortest travel route** between them.
- 🗺️ **Map Visualization**: While selecting stations and viewing the shortest path, users will see a **real-time map representation** implemented using **OpenStreetMap and Leaflet.js**.

## 🔄 Implementation Flow (Shortest Path Calculation using Dijkstra's Algorithm)

1. Users select the **source and destination stations** from a dropdown list of metro stations.
2. The system applies **Dijkstra's Algorithm** to determine the shortest path based on the **distance between stations**.
3. The shortest route is displayed on the **interactive map**, providing a clear travel visualization.

## 📌 How to Run the Application

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/hyderabad-metro-app.git
   ```
2. Open **Eclipse IDE** and import the project as a **Dynamic Web Project**.
3. Configure **Tomcat Server** within Eclipse.
4. Set up the **Oracle Database**, including the metro station data.
5. Deploy the project on Tomcat and run the application.

## 💡 Future Enhancements
- 🗺️ Enhancing **real-time metro map visualization** with additional details.
- 📍 Providing nearby landmark suggestions for each metro station.
- 📊 Adding fare calculation based on distance.

---
**🔗 Connect & Contribute:** If you’d like to enhance this project, feel free to fork, contribute, and raise PRs!

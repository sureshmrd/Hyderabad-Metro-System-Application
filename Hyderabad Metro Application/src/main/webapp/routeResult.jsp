<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Route Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff; /* Light blue background */
            text-align: center;
            padding: 20px;
        }
        h2 {
            color: #333;
        }
        p {
            font-size: 18px;
            font-weight: bold;
        }
        a {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 15px;
            font-size: 16px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background: #0056b3;
        }
    </style>
    <script>
        window.onload = function () {
            let routeData = localStorage.getItem("shortestRoute");
            if (routeData) {
                let route = JSON.parse(routeData);
                document.getElementById("routeDisplay").innerHTML = route.join(" → ");
            } else {
                document.getElementById("routeDisplay").innerHTML = "<span style='color:red;'>No route found.</span>";
            }
        };
    </script>
</head>
<body>
    <h2>Shortest Metro Route</h2>
    <p id="routeDisplay"></p>
    <br><a href="route.html">Go Back</a>
</body>
</html>

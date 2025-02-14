document.addEventListener("DOMContentLoaded", function () {
    fetchMetroRoutes();
    setupDarkModeToggle();
});

/* Fetch Metro Stations & Group by Lines */
function fetchMetroRoutes() {
    fetch("http://localhost:8082/Hyderabad_Metro_Application/metroStation")
    .then(response => response.json())
    .then(data => {
        let metroLines = {};
        data.forEach(station => {
            if (!metroLines[station.line_id]) {
                metroLines[station.line_id] = [];
            }
            metroLines[station.line_id].push(station.station_name);
        });

        displayMetroRoutes(metroLines);
    })
    .catch(error => console.error("Error fetching metro stations:", error));
}

/* Display Metro Routes */
function displayMetroRoutes(metroLines) {
    let container = document.getElementById("metro-lines");
    container.innerHTML = "";

    for (let line in metroLines) {
        let lineDiv = document.createElement("div");
        lineDiv.classList.add("metro-line");

        // Assign color based on line ID
        if (line == "1") {
            lineDiv.classList.add("red-line");
            lineDiv.innerHTML = "🔴 Red Line: " + metroLines[line].join(" → ");
        } else if (line == "2") {
            lineDiv.classList.add("blue-line");
            lineDiv.innerHTML = "🔵 Blue Line: " + metroLines[line].join(" → ");
        } else if (line == "3") {
            lineDiv.classList.add("green-line");
            lineDiv.innerHTML = "🟢 Green Line: " + metroLines[line].join(" → ");
        } else {
            lineDiv.innerHTML = "⚪ Line " + line + ": " + metroLines[line].join(" → ");
        }

        container.appendChild(lineDiv);
    }
}

/* Dark Mode Toggle */
function setupDarkModeToggle() {
    let toggle = document.getElementById("darkModeToggle");

    // Load previous dark mode setting
    if (localStorage.getItem("dark-mode") === "enabled") {
        document.body.classList.add("dark-mode");
        toggle.checked = true;
    }

    toggle.addEventListener("change", function () {
        if (toggle.checked) {
            document.body.classList.add("dark-mode");
            localStorage.setItem("dark-mode", "enabled");
        } else {
            document.body.classList.remove("dark-mode");
            localStorage.setItem("dark-mode", "disabled");
        }
    });
}

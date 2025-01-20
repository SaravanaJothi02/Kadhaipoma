const BASE_URL = "http://localhost:8080/chat";
const headers = { "Content-Type": "application/json" };

async function login() {
    const email = document.getElementById('mail').value;
    const password = document.getElementById('password').value;
    const credentials = { mail: email, pass: password };

    try {
        const response = await fetch(`${BASE_URL}/login`, {
            method: "POST", // PUT, DELETE, PATCH, HEAD, OPTIONS
            headers, // Content-Type, Authorization
            body: JSON.stringify(credentials)
        });
        const result = await response.json();

        if (response.ok) {
            alert(result.message);
            sessionStorage.setItem("userId", result.userId);
            window.location = "../chat-window/index.html";
        } else {
            alert(result.message);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred. Please try again later.");
    }
}
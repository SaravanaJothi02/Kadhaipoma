async function register() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    if (password !== confirmPassword) {
        alert("Password and confirm password do not match");
        document.getElementById("password").value = "";
        document.getElementById("confirm-password").value = "";
        return;
    }
    const user = { name: username, pass: password };
    try {
        const resp = await fetch("http://localhost:8080/chat/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user),
        });
        const message = await resp.json();
        if (resp["status"] == 200) {
            alert(message["message"]);
            sessionStorage.setItem("userId", message["userId"]);
            window.location = "../chat-window/index.html";
        } else {
            alert(message["message"]);
        }
    } catch (e) {
        console.error("Error:", e);
        alert("An error occurred. Please try again later.");
    }
}

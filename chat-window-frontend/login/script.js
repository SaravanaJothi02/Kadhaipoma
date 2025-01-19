async function login(){
    const userMail = document.getElementById('mail').value;
    const passwrod = document.getElementById('password').value;
    const json = {mail: userMail, pass: passwrod};
    try {
        const resp = await fetch("http://localhost:8080/chat/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(json)
        });
        const res = await resp.json();
        if (resp.ok) {
            alert(res["message"]);
            sessionStorage.setItem("userId", res["userId"]);
            window.location = "../chat-window/index.html";
        } else {
            alert(res["message"]);
        }
    } catch (e) {
        console.error("Error:", e);
        alert("An error occurred. Please try again later.");
    }
}
const BASE_URL = "http://localhost:8080/chat";

async function register() {
    const otp = document.getElementById("otp").value;
    const email = document.getElementById("email").value;
    if(!verifyOTP(email, otp)){
        return;
    }
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    if (password !== confirmPassword) {
        alert("Password and confirm password do not match");
        document.getElementById("password").value = "";
        document.getElementById("confirm-password").value = "";
        return;
    }
    const user = { name: username, pass: password, mail:email };
    try {
        const resp = await fetch(`${BASE_URL}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });
        const message = await resp.json();
        if (resp.ok) {
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

async function verifyOTP(email, otp) {
    if(otp == "" || otp == null){
        alert("enter OTP");
        return false;
    }
    try {
        const req = await fetch(`${BASE_URL}/verifyOtp`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ 
                OTP : otp,
                mail : email
            })
        });
        const resp = await req.json();
        if(req.ok){
            alert(resp["message"]);
            return true;
        } else {
            alert(resp["error"]);
            return false;
        }
    } catch (err) {
        console.log(err);
    }
    return false;
}

async function getOTP() {
    const email = document.getElementById("email").value;
    const strictEmailRegex = /^[a-zA-Z0-9.]+@[a-zA-Z]+\.[a-zA-Z]{2,}$/;
    if (strictEmailRegex.test(email)) {
        try {
            const req = await fetch(`${BASE_URL}/generateOtp`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ mail: email }),
            });
            const resp = await req.json();
            if (req.ok) {
                document.getElementById("register-btn").disabled = false;
                document.querySelector(".otp-feild").style =
                    "visibility: visible";
                alert(resp["message"]);
            } else {
                alert(resp["error"]);
            }
        } catch (error) {
            console.log(error);
        }
    } else {
        alert("Email is Invalid...");
    }
}



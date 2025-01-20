const BASE_URL = "http://localhost:8080/chat";
const headers = { "Content-Type": "application/json" };

async function register() {
    const otp = document.getElementById("otp").value;
    const email = document.getElementById("email").value;
    if (!await verifyOTP(email, otp)) {
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

    const user = { name: username, pass: password, mail: email };

    try {
        const response = await fetch(`${BASE_URL}/register`, {
            method: "POST",
            headers,
            body: JSON.stringify(user),
        });
        const message = await response.json();

        if (response.ok) {
            alert(message.message);
            sessionStorage.setItem("userId", message.userId);
            window.location = "../chat-window/index.html";
        } else {
            alert(message.message);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred. Please try again later.");
    }
}

async function verifyOTP(email, otp) {
    if (!otp) {
        alert("Enter OTP");
        return false;
    }

    try {
        const response = await fetch(`${BASE_URL}/verifyOtp`, {
            method: "POST",
            headers,
            body: JSON.stringify({ OTP: otp, mail: email }),
        });
        const result = await response.json();

        if (response.ok) {
            alert(result.message);
            document.querySelector(".otp-field").style.display = "block";
            return true;
        } else {
            alert(result.error);
            return false;
        }
    } catch (error) {
        console.error("Error verifying OTP:", error);
        return false;
    }
}

async function getOTP() {
    const email = document.getElementById("email").value;
    const strictEmailRegex = /^[a-zA-Z0-9.]+@[a-zA-Z]+\.[a-zA-Z]{2,}$/;

    if (!strictEmailRegex.test(email)) {
        alert("Email is Invalid...");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/generateOtp`, {
            method: "POST",
            headers,
            body: JSON.stringify({ mail: email }),
        });
        const result = await response.json();

        if (response.ok) {
            document.getElementById("register-btn").disabled = false;
            document.querySelector(".otp-field").style.display = "block";
            alert(result.message);
        } else {
            alert(result.error);
        }
    } catch (error) {
        console.error("Error generating OTP:", error);
    }
}

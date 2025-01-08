function register(){
    const username = document.getElementById('username').value;
    const passwrod = document.getElementById('password').value;
    const confrimPassword = document.getElementById('confirm-password').value;
    if(passwrod !== confrimPassword){
        alert("password and confirm password does not match");
        document.getElementById('password').value = "";
        document.getElementById('confirm-password').value = "";
        return;
    }
    const user = {name:username, pass:passwrod};
    fetch("", {
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        
    })
}
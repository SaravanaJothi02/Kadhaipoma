const db = {
    contactlist : [
        {
            name:"Nagarajan",
            id:"1",
            passwrod : "Nagarajan"
        },
        {
            name : "Sukumar",
            id:"2",
            passwrod : "Sukumar"
        },
        {
            name:"Krishnamoorthy",
            id:"3",
            passwrod:"Krishnamoorthy"
        },
        {
            name:"Karthi",
            id:"4",
            passwrod:"Karthi"
        },
        {
            name:"Hari",
            id:"5",
            passwrod:"Hari"
        },
        {
            name:"Rishi",
            id:"6",
            passwrod:"Rishi"
        },
        {
            name:"Gowtham",
            id:"7",
            passwrod:"Gowtham"
        },
        {
            name:"Subi",
            id:"8",
            passwrod:"Subi"
        },
        {
            name:"SJ",
            id:"9",
            passwrod:"SJ"
        },
        {
            name:"Harini",
            id:"10",
            passwrod:"Harini"
        }
    ]
}



function login(){
    const username = document.getElementById('username').value;
    const passwrod = document.getElementById('password').value;
    db.contactlist.forEach(user => {
        if(user.name === username && user.passwrod === passwrod){
            console.log("credential are correct")
            sessionStorage.setItem("userId", user.id);
            alert("login successfull")
            window.location = "../chat-window/index.html"
            return;
        }
    })
    alert("username or password are incorrect...");
}
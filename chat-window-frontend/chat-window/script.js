const json = {
    contactlist: [],
    messages: [],
    requestList: []
};

let totalMessage = json.messages.length;

let userId = sessionStorage.getItem("userId");
let receiverId = null;
const ws = new WebSocket(`ws://localhost:8080/chat/${userId}`);

ws.onopen = () => {
    console.log("Connection established...");
    loadContact();
};

ws.onmessage = (event) => {
    const message = event.data;
    console.log("message ==> "+message);
    if(message.startsWith("friend request")){
        addFriendRequest(message.split(":"));
    } else if(message.startsWith("add contact")){
        const contacts = document.querySelector(".contact-list");
        const child = document.createElement("li");
        child.className = "contact-item";
        child.setAttribute("data-id", message.split(":")[1]);
        child.textContent = message.split(":")[2];
        contacts.appendChild(child);
        json.contactlist.push({
            name : message.split(":")[2],
            id : message.split(":")[1]
        });
    }
    const messagesDiv = document.getElementById("messages");
    messagesDiv.innerHTML += `<p>${message}</p>`;
    const msg = event.data.split(":");
    json.messages.push({
        msgId: `${++totalMessage}`,
        senderId: `${msg[0]}`,
        receiverId: `${msg[1]}`,
        msgContent: `${msg[2]}`,
    });
    // console.log(msg);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
    loadChat();
};

ws.onclose = () => {
    console.log("Disconnected...");
};

async function loadContact() {
    // console.log(userId);
    const contacts = document.querySelector(".contact-list");
    const contactlist = json.contactlist;

    try {
        const resp = await fetch(`http://localhost:8080/chat/get-contact-list?userId=${userId}`);
    
        const contactList = await resp.json();
    
        contactList.forEach(user => {
            json.contactlist.push({
                name : user["name"],
                id : user["id"]
            });
        })
    } catch (error) {
        alert("contact fetch error...")
    }

    contactlist.forEach((contact) => {
        if (contact.id !== userId) {
            const child = document.createElement("li");
            child.className = "contact-item";
            child.setAttribute("data-id", contact.id);
            child.textContent = contact.name;
            contacts.appendChild(child);
        }
    });
    setupContactListeners();
}

function sendMessage() {
    const input = document.getElementById("message-input");
    const msg = `${userId}:${receiverId}:${input.value}`;
    json.messages.push({
        msgId: `${++totalMessage}`,
        senderId: `${userId}`,
        receiverId: `${receiverId}`,
        msgContent: `${input.value}`,
    });
    const messageContainer = document.querySelector(".messages");
    const child = document.createElement("li");
    child.className =
        msg.senderId == userId ? "message received" : "message sent";
    child.textContent = input.value;
    messageContainer.appendChild(child);
    ws.send(msg);
    input.value = "";
}

function setupContactListeners() {
    const contacts = document.querySelectorAll(".contact-item");
    contacts.forEach((contact) => {
        contact.addEventListener("click", () => {
            document.querySelector("#messages").style = "visibility: visible";
            document.querySelector(".message-input-container").style =
                "visibility: visible";
            receiverId = contact.getAttribute("data-id");
            highlightSelectedContact(contact);

            // console.log(`Chatting with User ID: ${receiverId}`);
        });
    });
}

function highlightSelectedContact(selectedContact) {
    const contacts = document.querySelectorAll(".contact-item");
    contacts.forEach((contact) => {
        contact.classList.remove("selected");
    });
    selectedContact.classList.add("selected");
    loadChat();
}

function loadChat() {
    const messages = json.messages.filter(
        (msg) =>
            (msg.senderId == userId && msg.receiverId == receiverId) ||
            (msg.senderId == receiverId && msg.receiverId == userId)
    );
    const messageContainer = document.querySelector(".messages");
    messageContainer.replaceChildren();
    messages.forEach((msg) => {
        const child = document.createElement("li");
        if (msg.senderId == userId) {
            child.className = "message sent";
        } else if (msg.receiverId == userId) {
            child.className = "message received";
        }
        child.textContent = msg.msgContent;
        messageContainer.appendChild(child);
    });
}

function showContact(){
    document.querySelector(".notification").style = "visibility: hidden;";
    document.querySelector(".add-friends").style = "visibility: hidden;";
    document.querySelector(".contact-list").style = "visibility: visible;";
}
function showNotification(){
    document.querySelector(".notification").style = "visibility: visible;";
    document.querySelector(".add-friends").style = "visibility: hidden;";
    document.querySelector(".contact-list").style = "visibility: hidden;";
}
function search(){
    document.querySelector(".notification").style = "visibility: hidden;";
    document.querySelector(".add-friends").style = "visibility: visible;";
    document.querySelector(".contact-list").style = "visibility: hidden;";
}

async function searchUser() {
    console.log("search called");
    const response = await fetch(`http://localhost:8080/chat/search?key=${document.getElementById('search').value}`);

    const list = await response.json();
    console.log(list);

    let searchList = document.querySelector('.search-list');
    searchList.replaceChildren();
    let frdId = [];
    json.contactlist.forEach(u => frdId.push(u.id));
    list.forEach(user => {
        if(userId != user["id"] && !frdId.includes(user["id"])){
            const child = document.createElement("li");
            const name = document.createElement('span');
            const btn = document.createElement('button');
            child.className = "search-contact-item contact-item";
            child.setAttribute("data-id", user["id"]);
            name.textContent = user["name"];
            btn.innerText = (json.requestList.includes(user["id"])) ? "~": "req";
            btn.addEventListener('click', async () => {
                console.log("req");
                try {
                    const req = await fetch("http://localhost:8080/chat/send-request", {
                        method : "POST",
                        headers : {"Content-Type" : "application/json"},
                        body : JSON.stringify({
                            uId : userId,
                            fId : user["id"]
                        })
                    });
                    const resp = await req.json();
                    if(resp["status"] == 200){
                        json.requestList.push(user["id"]);
                    } else {
                        alert("Internal Error")
                    }  
                } catch (error) {
                    alert(error)
                }
            });
            console.log(child);
            child.appendChild(name);
            child.appendChild(btn);
            searchList.appendChild(child);
        }
    })
}

function addFriendRequest(req){
    const senderId = req[1];
    const name = req[2];
    const noti = document.querySelector('.notification');
    const child = document.createElement("li");
    child.className = "contact-item";
    child.setAttribute("data-id", senderId);
    const n = document.createElement('span');
    n.innerText = name;
    const abtn = document.createElement('button');
    const rbtn = document.createElement('button');
    abtn.innerText = "a";
    rbtn.innerText = "r";
    abtn.addEventListener('click', async (event) => {
        try {
            const req = await fetch("http://localhost:8080/chat/friend-request-accept", {
                method : "POST",
                headers : {"Content-Type":"application/json"},
                body : JSON.stringify({
                    uId : userId,
                    fId : senderId,
                    status : "accept"
                })
            })
            const resp = await req.json();
            if(resp["status"] == 200){
                alert("friend added successfully...");
                const contacts = document.querySelector(".contact-list");
                const contactsChild = document.createElement("li");
                contactsChild.className = "contact-item";
                contactsChild.setAttribute("data-id", senderId);
                contactsChild.textContent = name;
                contacts.appendChild(contactsChild);
                json.contactlist.push({
                    name : name,
                    id : senderId
                });
                child.remove();
            } else {
                alert("Try agin later...")
            }
        } catch (error) {
            alert(error)
        }
    })
    rbtn.addEventListener('click', async (event) => {
        try {
            const req = await fetch("http://localhost:8080/chat/friend-request-accept", {
                method : "POST",
                headers : {"Content-Type":"application/json"},
                body : JSON.stringify({
                    uId : userId,
                    fId : senderId,
                    status : "rejected"
                })
            })
            const resp = await req.json();
            if(resp["status"] == 202){
                alert("request rejected successfully");
                const contacts = document.querySelector(".contact-list");
                const contactsChild = document.createElement("li");
                contactsChild.className = "contact-item";
                contactsChild.setAttribute("data-id", senderId);
                contactsChild.textContent = name;
                contacts.appendChild(contactsChild);
                child.remove();
            } else {
                alert("Try again later...")
            }
        } catch (error) {
            alert(error)
        }
    })
    child.appendChild(n);
    child.appendChild(abtn);
    child.appendChild(rbtn);
    noti.appendChild(child);
}



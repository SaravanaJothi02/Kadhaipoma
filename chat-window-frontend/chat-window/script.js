const json = {
    contactlist: [],
    messages: [],
    requestList: [],
};

let totalMessage = json.messages.length;
let userId = sessionStorage.getItem("userId");
let receiverId = null;
const BASE_URL = "http://localhost:8080/chat";
const ws = new WebSocket(`ws://localhost:8080/chat/${userId}`);

ws.onopen = () => {
    console.log("Connection established...");
    loadContact();
};

ws.onmessage = (event) => {
    const message = event.data;
    console.log("message ==> " + message);
    if (message.startsWith("friend request")) {
        addFriendRequest(message.split(":"));
    } else if (message.startsWith("add contact")) {
        addContact(message.split(":"));
    } else {
        displayMessage(message);
    }
};

ws.onclose = () => {
    console.log("Disconnected...");
};

async function loadContact() {
    const contacts = document.querySelector(".contact-list");
    const template = document.getElementById("contact-list-template");
    try {
        const resp = await fetch(
            `${BASE_URL}/get-contact-list?userId=${userId}`
        );
        const contactList = await resp.json();
        contactList.forEach((data) => {
            const card = template.content.cloneNode(true);
            card.querySelector(".contact-item").setAttribute(
                "data-id",
                data.user.userId
            );
            card.querySelector(".name").textContent = data.user.userName;
            card.querySelector(".msg").textContent = data.message.text;
            if (data.message.status === "DELIVERED") {
                card.querySelector(".msg").className = "msg-delivered";
            }
            contacts.appendChild(card);
        });
        contactList.forEach((user) => {
            json.contactlist.push({ name: user["name"], id: user["id"] });
        });
    } catch (error) {
        alert("Error fetching contacts: " + error);
    }
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
    displayMessage(msg);
    ws.send(msg);
    input.value = "";
}

function setupContactListeners() {
    const contacts = document.querySelectorAll(".contact-item");
    contacts.forEach((contact) => {
        contact.addEventListener("click", () => {
            document.querySelector("#messages").style.display = "block";
            document.querySelector(".message-input-container").style.display =
                "flex";
            receiverId = contact.getAttribute("data-id");
            highlightSelectedContact(contact);
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

async function loadChat() {
    const messages = [];
    try {
        const req = await fetch(`${BASE_URL}/get-message`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userId: userId, contactId: receiverId }),
        });
        const resp = await req.json();
        if (req.ok) {
            resp.forEach((msg) => {
                messages.push({
                    senderId: msg.senderId,
                    receiverId: msg.receiverId,
                    msgContent: msg.text,
                });
            });
        } else {
            alert("Error fetching messages");
        }
    } catch (error) {
        console.log("Error: " + error);
    }
    displayMessages(messages);
}

function displayMessages(messages) {
    const messageContainer = document.querySelector(".messages");
    messageContainer.replaceChildren();
    messages.forEach((msg) => {
        const child = document.createElement("li");
        child.className =
            msg.senderId == userId ? "message sent" : "message received";
        child.textContent = msg.msgContent;
        messageContainer.appendChild(child);
    });
}

function displayMessage(msg) {
    console.log("msg ==> " + msg);
    const message = msg.split(":");
    const messagesDiv = document.getElementById("messages");
    const child = document.createElement("li");
    child.className =
        message[0] == userId ? "message sent" : "message received";
    child.textContent = message[2];
    messagesDiv.appendChild(child);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
    document.querySelector(`[data-id="${(message[0] !== userId) ? message[0] : message[1]}"]`).querySelector(".msg").textContent = message[2];
}

function addContact(messageParts) {
    const contacts = document.querySelector(".contact-list");
    const template = document.getElementById("contact-list-template");
    const card = template.content.cloneNode(true);
    card.querySelector(".contact-item").setAttribute(
        "data-id",
        messageParts[1]
    );
    card.querySelector(".name").textContent = messageParts[2];
    card.querySelector(".msg").textContent = "No messages";
    card.querySelector(".contact-item").addEventListener("click", () => {
        document.querySelector("#messages").style.display = "block";
        document.querySelector(".message-input-container").style.display =
            "flex";
        receiverId = card
            .querySelector(".contact-item")
            .getAttribute("data-id");
        highlightSelectedContact(card.querySelector(".contact-item"));
    });
    contacts.appendChild(card);
    // const child = document.createElement("li");
    // child.className = "contact-item";
    // child.setAttribute("data-id", messageParts[1]);
    // child.textContent = messageParts[2];
    // contacts.appendChild(child);
    json.contactlist.push({ name: messageParts[2], id: messageParts[1] });
}

function showContact() {
    document.querySelector(".notification").style.display = "none";
    document.querySelector(".add-friends").style.display = "none";
    document.querySelector(".contact-list").style.display = "block";
}

function showNotification() {
    document.querySelector(".notification").style.display = "block";
    document.querySelector(".add-friends").style.display = "none";
    document.querySelector(".contact-list").style.display = "none";
}

function search() {
    document.querySelector(".notification").style.display = "none";
    document.querySelector(".add-friends").style.display = "block";
    document.querySelector(".contact-list").style.display = "none";
}

async function searchUser() {
    try {
        const response = await fetch(
            `${BASE_URL}/search?key=${document.getElementById("search").value}`
        );
        const list = await response.json();
        displaySearchResults(list);
    } catch (error) {
        console.log("Error: " + error);
    }
}

function displaySearchResults(list) {
    const searchList = document.querySelector(".search-list");
    searchList.replaceChildren();
    const frdId = json.contactlist.map((u) => u.id);
    list.forEach((user) => {
        let uId = user["id"];
        let uName = user["name"];
        if (userId != uId && !frdId.includes(uId)) {
            const child = document.createElement("li");
            const name = document.createElement("span");
            const btn = document.createElement("button");
            child.className = "search-contact-item contact-item";
            child.setAttribute("data-id", uId);
            name.textContent = uName;
            btn.innerText = json.requestList.includes(uId) ? "~" : "req";
            btn.addEventListener("click", () => sendFriendRequest(uId));
            child.appendChild(name);
            child.appendChild(btn);
            searchList.appendChild(child);
        }
    });
}

async function sendFriendRequest(friendId) {
    try {
        const req = await fetch(`${BASE_URL}/friend-request`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userId: userId,
                friendId: String(friendId),
            }),
        });
        console.log(req.ok);
        if (req.ok) {
            alert("Request sent successfully...");
        } else {
            const resp = await req.json();
            alert("Error: " + resp.message);
        }
    } catch (error) {
        console.log("Error: " + error);
    }
}

function addFriendRequest(req) {
    console.log(req);
    const senderId = req[1];
    const name = req[2];
    const noti = document.querySelector(".notification");
    const child = document.createElement("li");
    child.className = "contact-item";
    child.setAttribute("data-id", senderId);
    const n = document.createElement("span");
    n.innerText = name;
    const abtn = document.createElement("button");
    const rbtn = document.createElement("button");
    abtn.innerText = "a";
    rbtn.innerText = "r";
    abtn.addEventListener("click", () =>
        handleFriendRequest(senderId, name, "accepted")
    );
    rbtn.addEventListener("click", () =>
        handleFriendRequest(senderId, name, "rejected")
    );
    child.appendChild(n);
    child.appendChild(abtn);
    child.appendChild(rbtn);
    noti.appendChild(child);
}

async function handleFriendRequest(senderId, name, status) {
    try {
        const req = await fetch(`${BASE_URL}/friend-request`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userId: userId,
                friendId: senderId,
                action: status,
            }),
        });
        const resp = await req.json();
        if (req.ok && status === "accepted") {
            alert("Friend added successfully...");
            addContactToList(senderId, name);
        } else if (req.ok) {
            alert("Request rejected successfully");
        } else {
            alert(resp.message);
        }
    } catch (error) {
        alert("Error: " + error);
    }
}

function addContactToList(senderId, name) {
    const contacts = document.querySelector(".contact-list");
    const template = document.getElementById("contact-list-template");
    const card = template.content.cloneNode(true);
    card.querySelector(".contact-item").setAttribute("data-id", senderId);
    card.querySelector(".name").textContent = name;
    card.querySelector(".msg").textContent = "No messages";
    card.querySelector(".contact-item").addEventListener("click", () => {
        document.querySelector("#messages").style.display = "block";
        document.querySelector(".message-input-container").style.display =
            "flex";
        receiverId = card
            .querySelector(".contact-item")
            .getAttribute("data-id");
        highlightSelectedContact(card.querySelector(".contact-item"));
    });
    contacts.appendChild(card);
    json.contactlist.push({ name: name, id: senderId });
}

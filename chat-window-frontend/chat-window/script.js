const json = {
    contactlist: [
        {
            name: "Nagarajan",
            id: "1",
        },
        {
            name: "Sukumar",
            id: "2",
        },
        {
            name: "Krishnamoorthy",
            id: "3",
        },
        {
            name: "Karthi",
            id: "4",
        },
        {
            name: "Hari",
            id: "5",
        },
        {
            name: "Rishi",
            id: "6",
        },
        {
            name: "Gowtham",
            id: "7",
        },
        {
            name: "Subi",
            id: "8",
        },
        {
            name: "SJ",
            id: "9",
        },
        {
            name: "Harini",
            id: "10",
        },
    ],
    messages: [],
};
let totalMessage = json.messages.length;

let userId = sessionStorage.getItem("userId");
let receiverId = null;
const ws = new WebSocket(`ws://localhost:8080/chat/${userId}`);

ws.onopen = () => {
    console.log("Connection established...");
};

ws.onmessage = (event) => {
    const messagesDiv = document.getElementById("messages");
    messagesDiv.innerHTML += `<p>${event.data}</p>`;
    const msg = event.data.split(":");
    json.messages.push({
        msgId: `${++totalMessage}`,
        senderId: `${msg[0]}`,
        receiverId: `${msg[1]}`,
        msgContent: `${msg[2]}`,
    });
    console.log(msg);
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
    loadChat();
};

ws.onclose = () => {
    console.log("Disconnected...");
};

function loadContact() {
    console.log(userId);
    const contacts = document.querySelector(".contact-list");
    const contactlist = json.contactlist;

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

loadContact();

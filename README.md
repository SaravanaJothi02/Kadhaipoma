# **Kadhaipoma**

**Kadhaipoma** is a simple chat application built using Java Servlets, WebSockets, and MySQL. It supports user registration, real-time messaging, and queuing undelivered messages for offline users.  

---

## **Setup Instructions**

### **1. Add Dependencies**
Clone the repository and add the following dependencies to your `pom.xml` file:

```
<dependencies>
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
  </dependency>
  <dependency>
    <groupId>javax.websocket</groupId>
    <artifactId>javax.websocket-api</artifactId>
    <version>1.1</version>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.32</version>
  </dependency>
  <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20231013</version>
  </dependency>
</dependencies>
```
setup database
user :
CREATE TABLE user (
    user_id INT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    status ENUM('1', '0') DEFAULT NULL
);

message:
CREATE TABLE message (
    msg_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    text TEXT DEFAULT NULL,
    status ENUM('1', '0') DEFAULT NULL
);

default users:
INSERT INTO user (user_id, user_name, password, status) VALUES
(1, 'Nagarajan', 'Nagarajan', '1'),
(2, 'Sukumar', 'Sukumar', '1'),
(3, 'Krishnamoorthy', 'Krishnamoorthy', '1'),
(4, 'Karthi', 'Karthi', '1'),
(5, 'Hari', 'Hari', '1'),
(6, 'Rishi', 'Rishi', '1'),
(7, 'Gowtham', 'Gowtham', '1'),
(8, 'Subi', 'Subi', '1'),
(9, 'SJ', 'SJ', '1'),
(10, 'Harini', 'Harini', '1');


feature update : register

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
```
CREATE TABLE user (
    user_id INT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    status ENUM('1', '0') DEFAULT NULL
);
```

message:
```
CREATE TABLE message (
    msg_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    text TEXT DEFAULT NULL,
    status ENUM('1', '0') DEFAULT NULL
);
```

contact :
```
CREATE TABLE contact (
    user_id INT,
    friend_id INT
);
```

request :
```
CREATE TABLE request(
    req_id INT PRIMARY KEY AUTO_INCREMENT,
    uesr_id INT,
    friend_id INT,
    status ENUM('pending', 'success', 'rejected') DEFAULT 'pending',
    receive_status ENUM('send','not-send')
);
```

default users:
```
+---------+----------------+----------------+--------+
| user_id | user_name      | password       | status |
+---------+----------------+----------------+--------+
|       1 | Nagarajan      | Nagarajan      | 0      |
|       2 | Sukumar        | Sukumar        | 0      |
|       3 | Krishnamoorthy | Krishnamoorthy | 0      |
|       4 | Karthi         | Karthi         | 0      |
|       5 | Hari           | Hari           | 0      |
|       6 | Rishi          | Rishi          | 0      |
|       7 | Gowtham        | Gowtham        | 0      |
|       8 | Subi           | Subi           | 0      |
|       9 | SJ             | SJ             | 0      |
|      10 | Harini         | Harini         | 0      |
|      17 | raja           | raja           | 0      |
|      20 | Krish          | krish          | 0      |
+---------+----------------+----------------+--------+
```


feature update : register

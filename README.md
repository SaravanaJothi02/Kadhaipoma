# Kadhaipoma

static user data is present in >> chat-window-frontend/login/script.js

clone the file and add the dependency to your pom.xml
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

further set the database to your local system
user;
+-----------+---------------+------+-----+---------+-------+
| Field     | Type          | Null | Key | Default | Extra |
+-----------+---------------+------+-----+---------+-------+
| user_id   | int           | NO   | PRI | NULL    |       |
| user_name | varchar(50)   | NO   |     | NULL    |       |
| password  | varchar(50)   | NO   |     | NULL    |       |
| status    | enum('1','0') | YES  |     | NULL    |       |
+-----------+---------------+------+-----+---------+-------+

mssage;
+-------------+---------------+------+-----+---------+----------------+
| Field       | Type          | Null | Key | Default | Extra          |
+-------------+---------------+------+-----+---------+----------------+
| msg_id      | int           | NO   | PRI | NULL    | auto_increment |
| sender_id   | int           | NO   |     | NULL    |                |
| receiver_id | int           | NO   |     | NULL    |                |
| text        | text          | YES  |     | NULL    |                |
| status      | enum('1','0') | YES  |     | NULL    |                |
+-------------+---------------+------+-----+---------+----------------+

insert the default user in user table;
refer : chat-window-frontend/login/script.js (default user)

feature update : register...

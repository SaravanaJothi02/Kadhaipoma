package com.base.dao;

import com.base.model.User;
import com.base.model.UserStatus;
import com.base.utilities.PasswordEncryptionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Add the new User to the Database
     *
     * @param userName name of the user
     * @param password password of the user (it may be hashed)
     * @param email    email of the user
     * @return {@code true} if the user is inserted, otherwise {@code false}
     */
    public boolean registerUser(String userName, String password, String email) {
        String query = "INSERT INTO user (user_mail, user_name, password, status) VALUES(?,?,?,'active')";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, userName);
            ps.setString(3, password);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("registerUser exception : " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is used to Authenticate the user
     *
     * @param email    email of the user
     * @param password password of the user (it may be hashed)
     * @return {@code user_id} if the is present, otherwise empty optional class
     */
    public Optional<Integer> authenticateUser(String email, String password) {
        String query = "SELECT user_id, password FROM user WHERE user_mail = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (!PasswordEncryptionUtil.verifyPassword(password, hashedPassword)) {
                    return Optional.empty();
                }
                return Optional.of(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            System.out.println("authenticateUser exception : " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * find the user by user id
     *
     * @param email email of the user
     * @return {@code true} if the user is existed, otherwise false
     */
    public boolean isUserExist(String email) {
        String query = "SELECT user_id FROM user WHERE user_mail = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("isUserExist exception : " + e.getMessage());
        }
        return false;
    }

    /**
     * get the user details
     *
     * @param userId unique is of the user
     * @return user with necessary user details
     */
    public Optional<User> getUserById(String userId) {
        String query = "SELECT user_name, user_mail, status, created_at, updated_at FROM user WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(
                        Integer.parseInt(userId),
                        rs.getString("user_mail"),
                        rs.getString("user_name"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.out.println("getUserById exception : " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Set the Online status of the User
     *
     * @param userId unique id of user
     */
    public void setOnlineStatus(String userId, boolean isOnline) {
        String query = "UPDATE user SET status = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, (isOnline) ? "active" : "inactive");
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("setOnlineStatus exception : " + e.getMessage());
        }
    }
}

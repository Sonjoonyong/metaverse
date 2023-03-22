package com.example.metaverse.dao;

import com.example.metaverse.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO {

    private DataSource dataSource;

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findByUsername(String username) {
        User user = null;
        String sql = User.FIND_BY_USERNAME;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("user_id");
                    String password = rs.getString("password");
                    boolean active = rs.getBoolean("active");
                    Set<String> roles = findRolesByUsername(username);
                    user = new User(id, username, password, active, roles);
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return user;
    }

    private Set<String> findRolesByUsername(String username) {
        Set<String> roles = new HashSet<>();
        String sql = User.FIND_ROLES_BY_USERNAME;
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, findByUsername(username).getId());
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    roles.add(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return roles;
    }

    public void saveUser(User user) {
        String sql = User.SAVE_USER;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isActive());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()){
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    user.setId(id);
                    saveUserRoles(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveUserRoles(User user) {
        String sql = User.SAVE_USER_ROLE;
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            for (String role : user.getRoles()) {
                ps.setLong(1, user.getId());
                ps.setString(2, role);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

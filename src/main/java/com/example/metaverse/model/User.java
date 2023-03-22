package com.example.metaverse.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class User {

    private long id;
    private String username;
    private String password;
    private boolean active;
    private Set<String> roles;

    public User(long id, String username, String password, boolean active, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
        long id = rs.getLong("user_id");
        String username = rs.getString("username");
        String password = rs.getString("password");
        boolean active = rs.getBoolean("active");
        Set<String> roles = new HashSet<>();
        roles.add(rs.getString("role"));
        return new User(id, username, password, active, roles);
    }

    // SQL queries

    public static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    public static final String FIND_ROLES_BY_USERNAME = "SELECT role FROM user_roles WHERE user_id = ?";
    public static final String SAVE_USER = "INSERT INTO users (username, password, active) VALUES (?. ?, ?)";
    public static final String SAVE_USER_ROLE = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
}

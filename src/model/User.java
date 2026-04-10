package model;

import java.sql.Timestamp;

/**
 * Model class representing a User (Admin or Student).
 */
public class User {

    private int       id;
    private String    fullName;
    private String    email;
    private String    password;
    private String    role;          // "admin" or "student"
    private Timestamp createdAt;

    public User() {}

    public User(int id, String fullName, String email, String role) {
        this.id       = id;
        this.fullName = fullName;
        this.email    = email;
        this.role     = role;
    }

    // ── Getters ──────────────────────────────────
    public int       getId()        { return id; }
    public String    getFullName()  { return fullName; }
    public String    getEmail()     { return email; }
    public String    getPassword()  { return password; }
    public String    getRole()      { return role; }
    public Timestamp getCreatedAt() { return createdAt; }

    // ── Setters ──────────────────────────────────
    public void setId(int id)                   { this.id = id; }
    public void setFullName(String fullName)    { this.fullName = fullName; }
    public void setEmail(String email)          { this.email = email; }
    public void setPassword(String password)    { this.password = password; }
    public void setRole(String role)            { this.role = role; }
    public void setCreatedAt(Timestamp t)       { this.createdAt = t; }

    public boolean isAdmin()   { return "admin".equals(role); }
    public boolean isStudent() { return "student".equals(role); }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + fullName + "', role='" + role + "'}";
    }
}

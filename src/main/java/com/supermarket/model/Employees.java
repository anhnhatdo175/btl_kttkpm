package com.supermarket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Employees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String role;

    private String userName;

    private String passWord;

    public Employees() {}

    public Employees(String name, String role, String userName, String passWord) {
        this.name = name;
        this.role = role;
        this.userName = userName;
        this.passWord = passWord;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassWord() { return passWord; }
    public void setPassWord(String passWord) { this.passWord = passWord; }
}

package com.sw1project.profilemanagement.dto;

public class UserResponse {

    private String username;
    private String name;
    private String email;
    private String homeAddress;

    public UserResponse(String username, String name, String email, String homeAddress) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.homeAddress = homeAddress;
    }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getHomeAddress() { return homeAddress; }
}

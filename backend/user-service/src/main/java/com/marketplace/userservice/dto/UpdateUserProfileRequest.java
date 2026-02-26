package com.marketplace.userservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserProfileRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String login;
    private String city;
    private String country;

    private String bio;


}

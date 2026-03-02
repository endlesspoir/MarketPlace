package com.marketplace.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserProfileResponse {

    private  Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String login;
    private String avatarUrl;
    private String city;
    private String country;

    private String bio;
    private Set<String> roles;
}

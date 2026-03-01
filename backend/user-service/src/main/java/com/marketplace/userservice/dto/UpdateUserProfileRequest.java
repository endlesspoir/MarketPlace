package com.marketplace.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class UpdateUserProfileRequest {

    @Size(min = 1, max = 20, message = "{user.firstName.size}")
    private String firstName;

    @Size(min = 1, max = 20, message = "{user.lastName.size}")
    private String lastName;


    @Pattern(regexp ="^\\+[1-9][0-9]{9,14}$", message = "{user.phone.invalid}")
    private String phone;

    @Size(min = 3, max = 20, message = "{user.login.size}")
    private String login;

    @Size(min = 1, max = 20, message = "{user.city.size}")
    private String city;

    @Size(min = 1, max = 20, message = "{user.country.size}")
    private String country;

    @Size(max = 500, message = "{user.bio.size}")
    private String bio;
}

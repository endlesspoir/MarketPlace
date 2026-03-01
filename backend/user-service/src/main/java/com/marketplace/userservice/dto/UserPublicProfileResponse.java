package com.marketplace.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserPublicProfileResponse {

    String firstname;

    String lastname;

    String avatarUrl;

    String bio;
}

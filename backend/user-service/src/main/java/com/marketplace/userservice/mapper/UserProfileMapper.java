package com.marketplace.userservice.mapper;

import com.marketplace.userservice.dto.UserProfileResponse;
import com.marketplace.userservice.dto.UserPublicProfileResponse;
import com.marketplace.userservice.model.Role;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.model.UserProfile;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileMapper {

    public static  UserProfileResponse toUserProfile(User user){

        UserProfile userProfile = user.getProfile();


        return new UserProfileResponse()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setLogin(user.getLogin())
                .setAvatarUrl(userProfile.getAvatarUrl())
                .setCity(userProfile.getCity())
                .setCountry(userProfile.getCountry())
                .setBio(userProfile.getBio())
                .setRoles(user.getRoles().stream()
                        .map(role -> role.getName().toString())
                        .collect(Collectors.toSet()));
    }

    public static UserPublicProfileResponse toUserPublicProfile(User user){

        return new UserPublicProfileResponse().
                setFirstname(user.getFirstName())
                .setLastname(user.getLastName())
                .setBio(user.getProfile().getBio())
                .setAvatarUrl(user.getProfile().getAvatarUrl());
    }
}

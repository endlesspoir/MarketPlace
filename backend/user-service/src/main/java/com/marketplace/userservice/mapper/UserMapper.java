package com.marketplace.userservice.mapper;

import com.marketplace.userservice.dto.RegisterRequest;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.model.UserProfile;


import java.util.HashSet;


public class UserMapper {


    public static User toUser(RegisterRequest request){



         User user = new User()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setEmail(request.getEmail())
                .setPhone(request.getPhone())
                .setPassword(request.getPassword())
                .setLogin(request.getLogin());

         user.setRoles(new HashSet<>());
         UserProfile userProfile = new UserProfile();
         userProfile.setUser(user);
         user.setProfile(userProfile);

         return user;
    }


}

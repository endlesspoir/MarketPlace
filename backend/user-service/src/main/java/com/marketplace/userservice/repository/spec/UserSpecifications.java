package com.marketplace.userservice.repository.spec;

import com.marketplace.userservice.model.RoleType;
import com.marketplace.userservice.model.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {


    public static Specification<User> smartSearch(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isBlank()) return null;

            String like = "%" + q.toLowerCase() + "%";

            var email = cb.like(cb.lower(root.get("email")), like);
            var login = cb.like(cb.lower(root.get("login")), like);
            var phone = cb.like(cb.lower(root.get("phone")), like);
            var firstName = cb.like(cb.lower(root.get("firstName")), like);
            var lastName = cb.like(cb.lower(root.get("lastName")), like);


            return cb.or(email, login, phone, firstName, lastName);
        };
    }

    public static Specification<User> verifiedEmail(Boolean value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get("verifiedEmail"), value);
    }

    public static Specification<User> verifiedPhone(Boolean value) {
        return (root, query, cb) ->
                value == null ? null : cb.equal(root.get("verifiedPhone"), value);
    }



    public static Specification<User> hasRole(String role) {
        return (root, query, cb) -> {
            if (role == null) return null;
            var roleEnum = RoleType.valueOf(role.toUpperCase());
            return cb.equal(root.join("roles").get("name"), roleEnum);
        };
    }
}
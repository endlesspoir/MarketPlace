package com.automarket.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@ToString(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", schema = "user_service")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @Column(length = 255)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String permissions;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}

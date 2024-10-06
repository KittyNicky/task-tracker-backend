package com.kittynicky.tasktrackerbackend.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "public",
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username", name = "uix_users_username"),
                @UniqueConstraint(columnNames = "email", name = "uix_user_email")
        })
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//, generator = "user_id_seq")
//    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", nullable = false)
    @Size(max = 64)
    private String username;

    @Column(name = "email", nullable = false)
    @Email
    @Size(max = 255)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 255)
    @EqualsAndHashCode.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @EqualsAndHashCode.Exclude
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

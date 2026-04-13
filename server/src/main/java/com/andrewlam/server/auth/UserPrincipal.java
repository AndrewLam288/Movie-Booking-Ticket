package com.andrewlam.server.auth;

import com.andrewlam.server.enums.UserRole;
import com.andrewlam.server.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class UserPrincipal implements UserDetails {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String fullName;
    private final UserRole role;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(Long id,
                          String email,
                          String passwordHash,
                          String fullName,
                          UserRole role,
                          boolean active,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.authorities = authorities;
    }

    public static UserPrincipal fromUser(User user) {
        return new UserPrincipal(
                user.getId(),
                normalizeEmail(user.getEmail()),
                user.getPasswordHash(),
                user.getFullName(),
                user.getRole(),
                Boolean.TRUE.equals(user.isActive()),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserPrincipal that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
package com.hqvoyage.platform.business.web.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * Security User
 * Created by zhangxd on 16/3/17.
 */
public class AuthUser implements UserDetails {

    private final String id;
    private final String loginName;
    private final String name;
    private final String password;
    private final String email;
    private final String phone;
    private final String mobile;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    private final Date passwordResetDate;

    public AuthUser(
            String id,
            String loginName,
            String name,
            String email,
            String phone,
            String mobile,
            String password, Collection<? extends GrantedAuthority> authorities,
            boolean enabled,
            Date passwordResetDate
    ) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.passwordResetDate = passwordResetDate;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return loginName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Date getPasswordResetDate() {
        return passwordResetDate;
    }
}

package com.hqvoyage.platform.business.web.security.model;

import com.hqvoyage.platform.business.api.entity.SysRole;
import com.hqvoyage.platform.business.api.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class AuthUserFactory {

    private AuthUserFactory() {
    }

    public static AuthUser create(SysUser user) {
        return new AuthUser(
                user.getId(),
                user.getLoginName(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getMobile(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getEnabled(),
                user.getPasswordResetDate()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<SysRole> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }
}

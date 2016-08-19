package com.hqvoyage.platform.business.web.security;

import com.hqvoyage.platform.business.api.entity.SysUser;
import com.hqvoyage.platform.business.api.service.ISystemService;
import com.hqvoyage.platform.business.web.security.model.AuthUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security User Detail Service
 * Created by zhangxd on 16/3/17.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ISystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        SysUser user = systemService.getUserByLoginName(loginName);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", loginName));
        } else {
            return AuthUserFactory.create(user);
        }
    }
}

package me.zhangxudong.platform.business.web.controller;

import me.zhangxudong.platform.business.web.security.AuthenticationTokenFilter;
import me.zhangxudong.platform.business.web.security.utils.TokenUtil;
import me.zhangxudong.platform.business.web.security.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    @Autowired
    private TokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public AuthUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(AuthenticationTokenFilter.TOKEN_HEADER);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (AuthUser) userDetailsService.loadUserByUsername(username);
    }

}

package me.zhangxudong.platform.admin.web.security;

import me.zhangxudong.platform.admin.web.common.base.property.Global;
import me.zhangxudong.platform.admin.web.common.web.Servlets;
import me.zhangxudong.platform.admin.web.controller.LoginController;
import me.zhangxudong.platform.admin.web.servlet.ValidateCodeServlet;
import me.zhangxudong.platform.admin.web.utils.UserUtils;
import me.zhangxudong.platform.admin.web.utils.WebUtils;
import me.zhangxudong.platform.common.redis.RedisRepository;
import me.zhangxudong.platform.common.shiro.session.SessionDAO;
import me.zhangxudong.platform.common.utils.*;
import me.zhangxudong.platform.system.api.entity.SysMenu;
import me.zhangxudong.platform.system.api.entity.SysUser;
import me.zhangxudong.platform.system.api.service.ISystemService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 系统安全认证实现类
 * Created by zhangxd on 15/10/20.
 */
public class SystemAuthorizingRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISystemService systemService;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private RedisRepository redisRepository;

    /**
     * 认证回调函数, 登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        int activeSessionSize = sessionDAO.getActiveSessions(false).size();
        if (logger.isDebugEnabled()) {
            logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
        }

        // 校验登录验证码
        if (isValidateCodeLogin(token.getUsername())) {
            Session session = WebUtils.getSession();
            String code = (String) (session != null ? session.getAttribute(ValidateCodeServlet.VALIDATE_CODE) : "");
            if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
                throw new AuthenticationException("msg:验证码错误, 请重试.");
            }
        }

        // 校验用户名密码
        SysUser user = systemService.getUserByLoginName(token.getUsername());
        if (user != null) {
            if (Global.NO.equals(user.getLoginFlag())) {
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }
            byte[] salt = EncodeHelper.decodeHex(user.getPassword().substring(0, 16));
            return new SimpleAuthenticationInfo(new Principal(user),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }

    private boolean isValidateCodeLogin(String username) {
        Integer loginFailNum = NumberHelper.toInt(redisRepository.getHashValues(LoginController.LOGIN_FAIL_MAP, username), 0);
        return loginFailNum >= 3;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object obj = getAvailablePrincipal(principals);
        Principal principal;
        try {
            principal = (Principal) obj;
        } catch (Exception e) {
            return null;
        }
        // 获取当前已登录的用户
        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
            Collection<Session> sessions = sessionDAO.getActiveSessions(true, principal, WebUtils.getSession());
            if (sessions.size() > 0) {
                // 如果是登录进来的，则踢出已在线用户
                if (WebUtils.getSubject().isAuthenticated()) {
                    for (Session session : sessions) {
                        sessionDAO.delete(session);
                    }
                }
                // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
                else {
                    WebUtils.getSubject().logout();
                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
                }
            }
        }
        SysUser user = systemService.getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<SysMenu> list = UserUtils.getMenuList();
            for (SysMenu menu : list) {
                if (StringHelper.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringHelper.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 更新登录IP和时间
            user.setLoginIp(WebHelper.getRemoteAddr(Servlets.getRequest()));
            systemService.updateUserLoginInfo(user);
            return info;
        } else {
            return null;
        }
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 授权验证方法
     *
     * @param permission
     */
    private void authorizationValidate(Permission permission) {
        // 模块授权预留接口
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtils.HASH_ALGORITHM);
        matcher.setHashIterations(PasswordUtils.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id; // 编号
        private String loginName; // 登录名
        private String name; // 姓名

        public Principal(SysUser user) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
        }

        public String getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }


        /**
         * 获取SESSIONID
         */
        public String getSessionid() {
            try {
                Session session = WebUtils.getSession();
                if (session != null) {
                    return (String) session.getId();
                }
                return "";
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public String toString() {
            return id;
        }

    }
}

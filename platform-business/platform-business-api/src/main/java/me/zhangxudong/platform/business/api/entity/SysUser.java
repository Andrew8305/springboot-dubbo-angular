package me.zhangxudong.platform.business.api.entity;

import me.zhangxudong.platform.common.api.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 用户Entity
 * Created by zhangxd on 15/10/20.
 */
public class SysUser extends DataEntity<SysUser> {

    private String loginName;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String mobile;

    private Boolean enabled;

    private Date passwordResetDate;

    private List<SysRole> roles;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getPasswordResetDate() {
        return passwordResetDate;
    }

    public void setPasswordResetDate(Date passwordResetDate) {
        this.passwordResetDate = passwordResetDate;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
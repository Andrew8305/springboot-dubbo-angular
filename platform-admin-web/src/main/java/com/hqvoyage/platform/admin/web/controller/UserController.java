package com.hqvoyage.platform.admin.web.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hqvoyage.platform.admin.web.utils.UserUtils;
import com.hqvoyage.platform.admin.web.common.web.BaseController;
import com.hqvoyage.platform.common.utils.PasswordUtils;
import com.hqvoyage.platform.common.utils.StringHelper;
import com.hqvoyage.platform.system.api.entity.SysRole;
import com.hqvoyage.platform.system.api.entity.SysUser;
import com.hqvoyage.platform.system.api.service.ISystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户Controller
 * Created by zhangxd on 15/10/20.
 */
@Controller
@RequestMapping(value = "/sys/user")
public class UserController extends BaseController {

    @Autowired
    private ISystemService systemService;

    @ModelAttribute
    public SysUser get(@RequestParam(required = false) String id) {
        if (StringHelper.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new SysUser();
        }
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = {"list", ""})
    public String list(SysUser user, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page page = getPage(request, response);
        user.setCurrentUser(UserUtils.getUser());
        PageInfo<SysUser> pageInfo = systemService.findUser(page, user);
        model.addAttribute("page", pageInfo);
        return "modules/sys/userList";
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping(value = "form")
    public String form(SysUser user, Model model) {
        model.addAttribute("sysUser", user);
        model.addAttribute("allRoles", systemService.findAllRole(UserUtils.getUser()));
        return "modules/sys/userForm";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "save")
    public String save(SysUser user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        // 如果新密码为空，则不更换密码
        if (StringHelper.isNotBlank(user.getNewPassword())) {
            user.setPassword(PasswordUtils.encryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            return form(user, model);
        }
        if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model);
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<SysRole> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (SysRole r : systemService.findAllRole(UserUtils.getUser())) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        addMessage(redirectAttributes, "保存用户'" + user.getLoginName() + "'成功");
        return "redirect:/sys/user/list?repage";
    }

    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "delete")
    public String delete(SysUser user, RedirectAttributes redirectAttributes) {
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (SysUser.isAdmin(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            systemService.deleteUser(user);
            addMessage(redirectAttributes, "删除用户成功");
        }
        return "redirect:/sys/user/list?repage";
    }

    /**
     * 验证登录名是否有效
     *
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 用户信息显示及保存
     *
     * @param user
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "info")
    public String info(SysUser user, HttpServletResponse response, Model model) {
        SysUser currentUser = UserUtils.getUser();
        if (StringHelper.isNotBlank(user.getName())) {
            currentUser.setEmail(user.getEmail());
            currentUser.setPhone(user.getPhone());
            currentUser.setMobile(user.getMobile());
            currentUser.setRemarks(user.getRemarks());
            systemService.updateUserInfo(currentUser);
            model.addAttribute("message", "保存用户信息成功");
        }
        model.addAttribute("sysUser", currentUser);
        return "modules/sys/userInfo";
    }

    /**
     * 返回用户信息
     *
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "infoData")
    public SysUser infoData() {
        return UserUtils.getUser();
    }

    /**
     * 修改个人用户密码
     *
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "modifyPwd")
    public String modifyPwd(String oldPassword, String newPassword, Model model) {
        SysUser user = UserUtils.getUser();
        if (StringHelper.isNotBlank(oldPassword) && StringHelper.isNotBlank(newPassword)) {
            if (PasswordUtils.validatePassword(oldPassword, user.getPassword())) {
                systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
                model.addAttribute("message", "修改密码成功");
            } else {
                model.addAttribute("message", "修改密码失败，旧密码错误");
            }
        }
        model.addAttribute("sysUser", user);
        return "modules/sys/userModifyPwd";
    }

}

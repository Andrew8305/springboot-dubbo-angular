package com.hqvoyage.platform.admin.web.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hqvoyage.platform.admin.web.utils.UserUtils;
import com.hqvoyage.platform.admin.web.common.base.property.Global;
import com.hqvoyage.platform.admin.web.common.web.BaseController;
import com.hqvoyage.platform.common.utils.Collections3;
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

import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 * Created by zhangxd on 15/10/20.
 */
@Controller
@RequestMapping(value = "/sys/role")
public class RoleController extends BaseController {

    @Autowired
    private ISystemService systemService;

    @ModelAttribute
    public SysRole get(@RequestParam(required = false) String id) {
        if (StringHelper.isNotBlank(id)) {
            return systemService.getRole(id);
        } else {
            return new SysRole();
        }
    }

    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = {"list", ""})
    public String list(SysRole role, Model model) {
        List<SysRole> list = systemService.findAllRole(UserUtils.getUser());
        model.addAttribute("list", list);
        return "modules/sys/roleList";
    }

    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = "form")
    public String form(SysRole role, Model model) {
        model.addAttribute("sysRole", role);
        model.addAttribute("menuList", systemService.findAllMenu(UserUtils.getUser()));
        return "modules/sys/roleForm";
    }

    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "save")
    public String save(SysRole role, Model model, RedirectAttributes redirectAttributes) {
        if (!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)) {
            addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
            return "redirect:/sys/role?repage";
        }
        if (!beanValidator(model, role)) {
            return form(role, model);
        }
        if (!"true".equals(checkName(role.getOldName(), role.getName()))) {
            addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
            return form(role, model);
        }
        systemService.saveRole(role);
        addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
        return "redirect:/sys/role?repage";
    }

    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "delete")
    public String delete(SysRole role, RedirectAttributes redirectAttributes) {
        if (!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)) {
            addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
            return "redirect:/sys/role?repage";
        }
        systemService.deleteRole(role);
        addMessage(redirectAttributes, "删除角色成功");
        return "redirect:/sys/role?repage";
    }

    /**
     * 角色分配页面
     *
     * @param role
     * @param model
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "assign")
    public String assign(SysRole role, Model model) {
        SysUser user = new SysUser(new SysRole(role.getId()));
        user.setCurrentUser(UserUtils.getUser());
        List<SysUser> userList = systemService.findUser(user);
        model.addAttribute("userList", userList);
        return "modules/sys/roleAssign";
    }

    /**
     * 角色分配 -- 打开角色分配对话框
     *
     * @param role
     * @param model
     * @return
     */
    @RequiresPermissions("sys:role:view")
    @RequestMapping(value = "usertorole")
    public String selectUserToRole(SysRole role, Model model) {
        List<Map<String, Object>> allUserList = Lists.newArrayList();
        SysUser user = new SysUser();
        user.setCurrentUser(UserUtils.getUser());
        PageInfo<SysUser> page = systemService.findUser(new Page(1, 0), user);
        for (SysUser e : page.getList()) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", 0);
            map.put("name", e.getName());
            allUserList.add(map);
        }

        SysUser roleUser = new SysUser(new SysRole(role.getId()));
        user.setCurrentUser(UserUtils.getUser());
        List<SysUser> userList = systemService.findUser(roleUser);
        model.addAttribute("sysRole", role);
        model.addAttribute("userList", userList);
        model.addAttribute("allUserList", allUserList);
        model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
        return "modules/sys/selectUserToRole";
    }

    /**
     * 角色分配 -- 从角色中移除用户
     *
     * @param userId
     * @param roleId
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "outrole")
    public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
        SysRole role = systemService.getRole(roleId);
        SysUser user = systemService.getUser(userId);
        if (UserUtils.getUser().getId().equals(userId)) {
            addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
        } else {
            if (user.getRoleList().size() <= 1) {
                addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！这已经是该用户的唯一角色，不能移除。");
            } else {
                Boolean flag = systemService.outUserInRole(role, user);
                if (!flag) {
                    addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
                } else {
                    addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
                }
            }
        }
        return "redirect:/sys/role/assign?id=" + role.getId();
    }

    /**
     * 角色分配
     *
     * @param role
     * @param idsArr
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("sys:role:edit")
    @RequestMapping(value = "assignrole")
    public String assignRole(SysRole role, String[] idsArr, RedirectAttributes redirectAttributes) {
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (String anIdsArr : idsArr) {
            SysUser user = systemService.assignUserToRole(role, systemService.getUser(anIdsArr));
            if (null != user) {
                msg.append("<br/>新增用户【").append(user.getName()).append("】到角色【").append(role.getName()).append("】！");
                newNum++;
            }
        }
        addMessage(redirectAttributes, "已成功分配 " + newNum + " 个用户" + msg);
        return "redirect:/sys/role/assign?id=" + role.getId();
    }

    /**
     * 验证角色名是否有效
     *
     * @param oldName
     * @param name
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null && systemService.getRoleByName(name) == null) {
            return "true";
        }
        return "false";
    }

}

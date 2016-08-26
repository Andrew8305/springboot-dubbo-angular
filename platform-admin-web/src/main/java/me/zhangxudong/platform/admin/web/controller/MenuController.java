package me.zhangxudong.platform.admin.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.zhangxudong.platform.admin.web.utils.UserUtils;
import me.zhangxudong.platform.admin.web.common.web.BaseController;
import me.zhangxudong.platform.common.utils.StringHelper;
import me.zhangxudong.platform.system.api.entity.SysMenu;
import me.zhangxudong.platform.system.api.service.ISystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 菜单Controller
 * Created by zhangxd on 15/10/20.
 */
@Controller
@RequestMapping(value = "/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private ISystemService systemService;

    @ModelAttribute
    public SysMenu get(@RequestParam(required = false) String id) {
        if (StringHelper.isNotBlank(id)) {
            return systemService.getMenu(id);
        } else {
            return new SysMenu();
        }
    }

    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        List<SysMenu> list = Lists.newArrayList();
        List<SysMenu> sourcelist = systemService.findAllMenu(UserUtils.getUser());
        SysMenu.sortList(list, sourcelist, SysMenu.getRootId(), true);
        model.addAttribute("list", list);
        return "modules/sys/menuList";
    }

    @RequiresPermissions("sys:menu:view")
    @RequestMapping(value = "form")
    public String form(SysMenu menu, Model model) {
        if (menu.getParent() == null || menu.getParent().getId() == null) {
            menu.setParent(new SysMenu(SysMenu.getRootId()));
        }
        menu.setParent(systemService.getMenu(menu.getParent().getId()));
        // 获取排序号，最末节点排序号+30
        if (StringHelper.isBlank(menu.getId())) {
            List<SysMenu> list = Lists.newArrayList();
            List<SysMenu> sourcelist = systemService.findAllMenu(UserUtils.getUser());
            SysMenu.sortList(list, sourcelist, menu.getParentId(), false);
            if (list.size() > 0) {
                menu.setSort(list.get(list.size() - 1).getSort() + 30);
            }
        }
        model.addAttribute("sysMenu", menu);
        return "modules/sys/menuForm";
    }

    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "save")
    public String save(SysMenu menu, Model model, RedirectAttributes redirectAttributes) {
        if (!UserUtils.getUser().isAdmin()) {
            addMessage(redirectAttributes, "越权操作，只有超级管理员才能添加或修改数据！");
            return "redirect:/sys/role?repage";
        }
        if (!beanValidator(model, menu)) {
            return form(menu, model);
        }
        systemService.saveMenu(menu);
        addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
        return "redirect:/sys/menu/";
    }

    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "delete")
    public String delete(SysMenu menu, RedirectAttributes redirectAttributes) {
        systemService.deleteMenu(menu);
        addMessage(redirectAttributes, "删除菜单成功");
        return "redirect:/sys/menu/";
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "tree")
    public String tree() {
        return "modules/sys/menuTree";
    }

    @RequiresPermissions("user")
    @RequestMapping(value = "treeselect")
    public String treeselect(String parentId, Model model) {
        model.addAttribute("parentId", parentId);
        return "modules/sys/menuTreeselect";
    }

    /**
     * 批量修改菜单排序
     */
    @RequiresPermissions("sys:menu:edit")
    @RequestMapping(value = "updateSort")
    public String updateSort(String[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
        for (int i = 0; i < ids.length; i++) {
            SysMenu menu = new SysMenu(ids[i]);
            menu.setSort(sorts[i]);
            systemService.updateMenuSort(menu);
        }
        addMessage(redirectAttributes, "保存菜单排序成功!");
        return "redirect:/sys/menu/";
    }

    /**
     * isShowHide是否显示隐藏菜单
     *
     * @param extId
     * @param isShowHide
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, @RequestParam(required = false) String isShowHide, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<SysMenu> list = systemService.findAllMenu(UserUtils.getUser());
        for (SysMenu e : list) {
            if (StringHelper.isBlank(extId) || (!extId.equals(e.getId()) && !e.getParentIds().contains("," + extId + ","))) {
                if (isShowHide != null && isShowHide.equals("0") && e.getIsShow().equals("0")) {
                    continue;
                }
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }
}

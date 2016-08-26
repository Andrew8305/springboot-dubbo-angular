package me.zhangxudong.platform.admin.web.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.zhangxudong.platform.admin.web.common.web.BaseController;
import me.zhangxudong.platform.common.utils.StringHelper;
import me.zhangxudong.platform.system.api.entity.SysDict;
import me.zhangxudong.platform.system.api.service.ISysDictService;
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
import java.util.Map;

/**
 * 字典Controller
 * Created by zhangxd on 15/10/20.
 */
@Controller
@RequestMapping(value = "/sys/dict")
public class DictController extends BaseController {

    @Autowired
    private ISysDictService sysDictService;

    @ModelAttribute
    public SysDict get(@RequestParam(required = false) String id) {
        if (StringHelper.isNotBlank(id)) {
            return sysDictService.get(id);
        } else {
            return new SysDict();
        }
    }

    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = {"list", ""})
    public String list(SysDict dict, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page page = getPage(request, response);
        List<String> typeList = sysDictService.findTypeList();
        model.addAttribute("typeList", typeList);
        PageInfo<SysDict> pageInfo = sysDictService.findPage(page, dict);
        model.addAttribute("page", pageInfo);
        return "modules/sys/dictList";
    }

    @RequiresPermissions("sys:dict:view")
    @RequestMapping(value = "form")
    public String form(SysDict dict, Model model) {
        model.addAttribute("sysDict", dict);
        return "modules/sys/dictForm";
    }

    @RequiresPermissions("sys:dict:edit")
    @RequestMapping(value = "save")
    public String save(SysDict dict, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, dict)) {
            return form(dict, model);
        }
        sysDictService.save(dict);
        addMessage(redirectAttributes, "保存字典'" + dict.getLabel() + "'成功");
        return "redirect:/sys/dict?repage&type=" + dict.getType();
    }

    @RequiresPermissions("sys:dict:edit")
    @RequestMapping(value = "delete")
    public String delete(SysDict dict, RedirectAttributes redirectAttributes) {
        sysDictService.delete(dict);
        addMessage(redirectAttributes, "删除字典成功");
        return "redirect:/sys/dict?repage&type=" + dict.getType();
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String type, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        SysDict dict = new SysDict();
        dict.setType(type);
        List<SysDict> list = sysDictService.findList(dict);
        for (SysDict e : list) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("name", StringHelper.replace(e.getLabel(), " ", ""));
            mapList.add(map);
        }
        return mapList;
    }

    @ResponseBody
    @RequestMapping(value = "listData")
    public List<SysDict> listData(@RequestParam(required = false) String type) {
        SysDict dict = new SysDict();
        dict.setType(type);
        return sysDictService.findList(dict);
    }

}

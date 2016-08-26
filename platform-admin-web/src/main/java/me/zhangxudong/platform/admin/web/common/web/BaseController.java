package me.zhangxudong.platform.admin.web.common.web;

import com.github.pagehelper.Page;
import me.zhangxudong.platform.admin.web.common.base.property.Global;
import me.zhangxudong.platform.common.utils.BeanValidators;
import me.zhangxudong.platform.common.utils.CookieUtils;
import me.zhangxudong.platform.common.utils.DateHelper;
import me.zhangxudong.platform.common.utils.StringHelper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * 控制器支持类
 * Created by zhangxd on 16/3/10.
 */
public abstract class BaseController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
     */
    protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
        return beanValidator(null, model, object, groups);
    }

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组
     * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
     */
    protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
        return beanValidator(redirectAttributes, null, object, groups);
    }

    private boolean beanValidator(RedirectAttributes redirectAttributes, Model model, Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException ex) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
            list.add(0, "数据验证失败：");
            if (redirectAttributes != null) {
                addMessage(redirectAttributes, list.toArray(new String[list.size()]));
            } else if (model != null) {
                addMessage(model, list.toArray(new String[list.size()]));
            }
            return false;
        }
        return true;
    }

    /**
     * 服务端参数有效性验证
     *
     * @param object 验证的实体对象
     * @param groups 验证组，不传入此参数时，同@Valid注解验证
     * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
     */
    protected void beanValidator(Object object, Class<?>... groups) {
        BeanValidators.validateWithException(validator, object, groups);
    }

    /**
     * 添加Model消息
     *
     * @param messages
     */
    protected void addMessage(Model model, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        model.addAttribute("message", sb.toString());
    }

    /**
     * 添加Flash消息
     *
     * @param messages
     */
    protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(messages.length > 1 ? "<br/>" : "");
        }
        redirectAttributes.addFlashAttribute("message", sb.toString());
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler({BindException.class, ConstraintViolationException.class, ValidationException.class})
    public String bindException() {
        return "error/400";
    }

    /**
     * 授权登录异常
     */
    @ExceptionHandler({AuthenticationException.class})
    public String authenticationException() {
        return "error/403";
    }

    /**
     * 构造方法
     *
     * @param request  传递 repage 参数，来记住页码
     * @param response 用于设置 Cookie，记住页码
     */
    protected Page getPage(HttpServletRequest request, HttpServletResponse response) {
        return getPage(request, response, -2);
    }

    /**
     * 构造方法
     *
     * @param request         传递 repage 参数，来记住页码
     * @param response        用于设置 Cookie，记住页码
     * @param defaultPageSize 默认分页大小，如果传递 0 则为不分页，返回所有数据
     */
    protected Page getPage(HttpServletRequest request, HttpServletResponse response, int defaultPageSize) {
        int pageNum = 1;
        int pageSize = Integer.valueOf(Global.getConfig("page.pageSize"));

        // 设置页码参数（传递repage参数，来记住页码）
        String no = request.getParameter("pageNum");
        if (StringHelper.isNumeric(no)) {
            CookieUtils.setCookie(response, "pageNum", no);
            pageNum = Integer.parseInt(no);
        } else if (request.getParameter("repage") != null) {
            no = CookieUtils.getCookie(request, "pageNum");
            if (StringHelper.isNumeric(no)) {
                pageNum = Integer.parseInt(no);
            }
        }
        if (defaultPageSize != -2) {
            pageSize = defaultPageSize;
        }

        Page page = new Page(pageNum, pageSize);

        // 设置排序参数
        String orderBy = request.getParameter("orderBy");
        if (StringHelper.isNotBlank(orderBy)) {
            page.setOrderBy(orderBy);
        }
        return page;
    }

    /**
     * 初始化数据绑定
     * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
     * 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateHelper.parseDate(text));
            }
        });
    }

}

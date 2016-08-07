package com.hqvoyage.platform.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;


/**
 * Web层面的工具类.
 * Created by zhangxd on 16/3/10.
 */
public class WebHelper {

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringHelper.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        } else if (StringHelper.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        } else if (StringHelper.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    /**
     * 将request请求中的参数及值转成一个Map格式.
     *
     * @param request HttpServletRequest
     * @return request中的请求及参数
     */
    public static Dto getRequestMap(HttpServletRequest request) {
        Dto dto = new BaseDto();
        Enumeration<?> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = Objects.toString(enumeration.nextElement());
            String[] values = request.getParameterValues(name);

            Object val;
            if (values.length == 1) {
                val = values[0];
            } else {
                val = Arrays.asList(values);
            }
            dto.put(name, val);
        }
        return dto;
    }

    /**
     * 获取访问路径
     *
     * @param request HttpServletRequest
     * @return 访问路径
     */
    public static String getBaseURL(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

}
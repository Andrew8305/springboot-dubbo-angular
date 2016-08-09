package com.hqvoyage.platform.common.shiro.util;

import com.hqvoyage.platform.common.utils.RandomHelper;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * 封装各种生成唯一性ID算法的工具类.
 */
public class IdGen implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        return RandomHelper.uuid();
    }

}

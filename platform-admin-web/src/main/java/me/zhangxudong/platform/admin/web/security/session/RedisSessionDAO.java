package me.zhangxudong.platform.admin.web.security.session;

import com.google.common.collect.Sets;
import me.zhangxudong.platform.admin.web.common.web.Servlets;
import me.zhangxudong.platform.common.redis.RedisRepository;
import me.zhangxudong.platform.common.shiro.session.SessionDAO;
import me.zhangxudong.platform.common.utils.DateHelper;
import me.zhangxudong.platform.common.utils.ObjectHelper;
import me.zhangxudong.platform.common.utils.StringHelper;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 自定义授权会话管理类
 */
public class RedisSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SESSION_KEY_PREFIX = "shiro_session_";

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }

        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不更新SESSION
            if (Servlets.isStaticFile(uri)) {
                return;
            }
        }

        try {

            // 获取登录者编号
            PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            String principalId = pc != null ? pc.getPrimaryPrincipal().toString() : StringHelper.EMPTY;

            redisRepository.putHashValue(SESSION_KEY_PREFIX, session.getId().toString(), principalId + "|" + session.getTimeout() + "|" + session.getLastAccessTime().getTime());

            // 设置超期时间
            int timeoutSeconds = (int) (session.getTimeout() / 1000);
            redisRepository.setExpire(ObjectHelper.serialize(SESSION_KEY_PREFIX + session.getId()), ObjectHelper.serialize(session), timeoutSeconds);

            logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
        } catch (Exception e) {
            logger.error("update {} {}", session.getId(), request != null ? request.getRequestURI() : "", e);
        }
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }

        try {

            redisRepository.delHashValues(SESSION_KEY_PREFIX, session.getId().toString());
            redisRepository.del(SESSION_KEY_PREFIX + session.getId());

            logger.debug("delete {} ", session.getId());
        } catch (Exception e) {
            logger.error("delete {} ", session.getId(), e);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return getActiveSessions(true);
    }

    /**
     * 获取活动会话
     *
     * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

    /**
     * 获取活动会话
     *
     * @param includeLeave  是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal     根据登录者对象获取活动会话
     * @param filterSession 不为空，则过滤掉（不包含）这个会话。
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        Set<Session> sessions = Sets.newHashSet();

        try {

            Map<String, String> map = redisRepository.getHashValue(SESSION_KEY_PREFIX);
            for (Map.Entry<String, String> e : map.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();
                if (StringHelper.isNotBlank(key) && StringHelper.isNotBlank(value)) {

                    String[] ss = StringHelper.split(value, "|");
                    if (ss != null && ss.length == 3) {
                        SimpleSession session = new SimpleSession();
                        session.setId(key);
                        session.setAttribute("principalId", ss[0]);
                        session.setTimeout(Long.valueOf(ss[1]));
                        session.setLastAccessTime(new Date(Long.valueOf(ss[2])));
                        try {
                            // 验证SESSION
                            session.validate();

                            boolean isActiveSession = false;
                            // 不包括离线并符合最后访问时间小于等于3分钟条件。
                            if (includeLeave || DateHelper.pastMinutes(session.getLastAccessTime()) <= 3) {
                                isActiveSession = true;
                            }
                            // 符合登陆者条件。
                            if (principal != null) {
                                PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                                if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringHelper.EMPTY)) {
                                    isActiveSession = true;
                                }
                            }
                            // 过滤掉的SESSION
                            if (filterSession != null && filterSession.getId().equals(session.getId())) {
                                isActiveSession = false;
                            }
                            if (isActiveSession) {
                                sessions.add(session);
                            }

                        }
                        // SESSION验证失败
                        catch (Exception e2) {
                            redisRepository.delHashValues(SESSION_KEY_PREFIX, e.getKey());
                        }
                    }
                    // 存储的SESSION不符合规则
                    else {
                        redisRepository.delHashValues(SESSION_KEY_PREFIX, e.getKey());
                    }
                }
                // 存储的SESSION无Value
                else if (StringHelper.isNotBlank(key)) {
                    redisRepository.delHashValues(SESSION_KEY_PREFIX, e.getKey());
                }
            }
            logger.info("getActiveSessions size: {} ", sessions.size());
        } catch (Exception e) {
            logger.error("getActiveSessions", e);
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不创建SESSION
            if (Servlets.isStaticFile(uri)) {
                return null;
            }
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.update(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        Session s = null;
        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不获取SESSION
            if (Servlets.isStaticFile(uri)) {
                return null;
            }
            s = (Session) request.getAttribute("session_" + sessionId);
        }
        if (s != null) {
            return s;
        }

        Session session = null;
        try {
            session = (Session) ObjectHelper.unserialize(redisRepository.get(ObjectHelper.serialize(SESSION_KEY_PREFIX + sessionId)));
            logger.debug("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "");
        } catch (Exception e) {
            logger.error("doReadSession {} {}", sessionId, request != null ? request.getRequestURI() : "", e);
        }

        if (request != null && session != null) {
            request.setAttribute("session_" + sessionId, session);
        }

        return session;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try {
            return super.readSession(sessionId);
        } catch (UnknownSessionException e) {
            return null;
        }
    }

}

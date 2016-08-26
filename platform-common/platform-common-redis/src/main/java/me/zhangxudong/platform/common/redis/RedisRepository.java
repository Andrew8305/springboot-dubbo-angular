package me.zhangxudong.platform.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Redis Repository
 * Created by zhangxd on 15/10/20.
 */
public class RedisRepository {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Logger logger = LoggerFactory.getLogger(RedisRepository.class);

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间
     * @throws Exception
     */
    public void setExpire(final byte[] key, final byte[] value, final long time) throws Exception {
        if (redisTemplate != null) {
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                connection.set(key, value);
                connection.expire(key, time);
                logger.info("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", key, time);
                return 1L;
            });
        } else {
            logger.info("[redisTemplate is null]");
        }
    }

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间
     * @throws Exception
     */
    public void setExpire(final String key, final String value, final long time) throws Exception {
        if (redisTemplate != null) {
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] keys = serializer.serialize(key);
                byte[] values = serializer.serialize(value);
                connection.set(keys, values);
                connection.expire(keys, time);
                logger.info("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为{}秒", key, time);
                return 1L;
            });
        } else {
            logger.info("[redisTemplate is null]");
        }
    }

    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     *
     * @param keys   redis主键数组
     * @param values 值数组
     * @param time   过期时间
     */
    public void setExpire(final String[] keys, final String[] values, final long time) throws Exception {
        if (redisTemplate != null) {
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                RedisSerializer<String> serializer = getRedisSerializer();
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i];
                    String value = values[i];
                    byte[] _keys = serializer.serialize(key);
                    byte[] _values = serializer.serialize(value);
                    connection.set(_keys, _values);
                    connection.expire(_keys, time);
                    logger.info("[redisTemplate redis]放入 缓存  url:{} ========缓存时间为:{}秒", key, time);
                }
                return 1L;
            });
        } else {
            logger.info("[redisTemplate is null]");
        }
    }


    /**
     * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
     */
    public void set(final String[] keys, final String[] values) throws Exception {
        if (redisTemplate != null) {
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                RedisSerializer<String> serializer = getRedisSerializer();

                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i];
                    String value = values[i];
                    byte[] _keys = serializer.serialize(key);
                    byte[] _values = serializer.serialize(value);
                    connection.set(_keys, _values);
                    logger.info("[redisTemplate redis]放入 缓存  url:{}", key);
                }
                return 1L;
            });
        } else {
            logger.info("[redisTemplate is null]");
        }
    }


    /**
     * 添加到缓存
     */
    public void set(final String key, final String value) throws Exception {
        if (redisTemplate != null) {
            redisTemplate.execute((RedisCallback<Long>) connection -> {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] keys = serializer.serialize(key);
                byte[] values = serializer.serialize(value);
                connection.set(keys, values);
                logger.info("[redisTemplate redis]放入 缓存  url:{}", key);
                return 1L;
            });
        } else {
            logger.info("[redisTemplate is null]");
        }
    }

    /**
     * 查询在这个时间段内即将过期的key
     */
    public List<String> willExpire(final String key, final long time) throws Exception {
        final List<String> keysList = new ArrayList<>();
        redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            Set<String> keys = redisTemplate.keys(key + "*");
            for (String key1 : keys) {
                Long ttl = -1L;
                try {
                    ttl = connection.ttl(key1.getBytes(DEFAULT_URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    logger.error("willExpire", e);
                }
                if (0 <= ttl && ttl <= 2 * time) {
                    keysList.add(key1);
                }
            }
            return keysList;
        });
        return keysList;
    }


    /**
     * 查询在以keyPatten的所有  key
     */
    public Set<String> keys(final String keyPatten) throws Exception {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> redisTemplate.keys(keyPatten + "*"));
    }

    /**
     * 根据key获取对象
     */
    public byte[] get(final byte[] key) throws Exception {
        byte[] result = null;
        if (redisTemplate != null) {
            result = redisTemplate.execute((RedisCallback<byte[]>) connection -> {
                byte[] values = connection.get(key);
                if (values == null) {
                    return null;
                }
                return values;
            });
        }
        logger.info("[redisTemplate redis]取出 缓存  url:{} ", key);
        return result;
    }

    /**
     * 根据key获取对象
     */
    public String get(final String key) throws Exception {
        String resultStr = null;
        if (redisTemplate != null) {
            resultStr = redisTemplate.execute((RedisCallback<String>) connection -> {
                RedisSerializer<String> serializer = getRedisSerializer();
                byte[] keys = serializer.serialize(key);
                byte[] values = connection.get(keys);
                if (values == null) {
                    return null;
                }
                return serializer.deserialize(values);
            });
        }
        logger.info("[redisTemplate redis]取出 缓存  url:{} ", key);
        return resultStr;
    }


    /**
     * 根据key获取对象
     */
    public Map<String, String> getKeysValues(final String keyPatten) throws Exception {
        logger.info("[redisTemplate redis]  getValues()  patten={} ", keyPatten);
        final Map<String, String> maps = new HashMap<>();
        return redisTemplate.execute((RedisCallback<Map<String, String>>) connection -> {
            RedisSerializer<String> serializer = getRedisSerializer();
            Set<String> keys = redisTemplate.keys(keyPatten + "*");
            for (String key : keys) {
                byte[] _keys = serializer.serialize(key);
                byte[] _values = connection.get(_keys);
                String value = serializer.deserialize(_values);
                maps.put(key, value);
            }
            return maps;
        });
    }

    public HashOperations<String, String, String> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * 对HashMap操作
     */
    public void putHashValue(String key, String hashKey, String hashValue) {
        logger.info("[redisTemplate redis]  putHashValue()  key={},hashKey={},hashValue={} ", key, hashKey, hashValue);
        opsForHash().put(key, hashKey, hashValue);
    }

    /**
     * 获取单个field对应的值
     */
    public Object getHashValues(String key, String hashKey) {
        logger.info("[redisTemplate redis]  getHashValues()  key={},hashKey={}", key, hashKey);
        return opsForHash().get(key, hashKey);
    }

    /**
     * 根据key值删除
     */
    public void delHashValues(String key, Object... hashKeys) {
        logger.info("[redisTemplate redis]  delHashValues()  key={}", key);
        opsForHash().delete(key, hashKeys);
    }

    /**
     * key只匹配map
     */
    public Map<String, String> getHashValue(String key) {
        logger.info("[redisTemplate redis]  getHashValue()  key={}", key);
        return opsForHash().entries(key);
    }

    /**
     * 批量添加
     */
    public void putHashvalues(String key, Map<String, String> map) {
        opsForHash().putAll(key, map);
    }

    /**
     * 集合数量
     */
    public long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**
     * 清空redis存储的数据
     */
    public String flushDB() {
        return redisTemplate != null ? redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        }) : null;
    }

    /**
     * 判断某个主键是否存在
     */
    public boolean exists(final String key) throws Exception {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                return connection.exists((key).getBytes(DEFAULT_URL_ENCODING));
            } catch (UnsupportedEncodingException e) {
                logger.error("exists", e);
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * 删除key
     */
    public long del(final String... keys) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            long result = 0;
            for (String key : keys) {
                try {
                    result = connection.del((key).getBytes(DEFAULT_URL_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    logger.error("del", e);
                }
            }
            return result;
        });
    }

    /**
     * 获取 RedisSerializer
     */
    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }

    /**
     * 对某个主键对应的值加一,value值必须是全数字的字符串
     */
    public long incr(final String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            RedisSerializer<String> redisSerializer = getRedisSerializer();
            return connection.incr(redisSerializer.serialize(key));
        });
    }

    /**
     * redis List 引擎
     */
    public ListOperations<String, String> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
     */
    public Long leftPush(String key, String value) {
        return opsForList().leftPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的头元素
     */
    public String leftPop(String key) {
        return opsForList().leftPop(key);
    }

    /**
     * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     */
    public Long in(String key, String value) {
        return opsForList().rightPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     */
    public String rightPop(String key) {
        return opsForList().rightPop(key);
    }


    /**
     * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
     */
    public Long length(String key) {
        return opsForList().size(key);
    }


    /**
     * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
     */
    public void remove(String key, long i, String value) {
        opsForList().remove(key, i, value);
    }

    /**
     * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
     */
    public void set(String key, long index, String value) {
        opsForList().set(key, index, value);
    }

    /**
     * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
     */
    public List<String> getList(String key, int start, int end) {
        return opsForList().range(key, start, end);
    }

    /**
     * redis List数据结构 : 批量存储
     */
    public Long lpushAll(String key, List<String> list) {
        return opsForList().leftPushAll(key, list);
    }

    /**
     * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
     */
    public void insert(String key, long index, String value) {
        opsForList().set(key, index, value);
    }
}

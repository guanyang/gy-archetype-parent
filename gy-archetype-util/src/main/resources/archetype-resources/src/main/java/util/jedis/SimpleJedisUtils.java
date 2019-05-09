package ${package}.util.jedis;

import org.apache.commons.lang3.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * 功能描述：jedis封装工具类
 * 
 * @version 2.0.0
 * @author guanyang
 */
public class SimpleJedisUtils {

    /**
     * 功能描述: 根据key获取相应封装对象
     * 
     * @param jedis
     * @param bean
     * @return
     * @version 2.0.0
     * @author guanyang
     */
    public static JedisBean getByKey(Jedis jedis,
                                     JedisBean bean) {
        String key = bean.getKey();
        if (StringUtils.isNotBlank(key)) {
            String type = jedis.type(key);
            bean.setTimeToLive(jedis.ttl(key));
            bean.setType(type);
            if (JedisType.NONE.getCode().equals(type)) {
                bean.setValue(null);
            } else if (JedisType.STRING.getCode().equals(type)) {
                bean.setValue(jedis.get(key));
            } else if (JedisType.LIST.getCode().equals(type)) {
                if (bean.getStart() == null || bean.getEnd() == null) {
                    Long size = jedis.llen(key);
                    bean.setValue(jedis.lrange(key, 0, size - 1));
                } else {
                    bean.setValue(jedis.lrange(key, bean.getStart(), bean.getEnd()));
                }
            } else if (JedisType.SET.getCode().equals(type)) {
                bean.setValue(jedis.smembers(key));
            } else if (JedisType.ZSET.getCode().equals(type)) {
                if (bean.getStart() == null || bean.getEnd() == null) {
                    Long size = jedis.zcard(key);
                    bean.setValue(jedis.zrange(key, 0, size - 1));
                } else {
                    bean.setValue(jedis.zrange(key, bean.getStart(), bean.getEnd()));
                }
            } else if (JedisType.HASH.getCode().equals(type)) {
                if (StringUtils.isBlank(bean.getField())) {
                    bean.setValue(jedis.hgetAll(key));
                } else {
                    bean.setValue(jedis.hmget(key, bean.getField().split(",")));
                }
            }
        }
        return bean;
    }

    /**
     * 功能描述: 根据key获取相应封装对象
     * 
     * @param jedis
     * @param bean
     * @return
     * @version 2.0.0
     * @author guanyang
     */
    public static JedisBean getByKey(ShardedJedis jedis,
                                     JedisBean bean) {
        String key = bean.getKey();
        if (StringUtils.isNotBlank(key)) {
            String type = jedis.type(key);
            bean.setTimeToLive(jedis.ttl(key));
            bean.setType(type);
            if (JedisType.NONE.getCode().equals(type)) {
                bean.setValue(null);
            } else if (JedisType.STRING.getCode().equals(type)) {
                bean.setValue(jedis.get(key));
            } else if (JedisType.LIST.getCode().equals(type)) {
                if (bean.getStart() == null || bean.getEnd() == null) {
                    Long size = jedis.llen(key);
                    bean.setValue(jedis.lrange(key, 0, size - 1));
                } else {
                    bean.setValue(jedis.lrange(key, bean.getStart(), bean.getEnd()));
                }
            } else if (JedisType.SET.getCode().equals(type)) {
                bean.setValue(jedis.smembers(key));
            } else if (JedisType.ZSET.getCode().equals(type)) {
                if (bean.getStart() == null || bean.getEnd() == null) {
                    Long size = jedis.zcard(key);
                    bean.setValue(jedis.zrange(key, 0, size - 1));
                } else {
                    bean.setValue(jedis.zrange(key, bean.getStart(), bean.getEnd()));
                }
            } else if (JedisType.HASH.getCode().equals(type)) {
                if (StringUtils.isBlank(bean.getField())) {
                    bean.setValue(jedis.hgetAll(key));
                } else {
                    bean.setValue(jedis.hmget(key, bean.getField().split(",")));
                }
            }
        }
        return bean;
    }

    public static Object setRedis(ShardedJedis jedis,
                                  JedisBean bean) {
        String type = bean.getType();
        String key = bean.getKey();
        int seconds = bean.getTime();
        String value = bean.getValue().toString();
        if (JedisType.STRING.getCode().equals(type)) {
            // Long result = jedis.setnx(key, value);// 设置成功，返回 1 ,设置失败，返回 0 。
            String result = jedis.set(key, value);
            Long num = jedis.ttl(key);// 当 key 存在但没有设置剩余生存时间时，返回 -1
            if (num == -1) {
                jedis.expire(key, seconds);// 避免超时时间被覆盖
            }
            return result;
        } else if (JedisType.LIST.getCode().equals(type)) {
            Long result = jedis.rpush(key, value);// 执行 RPUSH 操作后，表的长度。
            if (result == 1) {
                jedis.expire(key, seconds);// 避免超时时间被覆盖
            }
            return result;
        } else if (JedisType.SET.getCode().equals(type)) {
            Long result = jedis.sadd(key, value);// 被添加到集合中的新元素的数量，不包括被忽略的元素。
            Long num = jedis.scard(key);// 集合的基数,当 key 不存在时，返回 0 。
            if (num == 1) {
                jedis.expire(key, seconds);// 避免超时时间被覆盖
            }
            return result;
        } else if (JedisType.ZSET.getCode().equals(type)) {
            Long result = jedis.zadd(key, bean.getScore(), value);// 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员
            Long num = jedis.zcard(key);// 集合的基数,当 key 不存在时，返回 0 。
            if (num == 1) {
                jedis.expire(key, seconds);// 避免超时时间被覆盖
            }
            return result;
        } else if (JedisType.HASH.getCode().equals(type)) {
            // 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。
            // 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
            Long result = jedis.hset(key, bean.getField(), value);
            Long num = jedis.hlen(key);// 返回哈希表 key 中域的数量。
            if (num == 1) {
                jedis.expire(key, seconds);// 避免超时时间被覆盖
            }
            return result;
        }
        return null;
    }

    /**
     * 功能描述: 分布式锁
     * 
     * @param redisClient redis客户端
     * @param key 分布式锁key
     * @param value 对应值
     * @param seconds 超时时间
     * @return 成功标志，1写锁成功，0失败
     * @version 2.0.0
     * @author guanyang
     */
    public static Long lock(ShardedJedisClient redisClient,
                            final String key,
                            final String value,
                            final int seconds) {
        return redisClient.execute(new ShardedJedisAction<Long>() {
            @Override
            public Long doAction(ShardedJedis jedis) {
                Long temp = jedis.setnx(key, value);
                if (temp.intValue() == 1) {// 为什么这么弄？不然每次都会设置超时时间，导致超时时间不准确
                    jedis.expire(key, seconds);// 超时设置
                }
                return temp;
            }
        });
    }

    /**
     * 功能描述: 释放分布式锁（即删除）
     * 
     * @param redisClient redis客户端
     * @param key 分布式锁key
     * @return 被删除 key 的数量
     * @version 2.0.0
     * @author guanyang
     */
    public static Long release(ShardedJedisClient redisClient,
                               final String key) {
        try {
            return redisClient.execute(new ShardedJedisAction<Long>() {
                @Override
                public Long doAction(ShardedJedis jedis) {
                    return jedis.del(key);
                }
            });
        } catch (Exception e) {
            return 0L;
        }

    }
}

package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

	private final JedisPool jedisPool;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}

	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	public Seckill getSeckill(long seckillId) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckillId;
				// 没有使用jdk的序列化工具，使用开源序列化工具，因为开源的更快更节省空间
				/**
				 * get: byte[] 反序列化 Object(Seckill)
				 */
				// 获取redis中的字节数组
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null) {
					// 空对象
					Seckill seckill = schema.newMessage();
					// 通过得到的字节数组和schema.反序列化将seckill对象初始化
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					// seckill被反序列化
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		// set:Object -> btye[]反序列化
		try {
			Jedis jedis = jedisPool.getResource();
			try {

				String key = "seckill:" + seckill.getSeckillId();
				/**
				 * 序列化seckill对象
				 */
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

				// 超时缓存，一小时后缓存进去的bytes失效
				int timeout = 60 * 60;// 一小时
				// setex会返回一个String类型的结果，表示设置缓存是否成功
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;

	}
}

package seckill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")

/**
 * RedisDao测试类
 */
public class RedisDaoTest {

	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;
	@Test
	public void test() {
		int seckillId = 1001;
		Seckill seckill = redisDao.getSeckill(seckillId);
		
		//如果redis中没有，
		if( seckill== null) {
			//则直接从数据库中取数据
			seckill = seckillDao.queryById(seckillId);
			if(seckill != null) {
				System.out.println("从数据库中获取:" + seckill);
				//并且存入redis中
				String result= redisDao.putSeckill(seckill);
				System.out.println("存入redis缓存中...");
				System.out.println("存入结果->result: " + result);
			}else {
				System.out.println("无id为" + seckillId + "的商品");
			}
		}else {
			System.out.println("从redis缓存中获取:" + seckill);
		}
		
	}
}

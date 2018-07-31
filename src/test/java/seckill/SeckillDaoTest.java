package seckill;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * SeckillDao测试类
 * spring和junit整合
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SeckillDaoTest {

	@Resource
	private SeckillDao seckillDao;
	@Test
	public void testQueryById() {
		long i = 1000;
		Seckill seckill = seckillDao.queryById(i);
		System.out.println(seckill.getName());
	}
	
	@Test
	public void testQueryAll() {
		List<Seckill> list = seckillDao.queryAll(0, 30);
		System.out.println(list.size());
	}
	
	@Test
	public void testReduceNumber() {
		int a = seckillDao.reduceNumber(1000, new Date());
		System.out.println(a);
	}
}

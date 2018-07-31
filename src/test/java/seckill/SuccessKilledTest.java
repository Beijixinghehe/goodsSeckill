package seckill;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * SuccessKilledDao测试类
 * @author Administrator
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledTest {
	
	@Resource
	private SuccessKilledDao successKilledDao;
//	@Test
//	public void testInsertSuccessKilled() {
//		long id = 1000L;
//		long phone = 18758655865L;
//		int a = successKilledDao.insertSuccessKilled(id, phone);
//		System.out.println(a);
//	}
	
	@Test
	public void testQueryByIdWithSeckill() {
		long id = 1000L;
		SuccessKilled a = successKilledDao.queryByIdWithSeckill(id,18758655865L);
		System.out.println(a);
		System.out.println(a.getSeckill().getName());
	}
	
	
}

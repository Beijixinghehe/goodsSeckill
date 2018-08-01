package seckill;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
		log.info("list={}", list);
	}

	@Test
	public void testGetById() {
		Seckill seckill = seckillService.getById(1000);
		log.info("seckill={}", seckill);
	}

	/**
	 * exportSeckillUrl方法测试
	 */
	@Test
	public void testExportSeckillUrl() {
		Exposer exposer = seckillService.exportSeckillUrl(1000L);
		log.info("exposer={}", exposer);
	}

	/**
	 * executeSeckill方法测试
	 */
	@Test
	public void testExecuteSeckill() {
		SeckillExecution seckillExecution = null;
		long id = 1000L;
		long phone = 1875858585L;
		String md5 = "09cac22e731dbfaf2680e7ae1a824b49";
		try {
			seckillExecution = seckillService.executeSeckill(id, phone, md5);
			log.info("seckillExecution={}", seckillExecution);
		} catch (RepeatKillException e1) {
			log.error(e1.getMessage());
		} catch (SeckillCloseException e2) {
			log.error(e2.getMessage());
		} catch (SecurityException e3) {
			log.error(e3.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 整合测试
	 */
	@Test
	public void testSeckillLogic() {
		Exposer exposer = seckillService.exportSeckillUrl(1001L);

		// 如果秒杀开启
		if (exposer.isExposed()) {
			log.info("exposer={}", exposer);

			SeckillExecution seckillExecution = null;
			long id = exposer.getSeckilld();
			long phone = 1875858585L;
			String md5 = exposer.getMd5();
			try {
				seckillExecution = seckillService.executeSeckill(id, phone, md5);
				log.info("seckillExecution={}", seckillExecution);
			} catch (RepeatKillException e1) {
				log.error(e1.getMessage());
			} catch (SeckillCloseException e2) {
				log.error(e2.getMessage());
			} catch (SecurityException e3) {
				log.error(e3.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else {
			// 如果秒杀未开启
			log.warn("秒杀未开启exposer={}" + exposer);
		}
	}

	/**
	 * 通过mysql存储过程执行秒杀，测试
	 */
	@Test
	public void executeSeckillByProcedure() {
		Exposer exposer = seckillService.exportSeckillUrl(1002L);

		// 如果秒杀开启
		if (exposer.isExposed()) {
			log.info("exposer={}", exposer);

			SeckillExecution seckillExecution = null;
			long id = exposer.getSeckilld();
			long phone = 1875858585L;
			String md5 = exposer.getMd5();
			try {
				seckillExecution = seckillService.executeSeckillByProcedure(id, phone, md5);
				log.info("seckillExecution={}", seckillExecution);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		} else {
			// 如果秒杀未开启
			log.warn("秒杀未开启。。。exposer={}" + exposer);
		}
	}

}

package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;

/**
 * 业务接口：站在“使用者”的角度来设计接口
 * 三个方面：方法定义粒度，参数，返回类型（return 类型/异常）
 * @author Administrator
 *
 */
public interface SeckillService {
	
	/**
	 * 查询所有秒杀商品
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 根据id查询秒杀商品
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * 秒杀开启时输出秒杀的接口地址，否则输出系统时间和秒杀开启时间
	 * @param seckilld
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
		throws Exception;
	
	/**
	 * 通过存储过程执行秒杀操作
	 */
	SeckillExecution executeSeckillByProcedure(long seckillId, 
			long userPhone, String md5);
}

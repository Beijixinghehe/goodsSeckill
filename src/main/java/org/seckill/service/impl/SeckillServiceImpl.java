package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


@Service
public class SeckillServiceImpl implements SeckillService {

	//加入日志对象
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	//加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
    private final String salt = "shsdssljddl";
	
    /**
	 * 注入Dao
	 */
	@Resource
	private SeckillDao seckillDao;
	@Resource
	private SuccessKilledDao successKilledDao;
	@Autowired
	private RedisDao redisDao;
	
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 30);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	/**
	 * 将商品的状态封装到Exposer对象中(已经开始秒杀的商品信息，未开始和已结束的商品秒杀的信息)
	 */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		//优化点:缓存优化
		/**
		 * 伪码
		 * get from cache
		 * if null
		 * 	get db
		 * else
		 * 	put cache
		 * locgoin
		 */
		Seckill seckill = redisDao.getSeckill(seckillId);
		
		if(seckill == null) {
			seckill = getById(seckillId);
			if(seckill !=null ) {
				String result = redisDao.putSeckill(seckill);
				//日志打印，把seckill存入redis的结果
				log.info("redis set result:"+ result);
			}
		}

		if(seckill != null) {
			Date startTime = seckill.getStartTime();
			Date endTime = seckill.getEndTime();
			//系统当前时间
			Date now = new Date();
			if(now.getTime()<startTime.getTime() || 
					now.getTime()>endTime.getTime()) {
				return new Exposer(false, seckillId, now.getTime(), 
						startTime.getTime(), endTime.getTime());
			}
			
			String md5 = getMD5(seckillId);
			return new Exposer(true, md5, seckillId);
		}
		
		return null;
		
		
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" +salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
		
	}
	
	/**
	 * 执行秒杀
	 */
	@Override
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws Exception {

		
		if(md5==null || !md5.equals(getMD5(seckillId))) {
			//md5被篡改，抛出异常
			throw new SeckillException("seckill data rewrite");
		}
		Date now = new Date();
		
		try {
			int updateCount= seckillDao.reduceNumber(seckillId, now);
			
			//跟新记录小于等于0，表示秒杀结束(秒杀未开始，或者秒杀已结束，或库存不足，或商品不存在)
			if(updateCount<=0) {
				throw new SeckillCloseException("Seckill is closed");
			} else {
				
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				//insertCount小于等于0，表示相同用户重复秒杀
				if(insertCount<=0) {
					throw new RepeatKillException("seckill repeated");
				} else {
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilledDao.queryByIdWithSeckill(seckillId, userPhone));
				}
			}
		} catch (SeckillCloseException e1){
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			/**
			 * 捕获其他编译期异常，抛出运行时异常SeckillException，
			 * 让spring事务管理捕获，从而回滚
			 */
			throw new SeckillException("seckill inner error" + e.getMessage());
		}		
	}

}

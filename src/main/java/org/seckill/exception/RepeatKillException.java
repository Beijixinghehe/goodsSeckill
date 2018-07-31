package org.seckill.exception;


/**
 * 重复秒杀异常，当一个用户对同一个商品秒杀大于1次时抛出异常
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class RepeatKillException extends SeckillException {

	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatKillException(String message) {
		super(message);
	}

}

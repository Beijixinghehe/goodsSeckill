package org.seckill.exception;

/**
 * 秒杀关闭异常，当秒杀结束时或者秒杀未开始时用户还要秒杀时，抛出异常
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class SeckillCloseException extends SeckillException {

	public SeckillCloseException(String message) {
		super(message);
	}

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

}

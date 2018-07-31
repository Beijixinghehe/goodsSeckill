package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/**
 * 封装秒杀执行后的结果
 * 
 * @author Administrator
 *
 */
public class SeckillExecution {

	// 商品id
	private long seckillId;

	// 秒杀状态
	private int state;

	// 秒杀状态的说明
	private String stateInfo;

	// 秒杀成功的详情
	private SuccessKilled successKilled;

	public SeckillExecution(long seckillId, SeckillStatEnum sse, SuccessKilled successKilled) {
		this.seckillId = seckillId;
		this.state = sse.getState();
		this.stateInfo = sse.getStateInfo();
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId, SeckillStatEnum sse) {
		this.seckillId = seckillId;
		this.state = sse.getState();
		this.stateInfo = sse.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", stateInfo=" + stateInfo
				+ ", successKilled=" + successKilled + "]";
	}

}

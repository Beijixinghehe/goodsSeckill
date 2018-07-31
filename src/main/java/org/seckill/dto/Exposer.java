package org.seckill.dto;

/**
 * 封装商品的状态，判断是否暴露秒杀接口
 * @author Administrator
 *
 */
public class Exposer {

	//是否开启秒杀
	private boolean exposed;
	
	//一种加密措施
	private String md5;
	
	//id
	private long seckilld;
	
	//当前时间
	private long now;
	
	//开始时间
	private long start;
	
	//结束时间
	private long end;

	
	public Exposer(boolean exposed, String md5, long seckilld) {
		this.exposed = exposed;
		this.md5 = md5;
		this.seckilld = seckilld;
	}

	public Exposer(boolean exposed, long seckilld, long now, long start, long end) {
		this.exposed = exposed;
		this.seckilld = seckilld;
		this.now = now;
		this.start = start;
		this.end = end;
	}

	public Exposer(boolean exposed, long seckilld) {
		this.exposed = exposed;
		this.seckilld = seckilld;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSeckilld() {
		return seckilld;
	}

	public void setSeckilld(long seckilld) {
		this.seckilld = seckilld;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckilld=" + seckilld + ", now=" + now + ", start="
				+ start + ", end=" + end + "]";
	}
	
	
}

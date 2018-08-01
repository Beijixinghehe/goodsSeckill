package org.seckill.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	@Resource
	private SeckillService seckillService;

	/**
	 * 秒杀列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String seckillList(Model model) {
		List<Seckill> list = seckillService.getSeckillList();
		model.addAttribute("list", list);
		return "list";
	}

	/**
	 * 秒杀详情页
	 * 
	 * @param seckillId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/detail", method=RequestMethod.GET)
	public String seckillDetail(@PathVariable("seckillId") Long seckillId, Model model) {
		if (seckillId == null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill = seckillService.getById(seckillId);
		if (seckill == null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill", seckill);
		return "detail";
	}

	/**
	 * ajax/json 暴露秒杀接口
	 * 
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {

		if (seckillId == null) {
			return new SeckillResult<Exposer>(false, "seckillId is null");
		}

		try {
			return new SeckillResult<Exposer>(true, seckillService.exportSeckillUrl(seckillId));
		} catch (Exception e) {
			return new SeckillResult<Exposer>(false, e.getMessage());
		}
	}

	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param phone
	 * @param md5
	 * @return
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	//required = false表示 cookie killPhone为空时不抛出异常
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
			@CookieValue(value = "killPhone", required = false) Long phone, @PathVariable("md5") String md5) {

		if (phone == null) {
			return new SeckillResult<SeckillExecution>(true, "未登录");
		}
		try {
			SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (SeckillCloseException e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (Exception e) {
			SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(true, execution);
		}
	}
	
	/**
	 * 获取系统时间
	 * @return
	 */
	@RequestMapping(value="/time/now", method=RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time(){
		
		Date date = new Date();
		return new SeckillResult<Long>(true, date.getTime());
	}
}

//存放主要交互逻辑
// javascript 模块化

var seckill = {
	// 封装秒杀相关ajax的url
	URL : {
		now : function() {
			return '/seckill/time/now';
		},
		exposer : function(seckillId) {
			return '/seckill/' + seckillId + '/exposer';
		},
		execution : function(seckillId, md5) {
			return '/seckill/' + seckillId + '/' + md5 + '/execution'
		}
	},
	//判断手机号是否正确的方法
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	handlerSeckill : function(seckillId,seckillBox){ 
		//向后台请求，获取exposer，判断是否暴露秒杀接口
		$.post(seckill.URL.exposer(seckillId),{},function(result){
			
			if(result && result['success']){//如果为true，表示请求成功
				var exposer = result['data'];
				if(exposer['exposed']){//如果为true，则暴露秒杀的按钮
					seckillBox.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
					var md5 = exposer['md5'];
					//秒杀地址
					var seckillUrl = seckill.URL.execution(seckillId,md5);
					
					//绑定一次点击事件，避免多个请求发到服务端。
					$('#killBtn').one('click',function(){
						//点击后禁用该按钮
						$(this).addClass('disabled');
						//发送秒杀请求
						$.post(seckillUrl,{},function(result){
							if(result && result['success']){//请求成功
								var execution = result['data'];
								var state = execution['state'];
								var stateInfo = execution['stateInfo'];
								seckillBox.html('<span class="label label-success">' + stateInfo + '</span>');
							}else{//请求失败
								seckillBox.html('<span class="label label-danger">请求失败!</span>');
							}
							
						});
					});
					seckillBox.show();
				}else{//如果为false表示，客户端计时器偏快，商品秒杀其实还未开始。继续执行计时器
					seckill.countDown(exposer['now'],exposer['start'],
							exposer['end'],exposer['seckillId']);
				}
				
			}else{//请求失败
				seckillBox.html('<span class="label label-danger">请求失败!</span>');
			}
		})
	},
	//判断时间
	countDown : function(nowTime, startTime, endTime, seckillId) {
		var seckillBox = $('#seckill-box');
		if(nowTime > endTime){//秒杀结束
			seckillBox.html('秒杀结束!');
		}else if(nowTime < startTime){//秒杀未开始，开始计时
			var killTime = new Date(startTime + 1000);
			seckillBox.countdown(killTime, function(event) {
				var format = event.strftime('秒杀倒计时: %D天  %H时  %M分  %S秒');
				seckillBox.html(format);
				
			}).on('finish.countdown',function(){//绑定计时结束后的事件
				console.log('______fininsh.countdown');
				//计时结束调用handlerSeckill方法向后端发送post请求
                seckill.handlerSeckill(seckillId, seckillBox);
			});
		}else {//秒杀开始
			seckill.handlerSeckill(seckillId, seckillBox);
		}
	},

	// 详情页秒杀逻辑
	detail : {
		// 详情页初始化
		init : function(params) {
			// 手机验证和登录，计时交互
			// 规划交互流程
			// 在Cookie中查找手机号
			var killPhone = $.cookie('killPhone');

			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			
			// 验证手机号
			// 如果cookie中的手机号为空或者有误
			if (!seckill.validatePhone(killPhone)) {
				// 绑定phone
				// 控制输出
				
				var killPhoneModal = $('#killPhoneModal');
				//显示弹出层
				killPhoneModal.modal({
					show : true,// 显示弹出层
					backdrop : 'static',// 禁止鼠标拖拽位置关闭
					keyboard : false
				});
				
				$('#killPhoneBtn').click(function() {
					//获取表单的值
					var inputPhone = $('#killPhoneKey').val();
					//如果输入的电话正确
					if(seckill.validatePhone(inputPhone)){
						//把phone存入cookie
						//path表示访问/seckill路径下的url时才会把killPhone这个cookie传到后端
						//可以减少不需要的cookie传到后端，减少发送量，和服务器接收量
						$.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
						//刷新页面
						window.location.reload();
					}else{
						$('#killPhoneMessage').hide().html('<label class="label label-danger text-center">手机号输入错误</label>').show(300);
					}
				});
			}
			//已经登录
			//计时交互
			//通过$.get请求后台，获得服务器的当前时间
			$.get(seckill.URL.now(),{},function(result){
				if(result && result['success']){
					var nowTime = result['data'];
					//判断时间
					seckill.countDown(nowTime,startTime,endTime,seckillId);
				}else{
					console.log('result:' + result);
					alter('result:' + result);
				}
			});
		}
	}
}

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%String path = request.getContextPath(); %>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>秒杀列表页</title>

<!-- 把重复的配置通过jsp的 @include加到本页面(@include 
     	是把目标jsp内容加过来，成为同一个servlet)-->
<%@include file="common/head.jsp"%>
</head>
<body>
	<!-- 页面显示部分 -->
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h2>秒杀列表</h2>
			</div>
			<div class="panel-body">
				<table class="table - table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情页</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="seckill" items="${list }">
							<tr>
								<td>${seckill.name }</td>
								<td>${seckill.number }</td>
								<td>
									<fmt:formatDate value="${seckill.startTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td><fmt:formatDate value="${seckill.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${seckill.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<a class="btn btn-info" href="/seckill/${seckill.seckillId }/detail" target="_black">详情</a>
								</td>
							</tr>
						</c:forEach>
						
					</tbody>
				</table>
			</div>
		</div>
	
	</div>
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
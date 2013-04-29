<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<#import "./include/sidebar.ftl" as sidebar />
<#import "../include/page.ftl" as page />
<html>
<head>
	<title>用户管理</title>
	<@header.head/>
</head>
<body>
	<@header.header "manage"/>
	<div class="content">
		<#if !self?? || ((!self.permission?? || self.permission==0) && self.identity!=4)>
			您没有管理权限。
		<#else>
			<@sidebar.left self "user"/>
			<div class="box fr" style="width:790px;">
				<div class="title">用户管理</div>
				<div class="content">
					<div class="wrapper">
						<table class="bordered span7">
							<thead>
								<tr>
									<th>昵称</th>
									<th>真实姓名</th>
									<th>年级</th>
									<th>专业</th>
									<th>身份</th>
									<th>账号状态</th>
									<th>操作</th>
								</tr>
							<thead>
							<tbody>
							<#list users as user>
								<tr>
									<td>${user.nickName}</td>
									<td>${user.trueName}</td>
									<td>${user.grade}</td>
									<td>${user.professional}</td>
									<td>
										${statics["com.network.modal.User"].getIdentityName(user.identity)}
									</td>
									<td node-type="status">
										${statics["com.network.modal.User"].getStatusString(user.status)}
									</td>
									<td>
										<a href="javascript:;" uid="${user._id}" action-type="delete_user">删除</a>
										<#if user.status==statics["com.network.modal.User"].STATUS_WAIT_FOR_VERIFY>
											<a href="javascript:;" uid="${user._id}" action-type="verify_pass">审核通过</a>
										<#elseif user.status==statics["com.network.modal.User"].STATUS_VERIFY_PASS>
											<a href="javascript:;" uid="${user._id}" action-type="verify_refuse">审核拒绝</a>
										<#elseif user.status==statics["com.network.modal.User"].STATUS_VERIFY_REFUSE>
											<a href="javascript:;" uid="${user._id}" action-type="verify_pass">审核通过</a>
										<#elseif user.status==statics["com.network.modal.User"].STATUS_ENABLE>
											<a href="javascript:;" uid="${user._id}" action-type="verify_refuse">审核拒绝</a>
										</#if>
									</td>
								</tr>
							</#list>
							</tbody>
						</table>
						<@page.page page_index page_count "${root}/mc/user.html" />
					</div>
				</div>
			</div>
		</#if>
	</div>
	<@footer.footer/>
	
	<script>
	seajs.use(['thirdparty/jquery','lib/ajax','ui/notice'],function($,ajax,notice){
		var $table = $('table.bordered');
		$table.delegate('a[action-type="delete_user"]','click',function(){
			if(!window.confirm("确定删除该用户？")){
				return;
			}
			var id = $(this).attr("uid");
			ajax.sendAjax({
				url:'${root}/mc/user/delete.json',
				data:{id:id},
				successCallback:function(json){
					if(json.status === 1){
						window.location.reload();
					}else{
						new notice.Error('错误',json.msg);
					}
				}
			});
		});
		$table.delegate('a[action-type="verify_pass"]','click',function(){
			var $self = $(this);
			var id = $self.attr("uid");
			ajax.sendAjax({
				url:'${root}/mc/user/verify/pass.json',
				data:{id:id},
				successCallback:function(json){
					if(json.status === 1){
						$self.attr('action-type','verify_refuse');
						$self.text('审核拒绝');
						$self.closest('tr').find('td[node-type="status"]').text('审核通过');
					}else{
						new notice.Error('错误',json.msg);
					}
				}
			});
		});
		$table.delegate('a[action-type="verify_refuse"]','click',function(){
			var $self = $(this);
			var id = $self.attr("uid");
			ajax.sendAjax({
				url:'${root}/mc/user/verify/refuse.json',
				data:{id:id},
				successCallback:function(json){
					if(json.status === 1){
						$self.attr('action-type','verify_pass');
						$self.text('审核通过');
						$self.closest('tr').find('td[node-type="status"]').text('审核拒绝');
					}else{
						new notice.Error('错误',json.msg);
					}
				}
			});
		});
	});
	</script>
</body>
</html>
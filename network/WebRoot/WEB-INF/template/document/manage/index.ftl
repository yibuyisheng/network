<!DOCTYPE html>
<#import "../../include/header.ftl" as header />
<#import "../../include/footer.ftl" as footer />
<#import "../../mc/include/sidebar.ftl" as sidebar />
<#import "../../include/page.ftl" as page />
<html>
<head>
	<title>文档管理</title>
	<@header.head/>
</head>
<body>
	<@header.header "manage"/>
	<div class="content fix">
		<@sidebar.left self "document"/>
		<div class="box fr" style="width:790px;">
			<div class="title">文档管理</div>
			<div class="content">
				<div class="wrapper">
					<div class="pager">
						<div class="controll fix">
							<ul>
								<li <#if documentStatus==statics["com.network.modal.DocumentModal"].STATUS_AVAILABLE>class="active"</#if>>
									<a href="${root}/doc/manage.html?status=${statics["com.network.modal.DocumentModal"].STATUS_AVAILABLE}">可用文档</a>
								</li>
								<li <#if documentStatus==statics["com.network.modal.DocumentModal"].STATUS_DRAFT>class="active"</#if>>
									<a href="${root}/doc/manage.html?status=${statics["com.network.modal.DocumentModal"].STATUS_DRAFT}">草稿</a>
								</li>
								<li <#if documentStatus==statics["com.network.modal.DocumentModal"].STATUS_MANAGER_FORBID>class="active"</#if>>
									<a href="${root}/doc/manage.html?status=${statics["com.network.modal.DocumentModal"].STATUS_MANAGER_FORBID}">管理员禁用</a>
								</li>
								<li <#if documentStatus==statics["com.network.modal.DocumentModal"].STATUS_MANAGER_DELETE>class="active"</#if>>
									<a href="${root}/doc/manage.html?status=${statics["com.network.modal.DocumentModal"].STATUS_MANAGER_DELETE}">管理员删除</a>
								</li>
								<li <#if documentStatus==statics["com.network.modal.DocumentModal"].STATUS_SELF_DELETE>class="active"</#if>>
									<a href="${root}/doc/manage.html?status=${statics["com.network.modal.DocumentModal"].STATUS_SELF_DELETE}">自己删除</a>
								</li>
							</ul>
						</div>
						<div class="frames tl">
							<div class="active">
								<#if documents?size gt 0>
									<ul>
										<#list documents as document>
											<li>
												<a href="${root}/doc/get.html?id=${document.id}" target="_blank">${document.title}</a>
												<span node-type="time_show" time="${document.uploadTime}"></span>
												<span node-type="operation">
													<#if documentStatus!=statics['com.network.modal.DocumentModal'].STATUS_MANAGER_DELETE>
														<a href="javascript:;" doc-id="${document.id}" action-type="delete">删除</a>
													</#if>
													<#if documentStatus==statics['com.network.modal.DocumentModal'].STATUS_AVAILABLE>
														<a href="javascript:;" doc-id="${document.id}" action-type="forbid">禁用</a>
													</#if>
													<#if documentStatus==statics['com.network.modal.DocumentModal'].STATUS_MANAGER_FORBID>
														<a href="javascript:;" doc-id="${document.id}" action-type="unforbid">启用</a>
													</#if>
												</span>
											</li>
										</#list>
									</ul>
									<@page.page page_index page_count '${root}/doc/manage.html?status=${documentStatus}' />
								</#if>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<@footer.footer/>
	
	<script>
        seajs.use(['thirdparty/jquery','lib/timeFormat','modules/user/loginDialog','lib/ajax','ui/notice','lib/timeFormat','ui/ui_strengthen'],function($,timeFormat,LoginDialog,ajax,notice,timeFormat,ui_strengthen){
        	ui_strengthen.initTime();
        	var $pager = $('.pager');
        	$pager.delegate('a[action-type="delete"]','click',function(event){
        		if(!window.confirm("确定删除？")){
        			return;
        		}
        		var $self = $(this);
        		var docId = $self.attr('doc-id');
        		ajax.sendAjax({
        			url:'${root}/doc/delete.json',
        			data:{id:docId},
        			successCallback:function(json){
        				if(json.status === 1){
        					new notice.Success("成功","删除成功！",function(){
        						window.location.reload();
        					});
        				}else{
        					new notice.Error("错误",json.msg);
        				}
        			}
        		});
        	});
        	$pager.delegate('a[action-type="forbid"]','click',function(event){
        		var $self = $(this);
        		var docId = $self.attr('doc-id');
        		ajax.sendAjax({
        			url:'${root}/doc/forbid.json',
        			data:{id:docId},
        			successCallback:function(json){
        				if(json.status === 1){
        					new notice.Success("成功","禁用成功！",function(){
        						window.location.reload();
        					});
        				}else{
        					new notice.Error("错误",json.msg);
        				}
        			}
        		});
        	});
        	$pager.delegate('a[action-type="unforbid"]','click',function(event){
        		var $self = $(this);
        		var docId = $self.attr('doc-id');
        		ajax.sendAjax({
        			url:'${root}/doc/unforbid.json',
        			data:{id:docId},
        			successCallback:function(json){
        				if(json.status === 1){
        					new notice.Success("成功","启用成功！",function(){
        						window.location.reload();
        					});
        				}else{
        					new notice.Error("错误",json.msg);
        				}
        			}
        		});
        	});
        });
	</script>
</body>
</html>
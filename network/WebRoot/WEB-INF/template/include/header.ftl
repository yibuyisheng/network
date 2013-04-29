<#macro header page>
	<div class="header">
		<div class="log fl"><img src="${static_root}/images/log.png"/></div>
		<div class="user">
			<#if self??>
				欢迎${self.nickName}&nbsp;<a href="javascript:;" action-type="exit">退出</a>
				<script>
					seajs.use(['thirdparty/jquery','ui/notice'],function($){
						$('.header .user a[action-type="exit"]').click(function(){
							$.ajax({
								type:'post',
								url:'${root}/uc/exit.json',
								dataType:'text',
								success:function(data){
									var json = eval('('+data+')');
									if(json.status === 1){
										window.location.href="${root}/page/course.html";
									}else{
										new notice.Error("错误",json.msg);
									}
								}
							});
						});
					});
				</script>
			<#else>
				<a href="${root}/uc/login.html">登录</a>&nbsp;<a href="${root}/uc/registe.html">注册</a>
			</#if>
		</div>
		<div class="nav">
			<a href="${root}/page/course.html" <#if page?? && page=='course'>class="active"</#if>>课程</a>
			<a href="${root}/posts/all.html" <#if page?? && page=='forum'>class="active"</#if>>论坛</a>
			<a href="${root}" <#if page?? && page=='forward_back'>class="active"</#if>>反馈</a>
			<a href="${root}/mc/index.html" <#if page?? && page=='manage'>class="active"</#if>>管理</a>
		</div>
	</div>
</#macro>

<#macro head>
	<meta charset="utf-8"/>
	<link rel="shortcut icon" href="${static_root}/images/log.png">
	<link href="${static_root}/css/global.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${static_root}/js/package.js"></script>
</#macro>
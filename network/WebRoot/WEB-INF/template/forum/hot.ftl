<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<#import "../include/page.ftl" as page />
<html>
<head>
	<title>最热帖子</title>
	<@header.head/>
</head>
<body class="forum">
	<@header.header "forum"/>
	<div class="content">
        <div class="pager">
			<div class="controll fix">
				<ul>
					<li>
						<a href="${root}/posts/all.html">最新</a>
					</li class="active">
					<li class="active">
						<a href="${root}/posts/hot.html">热门</a>
					</li>
					<li class="fr">
						<a href="${root}/posts/publish.html" target="_blank" class="button_orange fwb">发帖</a>
					</li>
				</ul>
			</div>
			<div class="frames tl">
				<div class="active posts">
					<#if status==statics["com.network.dealer.DealerParamWrap"].STATUS_SUCCESS>
						<#list posts as post>
							<div class="row fix">
								<div class="span4 fl text-hide tl fs14" title="${post.title}">
									<a href="${root}/posts/post.html?id=${post._id}" target="_blank">${post.title}</a>
								</div>
								<div class="fl span1-5 fs12 tc"><a href="javascript:;">${post.provider.nickName}</a></div>
								<div class="fl span1-5 fs12 tc">
									<span node-type="time_show" time="${post.createTime}"></span>
								</div>
								<div class="fl span1-5 fs12 tc">更新：<span node-type="time_show" time="${post.refreshTime}"></span></div>
								<div class="fl fs12 tc">
									回复数：${post.replyNo}
								</div>
							</div>
						</#list>
						<@page.page page_index page_count "${root}/posts/all.html"/>
					</#if>
				</div>
			</div>
		</div>
	</div>
	<@footer.footer />
	<script>
        seajs.use(['thirdparty/jquery','lib/timeFormat','modules/user/loginDialog','lib/ajax','ui/notice','lib/timeFormat','ui/ui_strengthen'],function($,timeFormat,LoginDialog,ajax,notice,timeFormat,ui_strengthen){
        	ui_strengthen.initTime();
        });
	</script>
</body>
</html>
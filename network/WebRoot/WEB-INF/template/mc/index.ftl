<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<#import "./include/sidebar.ftl" as sidebar />
<html>
<head>
	<title>后台管理</title>
	<@header.head/>
</head>
<body>
	<@header.header "manage"/>
	<div class="content fix">
		<@sidebar.left self ""/>
		<div class="span7 fr">数据宝贵，谨慎操作。</div>
	</div>
	<@footer.footer/>
	
</body>
</html>
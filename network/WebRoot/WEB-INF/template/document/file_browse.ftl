<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>图片文件浏览</title>
	<link href="${static_root}/css/global.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${static_root}/js/package.js"></script>
</head>
<body>
	<div class="header">
		图片文件浏览
	</div>
	<div class="content">
		<#--5 columns each row-->
		<#assign columns = 5 >
		<table border="1">
			<#list files as file>
				<#if file_index % columns==0>
					<tr>
				</#if>
				<td style="width:200px;height:200px;word-break:break-all">
					<img alt="" src="${root}/doc/image?name=${file.name}" style="max-width:100px;max-height:200px;"/><br/>
					<a href="javascript:;" action-type="image_choose" url="${root}/doc/image?name=${file.name}">${file.name}</a>
				</td>
				<#if file_index % columns==columns-1>
					</tr>
				</#if>
			</#list>
		</table>
	</div>
	<div class="footer">
		底部
	</div>
	
	<script type="text/javascript">
	//http://localhost:8080/network/doc/browse/images?type=Images&CKEditor=editor1&CKEditorFuncNum=3&langCode=zh-cn
		seajs.use(['thirdparty/jquery','lib/utils'],function($,utils){
			$('a[action-type="image_choose"]').click(function(){
				var funcNum = utils.getStrBetween(window.location.href,'CKEditorFuncNum=','&');
				var fileUrl = $(this).attr('url');
				window.opener.CKEDITOR.tools.callFunction(funcNum,fileUrl);
				window.close();
			});
		});
	</script>
</body>
</html>
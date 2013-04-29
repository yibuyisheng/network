<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>登陆</title>
	<link href="${static_root}/css/global.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${static_root}/js/package.js"></script>
</head>
<body>
	<div class="header">
		登陆
	</div>
	<div class="content">
		<textarea name="editor1">&lt;p&gt;Initial value.&lt;/p&gt;</textarea>
	</div>
	<div class="footer">
		底部
	</div>
	
	<script type="text/javascript">
		seajs.use(['thirdparty/jquery','thirdparty/ckeditor/ckeditor'],function($,CKEDITOR){
			CKEDITOR.replace( 'editor1',{
			    filebrowserBrowseUrl: '${root}/doc/browse/images?type=Images'
			} );
		});
	</script>
</body>
</html>
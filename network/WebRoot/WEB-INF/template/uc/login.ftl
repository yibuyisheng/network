<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<html>
<head>
	<title>登陆</title>
	<@header.head />
</head>
<body>
	<@header.header ""/>
	<div class="content">
		<div class="box">
			<div class="title">登陆</div>
			<div class="content" style="text-align:left;">
				<form action="${root}/uc/login.json" method="POST" id="login">
					<ul>
						<li>
							<label class="label">昵称：</label>
							<input type="text" class="text length_middle" name="nick_name" tabindex="1"/>
						</li>
						<li>
							<label class="label">密码：</label>
							<input type="password" class="text length_middle" name="password" tabindex="9"/>
						</li>
						<li><input type="button" class="button_orange ml450" value="提交" tabindex="11"/></li>
					</ul>
				</form>
			</div>
		</div>
	</div>
	<@footer.footer/>
	
	<script type="text/javascript">
		seajs.use(['thirdparty/jquery','ui/notice'],function($,notice){
			var $form = $('#login'),
				$submit = $form.find('input[type="submit"]'),
				$nickName = $form.find('input[name="nick_name"]'),
				$password = $form.find('input[name="password"]');
			
			$form.find('input[type="button"]').bind('click',function(){
				
				if($submit.text()==='提交中...') return false;
				$submit.text('提交中...');
				$submit.removeClass('button_orange').addClass('button_disable');
				$.ajax({
					url:$form.attr('action'),
					type:$form.attr('method'),
					data:$form.serialize(),
					dataType:'text',
					success:function(data){
						var json = eval('('+data+')');
						if(json.status!==1){
							new notice.Error('失败',json.msg);
						}else{
							window.location.reload();
						}
						$submit.text('提交');
						$submit.removeClass('button_disable').addClass('button_orange');
					},
					error:function(){
						new notice.Error('失败','网络可能出现问题了！');
					}
				});
				return false;
			});
		});
	</script>
</body>
</html>
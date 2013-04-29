<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<html>
<head>
	<title>注册</title>
	<@header.head/>
</head>
<body>
	<@header.header ""/>
	<div class="content">
		<div class="box">
			<div class="title">注册</div>
			<div class="content" style="text-align:left;">
				<form action="${root}/uc/registe.json" method="POST" id="registe">
					<ul>
						<li>
							<label class="label">真实姓名：</label>
							<input type="text" class="text length_middle" name="true_name" tabindex="1"/>
						</li>
						<li>
							<label class="label">专业：</label>
							<input type="text" class="text length_middle" name="professional" tabindex="2"/>
						</li>
						<li>
							<label class="label">班级：</label>
							<input type="text" class="text length_short" name="grade" tabindex="3"/>级
							<input type="text" class="text length_short" name="class" tabindex="4"/>班
						</li>
						<li>
							<label class="label">身份：</label>
							<label>
								<input type="radio" checked="checked" name="identity" value="1" tabindex="5"/>
								<span>学生</span>
							</label>
							<label>
								<input type="radio" name="identity" value="2" tabindex="6"/>
								<span>教师</span>
							</label>
							<label>
								<input type="radio" name="identity" value="3" tabindex="7"/>
								<span>其他用户</span>
							</label>
						</li>
						<li class="separator"></li>
						<li>
							<label class="label">昵称：</label>
							<input type="text" class="text length_middle" name="nick_name" tabindex="8"/>
						</li>
						<li>
							<label class="label">密码：</label>
							<input type="password" class="text length_middle" name="password" tabindex="9"/>
						</li>
						<li>
							<label class="label">确认密码：</label>
							<input type="password" class="text length_middle" name="confirm_password" tabindex="10"/>
						</li>
						<li><input type="submit" class="button_orange ml450" value="提交" tabindex="11"/></li>
					</ul>
				</form>
			</div>
		</div>
	</div>
	<@footer.footer/>
	
	<script type="text/javascript">
		seajs.use(['thirdparty/jquery','ui/notice'],function($,notice){
			var $form = $('#registe'),
				$submit = $form.find('input[type="submit"]'),
				$trueName = $form.find('input[name="true_name"]'),
				$professional = $form.find('input[name="professional"]'),
				$grade = $form.find('input[name="grade"]'),
				$class = $form.find('input[name="class"]'),
				$identity = $form.find('input[name="identity"]'),
				$nickName = $form.find('input[name="nick_name"]'),
				$password = $form.find('input[name="password"]'),
				$confirm_password = $form.find('input[name="confirm_password"]');
			
			/*function valudate(){
				if($trueName.text().replace(/\s/,'')=='') {
					
					return;
				}else if()
			}*/
			$form.bind('submit',function(){
				
				if($submit.text()==='提交中...') return;
				$submit.text('提交中...');
				$submit.removeClass('button_orange').addClass('button_disable');
				$.ajax({
					url:$form.attr('action'),
					type:$form.attr('type'),
					data:$form.serialize(),
					dataType:'text',
					success:function(data){
						var json = eval('('+data+')');
						if(json.status!==1){
							new notice.Error('失败',json.msg);
						}else{
							new notice.Success('成功','注册成功！');
						}
						$submit.text('提交');
						$submit.removeClass('button_disable').addClass('button_orange');
					}
				});
				return false;
			});
		});
	</script>
</body>
</html>
<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<#import "../include/page.ftl" as page />
<html>
<head>
	<title>帖子<#if post??>-${post.title}</#if></title>
	<@header.head/>
</head>
<body class="forum">
	<@header.header "forum"/>
	<div class="content">
        <#if status==statics["com.network.dealer.DealerParamWrap"].STATUS_SUCCESS>
			<div class="box">
				<div class="title">${post.title}</div>
				<div class="content">
					<div class="wrapper tl fs14">${post.content}</div>
					<div style="background:black;height:3px;margin:0 5px;"></div>
					<div class="wrapper">
						<div class="fix">
							<span class="fl fs16">评论</span>
							<span class="fr fs14">共<em class="em">${post.replyNo}</em>条评论</span>
						</div>
					</div>
					<div class="wrapper comments fix">
						<ul></ul>
						<div class="page"></div>
					</div>
					<div class="wrapper tl fix">
						我要评论
						<#if self??>
							<form action="${root}/posts/reply.json">
								<ul>
									<li style="height:auto;">
										<textarea name="content" class="dn"></textarea>
										<input type="hidden" name="target" value="${post.id}"/>
									</li>
									<li><input type="button" value="提交" class="button_orange fr"/></li>
								</ul>
							</form>
						<#else>
							<p>评论请<a href="${root}/uc/login.html?url=${root}/posts/post.html?id=${post.id}">登录</a>。</p>
						</#if>
					</div>
				</div>
			</div>			
		</#if>
	</div>
	<@footer.footer />
	<script>
        seajs.use(['thirdparty/jquery','lib/timeFormat','modules/user/loginDialog','lib/ajax','ui/notice','lib/timeFormat','ui/ui_strengthen','thirdparty/ckeditor/ckeditor'],function($,timeFormat,LoginDialog,ajax,notice,timeFormat,ui_strengthen,CKEDITOR){
        	<#if self??>
	        	CKEDITOR.replace('content',{
				    filebrowserBrowseUrl: '${root}/doc/browse/images?type=Images'
				});
				
				var $form = $('form');
				$form.find('input[type="button"]').bind('click',function(){
					$form.find('textarea').val(CKEDITOR.instances.content.getData());
					ajax.sendAjax({
						url:$form.attr('action'),
						data:$form.serialize(),
						successCallback:function(json){
							if(json.status == 1){
								new notice.Success("成功","回复成功！");
							}else{
								new notice.Error("失败！",json.msg);
							}
						}
					});
				});
			</#if>
			
			var $comments = $('.comments');
			function renderPage(page_index,page_count){
				var html='<a href="javascript:;" class="'+(page_index>1?'button_gray':'button_disable')+'" action-type="pre_page" page="'+page_index+'">上一页</a>'+
							'<a href="javascript:;" class="'+(page_index < page_count ? 'button_gray' : 'button_disable' )+'" action-type="next_page" page="'+page_index+'">下一页</a>'+
					        '<label>第'+page_index+'页/共'+page_count+'页</label>';
				return html;
			}
			function renderComments(targetId,page){
				ajax.sendAjax({
					url:'${root}/posts/comments.json',
					data:{target:targetId,page:page},
					successCallback:function(json){
						if(json.status == 1){
							var html = [];
							for(var i=0,il=json.comments.length;i<il;i++){
								html.push('<li class="comment fs14">'+
											'<div class="fix">'+
												'<div class="fl span1-5 tc fs14" style="margin-top:15px;">'+json.comments[i].provider.nickName+'</div>'+
												'<div class="fl span8">'+json.comments[i].content+'</div>'+
											'</div>'+
										'</li>');
							}
							$comments.find('ul').html(html.join(''));
							
							$comments.find('.page').html(renderPage(json.comments_page_index,json.comments_page_count,window.location.href));						
						}else{
							new notice.Error("错误",json.msg);
						}
					}
				});
			}
			renderComments('${post.id}',1);

			$comments.delegate('.page a[action-type="pre_page"]','click',function(){
				if($(this).hasClass('button_disable')) return;
				renderComments('${post.id}',parseInt($(this).attr('page'))-1);
			});
			$comments.delegate('.page a[action-type="next_page"]','click',function(){
				if($(this).hasClass('button_disable')) return;
				renderComments('${post.id}',parseInt($(this).attr('page'))+1);
			});
			
        });
	</script>
</body>
</html>
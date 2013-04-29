<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<html>
<head>
	<title>文档上传</title>
	<@header.head />
</head>
<body>
	<@header.header ""/>
	<div class="content">
		<div class="box fix">
			<div class="title">文档上传设置</div>
			<div class="content" style="text-align:left;">
				<form action="${root}/doc/upload.json" method="POST" id="upload">
                    <div>
                        <label style="display:block;height:40px;">
                        	标题：
                        	<input type="text" class="text length_long" name="title" value="<#if document?? && document.title??>${document.title}</#if>"/>
                        </label>
                    </div>
					<div>
                        <textarea name="doc" class="dn"><#if document?? && document.docContent??>${document.docContent}</#if></textarea>
                        <input type="hidden" name="status" value="2"/>
                    </div>
                    <div class="fix">
                        <input type="button" class="button_orange ml450 fr" action-type="save_as_note" value="保存为草稿"/>
                        <input type="button" class="button_orange ml450 fr" action-type="submit" value="提交"/>
                    </div>
				</form>
			</div>
		</div>
	</div>
	<@footer.footer />
	
	<script type="text/javascript">
		seajs.use(['thirdparty/jquery','thirdparty/ckeditor/ckeditor','ui/notice','modules/user/loginDialog'],function($,CKEDITOR,notice,LoginDialog){
			CKEDITOR.replace('doc',{
			    filebrowserBrowseUrl: '${root}/doc/browse/images?type=Images'
			});
			
			var $form = $('#upload'),
				$title = $form.find('input[name="title"]'),
				$doc = $form.find('textarea[name="doc"]'),
				$status = $form.find('input[name="status"]'),
				$saveAsNote = $form.find('input[action-type="save_as_note"]'),
				$submit = $form.find('input[action-type="submit"]');

			function submitCallback(event){
				var $target = $(event.target);

				var loadingText = '提交中...',preText = $target.val();
				if($target.val()===loadingText) return;
				$target.val(loadingText);
				$target.removeClass('button_orange').addClass('button_disable');
				$doc.val(CKEDITOR.instances.doc.getData());
				$.ajax({
					url:$form.attr('action'),
					type:$form.attr('method'),
					data:$form.serialize(),
					dataType:'text',
					success:function(data){
						var json = eval('('+data+')');
						if(json.status!==1){
                            if(json.status===-1){
                                new LoginDialog().show();
                            }else{
                                new notice.Error('失败',json.msg);
                            }
						}else{
							new notice.Success('成功','操作成功！');
						}
						$target.val(preText);
						$target.removeClass('button_disable').addClass('button_orange');
					}
				});
			}


			/*提交*/
			$submit.click(function(event){
				$status.val(2);
				submitCallback(event);
			});
			/*保存为草稿*/
			$saveAsNote.click(function(event){
				$status.val(1);
				submitCallback(event);
			});
		});
	</script>
</body>
</html>
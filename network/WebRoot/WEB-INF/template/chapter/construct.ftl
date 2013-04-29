<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<#import "../mc/include/sidebar.ftl" as sidebar />
<html>
<head>
	<title>章节结构</title>
	<@header.head/>
</head>
<body>
	<@header.header "manage"/>
	<div class="content">
		<@sidebar.left self "chapter"/>
		<div class="box fr" style="width:790px;">
			<div class="title">章节构建</div>
			<div class="content">
				<div class="wrapper">
					<div>
		        		<a href="javascript:;" action-type="add_root">添加根目录</a>
			        </div>
			        <div id="chapter_container">
			        	<ul>
			        		<#list chapters as chapter>
			        			<li>
			        				<div>
										<a href="javascript:;" chapter-id="${chapter._id}" action-type="open" class="open" title="展开">&nbsp;</a>
						            	<span node-type="chapter_name">${chapter.name}</span>
						            	<span node-type="operations" class="operations">
							            	<a href="javascript:;" chapter-id="${chapter._id}" depth="${chapter.depth}" action-type="add_child" class="b">添加子节点</a>
							            	<a href="javascript:;" pre-name="${chapter.name}" chapter-id="${chapter._id}" action-type="modify" class="b">修改</a>
							            	<a href="javascript:;" action-type="set_content" chapter-id="${chapter._id}" doc-id="${chapter.docId!''}" class="b">内容文档</a>
							            	<a href="javascript:;" action-type="delete" chapter-id="${chapter._id}" class="b">删除</a>
						            	</span>
						            	<a href="javascript:;" class="slide-right" action-type="slide_right" title="显示操作">&nbsp;</a>
					            	</div>
					           	</li>
			        		</#list>
			        	</ul>
			        </div>
				</div>
			</div>
		</div>
	</div>
	<@footer.footer/>
	<script>
        seajs.use(['thirdparty/jquery','ui/dialog','lib/ajax','ui/notice','modules/user/loginDialog'],function($,Dialog,ajax,notice,LoginDialog){
        	var $chapterContainer = $('#chapter_container');
        	/*
        	operation=1表示添加
        	operation=2表示修改
        	*/
			function addNode(event){
				var $self = $(event.target);
				var parentDepth = $self.attr('depth');
				var isAddRoot = !parentDepth;
				var dlgContent = ['<form action="${root}/chapter/add.json" method="POST">',
									'<ul>',
										'<li>',
											'<label class="label">名称：</label>',
											'<input type="text" class="text length_middle" name="name" value="">',
											'<input type="hidden" name="depth" value="' + ( isAddRoot ? 1 : (parseInt(parentDepth)+1) )+'"/>',
											'<input type="hidden" name="parent_id" value="'+(!!$self.attr('chapter-id') ? $self.attr('chapter-id') : '')+'">',
										'</li>',
										'<li><input type="button" class="button_blue fr" value="提交"></li>',
									'</ul>',
								'</form>'
            	                  ].join('');
				var title = '添加';
				var dlg = new Dialog({title:title,content:dlgContent,height:'130px'});
				var $form = dlg.getDialogElem().find('form');
				dlg.getDialogElem().find('input[type="button"]').click(function(){
					ajax.sendAjax({
						type:$form.attr('method'),
						url:$form.attr('action'),
						data:$form.serialize(),
						successCallback:function(json){
							if(json.status === 1){
								dlg.close();
								new notice.Success('提示','操作成功');
							}else{
								new notice.Error('错误',json.msg);
							}
						}
					});
				});
				dlg.show();
			}
			function modify(event){
				var $self = $(event.target);
				var dlgContent = ['<form action="${root}/chapter/modify.json" method="POST">',
									'<ul>',
										'<li>',
											'<label class="label">名称：</label>',
											'<input type="text" class="text length_middle" name="name" value="'+$self.attr('pre-name')+'">',
											'<input type="hidden" name="id" value="'+$self.attr('chapter-id')+'">',
										'</li>',
										'<li><input type="button" class="button_blue fr" value="提交"></li>',
									'</ul>',
								'</form>'
            	                  ].join('');
				var title = '修改';
				var dlg = new Dialog({title:title,content:dlgContent,height:'130px'});
				var $form = dlg.getDialogElem().find('form');
				dlg.getDialogElem().find('input[type="button"]').click(function(){
					ajax.sendAjax({
						type:$form.attr('method'),
						url:$form.attr('action'),
						data:$form.serialize(),
						successCallback:function(json){
							if(json.status === 1){
								var name = $form.find('input[name="name"]').val();
								$self.closest('div').find('span[node-type="chapter_name"]').text(name);
								$self.attr('pre-name',name);
								dlg.close();
								new notice.Success('提示','操作成功');
							}else{
								new notice.Error('错误',json.msg);
							}
						}
					});
				});
				dlg.show();
			}
			function open(event){
				var $self = $(this);
            	ajax.sendAjax({
					url:'${root}/chapter/chapters.json',
					data:{parent_id:$(this).attr('chapter-id')},
					successCallback:function(json){
						if(json.status === 1){
							var $li = $self.closest('li');
							if($li.find('ul:last-child').length>0){
								$li.find('ul:last-child').remove();
							}
							
							var html = '<ul>';
							for(var i=0,il=json.chapters.length;i<il;i++){
								html += ['<li>',
											'<div>',
												'<a href="javascript:;" chapter-id="'+json.chapters[i].id+'" action-type="open" class="open" title="展开">&nbsp;</a>&nbsp;',
			            						'<span node-type="chapter_name">'+json.chapters[i].name+'</span>&nbsp;',
			            						'<span node-type="operations" class="operations">&nbsp;',
				            						'<a href="javascript:;" chapter-id="'+json.chapters[i].id+'" depth="'+json.chapters[i].depth+'" action-type="add_child" class="b">添加子节点</a>&nbsp;',
								            		'<a href="javascript:;" pre-name="'+json.chapters[i].name+'" chapter-id="'+json.chapters[i].id+'" action-type="modify" class="b">修改</a>&nbsp;',
									            	'<a href="javascript:;" action-type="set_content" chapter-id="'+json.chapters[i].id+'" doc-id="'+(!!json.chapters[i].docId?json.chapters[i].docId:'')+'" class="b">内容文档</a>&nbsp;',
									            	'<a href="javascript:;" action-type="delete" class="b" chapter-id="'+json.chapters[i].id+'">删除</a>&nbsp;',
								            	'</span>&nbsp;',
								            	'<a href="javascript:;" action-type="slide_right" class="slide-right" title="显示操作">&nbsp;</a>&nbsp;',
		            						'</div>',
		            					'</li>'].join('');
							}
							html += '</ul>';
							$self.closest('li').append(html);
							
							$self.attr({'action-type':'close',title:'关闭'});
							$self.removeClass('open').addClass('close');
						}else{
							new notice.Error('警告',json.msg);
						}
					}
				});
			}
			function close(event){
				var $self = $(this);
            	var $li = $self.closest('li');
            	if($li.find('ul:last-child').length>0){
					$li.find('ul:last-child').remove();
				}
				
				$self.attr({'action-type':'open',title:'展开'});
				$self.removeClass('close').addClass('open');
			}
			function setDoc(event){
				function _dlg(documents,preDocId){
					var dlgContent = [];
					var dlgHeight = 95;
					if(documents.length > 0){
						dlgHeight = 70;
						dlgContent.push('<div class="dropdown" style="padding:10px;"><ul>');
						for(var i=0,il=documents.length;i<il;i++){
							dlgContent.push('<li chapter_id="'+$(event.target).attr('chapter-id')+'" doc_id="'+documents[i].id+'" '+(!!preDocId&&preDocId==documents[i].id?'class="hover"':'')+'>'+
												'<a href="javascript:;">'+documents[i].title+'</a>&nbsp;'+
												'<a href="${root}/doc/get.html?id='+documents[i].id+'" target="_blank" class="fs12 b">查看</a>'+
											'</li>');
							dlgHeight += 27;
						}
						dlgContent.push('</ul></div>');
						dlgHeight += 34;
						dlgContent.push('<div class="fix"><a href="javascript:void(0);" class="button_blue fr" action-type="done" style="margin-right:10px;">确定</a></div>');
					}else{
						dlgContent.push('<div style="height:60px;line-height:60px;">您还没有可用的文档，<a href="${root}/doc/upload.html" target="_blank">点此设置</a>。</div>');
					}
					var dlg = new Dialog({
						title:'设置文档',
						content:dlgContent.join(''),
						height:dlgHeight+'px'
					});
					dlg.show();
					
					var $lis = dlg.getDialogElem().find('li');
					$lis.click(function(){
						$lis.removeClass('hover');
						$(this).addClass('hover');
					});
					dlg.getDialogElem().find('a[action-type="done"]').click(function(){
						var $li = dlg.getDialogElem().find('li.hover');
						var chapter_id = $li.attr('chapter_id');
						var doc_id = $li.attr('doc_id');
						ajax.sendAjax({
							url:'${root}/chapter/setdoc.json',
							data:{chapter_id:chapter_id,doc_id:doc_id},
							successCallback:function(json){
								if(json.status == -1){
									new LoginDialog().show();
								}else if(json.status == 1){
									new notice.Success('成功','文档设置成功');
								}else{
									new notice.Error('失败',json.msg);
								}
							}
						});
					});
				}
				
				var $self = $(this);
				var docId = $self.attr('doc-id');
				ajax.sendAjax({
					url:'${root}/doc/my/ava.json',
					successCallback:function(json){
						if(json.status === 1){
							_dlg(json.documents,docId);
						}else if(json.status === -1){
							new LoginDialog().show();
						}else{
							new notice.Error('错误',json.msg);
						}
					}
				});
			}
			
			
            $(document).delegate('.content a[action-type="add_root"],.content a[action-type="add_child"]','click',function(event){
            	addNode.call(this,event);
            });
            $chapterContainer.delegate('a[action-type="modify"]','click',function(event){
            	modify.call(this,event);
            });
			$chapterContainer.delegate('a[action-type="open"]','click',function(event){
				open.call(this,event);
            });
			$chapterContainer.delegate('a[action-type="close"]','click',function(event){
            	close.call(this,event);
            });
            $chapterContainer.delegate('a[action-type="set_content"]','click',function(event){
            	setDoc.call(this,event);
            });
            $chapterContainer.delegate('a[action-type="delete"]','click',function(event){
            	var $self = $(this);
            	var id = $self.attr('chapter-id');
            	ajax.sendAjax({
            		url:'${root}/chapter/delete.json',
            		data:{id:id},
            		successCallback:function(json){
            			if(json.status === 1){
            				new notice.Success('成功','删除成功！');
            				$self.closest('li').remove();
            			}else{
            				new notice.Error('失败',json.msg);
            			}
            		}
            	});
            });
            
            $chapterContainer.delegate('a[action-type="slide_right"]','click',function(event){
            	var $operations = $(this).parent().find('*[node-type="operations"]');
            	$operations.animate({width:'223px'},'slow');
            	
            	$(this).removeClass('slide-right').addClass('slide-left');
            	$(this).attr({'action-type':'slide_left',title:'隐藏操作'});
            });
            $chapterContainer.delegate('a[action-type="slide_left"]','click',function(event){
            	var $operations = $(this).parent().find('*[node-type="operations"]');
            	$operations.animate({width:'0px'},'slow');
            	
            	$(this).removeClass('slide-left').addClass('slide-right');
            	$(this).attr({'action-type':'slide_right',title:'显示操作'});
            });
        });
	</script>
</body>
</html>
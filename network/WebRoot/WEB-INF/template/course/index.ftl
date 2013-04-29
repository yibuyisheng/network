<!DOCTYPE html>
<#import "../include/header.ftl" as header />
<#import "../include/footer.ftl" as footer />
<html>
<head>
	<@header.head />
	<title>课程学习</title>
</head>
<body>
	<@header.header "course"/>
	<div class="content fix">
		<div class="box fl span3">
			<div class="title">章节</div>
			<div class="content">
				<div id="chapter_container" class="fs14">
					<ul class="chapter_show">
		        		<#list chapters as chapter>
		        			<li>
		        				<div>
									<a href="javascript:;" chapter-id="${chapter._id}" action-type="open" class="open" title="展开">
										<img src="${static_root}/images/open.png" />
									</a>
					            	<span node-type="chapter_name">
					            		<a href="javascript:;" class="b" action-type="show_doc" doc-id="${chapter.docId!''}">${chapter.name}</a>
					            	</span>
				            	</div>
				           	</li>
		        		</#list>
		        	</ul>
				</div>
			</div>
		</div>
		<div class="box fr" style="width:690px;">
			<div class="title"></div>
			<div class="content">
				<div class="wrapper">
					<div id="doc_area" style="padding:0 10px;" class="tl"></div>
				</div>
			</div>
		</div>
	</div>
	<@footer.footer/>
	<script>
        seajs.use(['thirdparty/jquery','ui/dialog','lib/ajax','ui/notice','modules/user/loginDialog'],function($,Dialog,ajax,notice,LoginDialog){
        	var $chapterContainer = $('#chapter_container');
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
												'<a href="javascript:;" chapter-id="'+json.chapters[i].id+'" action-type="open" class="open" title="展开">',
													'<img src="${static_root}/images/open.png" />',
												'</a>&nbsp;',
			            						'<span node-type="chapter_name"><a href="javascript:;" class="b" action-type="show_doc" doc-id="'+(!!json.chapters[i].docId?json.chapters[i].docId:'')+'">'+json.chapters[i].name+'</a></span>&nbsp;',
		            						'</div>',
		            					'</li>'].join('');
							}
							html += '</ul>';
							$self.closest('li').append(html);
							
							$self.attr({'action-type':'close',title:'关闭'});
							$self.removeClass('open').addClass('close');
							$self.find('img').attr('src','${static_root}/images/close.png');
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
				$self.find('img').attr('src','${static_root}/images/open.png');
			}
			
			var $box_title = $('.box.fr .title');
			function showDoc(event){
				var $self = $(this);
				var docId=$self.attr('doc-id');
				if(!docId){
					$box_title.text($self.text());
					$box_title.attr('title',$self.text());
					$('#doc_area').html('该章节尚无内容文档！');
					return;
				}
				ajax.sendAjax({
					url:'${root}/doc/get.json',
					data:{id:docId},
					successCallback:function(json){
						if(json.status === 1){
							$('#doc_area').html(json.doc.docContent);
						}else{
							$('#doc_area').html(json.msg);
						}
						var title = $self.text();
						if(!!json.doc){
							title += ' 《'+json.doc.title+'》';
						}
						$box_title.text(title);
						$box_title.attr('title',title);
					}
				});
			}
			
			$chapterContainer.delegate('a[action-type="open"]','click',function(event){
				open.call(this,event);
            });
			$chapterContainer.delegate('a[action-type="close"]','click',function(event){
            	close.call(this,event);
            });
            $chapterContainer.delegate('a[action-type="show_doc"]','click',function(event){
            	showDoc.call(this,event);
            });
        });
	</script>
</body>
</html>
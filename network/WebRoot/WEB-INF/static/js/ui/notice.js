define('ui/notice',['thirdparty/jquery'],function(require, exports, module){
	var $ = require('thirdparty/jquery');
	
	var errorTmpl = [
         '<div class="notice_error fix" style="position:fixed;left:50%;top:50%;margin-left:-190px;">',
         	'<img src="${static_root}/images/armor.png" class="notice_image">',
         	'<div class="notice_content fix">',
             	'<div class="title">标题</div>',
             	'<div class="msg"></div>',
         	'</div>',
         	'<a href="javascript:;" class="close">×</a>',
         '</div>'].join('');
	var successTmpl = [
		'<div class="notice_success fix" style="position:fixed;left:50%;top:50%;margin-left:-190px;">',
			'<div class="notice_content fix">',
		 	'<div class="title">标题</div>',
		 	'<div class="msg"></div>',
			'</div>',
			'<a href="javascript:;" class="close">×</a>',
		'</div>'].join('');
	
	function findMaxZIndexValue(){
		var maxIndex = 0;
		$.each($(document.body).children(),function(){
			var temp = parseFloat($(this).css('z-index'));
			if(temp > maxIndex) maxIndex = temp;
		});
		return maxIndex;
	}
	
	var Success = function(title,msg,cb){
		var $success = $(successTmpl);
		$('body').append($success);
		$success.find('.title').text(title);
		$success.find('.msg').text(msg);
		$success.css('z-index',findMaxZIndexValue());

        function close(){
            $success.fadeOut('fast',function(){
                $success.remove();
                cb instanceof Function && cb();
            });
        }
		$success.find('.close').bind('click',function(){
			close();
		});
        setTimeout(function(){
            close();
        },2000);

        $success.css('margin-top',-$success.height()/2);
	};
	var Error = function(title,msg){
		var $error = $(errorTmpl);
		$('body').append($error);
		$error.find('.title').text(title);
		$error.find('.msg').text(msg);
		$error.find('.close').bind('click',function(){
			$error.fadeOut('fast',function(){
				$error.remove();
			});
		});
		$error.css('z-index',findMaxZIndexValue());

        $error.css('margin-top',-$error.height()/2);
	};
	module.exports = {Success:Success,Error:Error};
});
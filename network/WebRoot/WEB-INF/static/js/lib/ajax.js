define('lib/ajax',['thirdparty/jquery','ui/notice'],function(require, exports, module){
	var $ = require('thirdparty/jquery'),
		notice = require('ui/notice');

	function sendAjax(options){
		if(!options) return;

		var opt = {};
		opt.type = options.type || 'post';
		opt.data = options.data;
		opt.dataType = options.dataType || 'text';
		opt.url = options.url;

		if(options.success instanceof Function) {
			opt.success = options.success;
		}else{
			opt.success = function(data){
				var json = null;
				try{
					json = eval('('+data+')');
				}catch(e){
					if(options.errorCallback instanceof Function){
						options.errorCallback(-1,e);
					}else{
						new notice.Error('错误','无法解析的服务器数据！'+e);
					}
				}
				if(!!json)options.successCallback(json);
			};
		}
		
		if(options.error instanceof Function){
			opt.error = options.error;
		}else{
			opt.error = function(jqXHR, textStatus, errorThrown){
				if(options.errorCallback instanceof Function){
					options.errorCallback(-2,jqXHR, textStatus, errorThrown);
				}else{
					var msg = '';
					if(textStatus==='timeout'){
						msg += '连接服务器超时！';
					}else if(textStatus==='abort'){
						msg += '连接被终止！';
					}else if(textStatus==='error'){
						msg += '连接错误！';
					}else if(textStatus==='parsererror'){
						msg += '数据解析错误！';
					}else{
						msg += '连接服务器错误！'+String(errorThrown);
					}
					new notice.Error('错误',msg);
				}
			};
		}
		
		$.ajax(opt);
	}

	module.exports = {sendAjax:sendAjax};
});
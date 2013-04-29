define('ui/dialog',['thirdparty/jquery','ui/overlay'],function(require, exports, module){
	var $ = require('thirdparty/jquery');
    var Overlay = require('ui/overlay');

    var dialogBox = [
        '<div class="dialog">',
            '<div class="head">',
                '<label class="title">标题</label>',
                '<a href="javascript:void(0);" class="close">×</a>',
            '</div>',
            '<div class="content">',
            '</div>',
        '</div>'
    ].join('');
	var Dialog = function(options){
		this._options = $.extend({
			'title':'标题',
			'content':'内容[html格式]',
			
			'width':'400px',
			'height':'300px',
            'zIndex':100,

            'modal':true,
			'center':true,
			'close':function(){}
		},options);
		
		var self = arguments.callee;
		if(!self.prototype._initialized){
			$.extend(self.prototype,{
				_init:function(){
                    this._$dialog = $(dialogBox);
                    this._$title = this._$dialog.find('.title');
                    this._$close = this._$dialog.find('.close');
                    this._$content = this._$dialog.find('.content');

                    this._render();
                    this._bindEvent();
                },
                _render:function(){
                    this._$title.text(this._options.title);
                    this._$content.html(this._options.content);

                    this._$dialog.css({
                        'width':this._options.width,
                        'height':this._options.height,
                        'z-index':this._options.zIndex
                    });

                    if(!!this._options.center){
                        this._$dialog.css({
                            'position':'fixed',
                            'left':'50%',
                            'margin-left':-parseInt(this._options.width)/2,
                            'top':'50%',
                            'margin-top':-parseInt(this._options.height)/2
                        });
                    }

                    if(!!this._options.modal){
                        this._overlay = new Overlay({zIndex:this._options.zIndex,effect:'fast'});
                    }
                },
                _bindEvent:function(){
                    var currentObj = this;
                    this._$close.bind('click',function(){
                        currentObj.close.call(currentObj);
                    });
                },
                show:function(){
                    this._$dialog.css('opacity',0);
                    $(document.body).append(this._$dialog);
                    this._overlay.show();
                    this._$dialog.animate({opacity:1},'fast');
                },
                close:function(){
                    this._$dialog.fadeOut('fast',function(){
                        $(this).remove();
                    });
                    if(!!this._overlay) this._overlay.close();
                    if(!!this._options.close && this._options.close instanceof Function){
                        this._options.close();
                    }
                },
                getDialogElem:function(){
                    return this._$dialog;
                }
			});

            self.prototype._initialized = true;
		}

        this._init();
	};
	module.exports = Dialog;
});
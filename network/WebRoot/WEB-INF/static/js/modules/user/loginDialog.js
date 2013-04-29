define('modules/user/loginDialog',['thirdparty/jquery','ui/dialog','modules/helper','ui/notice'],function (require, exports, module) {
    var $ = require('thirdparty/jquery');
    var Dialog = require('ui/dialog');
    var helper = require('modules/helper');
    var notice = require('ui/notice');


    var loginBoxContent = [
        '<form action="${root}/uc/login.json" method="POST">',
            '<ul>',
                '<li>',
                    '<label class="label">昵称：</label>',
                    '<input type="text" class="text length_middle" name="nick_name" tabindex="1">',
                '</li>',
                '<li>',
                    '<label class="label">密码：</label>',
                    '<input type="password" class="text length_middle" name="password" tabindex="2">',
                '</li>',
                '<li><input type="button" class="fr button_blue" value="提交" tabindex="3"/></li>',
            '</ul>',
        '</form>'
    ].join('');
    var LoginDialog = function(options){
        this._options = $.extend({
            'close':function(){}
        },options);

        var self = arguments.callee;
        if(!self.prototype._initialized){
            $.extend(self.prototype,{
                _init:function(){
                    this._dialog = new Dialog({
                        'title':'登陆',
                        'content':loginBoxContent,
                        'height':'170px',
                        'zIndex':0,
                        'close':this._options.close
                    });

                    this._$dialogElem = this._dialog.getDialogElem();
                    this._$loginButton = this._$dialogElem.find('input[type="button"]');
                    this._$form = this._$dialogElem.find('form');

                    this._bindEvent();
                },
                _bindEvent:function(){
                    var currentObj = this;
                    this._$loginButton.bind('click',function(){
                        $.ajax({
                            type:currentObj._$form.attr('method'),
                            url:currentObj._$form.attr('action'),
                            data:currentObj._$form.serialize(),
                            dataType:'text',
                            success:function(data){
                                helper.parseJsonString(data,function(json){
                                    if(json.status===1){
                                        new notice.Success('成功','登陆成功！');
                                        currentObj.close.call(currentObj);
                                    }else{
                                        new notice.Error('失败',json.msg);
                                    }
                                });
                            }
                        });
                    });
                },
                show:function(){
                    this._dialog.show();
                },
                close:function(){
                    this._dialog.close();
                    if(this._options.close instanceof Function){
                        this._options.close();
                    }
                }
            });

            self.prototype._initialized = true;
        }

        this._init();
    };

    module.exports = LoginDialog;
});
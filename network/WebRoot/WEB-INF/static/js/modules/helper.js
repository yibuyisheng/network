/**
 * Created with JetBrains WebStorm.
 * User: zhangli
 * Date: 13-3-2
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
define('modules/helper',['ui/notice'],function (require, exports, module) {
    var notice = require('ui/notice');
    module.exports = {
        parseJsonString:function(data,cb){
            var json = null;
            try{
                json = eval('('+data+')');
            }catch(e){
                new notice.Error('错误','无法解析服务器返回的数据！');
            }

            if(!!json && !!cb && cb instanceof Function) cb(json);
        }
    };
});
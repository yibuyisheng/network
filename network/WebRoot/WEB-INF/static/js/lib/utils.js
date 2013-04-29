define('lib/utils',function(require, exports, module){
	var utils = {
		getStrBetween: function(str, start, end) {
            if (str.length > 0 && start.length > 0 && end.length > 0) {

                //获取开始位置
                var start_pos = str.indexOf(start);
                if (start_pos == -1) return '';
                start_pos += start.length;

                //获取结束位置
                var end_pos = str.substring(start_pos).indexOf(end);
                if (end_pos == -1) return str.substring(start_pos);
                end_pos += start_pos;

                return str.substring(start_pos, end_pos);

            }
            return '';
        }
	};
    module.exports = utils;
});
define('lib/timeFormat',['thirdparty/jquery'],function(require, exports, module){

	var $ = require('thirdparty/jquery');
	
	var date_string ='',
		day_offset ='',
		time_json,
		server_time_json,
		clock = '';

    var TimeFormat  = {
		   convertDate2Offset:function(time, server_time){
			
			   date_string = "";
				time_json = this.convertUTC2Date(time);

				clock_json = (time_json.hour<10?"0"+time_json.hour:time_json.hour)+":"+
							(time_json.minute<10?"0"+time_json.minute:time_json.minute);

				if(!!server_time){

					server_time_json = this.convertUTC2Date(server_time);
					
					day_offset = server_time_json.day - time_json.day;

					//不是今年
					if( server_time_json.year > time_json.year ){
						//date_string += time_json.year+"-";
						date_string += time_json.year+"年"+time_json.month+'月'+time_json.day+'日';
						return date_string;
					}

					//不是这个月
					if( server_time_json.month > time_json.month ){
						//date_string += time_json.month+"-";
						date_string += time_json.month+"月"+time_json.day+'日 '+clock_json;
						return date_string;
					}
					
					//不是今天
					if( day_offset > 0 ){
						
						if( day_offset > 2){//3天前
							date_string += time_json.day+" "+clock_json;
						}else if(day_offset == 2){//前天
							date_string += '前天 '+ clock_json;
						}else if(day_offset == 1){//昨天
							date_string += '昨天 '+ clock_json;
						}

					}else{//今天

						if(server_time_json.hour -time_json.hour>0){
							date_string = '今天 '+ clock_json;
						}else if(server_time_json.minute -time_json.minute>0){
							date_string = (server_time_json.minute - time_json.minute)+"分钟前";
						}else{
							date_string = (server_time_json.second - time_json.second)+"秒前";
						}
					
					}
				}else{
					date_string += (time_json.month<10 ? ("0"+time_json.month) : time_json.month)
							+"月"+(time_json.day<10 ? ("0"+time_json.day) : time_json.day)+"日 "+clock_json;
				}

				return date_string;
	   },
	   convertUTC2Date:function(time){

			//根据time创建Date对象
			var date = new Date(),
				date_json = {};
			date.setTime(time);
			
			return {
				year : date.getFullYear(),
				month : date.getMonth()+1,
				day : date.getDate(),
				hour : date.getHours(),
				minute : date.getMinutes(),
				second: date.getSeconds()
			}
		}
    }
	
	module.exports = TimeFormat;
});
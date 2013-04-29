define('ui/ui_strengthen',['thirdparty/jquery','lib/timeFormat'],function (require, exports, module) {
    var $ = require('thirdparty/jquery'),
    	timeFormat = require('lib/timeFormat');
    
    /*tab pager*/
    function initTabPager(){
    	var $page = $('.pager');
        $page.delegate('.pager .controll li','click',function(){
        	if($(this).hasClass('fr')) return;
        	$page.find('.controll li.active').removeClass('active');
        	$(this).addClass('active');
        	
        	$page.find('.frames div.active').removeClass('active');
        	$page.find('.frames div:nth-child('+($(this).index()+1)+')').addClass('active');
        });
    }
    
    /*时间格式化*/
    function initTime(){
    	var $server_time_show = $('*[node-type="time_show"]');
        for(var i=0,il=$server_time_show.length;i<il;i++){
        	var time = parseInt(!!$server_time_show.eq(i).attr('time') ? $server_time_show.eq(i).attr('time').replace(/,/g,'') : '');
        	var server_time = parseInt(!!$server_time_show.attr('server-time') ? $server_time_show.attr('server-time').replace(/,/g,'') : '');
        	if(!!time){
        		if(!!server_time){
        			$server_time_show.text(timeFormat.convertDate2Offset(time,server_time));
        		}else{
        			$server_time_show.text(timeFormat.convertDate2Offset(time,new Date().getTime()));
        		}
        	}
        }
    }
    module.exports = {initTabPager:initTabPager,initTime:initTime};
});
package com.network.log;

import javax.servlet.ServletContext;
import com.network.schedule.Schedule;

import com.network.Base;

/**
 * @author: zhangli
 * @QQ: 157142884
 * @date: 2012-11-24
 * @brief: the log class . It should clean the log files after a period
 */
public abstract class Log extends Schedule implements Base{
	
	/**
	 * @brief initialize
	 * @param context : the webapp's context
	 */
	@Override
	public void init(ServletContext context){
		this.initLog(context);
	}
	/**
	 * @brief 
	 */
	@Override
	public void destroy(){
		this.destroyLog();
	}
	
	@Override
	public void initSchedule(ServletContext context){
		
	}
	@Override
	public void destroySchedule(){
		
	}
	
	public abstract void initLog(ServletContext context);
	public abstract void destroyLog();
	
	public abstract void info(Object msg);
	public abstract void info(Exception e);
	public abstract void info(Object msg,Exception e);
	
	public abstract void warn(Object msg);
	public abstract void warn(Exception e);
	public abstract void warn(Object msg,Exception e);
	
	public abstract void error(Object msg);
	public abstract void error(Exception e);
	public abstract void error(Object msg,Exception e);
}

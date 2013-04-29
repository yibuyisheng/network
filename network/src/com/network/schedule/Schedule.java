package com.network.schedule;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.network.Base;

/**
 * @author: zhangli
 * @QQ: 157142884
 * @date: 2012-11-24
 * @brief: every timer-task component should extend from this class 
 */
public abstract class Schedule extends TimerTask implements Base{
	/**
	 * @brief : the timer object
	 */
	private Timer timer = new Timer(true);
	
	/**
	 * @brief : initialize scheduler,start the timer
	 * @param context : the webapp's context
	 */
	@Override
	public void init(ServletContext context){
		timer.schedule(this, this.getDelay(),this.getPeriod());
		this.initSchedule(context);
	}
	/**
	 * @brief : destroy the timer
	 */
	@Override
	public void destroy(){
		timer.cancel();
		this.destroySchedule();
	}
	
	/**
	 * @param context : the webapp's context
	 */
	public abstract void initSchedule(ServletContext context);
	/**
	 * @brief called when the timer is destroyed
	 */
	public abstract void destroySchedule();
	/**
	 * @brief : be called when the timer trigger
	 */
	public abstract void run();
	/**
	 * @return how long will the timer trigger
	 */
	public abstract long getDelay();
	/**
	 * @return how often will the timer trigger
	 */
	public abstract long getPeriod();
}

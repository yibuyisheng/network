package com.network;

import javax.servlet.ServletContext;


/**
 * @author: zhangli
 * @QQ: 157142884
 * @date: 2012-11-24
 * @brief: the base interface of all components 
 */
public interface Base {
	
	/**
	 * brief: to initialize some Object.This method will be called when the app starts
	 * @param context: the webapp's context
	 */
	public void init(ServletContext context);
	
	/**
	 * brief: to destroy some Object(release some resources like database connection).<br>
	 * 			This method will be called when the webapp is being destroyed;
	 * @param context: the webapp's context
	 */
	public void destroy();
	
	/**
	 * @brief get the initialization priority . The bigger one is initialized after the smaller one in the same layer
	 * @return the priority number
	 */
	public int getPriority();
}

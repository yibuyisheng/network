package com.network.manage.logic;

import java.util.Hashtable;

import javax.servlet.ServletContext;

import com.network.Base;
import com.network.logic.Logic;
import com.network.manage.Manager;

public class LogicManager extends Manager{

	@Override
	protected void destroyManager() {
		try{
			/*destroy logics order by priority*/
			_destroy();
		}catch(Exception e){
			log.error("destroy Logic failed!System will exit!",e);
		}
	}
	
	public <T extends Logic> T getLogic(Class<T> t){
		return (T)this.get(t);
	}
	
	@Override
	protected void initManager(ServletContext context) {
		try{
			/*initialize logics order by priority*/
			_init(context);
		}catch(Exception e){
			log.error("init Logic failed!System will exit!",e);
			System.exit(0);
		}
	}
}

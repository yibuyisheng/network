package com.network.logic;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.network.Base;
import com.network.log.Log;
import com.network.log.Logger;
import com.network.manage.ComponentManager;
import com.network.manage.db.DBManager;
import com.network.manage.logic.LogicManager;

public abstract class Logic implements Base{
	private Log log = null;
	protected DBManager dbManager = null;
	private LogicManager logicManager = null;
	
	private Map<String,Object> properties = new HashMap<String,Object>();
	
	@Override
	public void init(ServletContext context) {
		this.log = Logger.getInstance();
		ComponentManager manager = ComponentManager.getInstance();
		dbManager = manager.getDBManager();
		logicManager = manager.getLogicManager();
		
		this.initLogic(context);
	}
	@Override
	public void destroy() {
		this.destroyLogic();
	}
	
	protected void setProperty(String key,Object value){
		this.properties.put(key, value);
	}
	protected Object getProperty(String key){
		return this.properties.get(key);
	}
	protected int getPropertyInteger(String key,int def){
		try{
			return Integer.parseInt(String.valueOf(this.getProperty(key)));
		}catch(Exception e){
			Logger.getInstance().error(e);
			return def;
		}
	}
	
	protected abstract void initLogic(ServletContext context);
	protected abstract void destroyLogic();
}

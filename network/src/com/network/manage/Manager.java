package com.network.manage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.network.Base;
import com.network.log.Log;
import com.network.log.Logger;

public abstract class Manager implements Base{
	protected Log log = null;
	
	private Map<Class<? extends Base>,Base> objs = new HashMap<Class<? extends Base>,Base>();
	private ArrayList<Base> sorted_objs = new ArrayList<Base>();
	
	@Override
	public void init(ServletContext context) {
		this.log = Logger.getInstance();
		this.initManager(context);
	}
	@Override
	public void destroy() {
		this.destroyManager();
	}
	@Override
	public int getPriority() {
		return 0;
	}
	
	protected abstract void initManager(ServletContext context);
	protected abstract void destroyManager();
	
	protected void _destroy(){
		for(int i=this.sorted_objs.size()-1;i>=0;i--){
			sorted_objs.get(i).destroy();
			System.out.println("destroy "+sorted_objs.get(i).getClass().getName()+" successfully ! priority "+sorted_objs.get(i).getPriority());
		}
	}
	protected void _init(ServletContext context){
		for(int i=0,il=this.sorted_objs.size();i<il;i++){
			sorted_objs.get(i).init(context);
			System.out.println("initialize "+sorted_objs.get(i).getClass().getName()+" successfully ! priority "+sorted_objs.get(i).getPriority());
		}
	}
	
	protected void put(Class<?> k,Object v){
		Class<? extends Base> key = (Class<? extends Base>)k;
		Base value = (Base)v;
		this.objs.put(key, value);
		
		int index = 0;
		for(int i=0,il=this.sorted_objs.size();i<il;i++){
			if(value.getPriority() > this.sorted_objs.get(i).getPriority()){
				index = i+1;
				break;
			}
		}
		this.sorted_objs.add(index, value);
	}
	protected void putAll(Map<Class<?>,Object> objs){
		for(Map.Entry<Class<?>,Object> entry : objs.entrySet()){
			put(entry.getKey(),entry.getValue());
		}
	}
	public <T extends Base> T get(Class<T> cls){
		return (T)this.objs.get(cls);
	}
	protected <T extends Base> Collection<T> getValues(){
		return (Collection<T>)this.objs.values();
	}
}

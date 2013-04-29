package com.network.dealer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.network.Base;
import com.network.log.Logger;
import com.network.logic.Logic;
import com.network.manage.ComponentManager;
import com.network.manage.logic.LogicManager;
import com.network.modal.User;

public abstract class Dealer implements Base{
	protected LogicManager logicManager = null;
	protected String dealerPath;
	protected ServletContext context;
	
	protected Map<String,String> urlMethodMap = new HashMap<String,String>();
	
	@Override
	public void init(ServletContext context) {
		if(this.logicManager==null) this.logicManager = ComponentManager.getInstance().getLogicManager();
		this.context = context;
		this.initDealer(context);
	}
	@Override
	public void destroy() {
		this.destroyDealer();
	}
	
	public void setDealerPath(String path){
		this.dealerPath = path;
	}
	
	protected String getRequestUri(HttpServletRequest request){
		return request.getRequestURI().replace(request.getContextPath(), "");
	}
	public DealerParamWrap deal(HttpServletRequest request,HttpServletResponse response){
		DealerParamWrap params = new DealerParamWrap(request, response);
		String data = params.request.getParameter("data");
		if(data != null && !data.equals("")) {
			params.request.setAttribute("data", JSONObject.fromObject(data));
		}
		params.request.setAttribute("self", this.getSelf(params.request));
		this.dealHttp(params);
		return params;
	}
	protected <T extends Logic> T getLogic(Class<T> cls){
		return this.logicManager.getLogic(cls);
	}
	
	protected abstract void initDealer(ServletContext context);
	protected abstract void destroyDealer();
	protected abstract void dealHttp(DealerParamWrap params);
	
	public String getRoute(){
		return this.dealerPath;
	}
	protected String getDealerPathPrefix(){
		int startIndex = this.dealerPath.indexOf("/");
		int endIndex = this.dealerPath.lastIndexOf("/");
		return this.dealerPath.substring(startIndex, endIndex);
	}
	protected boolean uriEquals(HttpServletRequest request,String uri){
		return getRequestUri(request).replace(getDealerPathPrefix(), "").equals(uri);
	}
	
	protected String getDealMethodName(HttpServletRequest request){
		return this.urlMethodMap.get(this.getRequestUri(request).replace(this.getDealerPathPrefix(), ""));
	}
	protected void invokeDealMethod(DealerParamWrap params,Class<? extends Dealer> dealerChildClass){
		String methodName = this.getDealMethodName(params.request);
		if(methodName==null || methodName.equals("")){
			Logger.getInstance().error(new Exception("can not find method to deal with the path : "+this.getRequestUri(params.request)));
		}else{
			try {
				dealerChildClass.getMethod(methodName, DealerParamWrap.class).invoke(this, params);
			} catch (IllegalAccessException e) {
				Logger.getInstance().error(e);
			} catch (IllegalArgumentException e) {
				Logger.getInstance().error(e);
			} catch (InvocationTargetException e) {
				Logger.getInstance().error(e);
			} catch (NoSuchMethodException e) {
				Logger.getInstance().error(e);
			} catch (SecurityException e) {
				Logger.getInstance().error(e);
			}
		}
	}
	protected String getRealPath(String path){
		return this.context.getRealPath(path);
	}
	
	protected User getSelf(HttpServletRequest request){
		User self = null;
		Object selfObj = request.getSession().getAttribute("self");
		if(selfObj!=null && selfObj instanceof User){
			self = (User)selfObj;
		}
		return self;
	}
	protected void saveSelf(HttpServletRequest request,User self){
		request.getSession().setAttribute("self", self);
	}
}

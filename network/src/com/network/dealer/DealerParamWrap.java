package com.network.dealer;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.network.log.Logger;

public class DealerParamWrap {
	public HttpServletRequest request = null;
	public HttpServletResponse response = null;
	
	public static final int HTML_RESPONSE=1;
	public static final int JSON_RESPONSE=2;
	public static final int JS_RESPONSE=6;
	public static final int CSS_RESPONSE=7;
	public static final int PNG_RESPONSE=8;
	public static final int JPG_RESPONSE=9;
	public static final int GIF_RESPONSE=10;
	public static final int REDIRECT_FORWARD=3;
	public static final int REDIRECT_INCLUDE=4;
	public static final int REDIRECT_RESPONSE=5;
	public static final int XML_RESPONSE=11;
	private int responseKind = HTML_RESPONSE;
	
	public static final int STATUS_PARAMS_ERROR=-3;
	public static final int STATUS_NO_PERMISSION=-4;
	public static final int STATUS_NOT_FIND=-2;
	public static final int STATUS_NOT_LOGIN=-1;
	public static final int STATUS_COMMON_ERROR=0;
	public static final int STATUS_SUCCESS=1;
	
	/**
	 * the file route .<br>
	 * When the responseKind==HTML_RESPONSE , this parameter specify the route of the html resource or ftl resource .<br>
	 * When the responseKind==JSON_RESPONSE , this parameter is ignored .<br>
	 * When the responseKind==REDIRECT_FORWARD ,  this parameter specify the uri .<br>
	 * When the responseKind==REDIRECT_INCLUDE ,  this parameter specify the uri (not support currently).<br>
	 * When the responseKind==REDIRECT_RESPONSE , this parameter specify the url
	 */
	private String fileRoute = null;
	
	public DealerParamWrap(HttpServletRequest request,HttpServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	public void setResponseKind(int responseKind){
		this.responseKind = responseKind;
	}
	public int getResponseKind(){
		return responseKind;
	}
	public void setData(String key,Object value){
		request.setAttribute(key, value);
	}
	public void setData(Map<String,Object> map){
		if(map == null) return;
		Set<String> keys = map.keySet();
		for(String key : keys){
			this.setData(key,map.get(key));
		}
	}
	public void setFileRoute(String fileRoute){
		this.fileRoute = fileRoute;
	}
	public String getFileRoute(){
		return this.fileRoute;
	}
	
	public void setResponse(int responseKind,Map<String,Object> map,String fileRoute){
		this.setResponseKind(responseKind);
		this.setData(map);
		this.setFileRoute(fileRoute);
	}
	public void responseJson(String jsonStr){
		this.setResponseKind(JSON_RESPONSE);
		request.setAttribute("jsonStr", jsonStr);
	}
	public void responseJson(Object obj){
		this.responseJson(JSON.toJSONString(obj));
	}
	public void responseFtl(Map<String,Object> map,String path){
		if(!path.endsWith(".ftl")) return;
		this.setData(map);
		this.setFileRoute(path);
		this.setResponseKind(HTML_RESPONSE);
	}
	public void responseXml(Map<String,Object> map,String path){
		if(!path.endsWith(".xml")) {
			Logger.getInstance().error("wrong response type : "+path+", the path should end with .xml .");
			return;
		}
		this.setData(map);
		this.setFileRoute(path);
		this.setResponseKind(XML_RESPONSE);
	}
	public void responseXml(String data){
		request.setAttribute("plainStr", data);
		this.setResponseKind(XML_RESPONSE);
	}
	public void responseHtml(String path){
		this.setFileRoute(path);
		this.setResponseKind(HTML_RESPONSE);
	}
	public void responseJs(String path){
		this.setFileRoute(path);
		this.setResponseKind(JS_RESPONSE);
	}
	public void responseCss(String path){
		this.setFileRoute(path);
		this.setResponseKind(CSS_RESPONSE);
	}
	public void responseRedirect(String url){
		this.setResponseKind(REDIRECT_RESPONSE);
		this.setFileRoute(url);
	}
	
	public String getRoot(){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}
	public String getStaticRoot(){
		return getRoot()+"/static";
	}
	public String getDocumentRoot(){
		return getRoot()+"/documents";
	}
}

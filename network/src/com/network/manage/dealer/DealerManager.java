package com.network.manage.dealer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.network.Base;
import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.dealer.DefaultDealer;
import com.network.log.Logger;
import com.network.manage.Manager;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class DealerManager extends Manager{
	
	private ServletContext context = null;
	
	private Hashtable<String,Base> dealersRoute = new Hashtable<String,Base>(); /*dealer layer , route-object*/
	
	/**
	 * the freemarker template configuration
	 */
	private Configuration cfg = new Configuration();
	
	/**
	 * get some dealer object .  be called like this *.getDealer(Dealer.class);
	 * @param <T> the dealer's class
	 * @param t
	 * @return
	 */
	public <T extends Dealer> T getDealer(Class<T> t){
		return this.get(t);
	}
	/**
	 * get some dealer object .  be called like this *.getDealer(Dealer.class,route);
	 * @param <T> the dealer's class
	 * @param t
	 * @param route the route which the dealer should deal 
	 * @return the dealer object
	 */
	public <T> T getDealer(Class<T> t,String route){
		return (T)dealersRoute.get(route);
	}
	
	/**
	 * find the dealer by uri
	 * @param uri the uri need be handled
	 * @return the right dealer . If not found , then null
	 */
	public Dealer findDealer(String uri){
		Dealer dealer = null;
		Collection<Base> bases = this.getValues();
		for(Base base : bases){
			String dealerUri = ((Dealer)base).getRoute();
			if(dealerUri.endsWith("*")){
				if(!uri.startsWith(dealerUri.replace("*", ""))) continue;
				dealer = (Dealer)base;
			}else{
				if(!uri.equals(dealerUri)) continue;
				dealer = (Dealer)base;
			}
		}
		if(dealer==null) dealer = (Dealer)this.get(DefaultDealer.class);
		return dealer;
	}
	
	/**
	 * read the text file
	 * @param path the full path of the text file
	 * @return the text file's text content
	 * @throws IOException
	 */
	private String _readTextFile(String path,String charset) throws IOException{
		StringBuilder text = new StringBuilder();
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = (charset==null?new InputStreamReader(fileInputStream):new InputStreamReader(fileInputStream,charset));
		BufferedReader br = new BufferedReader(inputStreamReader);
		String line = null;
		while((line=br.readLine())!=null){  
		      text.append(line+System.getProperty("line.separator")); 
		}
		br.close();
		inputStreamReader.close();
		fileInputStream.close();
		return text.toString();
	}
	private void _responseBinaryData(String path,DealerParamWrap params){
		try{
			OutputStream outputStream = params.response.getOutputStream();
			Logger.getInstance().info(context.getRealPath(path));
			FileInputStream fileInputStream = new FileInputStream(context.getRealPath(path));
			byte []data = new byte[1024];
			while((fileInputStream.read(data))>0){
				outputStream.write(data);
			}
			fileInputStream.close();
			outputStream.flush();
		}catch(Exception e){
			log.error("binary file : "+path,e);
		}
	}
	
	private void _responseWithXml(DealerParamWrap params){
		String route = params.getFileRoute();
		try{
			if(route == null || route.equals("")) {
				String plainStr = String.valueOf(params.request.getAttribute("plainStr"));
				if(!plainStr.equals("null")){
					params.response.getWriter().print(plainStr);
					return;
				}
				return;
			}
			
			if(route.endsWith(".html")){
				String content = _readTextFile(context.getRealPath(route),"utf-8");
				params.response.getWriter().print(content);
			}else if(route.endsWith(".ftl") || route.endsWith(".xml")){
				Template template = cfg.getTemplate(route);
				HashMap<String,Object> p = new HashMap<String,Object>();
				Enumeration enumeration = params.request.getAttributeNames();
				while(enumeration.hasMoreElements()){
					String k = String.valueOf(enumeration.nextElement());
					p.put(k, params.request.getAttribute(k));
				}
				p.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
				template.process(p, params.response.getWriter());
			}
		}catch(Exception e){
			log.error("file path:"+context.getRealPath(route),e);
		}
	}
	private void _responseWithJSON(DealerParamWrap params){
		params.response.setContentType("text/plain");
		try {
			params.response.getWriter().print(params.request.getAttribute("jsonStr"));
			params.response.getWriter().flush();
		} catch (IOException e) {
			log.error(e);
		}
	}
	private void _responseForward(DealerParamWrap params){
		String forwardRoute = params.getFileRoute();
		if(forwardRoute==null || forwardRoute.equals("")) return;
		
		try {
			Dealer dealer = this.findDealer(forwardRoute);
			if(dealer==null){
				params.setFileRoute("WEB-INF"+File.separator+"template"+File.separator+"index.html");
				params.setResponseKind(DealerParamWrap.HTML_RESPONSE);
				this.response(params);
				log.warn("cannot find dealer to deal the forward path : "+forwardRoute);
			}else{
				response(dealer.deal(params.request, params.response));
			}
		} catch (Exception e) {
			log.error(e);
		}
	}
	private void _responseInclude(DealerParamWrap params){
		try {
			params.response.getWriter().print("we cannot support the include way currently!");
		} catch (IOException e) {
			log.error(e);
		}
		/*String includeRoute = params.getFileRoute();
		if(includeRoute==null || includeRoute.equals("")) return;
		
		try {
			Dealer dealer = this.findDealer(includeRoute);
			if(dealer==null){
				params.setFileRoute("WEB-INF"+File.separator+"template"+File.separator+"index.html");
				params.setResponseKind(DealerParamWrap.HTML_RESPONSE);
				this.response(params);
				log.warn("cannot find dealer to deal the include path : "+includeRoute);
			}else{
				response(dealer.deal(params.request, params.response));
			}
		} catch (Exception e) {
			log.error(e);
		}*/
	}
	private void _responseResponse(DealerParamWrap params){
		String redirectRoute = params.getFileRoute();
		if(redirectRoute==null || redirectRoute.equals("")) return;
		
		try {
			params.response.sendRedirect(redirectRoute);
		} catch (Exception e) {
			log.error(e);
		}
	}
	private void _responseWidthTextFile(DealerParamWrap params){
		if(params.getFileRoute() == null || params.getFileRoute().equals("")){
			Logger.getInstance().error("when the response type equals "+params.getResponseKind()+", the file route param can not be null!");
			return;
		}
		
		try{
			String content = _readTextFile(context.getRealPath(params.getFileRoute()),"utf-8");
			content = content.replace("${root}", params.getRoot()).replace("${static_root}", params.getStaticRoot());
			params.response.getWriter().print(content);
		}catch(Exception e){
			log.error("file path : "+context.getRealPath(params.getFileRoute()),e);
		}
	}
	
	/**
	 * response by response kind
	 * @param params the parameters which are needed when response
	 */
	public void response(DealerParamWrap params){
		HttpServletRequest request = params.request;
		request.setAttribute("root", params.getRoot());
		request.setAttribute("static_root", params.getStaticRoot());
		switch(params.getResponseKind()){
		case DealerParamWrap.HTML_RESPONSE:
			params.response.setContentType("text/html");
			_responseWithXml(params);
			break;
		case DealerParamWrap.JSON_RESPONSE:_responseWithJSON(params);break;
		case DealerParamWrap.REDIRECT_FORWARD:_responseForward(params);break;
		case DealerParamWrap.REDIRECT_INCLUDE:_responseInclude(params);break;
		case DealerParamWrap.REDIRECT_RESPONSE:_responseResponse(params);break;
		case DealerParamWrap.JS_RESPONSE:
			params.response.setContentType("application/x-javascript");
			_responseWidthTextFile(params);
			break;
		case DealerParamWrap.CSS_RESPONSE:
			params.response.setContentType("text/css");
			_responseWidthTextFile(params);
			break;
		case DealerParamWrap.PNG_RESPONSE:
			params.response.setContentType("image/png");
			_responseBinaryData(params.getFileRoute(),params);
			break;
		case DealerParamWrap.JPG_RESPONSE:
			params.response.setContentType("image/jpeg");
			_responseBinaryData(params.getFileRoute(),params);
			break;
		case DealerParamWrap.GIF_RESPONSE:
			params.response.setContentType("image/gif");
			_responseBinaryData(params.getFileRoute(),params);
			break;
		case DealerParamWrap.XML_RESPONSE:
			params.response.setContentType("text/xml");
			_responseWithXml(params);
			break;
		}
	}
	
	@Override
	protected void destroyManager() {
		try{
			/*destroy dealers order by priority*/
			_destroy();
		}catch(Exception e){
			log.error("destroy Dealer failed!System will exit!",e);
		}
	}

	@Override
	protected void initManager(ServletContext context) {
		this.context = context;
		try{
			/*initialize dealers order by priority*/
			_init(context);
		}catch(Exception e){
			log.error("init Dealer failed!System will exit!",e);
			System.exit(0);
		}
		
		try {
			File file = new File(context.getRealPath(File.separator)+"WEB-INF"+File.separator+"template"+File.separator);
			cfg.setDirectoryForTemplateLoading(file);
			cfg.setDefaultEncoding("utf-8");
		} catch (IOException e) {
			log.error("initialize freemarker template failed ! System will exit!",e);
			System.exit(0);
		}
	}
}

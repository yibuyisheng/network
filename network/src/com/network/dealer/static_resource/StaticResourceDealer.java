package com.network.dealer.static_resource;

import javax.servlet.ServletContext;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;

/**
 * deal with static resource ,such as html,js,css,png,jpeg(jpg),gif
 * @author zhangli
 */
public class StaticResourceDealer extends Dealer{

	@Override
	protected void dealHttp(DealerParamWrap params) {
		String uri = this.getRequestUri(params.request);
		if(uri.equals("/static/template.html")){
			params.responseHtml("WEB-INF/template/template.html");
		}else if(uri.endsWith(".html")){
			params.responseHtml("WEB-INF"+uri);
		}else if(uri.endsWith(".js")){
			params.responseJs("WEB-INF"+uri);
		}else if(uri.endsWith(".css")){
			params.responseCss("WEB-INF"+uri);
		}else if(uri.endsWith(".gif")){
			params.setResponse(DealerParamWrap.GIF_RESPONSE, null, "WEB-INF"+uri);
		}else if(uri.endsWith(".jpg") || uri.endsWith(".jpeg")){
			params.setResponse(DealerParamWrap.JPG_RESPONSE, null, "WEB-INF"+uri);
		}else if(uri.endsWith(".png")){
			params.setResponse(DealerParamWrap.PNG_RESPONSE, null, "WEB-INF"+uri);
		}else{
			params.responseHtml("WEB-INF"+uri);
		}
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/static/*");
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}

package com.network;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.log.Logger;
import com.network.manage.ComponentManager;

public class GlobalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public GlobalServlet() {
		super();
	}
	@Override
	public void destroy() {
		super.destroy();
		
		ComponentManager.getInstance().destroy();
		Logger.getInstance().info("system exit successfully!");
		Logger.getInstance().destroy();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String uri = request.getRequestURI().replace(request.getContextPath(), "");
		Dealer dealer = ComponentManager.getInstance().getDealerManager().findDealer(uri);
		if(dealer==null){
			DealerParamWrap params = new DealerParamWrap(request,response);
			params.setFileRoute("WEB-INF"+File.separator+"template"+File.separator+"404.html");
			params.setResponseKind(DealerParamWrap.HTML_RESPONSE);
			ComponentManager.getInstance().getDealerManager().response(params);
			Logger.getInstance().warn("cannot find dealer to deal the path : "+uri);
		}else{
			ComponentManager.getInstance().getDealerManager().response(dealer.deal(request, response));
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

	@Override
	public void init() throws ServletException {
		ServletContext context = this.getServletContext();
		Logger.initInstance(context);
		
		Logger.getInstance().info("network init start");
		ComponentManager.getInstance().init(context);
		Logger.getInstance().info("init successfully!");
	}

}

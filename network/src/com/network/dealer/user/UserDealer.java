package com.network.dealer.user;

import java.util.HashMap;

import javax.servlet.ServletContext;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.log.Logger;
import com.network.logic.user.UserLogic;
import com.network.modal.User;
import com.network.util.StringHelper;

public class UserDealer extends Dealer{

	private UserLogic userLogic = null;
	
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/uc/*");
		
		userLogic = logicManager.getLogic(UserLogic.class);
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub
		
	}
	
	public void registeHtml(DealerParamWrap params){
		String redirectUrl = params.request.getParameter("url");
		if(this.getSelf(params.request) == null){
			params.responseFtl(new HashMap<String,Object>(), "uc/registe.ftl");
		}else{
			if(redirectUrl==null || redirectUrl.length()==0){
				redirectUrl = params.getRoot()+"/page/course.html";
			}
			params.responseRedirect(redirectUrl);
		}
	}
	public void registeJson(DealerParamWrap params){
		Logger.getInstance().info("enter _registeJson");
		
		String trueName = params.request.getParameter("true_name");
		String professional = params.request.getParameter("professional");
		String grade = params.request.getParameter("grade");
		String _class = params.request.getParameter("class");
		String identity = params.request.getParameter("identity");
		String nickName = params.request.getParameter("nick_name");
		String password = params.request.getParameter("password");
		String confirmPassword = params.request.getParameter("confirm_password");
		
		User user = new User();
		user.setTrueName(trueName);
		user.setProfessional(professional);
		if(!StringHelper.isInteger(grade)){
			params.responseJson("{status:0,msg:\"年级输入错误！\"}");
			return;
		}else if(!StringHelper.isInteger(_class)){
			params.responseJson("{status:0,msg:\"班级输入错误！\"}");
			return;
		}
		user.setGrade(grade+"."+_class);
		if(!StringHelper.isByte(identity)){
			params.responseJson("{status:0,msg:\"身份输入错误！\"}");
			return;
		}
		user.setIdentity(Byte.parseByte(identity));
		user.setNickName(nickName);
		if(password==null||password.equals("")){
			params.responseJson("{status:0,msg:\"请输入密码！\"}");
			return;
		}
		if(!password.equals(confirmPassword)){
			params.responseJson("{status:0,msg:\"两次密码输入不一致！\"}");
			return;
		}
		user.setPassword(password);
		String msg = userLogic.registe(user);
		if(msg!=null){
			params.responseJson("{status:0,msg:\""+msg+"\"}");
			return;
		}else{
			params.responseJson("{status:1}");
		}
	}
	public void loginHtml(DealerParamWrap params){
		String redirectUrl = params.request.getParameter("url");
		if(this.getSelf(params.request) == null){
			params.responseFtl(null, "uc/login.ftl");
		}else{
			if(redirectUrl==null || redirectUrl.length()==0){
				redirectUrl = params.getRoot()+"/page/course.html";
			}
			params.responseRedirect(redirectUrl);
		}
	}
	public void loginJson(DealerParamWrap params){
		String nickName = params.request.getParameter("nick_name");
		String password = params.request.getParameter("password");
		
		StringBuilder msg = new StringBuilder();
		User self = userLogic.login(nickName, password, msg);
		if(msg.toString().equals("")){
			saveSelf(params.request, self);
			params.responseJson("{status:1}");
		}else{
			params.responseJson("{status:0,msg:\""+msg.toString()+"\"}");
		}
	}
	@Override
	protected void dealHttp(DealerParamWrap params) {
		String uri = this.getRequestUri(params.request);
		if("/uc/registe.html".equals(uri)){
			this.registeHtml(params);
		}else if("/uc/registe.json".equals(uri)){
			this.registeJson(params);
		}else if("/uc/login.html".equals(uri)){
			this.loginHtml(params);
		}else if("/uc/login.json".equals(uri)){
			this.loginJson(params);
		}else if("/uc/exit.json".equals(uri)){
			params.request.getSession().removeAttribute("self");
			params.responseJson("{status:1}");
		}
	}
}

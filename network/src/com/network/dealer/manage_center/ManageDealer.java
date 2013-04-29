package com.network.dealer.manage_center;

import javax.servlet.ServletContext;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.logic.LogicResult;
import com.network.logic.user.UserLogic;
import com.network.modal.User;

public class ManageDealer extends Dealer {
	
	UserLogic userLogic;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/mc/*");
		
		userLogic = logicManager.getLogic(UserLogic.class);
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub

	}
	
	public void index(DealerParamWrap params){
		if(this.getSelf(params.request)==null){
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"/mc/index.html");
		}else{
			params.responseFtl(null, "mc/index.ftl");
		}
	}
	public void userManage(DealerParamWrap params){
		String pageIndex = params.request.getParameter("page");
		LogicResult data = this.userLogic.getAllUser(pageIndex, this.getSelf(params.request));
		if(data.getInt("status", DealerParamWrap.STATUS_COMMON_ERROR) == DealerParamWrap.STATUS_NOT_LOGIN){
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"/mc/user.html");
		}else{
			params.responseFtl(data, "mc/user.ftl");
		}
	}
	public void deleteUser(DealerParamWrap params){
		String id = params.request.getParameter("id");
		params.responseJson(userLogic.deleteUserById(id, this.getSelf(params.request)));
	}
	public void userPass(DealerParamWrap params){
		String id = params.request.getParameter("id");
		User self = this.getSelf(params.request);
		params.responseJson(userLogic.userPass(id, self));
	}
	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		String uri = this.getRequestUri(params.request);
		if("/mc/index.html".equals(uri)){
			index(params);
		}else if("/mc/user.html".equals(uri)){
			userManage(params);
		}else if("/mc/user/delete.json".equals(uri)){
			deleteUser(params);
		}else if("/mc/user/verify/pass.json".equals(uri)){
			userPass(params);
		}else if("/mc/user/verify/refuse.json".equals(uri)){
			String id = params.request.getParameter("id");
			User self = this.getSelf(params.request);
			params.responseJson(userLogic.userRefuse(id, self));
		}
	}
}

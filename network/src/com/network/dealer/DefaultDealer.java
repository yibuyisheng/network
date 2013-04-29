package com.network.dealer;

import java.util.HashMap;

import javax.servlet.ServletContext;

public class DefaultDealer extends Dealer {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/default/*");
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		params.responseFtl(new HashMap<String,Object>(), "index.ftl");
	}
}

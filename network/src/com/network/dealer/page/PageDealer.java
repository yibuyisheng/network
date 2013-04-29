package com.network.dealer.page;

import java.util.HashMap;

import javax.servlet.ServletContext;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.logic.LogicResult;
import com.network.logic.chapter.ChapterLogic;

public class PageDealer extends Dealer{

	private ChapterLogic chapterLogic = null;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/page/*");
		
		chapterLogic = this.logicManager.getLogic(ChapterLogic.class);
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		if(uriEquals(params.request, "/") || uriEquals(params.request,"/index.html")){
			params.responseFtl(new HashMap<String, Object>(), "index.ftl");
		}else if(uriEquals(params.request, "/course.html")){
			course(params);
		}
	}
	
	public void course(DealerParamWrap params){
		LogicResult	data = chapterLogic.getChaptersByDepth((byte)1);
		params.responseFtl(data, "course/index.ftl");
	}
}

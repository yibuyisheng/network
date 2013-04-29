package com.network.dealer.chapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.alibaba.fastjson.JSON;
import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.log.Logger;
import com.network.logic.LogicResult;
import com.network.logic.chapter.ChapterLogic;
import com.network.modal.ChapterModal;
import com.network.modal.User;
import com.network.util.StringHelper;

public class ChapterDealer extends Dealer {
	
	private ChapterLogic chapterLogic;

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/chapter/*");
		this.chapterLogic = this.logicManager.getLogic(ChapterLogic.class);
		
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		String uri= this.getRequestUri(params.request);
		if("/chapter/construct.html".equals(uri)){
			this.constructPage(params);
		}else if("/chapter/add.json".equals(uri)){
			this.add(params);
		}else if("/chapter/chapters.json".equals(uri)){
			this.getChapters(params);
		}else if("/chapter/modify.json".equals(uri)){
			this.modify(params);
		}else if("/chapter/setdoc.json".equals(uri)){
			this.setDoc(params);
		}else if("/chapter/delete.json".equals(uri)){
			this.delete(params);
		}
	}
	
	public void delete(DealerParamWrap params){
		String id = params.request.getParameter("id");
		LogicResult data = this.chapterLogic.delete(id, this.getSelf(params.request));
		params.responseJson(data);
	}
	public void setDoc(DealerParamWrap params){
		String chapterId = params.request.getParameter("chapter_id");
		String docId = params.request.getParameter("doc_id");
		User self = this.getSelf(params.request);
		Map<String,Object> data = new HashMap<String,Object>();
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录");
		}else{
			String msg = this.chapterLogic.setDoc(chapterId, docId, self);
			if(msg.length() == 0){
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
			}else{
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", msg);
			}
		}
		params.responseJson(data);
	}
	
	public void modify(DealerParamWrap params){
		String id = params.request.getParameter("id");
		String name = params.request.getParameter("name");
		
		StringBuilder msg = new StringBuilder();
		Map<String,Object> data = new HashMap<String,Object>();
		if(this.chapterLogic.modify(id, name, msg)){
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}else{
			data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
			data.put("msg", msg);
		}
		params.responseJson(data);
	}

	public void getChapters(DealerParamWrap params){
		String parentId = params.request.getParameter("parent_id");
		StringBuilder msg = new StringBuilder();
		List<ChapterModal> chapters = this.chapterLogic.getChaptersByParentId(parentId, msg);
		if(msg.length() > 0){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\""+msg+"\"}");
		}else{
			params.responseJson("{status:"+DealerParamWrap.STATUS_SUCCESS+",chapters:"+JSON.toJSONString(chapters)+"}");
		}
	}
	public void constructPage(DealerParamWrap params){
		User self = this.getSelf(params.request);
		if(self == null){
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"/chapter/construct.html");
		}else{
			LogicResult data = this.chapterLogic.getChaptersByDepth((byte)1);
			params.responseFtl(data, "chapter/construct.ftl");
		}
	}
	
	public void add(DealerParamWrap params){
		String name = params.request.getParameter("name");
		String depth = params.request.getParameter("depth");
		String parent_id = params.request.getParameter("parent_id");
		
		if(!ChapterModal.isName(name)){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"名称不合法！\"}");
			return;
		}
		
		try{
			if(!StringHelper.isByte(depth)){
				params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"错误的章节目录深度！\"}");
				return;
			}
		}catch(Exception e){
			Logger.getInstance().error(e);
		}
		
		ChapterModal chapter = new ChapterModal();
		chapter.setName(name);
		chapter.setDepth(Byte.parseByte(depth));
		chapter.setParentId(parent_id);
		
		StringBuilder msg = new StringBuilder();
		if(!chapterLogic.add(chapter, msg)){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\""+msg+"\"}");
			return;
		}else{
			params.responseJson("{status:"+DealerParamWrap.STATUS_SUCCESS+"}");
		}
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}

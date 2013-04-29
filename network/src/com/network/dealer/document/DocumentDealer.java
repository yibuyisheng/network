package com.network.dealer.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.log.Logger;
import com.network.logic.LogicResult;
import com.network.logic.document.DocumentLogic;
import com.network.logic.user.UserLogic;
import com.network.modal.DocumentModal;
import com.network.modal.FileModal;
import com.network.modal.User;

public class DocumentDealer extends Dealer{

	private DocumentLogic documentLogic = null;
	private UserLogic userLogic = null;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/doc/*");
		
		this.documentLogic = this.logicManager.getLogic(DocumentLogic.class);
		this.userLogic = this.logicManager.getLogic(UserLogic.class);
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		String uri = this.getRequestUri(params.request);
		if("/doc/browse/images".equals(uri)){
			this.browseImages(params);
		}else if("/doc/image".equals(uri)){
			this.getImage(params);
		}else if("/doc/upload.html".equals(uri)){
			this.uploadPage(params);
		}else if("/doc/upload.json".equals(uri)){
			this.uploadJson(params);
		}else if("/doc/update.json".equals(uri)){
			this.updateJson(params);
		}else if("/doc/ava.html".equals(uri)){
			this.avalaibleDocs(params);
		}else if("/doc/get.html".equals(uri)){
			this.doc(params);
		}else if("/doc/get.json".equals(uri)){
			this.docJson(params);
		}else if("/doc/my/ava.json".equals(uri)){
			this.getMyAvalaibleDocs(params);
		}else if("/doc/manage.html".equals(uri)){
			this.manage(params);
		}else if("/doc/my/draft.json".equals(uri)){
			this.getDraft(params);
		}else if("doc/my/manage.html".equals(uri)){
			this.manageMy(params);
		}else if("/doc/draft.json".equals(uri)){
			this.allDraft(params);
		}else if("/doc/delete.json".equals(uri)){
			delete(params);
		}else if("/doc/forbid.json".equals(uri)){
			forbid(params);
		}else if("/doc/unforbid.json".equals(uri)){
			unforbid(params);
		}
	}
	
	public void unforbid(DealerParamWrap params) {
		String id = params.request.getParameter("id");
		LogicResult data = this.documentLogic.unforbid(id, this.getSelf(params.request));
		params.responseJson(data);
	}

	public void forbid(DealerParamWrap params){
		String id = params.request.getParameter("id");
		LogicResult data = this.documentLogic.forbid(id, this.getSelf(params.request));
		params.responseJson(data);
	}
	public void delete(DealerParamWrap params){
		String id = params.request.getParameter("id");
		LogicResult data = this.documentLogic.deleteDoc(id, this.getSelf(params.request));
		params.responseJson(data);
	}
	
	public void allDraft(DealerParamWrap params) {
		Map<String,Object> data = this.documentLogic.getAllDraft(params.request.getParameter("page"),this.getSelf(params.request));
		if(!data.containsKey("status")) data.put("status", DealerParamWrap.STATUS_SUCCESS);
		params.responseJson(data);
	}

	public void manageMy(DealerParamWrap params){
		String pageIndex = params.request.getParameter("page");
		User self = this.getSelf(params.request);
		LogicResult data = documentLogic.getMyAvalaibleDocs(pageIndex, self);
		if(data.getInt("status", DealerParamWrap.STATUS_COMMON_ERROR) == DealerParamWrap.STATUS_NOT_LOGIN){
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"doc/my/manage.html");
		}else{
			params.responseFtl(data, "document/manage/mydoc.ftl");
		}
	}
	
	public void docJson(DealerParamWrap params){
		String id = params.request.getParameter("id");
		StringBuilder msg = new StringBuilder();
		DocumentModal doc = documentLogic.getById(id, msg);
		if(doc.getStatus() != DocumentModal.STATUS_AVAILABLE){
			doc = null;
		}
		
		Map<String,Object> data = new HashMap<String,Object>();
		if(msg.length()==0){
			if(doc==null){
				data.put("status", DealerParamWrap.STATUS_NOT_FIND);
				data.put("msg", "该章节尚无内容文档！");
			}else{
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
				User provider = userLogic.getById(doc.getProviderId(), msg);
				doc.setProvider(provider);
				data.put("doc", doc);
			}
		}else{
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", msg);
		}
		params.responseJson(data);
	}
	public void getDraft(DealerParamWrap params){
		String pageIndex = params.request.getParameter("page");
		User self = this.getSelf(params.request);
		LogicResult data = documentLogic.getMyDraftDocs(pageIndex, self);
		params.responseJson(data);
	}
	public void manage(DealerParamWrap params){
		User self = this.getSelf(params.request);
		if(self != null){
			String pageIndex = params.request.getParameter("page");
			String statusStr = params.request.getParameter("status");
			params.responseFtl(documentLogic.getAllDocsByStatus(statusStr, pageIndex, self), "document/manage/index.ftl");
		}else{
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"/doc/manage.html");
		}
	}
	
	public void getMyAvalaibleDocs(DealerParamWrap params){
		String pageIndex = params.request.getParameter("page");
		User self = this.getSelf(params.request);
		LogicResult data = documentLogic.getMyAvalaibleDocs(pageIndex, self);
		params.responseJson(data);
	}
	
	public void ckeditorTest(DealerParamWrap params){
		params.responseFtl(null, "ckeditorTest.ftl");
	}
	
	private String _getImagesDirectory(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/WEB-INF/documents/images");
	}
	public void browseImages(DealerParamWrap params){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("resource_type", "Images");
		dataMap.put("current_folder_path", "/Images/");
		dataMap.put("current_folder_url", "/url");
		dataMap.put("current_folder_acl", 0);
		
		String directory = _getImagesDirectory(params.request);
		StringBuilder msg = new StringBuilder();
		List<FileModal> fileModals = documentLogic.findFiles(directory, new String[]{".jpg",".png",".jpeg"}, msg);
		if(msg.length()>0){
			Logger.getInstance().error(msg);
			dataMap.put("error_number", 1);
		}else{
			dataMap.put("files", fileModals);
			dataMap.put("error_number", 0);
		}
		params.responseFtl(dataMap, "document/file_browse.ftl");
	}
	public void getImage(DealerParamWrap params){
		String fileName = params.request.getParameter("name");
		if(fileName==null||fileName.lastIndexOf('.')==-1) return;
		String path = "/WEB-INF/documents/images/"+fileName;
		if(fileName.endsWith(".gif")){
			params.setResponse(DealerParamWrap.GIF_RESPONSE, null, path);
		}else if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
			params.setResponse(DealerParamWrap.JPG_RESPONSE, null, path);
		}else if(fileName.endsWith(".png")){
			params.setResponse(DealerParamWrap.PNG_RESPONSE, null, path);
		}
	}
	public void uploadPage(DealerParamWrap params){
		String id = params.request.getParameter("id");
		StringBuilder msg = new StringBuilder();
		DocumentModal doc = documentLogic.getById(id, msg);
		Map<String,Object> data = new HashMap<String,Object>();
		
		User self = this.getSelf(params.request);
		if(doc != null){
			if(doc.getStatus() == DocumentModal.STATUS_AVAILABLE || (self != null && self.get_id().equals(doc.getProviderId()))){
				data.put("document", doc);
			}
		}
		params.responseFtl(data, "document/upload.ftl");
	}
	public void uploadJson(DealerParamWrap params){
		String status = params.request.getParameter("status");
		String title = params.request.getParameter("title");
		String docContent = params.request.getParameter("doc");
		
		User self = getSelf(params.request);
		if(self==null){
			params.responseJson("{status:"+DealerParamWrap.STATUS_NOT_LOGIN+",msg:\"请先登录！\"}");
			return;
		}
		
		boolean exists = documentLogic.existsByTitleAndProvider(title, new ObjectId(self.get_id()));
		StringBuilder msg = new StringBuilder();
		if(status==null||(!status.equals(String.valueOf(DocumentModal.STATUS_AVAILABLE))&&!status.equals(String.valueOf(DocumentModal.STATUS_DRAFT)))){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"错误的状态！\"}");
		}else if(exists){
			if(status.equals(String.valueOf(DocumentModal.STATUS_AVAILABLE))){
				params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"已经存在相同标题的文章了，换个试试吧！\"}");
			}else{
				DocumentModal doc = new DocumentModal();
				doc.setProviderId(self.get_id());
				doc.setDocContent(docContent);
				doc.setTitle(title);
				this.documentLogic.update(doc, msg);
				if(msg.length()>0){
					params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\""+msg+"\"}");
				}else{
					params.responseJson("{status:"+DealerParamWrap.STATUS_SUCCESS+"}");
				}
			}
		}else{
			DocumentModal doc = new DocumentModal();
			doc.setTitle(title);
			doc.setDocContent(docContent);
			doc.setProviderId(self.get_id());
			doc.setStatus(Byte.parseByte(status));
			doc.setUploadTime(System.currentTimeMillis());
			
			documentLogic.insert(doc, msg);
			
			if(msg.length()==0){
				params.responseJson("{status:"+DealerParamWrap.STATUS_SUCCESS+"}");
			}else{
				params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\""+msg.toString()+"\"}");
			}
		}
	}
	public void updateJson(DealerParamWrap params){
		String status = params.request.getParameter("status");
		String title = params.request.getParameter("title");
		String docContent = params.request.getParameter("doc");
		
		User self = getSelf(params.request);
		if(self==null){
			params.responseJson("{status:"+DealerParamWrap.STATUS_NOT_LOGIN+",msg:\"请先登录！\"}");
			return;
		}
		
		boolean exists = documentLogic.existsByTitleAndProvider(title, new ObjectId(self.get_id()));
		if(status==null||(!status.equals("2")&&!status.equals("1"))){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"错误的状态！\"}");
		}else if(!exists){
			params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\"不存在这篇文章！\"}");
		}else{
			DocumentModal doc = new DocumentModal();
			doc.setTitle(title);
			doc.setDocContent(docContent);
			doc.setProviderId(self.get_id());
			doc.setStatus(Byte.parseByte(status));
			
			StringBuilder msg = new StringBuilder();
			documentLogic.update(doc, msg);
			
			if(msg.length()==0){
				params.responseJson("{status:"+DealerParamWrap.STATUS_SUCCESS+"}");
			}else{
				params.responseJson("{status:"+DealerParamWrap.STATUS_COMMON_ERROR+",msg:\""+msg.toString()+"\"}");
			}
		}
	}
	
	public void avalaibleDocs(DealerParamWrap params){
		String pageIndexStr = params.request.getParameter("page");
		params.responseFtl(documentLogic.getAllAvalaible(pageIndexStr,this.getSelf(params.request)), "document/avalaible.ftl");
	}
	public void doc(DealerParamWrap params){
		String id = params.request.getParameter("id");
		StringBuilder msg = new StringBuilder();
		DocumentModal doc = documentLogic.getById(id, msg);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("self", getSelf(params.request));
		if(msg.length()==0){
			if(doc==null){
				data.put("status", DealerParamWrap.STATUS_NOT_FIND);
			}else{
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
				User provider = userLogic.getById(doc.getProviderId(), msg);
				doc.setProvider(provider);
				data.put("doc", doc);
			}
		}else{
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", msg);
		}
		params.responseFtl(data, "document/doc.ftl");
	}
}

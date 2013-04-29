package com.network.logic.document;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.network.db.DocumentDB;
import com.network.db.UserDB;
import com.network.db.impl.DocumentDBImpl;
import com.network.db.impl.UserDBImpl;
import com.network.dealer.DealerParamWrap;
import com.network.logic.Logic;
import com.network.logic.LogicResult;
import com.network.modal.DocumentModal;
import com.network.modal.FileModal;
import com.network.modal.User;
import com.network.util.StringHelper;

public class DocumentLogic extends Logic {

	DocumentDB documentDB;
	UserDB userDB;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initLogic(ServletContext context) {
		// TODO Auto-generated method stub
		documentDB = dbManager.getDB(DocumentDBImpl.class);
		userDB = dbManager.getDB(UserDBImpl.class);
		
		this.setProperty("PAGE_SIZE", 2);
	}

	@Override
	protected void destroyLogic() {
		// TODO Auto-generated method stub

	}

	private boolean _isTheRightFileType(File file,String []types){
		int index = file.getName().lastIndexOf('.');
		if(index!=-1){
			String fileType = file.getName().substring(index);
			for(String type : types){
				if(fileType.equals(type)){
					return true;
				}
			}
		}
		return false;
	}
	public List<FileModal> findFiles(String directory,String []fileType,StringBuilder msg){
		List<FileModal> files = null;
		if(directory==null || directory.equals("")){
			msg.append("文件夹不能为空！");
		}else{
			File file = new File(directory);
			if(!file.exists()){
				msg.append("指定目录不存在！");
			}else if(!file.isDirectory()){
				msg.append("不是一个有效的目录！");
			}else{
				files = new LinkedList<FileModal>();
				File[] directoryFiles=file.listFiles();
				for(int i=0,il=directoryFiles.length;i<il;i++){
					if(!directoryFiles[i].isFile()) continue;
					if(!_isTheRightFileType(directoryFiles[i],fileType)) continue;
					FileModal fm = new FileModal();
					fm.setName(directoryFiles[i].getName());
					fm.setDate(directoryFiles[i].lastModified());
					fm.setSize(1);
					fm.setThumb(directoryFiles[i].getName());
					files.add(fm);
				}
			}
		}
		return files;
	}
	
	
	public boolean existsByTitleAndProvider(String title,ObjectId providerId){
		if(documentDB.findOneByTitleAndProvider(title, providerId)!=null){
			return true;
		}
		return false;
	}
	public void insert(DocumentModal doc,StringBuilder msg){
		if(!DocumentModal.isValideTitle(doc.getTitle())){
			msg.append("文档标题必须在10到1000个字符之间！");
		}else if(!DocumentModal.isValidateDocContent(doc.getDocContent(), msg)){
			msg.append(msg);
		}else{
			documentDB.insert(doc);
		}
	}
	
	public void update(DocumentModal doc,StringBuilder msg){
		if(DocumentModal.isValidateDocContent(doc.getDocContent(), msg)){
			documentDB.updateContentByTitleAndProvider(doc.getTitle(), new ObjectId(doc.getProviderId()), doc.getDocContent());
		}
	}
	
	public LogicResult getAllAvalaible(String pageIndex,User self){
		LogicResult data = new LogicResult();
		int index = 1;
		if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
		if(index<1) index = 1;
		
		int pageSize = this.getPropertyInteger("PAGE_SIZE",0);
		List<DocumentModal> docs = documentDB.findAllDocsByStatus(DocumentModal.STATUS_AVAILABLE,(index-1)*pageSize,pageSize);
		for(DocumentModal doc : docs){
			User provider = userDB.getById(new ObjectId(doc.getProviderId()));
			doc.setProvider(provider);
		}
		
		int totalCount = documentDB.getAllAvalaibleDocumentsCount();
		int pageCount = 0;
		pageCount = (totalCount % pageSize==0) ? totalCount / pageSize : (totalCount / pageSize + 1);
		
		data.put("documents", docs);
		data.put("page_index", index);
		data.put("page_size", pageSize);
		data.put("page_count", pageCount);
		data.put("total_count", totalCount);
		data.put("servertime", System.currentTimeMillis());
		data.put("status", DealerParamWrap.STATUS_SUCCESS);
		
		return data;
	}
	
	public Map<String,Object> getAllDraft(String pageIndex,User self){
		Map<String,Object> data = new HashMap<String,Object>();
		if(self.getIdentity() != User.IDENTITY_SUPER && !self.getPermissionDocument()){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			int index = 1;
			if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
			if(index<1) index = 1;
			
			int pageSize = this.getPropertyInteger("PAGE_SIZE",0);
			List<DocumentModal> docs = documentDB.findAllDocsByStatus(DocumentModal.STATUS_DRAFT,(index-1)*pageSize,pageSize);
			for(DocumentModal doc : docs){
				User provider = userDB.getById(new ObjectId(doc.getProviderId()));
				doc.setProvider(provider);
			}
			
			int totalCount = documentDB.getAllAvalaibleDocumentsCount();
			int pageCount = 0;
			pageCount = (totalCount % pageSize==0) ? totalCount / pageSize : (totalCount / pageSize + 1);
			
			data.put("documents", docs);
			data.put("page_index", index);
			data.put("page_size", pageSize);
			data.put("page_count", pageCount);
			data.put("total_count", totalCount);
			data.put("servertime", System.currentTimeMillis());
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}
		
		return data;
	}
	
	private boolean _hasManagerPermission(User self){
		if(self.getIdentity() != User.IDENTITY_SUPER && !self.getPermissionDocument()){
			return false;
		}
		return true;
	}
	public LogicResult getAllDocsByStatus(String statusStr,String pageIndex,User self){
		LogicResult data = new LogicResult();
		if(!_hasManagerPermission(self)){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			int index = 1;
			if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
			if(index<1) index = 1;
			
			int pageSize = this.getPropertyInteger("PAGE_SIZE",0);
			int status = DocumentModal.STATUS_AVAILABLE;
			if(StringHelper.isInteger(statusStr)) status = Integer.parseInt(statusStr);
			List<DocumentModal> docs = documentDB.findAllDocsByStatus(status,(index-1)*pageSize,pageSize);
			for(DocumentModal doc : docs){
				User provider = userDB.getById(new ObjectId(doc.getProviderId()));
				doc.setProvider(provider);
			}
			
			int totalCount = documentDB.getDocsCountByStatus(status);
			int pageCount = 0;
			pageCount = (totalCount % pageSize==0) ? totalCount / pageSize : (totalCount / pageSize + 1);
			
			data.put("documents", docs);
			data.put("page_index", index);
			data.put("page_size", pageSize);
			data.put("page_count", pageCount);
			data.put("total_count", totalCount);
			data.put("servertime", System.currentTimeMillis());
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
			data.put("documentStatus", status);
		}
		
		return data;
	}
	
	public LogicResult deleteDoc(String id,User self){
		LogicResult data = new LogicResult();
		if(!ObjectId.isValid(id)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "参数错误！");
		}else if(!_hasManagerPermission(self)){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			this.documentDB.setDocStatus(new ObjectId(id), DocumentModal.STATUS_MANAGER_DELETE);
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}
		return data;
	}
	public LogicResult forbid(String id,User self){
		LogicResult data = new LogicResult();
		if(!ObjectId.isValid(id)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "参数错误！");
		}else if(!_hasManagerPermission(self)){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			DocumentModal doc = this.documentDB.getById(new ObjectId(id));
			if(doc.getStatus() != DocumentModal.STATUS_AVAILABLE && doc.getStatus() != DocumentModal.STATUS_DRAFT){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "非法操作！只能禁用可用文档或者草稿");
			}else{
				this.documentDB.setDocStatus(new ObjectId(id), DocumentModal.STATUS_MANAGER_FORBID);
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return data;
	}
	public LogicResult unforbid(String id,User self){
		LogicResult data = new LogicResult();
		if(!ObjectId.isValid(id)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "参数错误！");
		}else if(!_hasManagerPermission(self)){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			DocumentModal doc = this.documentDB.getById(new ObjectId(id));
			if(doc.getStatus() != DocumentModal.STATUS_MANAGER_FORBID){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "非法操作！只能禁用可用文档或者草稿");
			}else{
				this.documentDB.setDocStatus(new ObjectId(id), DocumentModal.STATUS_AVAILABLE);
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return data;
	}
	
	public DocumentModal getById(String id,StringBuilder msg){
		DocumentModal doc = null;
		if(!ObjectId.isValid(id)){
			msg.append("非法参数！");
		}else{
			doc = documentDB.getById(new ObjectId(id));
		}
		return doc;
	}
	
	public LogicResult getMyAvalaibleDocs(String pageIndex,User self){
		LogicResult data = new LogicResult();
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else{
			int index = 1;
			if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
			if(index < 1) index = 1;
			
			int pageSize = this.getPropertyInteger("PAGE_SIZE", 0);
			int totalCount = documentDB.getDocsCountByStatusAndProvider(new ObjectId(self.get_id()), DocumentModal.STATUS_AVAILABLE);
			List<DocumentModal> documents = this.documentDB.getDocsByStatusAndProvider(new ObjectId(self.get_id()), DocumentModal.STATUS_AVAILABLE, (index-1)*pageSize, pageSize);
			data.put("documents", documents);
			data.put("page_index", index);
			data.put("page_size", pageSize);
			data.put("page_count", totalCount%pageSize==0 ? totalCount/pageSize : (totalCount/pageSize+1));
			data.put("total_count", totalCount);
			data.put("servertime", System.currentTimeMillis());
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}
		
		return data;
	}
	
	public LogicResult getMyDraftDocs(String pageIndex,User self){
		LogicResult data = new LogicResult();
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else{
			int index = 1;
			if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
			if(index < 1) index = 1;
			
			int pageSize = this.getPropertyInteger("PAGE_SIZE", 0);
			int totalCount = documentDB.getDocsCountByStatusAndProvider(new ObjectId(self.get_id()), DocumentModal.STATUS_DRAFT);
			List<DocumentModal> documents = this.documentDB.getDocsByStatusAndProvider(new ObjectId(self.get_id()), DocumentModal.STATUS_AVAILABLE, (index-1)*pageSize, pageSize);
			data.put("documents", documents);
			data.put("page_index", index);
			data.put("page_size", pageSize);
			data.put("page_count", totalCount%pageSize==0 ? totalCount/pageSize : (totalCount/pageSize+1));
			data.put("total_count", totalCount);
			data.put("servertime", System.currentTimeMillis());
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}
		
		return data;
	}
	
	public List<DocumentModal> getMyDocs(String selfId,StringBuilder msg){
		if(!ObjectId.isValid(selfId)){
			msg.append("非法操作！");
			return null;
		}
		
		return this.documentDB.getMyDocs(new ObjectId(selfId));
	}
}

package com.network.logic.chapter;

import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.network.db.ChapterDB;
import com.network.db.DocumentDB;
import com.network.db.impl.ChapterDBImpl;
import com.network.db.impl.DocumentDBImpl;
import com.network.dealer.DealerParamWrap;
import com.network.logic.Logic;
import com.network.logic.LogicResult;
import com.network.modal.ChapterModal;
import com.network.modal.DocumentModal;
import com.network.modal.User;

public class ChapterLogic extends Logic {
	private ChapterDB chapterDB;
	private DocumentDB documentDB;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initLogic(ServletContext context) {
		// TODO Auto-generated method stub
		this.chapterDB = this.dbManager.getDB(ChapterDBImpl.class);
		this.documentDB = this.dbManager.getDB(DocumentDBImpl.class);
	}

	@Override
	protected void destroyLogic() {
		// TODO Auto-generated method stub

	}
	
	public boolean add(ChapterModal chapter,StringBuilder msg){
		if(!ChapterModal.isName(chapter.getName())){
			msg.append("名称不合法！");
			return false;
		}
		
		if(chapter.getDepth()<=0){
			msg.append("章节目录深度必须大于或者等于1！");
			return false;
		}
		
		if(chapter.getDepth()>1){
			if(!ObjectId.isValid(chapter.getParentId())){
				msg.append("非法的父节点！");
				return false;
			}
		}
		
		if(chapterDB.chapterExist(chapter.getDepth(), chapter.getName())){
			msg.append("已经存在这样的章节！换个名字试试吧！");
			return false;
		}
		
		int index = this.chapterDB.getMaxIndexByDepth(chapter.getDepth());
		chapter.setIndex(index+1);
		this.chapterDB.insert(chapter);
		return true;
	}
	
	
	public LogicResult getChaptersByDepth(byte depth){
		LogicResult data = new LogicResult();
		data.put("status", DealerParamWrap.STATUS_SUCCESS);
		data.put("chapters", this.chapterDB.getChaptersByDepth(depth));
		return data;
	}
	
	public List<ChapterModal> getChaptersByParentId(String id,StringBuilder msg){
		if(!ObjectId.isValid(id)){
			msg.append("不合法的父节点！");
			return null;
		}
		
		return chapterDB.getChaptersByParentId(new ObjectId(id));
	}
	public boolean modify(String id,String name,StringBuilder msg){
		if(!ObjectId.isValid(id)){
			msg.append("不合法的父节点！");
			return false;
		}
		
		if(!ChapterModal.isName(name)){
			msg.append("不合法的名称！");
			return false;
		}
		
		if(!this.chapterDB.chapterExistById(new ObjectId(id))){
			msg.append("不存在这样的章节！");
			return false;
		}
		
		this.chapterDB.updateName(new ObjectId(id), name);
		return true;
	}
	
	public String setDoc(String chapterId,String docId,User self){
		StringBuffer msg = new StringBuffer();
		if(!ObjectId.isValid(chapterId)){
			msg.append("非法的章节id");
		}else if(!ObjectId.isValid(docId)){
			msg.append("非法的文档id");
		}else{
			DocumentModal doc = this.documentDB.getById(new ObjectId(docId));
			if(doc == null){
				msg.append("不存在这篇文档");
			}else{
				if(!self.get_id().equals(doc.getProviderId())){
					msg.append("这篇文档不属于您");
				}else{
					this.chapterDB.setDoc(new ObjectId(chapterId), new ObjectId(docId));
				}
			}
		}
		return msg.toString();
	}
	
	public LogicResult delete(String id,User self){
		LogicResult data = new LogicResult();
		if(self==null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else if(self.getIdentity() != User.IDENTITY_SUPER && !self.getPermissionCourseStructure()){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限");
		}else if(!ObjectId.isValid(id)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "错误的参数！");
		}else {
			ObjectId chapterId= new ObjectId(id);
			ChapterModal chapter = this.chapterDB.get(chapterId);
			if(chapter == null){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "不存在这个章节！");
			}else{
				if(this.chapterDB.hasChild(chapterId)){
					data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
					data.put("msg", "您必须删除该章节的子章节后才能删除该章节");
				}else{
					this.chapterDB.removeChild(new ObjectId(chapter.getParentId()), chapterId);
					this.chapterDB.delete(chapterId);
					data.put("status", DealerParamWrap.STATUS_SUCCESS);
				}
			}
		}
		return data;
	}

}

package com.network.db.impl;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.network.db.ChapterDB;
import com.network.db.DB;
import com.network.modal.ChapterModal;

public class ChapterDBImpl extends DB implements ChapterDB {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDB(ServletContext context) {
		// TODO Auto-generated method stub
		this.createCollection("chapter");
	}

	@Override
	protected void destroyDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addChild(ObjectId parentId, ObjectId childId) {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject("_id", parentId);
		DBObject update = new BasicDBObject("$push",new BasicDBObject("children",childId));
		this.collection.update(query, update);
	}
	

	@Override
	public void insert(ChapterModal chapter) {
		// TODO Auto-generated method stub
		this.collection.insert(chapter.toBasicDBObject());
	}

	@Override
	public boolean chapterExist(byte depth, String name) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.put("name", name);
		query.put("depth", depth);
		if(this.collection.findOne(query)!=null){
			return true;
		}
		return false;
	}

	@Override
	public List<ChapterModal> getChaptersByDepth(byte depth) {
		// TODO Auto-generated method stub
		List<ChapterModal> chapters = new LinkedList<ChapterModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("depth", depth);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("index",1));
		while(cs.hasNext()){
			ChapterModal chapter = new ChapterModal();
			chapter.parseFromDBObject((BasicDBObject)cs.next());
			chapters.add(chapter);
		}
		cs.close();
		return chapters;
	}

	@Override
	public List<ChapterModal> getChaptersByParentId(ObjectId parentId) {
		// TODO Auto-generated method stub
		List<ChapterModal> chapters = new LinkedList<ChapterModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("parent_id", parentId);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("index",0));
		while(cs.hasNext()){
			ChapterModal chapter = new ChapterModal();
			chapter.parseFromDBObject((BasicDBObject)cs.next());
			chapters.add(chapter);
			
			ObjectId childId = new ObjectId(chapter.get_id());
			if(!this.hasChild(parentId, childId)){
				this.addChild(parentId, childId);
			}
		}
		cs.close();
		return chapters;
	}

	@Override
	public boolean hasChild(ObjectId parentId,ObjectId childId) {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject("_id", parentId);
		BasicDBObject parentChapter = (BasicDBObject)this.collection.findOne(query);
		if(parentChapter.containsField("children")){
			List<ObjectId> chapters = (List<ObjectId>)parentChapter.get("children");
			for(int i=0,il=chapters.size();i<il;i++){
				if(chapters.get(i).equals(childId)) return true;
			}
		}
		return false;
	}

	@Override
	public boolean chapterExistById(ObjectId id) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		return this.collection.findOne(query)!=null;
	}

	@Override
	public void updateName(ObjectId id, String name) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject("_id",id);
		BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("name",name));
		this.collection.update(query, update);
	}

	@Override
	public void setDoc(ObjectId chapterId, ObjectId docId) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject("_id",chapterId);
		BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("doc_id",docId));
		this.collection.update(query, update);
	}

	@Override
	public int getMaxIndexByDepth(int depth) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("depth", depth);
		int ret = 0;
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("index",-1)).limit(1);
		if(cs.hasNext()){
			ret = ((BasicDBObject)cs.next()).getInt("index");
		}
		cs.close();
		return ret;
	}

	@Override
	public ChapterModal get(ObjectId id) {
		// TODO Auto-generated method stub
		ChapterModal chapter = new ChapterModal();
		if(!chapter.parseFromDBObject((BasicDBObject)this.collection.findOne(new BasicDBObject("_id",id)))){
			chapter = null;
		}
		return chapter;
	}

	@Override
	public boolean hasChild(ObjectId id) {
		// TODO Auto-generated method stub
		if(null != this.collection.findOne(new BasicDBObject("parent_id",id))){
			return true;
		}
		return false;
	}

	@Override
	public void delete(ObjectId id) {
		// TODO Auto-generated method stub
		this.collection.remove(new BasicDBObject("_id",id));
		
	}

	@Override
	public void removeChild(ObjectId parentId, ObjectId childId) {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject("_id", parentId);
		DBObject update = new BasicDBObject("$pull",new BasicDBObject("children",childId));
		this.collection.update(query, update);
	}
}

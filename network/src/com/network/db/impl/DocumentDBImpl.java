package com.network.db.impl;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.network.db.DB;
import com.network.db.DocumentDB;
import com.network.modal.DocumentModal;

public class DocumentDBImpl extends DB implements DocumentDB {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDB(ServletContext context) {
		// TODO Auto-generated method stub
		this.createCollection("document");
	}

	@Override
	protected void destroyDB() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void insert(DocumentModal doc) {
		// TODO Auto-generated method stub
		DBObject obj = doc.toBasicDBObject();
		this.collection.insert(obj);
	}

	@Override
	public DBObject findOneByTitleAndProvider(String title,ObjectId providerId) {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject();
		query.put("title", title);
		query.put("provider_id", providerId);
		DBObject obj = this.collection.findOne(query);
		return obj;
	}

	@Override
	public DocumentModal getById(ObjectId id) {
		// TODO Auto-generated method stub
		DBObject obj = this.collection.findOne(new BasicDBObject("_id",id));
		DocumentModal doc = new DocumentModal();
		doc.parseFromDBObject((BasicDBObject)obj);
		return doc;
	}

	@Override
	public void updateContentByTitleAndProvider(String title,ObjectId providerId, String content) {
		// TODO Auto-generated method stub
		DBObject query = new BasicDBObject();
		query.put("title", title);
		query.put("provider_id", providerId);
		DBObject update = new BasicDBObject("$set",new BasicDBObject("docContent",content));
		this.collection.update(query, update);
	}

	@Override
	public int getAllAvalaibleDocumentsCount() {
		// TODO Auto-generated method stub
		DBCursor cs = this.collection.find(new BasicDBObject("status",DocumentModal.STATUS_AVAILABLE));
		int count = cs.size();
		cs.close();
		return count;
	}

	@Override
	public List<DocumentModal> getMyDocs(ObjectId providerId) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("provider_id", providerId);
		DBCursor cs = this.collection.find(query);
		List<DocumentModal> docs = new LinkedList<DocumentModal>();
		while(cs.hasNext()){
			BasicDBObject obj = (BasicDBObject)cs.next();
			DocumentModal doc = new DocumentModal();
			if(!doc.parseFromDBObject(obj)) continue;
			docs.add(doc);
		}
		return docs;
	}

	@Override
	public List<DocumentModal> findAllDocsByStatus(int status, int start,int offset) {
		// TODO Auto-generated method stub
		List<DocumentModal> docs = new LinkedList<DocumentModal>();
		DBCursor cs = this.collection.find(new BasicDBObject("status",status))
				.sort(new BasicDBObject("upload_time",-1)).skip(start).limit(offset);
		while(cs.hasNext()){
			DBObject obj = cs.next();
			DocumentModal doc = new DocumentModal();
			doc.parseFromDBObject((BasicDBObject)obj);
			docs.add(doc);
		}
		cs.close();
		return docs;
	}

	@Override
	public List<DocumentModal> getDocsByStatusAndProvider(ObjectId providerId, int status,int start,int offset) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("provider_id", providerId);
		query.append("status", status);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("upload_time",-1))
									.skip(start).limit(offset);
		List<DocumentModal> docs = new LinkedList<DocumentModal>();
		while(cs.hasNext()){
			BasicDBObject obj = (BasicDBObject)cs.next();
			DocumentModal doc = new DocumentModal();
			if(!doc.parseFromDBObject(obj)) continue;
			docs.add(doc);
		}
		cs.close();
		return docs;
	}

	@Override
	public int getDocsCountByStatusAndProvider(ObjectId providerId, int status) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("provider_id", providerId);
		query.append("status", status);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("upload_time",-1));
		int ret = cs.size();
		cs.close();
		return ret;
	}

	@Override
	public void setDocStatus(ObjectId id,int status) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		BasicDBObject update = new BasicDBObject();
		update.append("$set", new BasicDBObject("status",status));
		this.collection.update(query, update);
	}

	@Override
	public int getDocsCountByStatus(int status) {
		// TODO Auto-generated method stub
		DBCursor cs = this.collection.find(new BasicDBObject("status",status));
		int ret = cs.size();
		cs.close();
		return ret;
	}

}

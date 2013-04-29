package com.network.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.DBObject;
import com.network.modal.DocumentModal;

public interface DocumentDB{
	public void insert(DocumentModal doc);
	public DBObject findOneByTitleAndProvider(String title,ObjectId providerId);
	public int getAllAvalaibleDocumentsCount();
	public int getDocsCountByStatusAndProvider(ObjectId providerId,int status);
	public int getDocsCountByStatus(int status);
	public DocumentModal getById(ObjectId id);
	public void updateContentByTitleAndProvider(String title,ObjectId providerId,String content);
	public List<DocumentModal> getMyDocs(ObjectId providerId);
	public List<DocumentModal> getDocsByStatusAndProvider(ObjectId providerId,int status,int start,int offset);
	public List<DocumentModal> findAllDocsByStatus(int status,int start,int offset);
	public void setDocStatus(ObjectId id,int status);
}

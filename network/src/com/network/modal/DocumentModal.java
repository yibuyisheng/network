package com.network.modal;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class DocumentModal implements MongoDBModal{
	public static final int STATUS_DRAFT=1;
	public static final int STATUS_AVAILABLE=2;
	/**
	 * this document is deleted by manager
	 */
	public static final int STATUS_MANAGER_DELETE=3;
	/**
	 * this document is deleted by self
	 */
	public static final int STATUS_SELF_DELETE=4;
	/**
	 * this document is forbad by manager
	 */
	public static final int STATUS_MANAGER_FORBID=5;
	
	private String _id;
	private String title;
	private String docContent;
	private User provider;
	private long uploadTime;
	private byte status;
	
	public static boolean isValideTitle(String src){
		boolean ret = true;
		if(src==null || src.length() < 10 || src.length()>1000){
			ret = false;
		}
		return ret;
	}
	public static boolean isValidateDocContent(String src,StringBuilder msg){
		boolean ret = true;
		if(src==null||src.length()==0){
			ret = false;
			if(msg!=null) msg.append("文档内容不能为空！");
		}else if(src.length() < 10){
			ret = false;
			if(msg!=null) msg.append("文档内容过短！");
		}else if(src.length() > 50000){
			ret = false;
			if(msg!=null) msg.append("文档内容过长！");
		}
		return ret;
	}
	
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getDocContent() {
		return docContent;
	}
	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}
	public String getId() {
		return _id;
	}
	public String get_id() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public User getProvider() {
		return provider;
	}
	public void setProvider(User provider) {
		this.provider = provider;
	}
	public long getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getProviderId(){
		return this.provider==null?null:this.provider.get_id();
	}
	public void setProviderId(String id){
		if(this.provider==null) provider = new User();
		provider.set_id(id);
	}
	@Override
	public BasicDBObject toBasicDBObject() {
		// TODO Auto-generated method stub
		BasicDBObject obj = new BasicDBObject();
		obj.put("docContent", docContent);
		obj.put("status", status);
		obj.put("title", title);
		obj.put("upload_time", uploadTime);
		obj.put("provider_id", new ObjectId(this.provider.get_id()));
		return obj;
	}
	@Override
	public boolean parseFromDBObject(BasicDBObject obj) {
		// TODO Auto-generated method stub
		if(obj==null) return false;
		if(obj.containsField("_id")) this._id = String.valueOf(obj.get("_id"));
		if(obj.containsField("docContent")) this.docContent = String.valueOf(obj.get("docContent"));
		if(obj.containsField("status")) this.status = Byte.parseByte(String.valueOf(obj.get("status")));
		if(obj.containsField("title")) this.title = String.valueOf(obj.get("title"));
		if(obj.containsField("upload_time")) this.uploadTime = Long.parseLong(String.valueOf(obj.get("upload_time")));
		if(obj.containsField("provider_id")) {
			this.provider = new User();
			this.provider.set_id(String.valueOf(obj.get("provider_id")));
		}
		
		return true;
	}
	
}

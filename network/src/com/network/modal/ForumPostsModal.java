package com.network.modal;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;

public class ForumPostsModal implements MongoDBModal {
	private String _id;
	/**
	 * 发布的时间
	 */
	private long createTime;
	/**
	 * 如果是帖子的话，这个时间代表最后回复的时间
	 */
	private long refreshTime;
	/**
	 * 帖子或者回复的内容
	 */
	private String content;
	/**
	 * 帖子的标题，回复没有标题
	 */
	private String title;
	/**
	 * 此条记录的类型（帖子、回复等）
	 */
	private int kind;
	/**
	 * 发布帖子的用户
	 */
	private User provider;
	/**
	 * 如果是回复的话，这个字段指代回复的帖子
	 */
	private ForumPostsModal target;
	/**
	 * 帖子的状态，比如审核中、可显示、不可显示等
	 */
	private int state;
	/**
	 * 回复数
	 */
	private int replyNo;
	/**
	 * 这个帖子被查看的次数
	 */
	private int viewNo;
	
	
	
	public static final int KIND_POST=1;
	public static final int KIND_REPLY=2;
	
	public static final int STATE_SHOW=1;
	public static final int STATE_HIDE=2;
	public static final int STATE_EXAMING=3;
	
	
	@Override
	public BasicDBObject toBasicDBObject() {
		// TODO Auto-generated method stub
		BasicDBObject obj = new BasicDBObject();
		if(ObjectId.isValid(this._id)){
			obj.append("_id", new ObjectId(this._id));
		}
		obj.append("create_time", createTime);
		obj.append("refresh_time", refreshTime);
		obj.append("content", content);
		obj.append("title", title);
		obj.append("kind", kind);
		if(this.provider!=null && ObjectId.isValid(this.provider.get_id())){
			obj.append("provider_id", new ObjectId(this.provider.get_id()));
		}
		if(this.target!= null && ObjectId.isValid(this.target.get_id())){
			obj.append("target_id", new ObjectId(this.target.get_id()));
		}
		obj.append("state", state);
		obj.append("reply_no", this.replyNo);
		obj.append("view_no", viewNo);
		return obj;
	}

	@Override
	public boolean parseFromDBObject(BasicDBObject obj) {
		// TODO Auto-generated method stub
		if(obj == null) return false;
		if(obj.containsField("_id")){
			this._id = obj.getObjectId("_id").toString();
		}
		if(obj.containsField("create_time")){
			this.createTime = obj.getLong("create_time");
		}
		if(obj.containsField("refresh_time")){
			this.refreshTime = obj.getLong("refresh_time");
		}
		if(obj.containsField("content")) this.content = obj.getString("content");
		if(obj.containsField("title")) this.title = obj.getString("title");
		if(obj.containsField("kind")) this.kind = obj.getInt("kind");
		if(obj.containsField("provider_id")){
			this.provider = new User();
			this.provider.set_id(obj.getString("provider_id"));
		}
		if(obj.containsField("target_id")){
			this.target = new ForumPostsModal();
			this.target.set_id(obj.getString("target_id"));
		}
		if(obj.containsField("state")){
			this.state = obj.getInt("state");
		}
		if(obj.containsField("reply_no")){
			this.replyNo = obj.getInt("reply_no");
		}
		if(obj.containsField("view_no")){
			this.viewNo = obj.getInt("view_no");
		}
		return true;
	}

	public String get_id() {
		return _id;
	}
	public String getId(){
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}
	public void setId(String id){
		this._id = id;
	}

	public int getViewNo() {
		return viewNo;
	}

	public void setViewNo(int viewNo) {
		this.viewNo = viewNo;
	}

	public long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(long create_time) {
		this.createTime = create_time;
	}

	public long getRefreshTime() {
		return this.refreshTime;
	}

	public void setRefreshTime(long refresh_time) {
		this.refreshTime = refresh_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public User getProvider() {
		return provider;
	}
	public String getProviderId(){
		return this.provider == null ? null : this.provider.get_id();
	}

	public void setProvider(User provider) {
		this.provider = provider;
	}
	
	public void setProviderId(String id){
		if(this.provider == null) this.provider = new User();
		this.provider.setId(id);
	}

	public ForumPostsModal getTarget() {
		return target;
	}
	
	public String getTargetId(){
		return this.target == null ? null : this.target.get_id();
	}

	public void setTarget(ForumPostsModal target) {
		this.target = target;
	}
	
	public void setTargetId(String id){
		if(this.target == null) this.target = new ForumPostsModal();
		this.target.set_id(id);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(int replyNo) {
		this.replyNo = replyNo;
	}
	
	/**
	 * the post's title's length must be between 5 and 100.
	 * @param src
	 * @return
	 */
	public static boolean isTitlte(String src){
		if(src==null){
			return false;
		}
		if(src.length() < 5 || src.length() > 100){
			return false;
		}
		return true;
	}
	/**
	 * the post's title's length must be between 30 and 50000.
	 * @param src
	 * @return
	 */
	public static boolean isContent(String src){
		if(src==null){
			return false;
		}
		if(src.length() < 30 || src.length() > 50000){
			return false;
		}
		return true;
	}
}

package com.network.db.impl;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.network.db.DB;
import com.network.db.PostsDB;
import com.network.modal.ForumPostsModal;

public class PostsDBImpl extends DB implements PostsDB {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDB(ServletContext context) {
		// TODO Auto-generated method stub
		this.createCollection("posts");
	}

	@Override
	protected void destroyDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ForumPostsModal> getAllShowPosts(int start, int len) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> ret = new LinkedList<ForumPostsModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_POST);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("refresh_time",-1)).skip(start).limit(len);
		while(cs.hasNext()){
			ForumPostsModal post = new ForumPostsModal();
			if(post.parseFromDBObject((BasicDBObject)cs.next())){
				ret.add(post);
			}
		}
		cs.close();
		return ret;
	}

	@Override
	public int getShowPostsCount() {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_POST);
		return this.collection.find(query).size();
	}

	@Override
	public void add(ForumPostsModal post) {
		// TODO Auto-generated method stub
		this.collection.insert(post.toBasicDBObject());
	}

	@Override
	public ForumPostsModal getById(ObjectId id) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		ForumPostsModal post = new ForumPostsModal();
		if(!post.parseFromDBObject((BasicDBObject)this.collection.findOne(query))){
			post = null;
		}
		return post;
	}

	@Override
	public boolean existByTitle(String title) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addViewNo(ObjectId id,int incNo) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		BasicDBObject update = new BasicDBObject();
		update.append("$inc", new BasicDBObject("view_no",incNo));
		this.collection.update(query, update);
	}

	@Override
	public List<ForumPostsModal> getPostsByState(int state, int start, int len) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> posts = new LinkedList<ForumPostsModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("state", state);
		query.append("kind", ForumPostsModal.KIND_POST);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("refresh_time",-1)).skip(start).limit(len);
		while(cs.hasNext()){
			ForumPostsModal post = new ForumPostsModal();
			if(post.parseFromDBObject((BasicDBObject)cs.next())){
				posts.add(post);
			}
		}
		cs.close();
		return posts;
	}

	@Override
	public List<ForumPostsModal> getHotPosts(int hotNo,int start, int len) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> posts = new LinkedList<ForumPostsModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_POST);
		query.append("view_no", new BasicDBObject("$gt",hotNo));
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("refresh_time",-1)).skip(start).limit(len);
		while(cs.hasNext()){
			ForumPostsModal post = new ForumPostsModal();
			if(post.parseFromDBObject((BasicDBObject)cs.next())){
				posts.add(post);
			}
		}
		cs.close();
		return posts;
	}

	@Override
	public int getHotPostsCount(int hotNo) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_POST);
		query.append("view_no", new BasicDBObject("$gt",hotNo));
		DBCursor cs = this.collection.find(query);
		int ret = cs.size();
		cs.close();
		return ret;
	}

	@Override
	public List<ForumPostsModal> getReply(ObjectId targetId, int start, int len) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> posts = new LinkedList<ForumPostsModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_REPLY);
		query.append("target_id", targetId);
		DBCursor cs = this.collection.find(query).sort(new BasicDBObject("create_time",-1)).skip(start).limit(len);
		while(cs.hasNext()){
			ForumPostsModal post = new ForumPostsModal();
			if(post.parseFromDBObject((BasicDBObject)cs.next())){
				posts.add(post);
			}
		}
		cs.close();
		return posts;
	}

	@Override
	public void addReplyNo(ObjectId id, int incNo) {
		// TODO Auto-generated method stub
		BasicDBObject query = new BasicDBObject();
		query.append("_id", id);
		BasicDBObject update = new BasicDBObject();
		update.append("$inc", new BasicDBObject("reply_no",incNo));
		this.collection.update(query, update);
	}

	@Override
	public int getReplyCount(ObjectId targetId) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> posts = new LinkedList<ForumPostsModal>();
		BasicDBObject query = new BasicDBObject();
		query.append("state", ForumPostsModal.STATE_SHOW);
		query.append("kind", ForumPostsModal.KIND_REPLY);
		query.append("target_id", targetId);
		DBCursor cs = this.collection.find(query);
		int ret = cs.size();
		cs.close();
		return ret;
	}

	@Override
	public List<ForumPostsModal> find(DBObject query) {
		// TODO Auto-generated method stub
		List<ForumPostsModal> posts = new LinkedList<ForumPostsModal>();
		DBCursor cs = this.collection.find(query);
		while(cs.hasNext()){
			ForumPostsModal post = new ForumPostsModal();
			if(post.parseFromDBObject((BasicDBObject)cs.next())){
				posts.add(post);
			}
		}
		cs.close();
		return posts;
	}

	@Override
	public ForumPostsModal findOne(DBObject query) {
		ForumPostsModal post = new ForumPostsModal();
		if(!post.parseFromDBObject((BasicDBObject)this.collection.findOne(query))){
			post = null;
		}
		return post;
	}
}

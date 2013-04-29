package com.network.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.DBObject;
import com.network.modal.ForumPostsModal;

public interface PostsDB {
	public List<ForumPostsModal> getAllShowPosts(int start,int len);
	public int getShowPostsCount();
	public void add(ForumPostsModal post);
	public ForumPostsModal getById(ObjectId id);
	public boolean existByTitle(String title);
	public void addViewNo(ObjectId id,int incNo);
	public void addReplyNo(ObjectId id,int incNo);
	public List<ForumPostsModal> getPostsByState(int state,int start,int len);
	public List<ForumPostsModal> getHotPosts(int hotNo,int start,int len);
	public int getHotPostsCount(int hotNo);
	public List<ForumPostsModal> getReply(ObjectId targetId,int start,int len);
	public int getReplyCount(ObjectId targetId);
	public List<ForumPostsModal> find(DBObject query);
	public ForumPostsModal findOne(DBObject query);
}

package com.network.logic.forum;

import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.network.db.PostsDB;
import com.network.db.UserDB;
import com.network.db.impl.PostsDBImpl;
import com.network.db.impl.UserDBImpl;
import com.network.dealer.DealerParamWrap;
import com.network.logic.Logic;
import com.network.logic.LogicResult;
import com.network.modal.ForumPostsModal;
import com.network.modal.User;
import com.network.util.StringHelper;

public class PostsLogic extends Logic {
	
	private PostsDB postsDB;
	private UserDB userDB;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initLogic(ServletContext context) {
		// TODO Auto-generated method stub
		this.postsDB = this.dbManager.getDB(PostsDBImpl.class);
		this.userDB = this.dbManager.getDB(UserDBImpl.class);
		this.setProperty("POSTS_PAGE_SIZE", 10);
		this.setProperty("MAX_VIEW_NO", 10000);
		this.setProperty("HOT_POST_NO", 100);
		this.setProperty("COMMENTS_PAGE_SIZE", 3);
	}

	@Override
	protected void destroyLogic() {
		// TODO Auto-generated method stub

	}

	public LogicResult getAllShowPosts(String pageIndex){
		LogicResult data = new LogicResult();
		int index = 1;
		if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
		if(index < 1) index = 1;
		int pageSize = this.getPropertyInteger("POSTS_PAGE_SIZE", 0);
		int count = this.postsDB.getShowPostsCount();
		List<ForumPostsModal> posts = this.postsDB.getAllShowPosts((index-1)*pageSize, pageSize);
		for(ForumPostsModal post : posts){
			post.setProvider(this.userDB.getById(new ObjectId(post.getProviderId())));
		}
		data.put("status", DealerParamWrap.STATUS_SUCCESS);
		data.put("posts", posts);
		data.put("page_size", pageSize);
		data.put("page_index", index);
		data.put("page_count", count%pageSize==0 ? count/pageSize : (count/pageSize+1));
		return data;
	}
	
	public LogicResult getHotPosts(String pageIndex){
		LogicResult data = new LogicResult();
		int index = 1;
		if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
		if(index < 1) index = 1;
		int pageSize = this.getPropertyInteger("POSTS_PAGE_SIZE", 0);
		int count = this.postsDB.getHotPostsCount(this.getPropertyInteger("HOT_POST_NO", 0));
		List<ForumPostsModal> posts = this.postsDB.getHotPosts(this.getPropertyInteger("HOT_POST_NO", 0),(index-1)*pageSize, pageSize);
		for(ForumPostsModal post : posts){
			post.setProvider(this.userDB.getById(new ObjectId(post.getProviderId())));
		}
		data.put("status", DealerParamWrap.STATUS_SUCCESS);
		data.put("posts", posts);
		data.put("page_size", pageSize);
		data.put("page_index", index);
		data.put("page_count", count%pageSize==0 ? count/pageSize : (count/pageSize+1));
		return data;
	}
	
	public LogicResult publish(String title,String content,User self){
		LogicResult data = new LogicResult();
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else if(!ForumPostsModal.isTitlte(title)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "标题必须在5到100个字符之间！");
		}else if(!ForumPostsModal.isContent(content)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "内容必须在30到50000个字符之间！");
		}else{
			DBObject query = new BasicDBObject("title",title);
			ForumPostsModal post = this.postsDB.findOne(query);
			if(post != null){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "已经存在相同标题的帖子了，换个标题试试吧！");
			}else{
				post = new ForumPostsModal();
				post.setTitle(title);
				post.setContent(content);
				post.setCreateTime(System.currentTimeMillis());
				post.setRefreshTime(System.currentTimeMillis());
				post.setKind(ForumPostsModal.KIND_POST);
				post.setProvider(self);
				post.setState(ForumPostsModal.STATE_SHOW);
				post.setReplyNo(0);
				post.setViewNo(0);
				this.postsDB.add(post);
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return data;
	}
	
	public LogicResult getPost(String id){
		LogicResult data = new LogicResult();
		if(!ObjectId.isValid(id)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "请求参数错误！");
		}else{
			ObjectId postId = new ObjectId(id);
			ForumPostsModal post = this.postsDB.getById(postId);
			if(post == null){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "这个帖子不存在！");
			}else if(post.getKind() != ForumPostsModal.KIND_POST){
				data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				data.put("msg", "这不是是一个帖子！");
			}else if(post.getState() != ForumPostsModal.STATE_SHOW){
				data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				data.put("msg", "没有权限！");
			}else{
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
				data.put("post", post);
				
				if(post.getViewNo() < this.getPropertyInteger("MAX_VIEW_NO", 0)){
					this.postsDB.addViewNo(postId , 1);
				}
			}
		}
		return data;
	}
	
	public LogicResult getComments(String targetId,String pageIndex){
		LogicResult data = new LogicResult();
		if(!ObjectId.isValid(targetId)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "请求参数错误！");
		}else{
			int index = 1;
			if(StringHelper.isInteger(pageIndex)) index = Integer.parseInt(pageIndex);
			if(index < 1) index = 1;
			
			int pageSize = this.getPropertyInteger("COMMENTS_PAGE_SIZE", 0);
			ObjectId postId = new ObjectId(targetId);
			ForumPostsModal post = this.postsDB.getById(postId);
			int count = post.getReplyNo();
			
			List<ForumPostsModal> comments = this.postsDB.getReply(postId, (index-1)*pageSize, pageSize);
			for(ForumPostsModal comment : comments){
				comment.setProvider(this.userDB.getById(new ObjectId(comment.getProviderId())));
			}
			data.put("comments", comments);
			data.put("comments_page_size", pageSize);
			data.put("comments_page_index", index);
			data.put("comments_page_count", count%pageSize==0 ? count/pageSize : (count/pageSize+1));
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
		}
		return data;
	}
	
	public LogicResult reply(String content,String targetId,User self){
		LogicResult data = new LogicResult();
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else if(!ObjectId.isValid(targetId)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "错误的参数！");
		}else if(!ForumPostsModal.isContent(content)){
			data.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			data.put("msg", "内容必须在30到50000个字符之间！");
		}else{
			ForumPostsModal post = this.postsDB.getById(new ObjectId(targetId));
			if(post == null){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "不存在这篇帖子！");
			}else if(post.getState() != ForumPostsModal.STATE_SHOW || post.getKind() != ForumPostsModal.KIND_POST){
				data.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				data.put("msg", "不能回复这篇帖子！");
			}else{
				ForumPostsModal reply = new ForumPostsModal();
				long now = System.currentTimeMillis();
				reply.setTitle("");
				reply.setContent(content);
				reply.setCreateTime(now);
				reply.setRefreshTime(now);
				reply.setKind(ForumPostsModal.KIND_REPLY);
				reply.setProvider(self);
				reply.setState(ForumPostsModal.STATE_SHOW);
				reply.setReplyNo(0);
				reply.setViewNo(0);
				reply.setTargetId(post.getId());
				this.postsDB.add(reply);
				this.postsDB.addReplyNo(new ObjectId(post.get_id()), 1);
				data.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return data;
	}
	
}

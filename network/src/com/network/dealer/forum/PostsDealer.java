package com.network.dealer.forum;

import javax.servlet.ServletContext;

import com.network.dealer.Dealer;
import com.network.dealer.DealerParamWrap;
import com.network.logic.LogicResult;
import com.network.logic.forum.PostsLogic;
import com.network.modal.User;

public class PostsDealer extends Dealer {
	private PostsLogic postsLogic;
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDealer(ServletContext context) {
		// TODO Auto-generated method stub
		this.setDealerPath("/posts/*");
		this.postsLogic = this.logicManager.getLogic(PostsLogic.class);
	}

	@Override
	protected void destroyDealer() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void dealHttp(DealerParamWrap params) {
		// TODO Auto-generated method stub
		String uri = this.getRequestUri(params.request);
		if("/posts/all.html".equals(uri)){
			this.allPosts(params);
		}else if("/posts/publish.html".equals(uri)){
			publishHtml(params);
		}else if("/posts/publish.json".equals(uri)){
			publishJson(params);
		}else if("/posts/post.html".equals(uri)){
			postHtml(params);
		}else if("/posts/hot.html".equals(uri)){
			hot(params);
		}else if("/posts/reply.json".equals(uri)){
			reply(params);
		}else if("/posts/comments.json".equals(uri)){
			comments(params);
		}
	}
	
	public void comments(DealerParamWrap params){
		String targetId = params.request.getParameter("target");
		String page = params.request.getParameter("page");
		LogicResult data = this.postsLogic.getComments(targetId, page);
		params.responseJson(data);
	}
	public void reply(DealerParamWrap params) {
		String content = params.request.getParameter("content");
		String targetId = params.request.getParameter("target");
		User self = this.getSelf(params.request);
		LogicResult data = this.postsLogic.reply(content, targetId, self);
		params.responseJson(data);
	}

	public void hot(DealerParamWrap params){
		String page = params.request.getParameter("page");
		LogicResult data = this.postsLogic.getHotPosts(page);
		params.responseFtl(data, "forum/hot.ftl");
	}
	public void postHtml(DealerParamWrap params){
		String id = params.request.getParameter("id");
		LogicResult data = this.postsLogic.getPost(id);
		params.responseFtl(data, "forum/post.ftl");
	}
	public void publishJson(DealerParamWrap params){
		String title = params.request.getParameter("title");
		String content = params.request.getParameter("content");
		User self = this.getSelf(params.request);
		LogicResult data = this.postsLogic.publish(title, content, self);
		params.responseJson(data);
	}
	public void publishHtml(DealerParamWrap params){
		User self = this.getSelf(params.request);
		if(self == null){
			params.responseRedirect(params.getRoot()+"/uc/login.html?url="+params.getRoot()+"/posts/publish.html");
		}else{
			params.responseFtl(null, "forum/publish.ftl");
		}
	}
	public void allPosts(DealerParamWrap params){
		LogicResult data = this.postsLogic.getAllShowPosts(params.request.getParameter("page"));
		params.responseFtl(data,"forum/posts.ftl");
	}
}

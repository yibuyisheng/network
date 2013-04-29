package com.network.db.impl;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.network.db.DB;
import com.network.db.UserDB;
import com.network.modal.User;

public class UserDBImpl extends DB implements UserDB{

	/*UserDB start*/
	@Override
	public void addUser(User user) {
		user.setStatus(User.STATUS_WAIT_FOR_VERIFY);
		BasicDBObject doc = user.toBasicDBObject();
		collection.insert(doc);
	}
	@Override
	public boolean isExistByNickName(String nickName) {
		DBObject obj = collection.findOne(new BasicDBObject("nick_name",nickName));
		return obj!=null;
	}
	@Override
	public User getUserByNickName(String nickName) {
		DBObject obj = collection.findOne(new BasicDBObject("nick_name",nickName));
		User user = new User();
		if(user.parseFromDBObject((BasicDBObject)obj)){
			return user;
		}
		return null;
	}
	@Override
	public List<User> getAllUser(int start,int len) {
		DBCursor cs = collection.find(new BasicDBObject("status",new BasicDBObject("$ne",User.STATUS_DELETE))).skip(start).limit(len);
		List<User> users = new LinkedList<User>();
		while(cs.hasNext()){
			User user = new User();
			user.parseFromDBObject((BasicDBObject)cs.next());
			users.add(user);
		}
		cs.close();
		return users;
	}
	@Override
	public void deleteUserById(ObjectId id) {
		// TODO Auto-generated method stub
		DBObject obj = new BasicDBObject();
		obj.put("_id", id);
		this.collection.update(obj, new BasicDBObject("status",new BasicDBObject("$set",User.STATUS_DELETE)));
	}
	@Override
	public void setUserStatus(ObjectId uid, byte status) {
		DBObject query = new BasicDBObject();
		query.put("_id", uid);
		this.collection.update(query, new BasicDBObject("status",new BasicDBObject("$set",status)));
	}
	/*UserDB end*/
	
	/*DB start*/
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initDB(ServletContext context) {
		// TODO Auto-generated method stub
		this.createCollection("user");
	}

	@Override
	protected void destroyDB() {
		// TODO Auto-generated method stub

	}
	/*DB end*/
	@Override
	public User getById(ObjectId id) {
		// TODO Auto-generated method stub
		User user = new User();
		user.parseFromDBObject((BasicDBObject)this.collection.findOne(new BasicDBObject("_id",id)));
		return user;
	}
	@Override
	public int getAllUserCount() {
		// TODO Auto-generated method stub
		return collection.find(new BasicDBObject("status",new BasicDBObject("$ne",User.STATUS_DELETE))).size();
	}
}

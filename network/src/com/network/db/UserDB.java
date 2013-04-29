package com.network.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.network.modal.User;

public interface UserDB {
	public void addUser(User user);
	public boolean isExistByNickName(String nickName);
	public User getUserByNickName(String nickName);
	public List<User> getAllUser(int start,int len);
	public int getAllUserCount();
	public void deleteUserById(ObjectId id);
	public void setUserStatus(ObjectId uid,byte status);
	public User getById(ObjectId id);
}

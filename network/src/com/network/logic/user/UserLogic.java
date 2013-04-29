package com.network.logic.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.bson.types.ObjectId;

import com.network.db.UserDB;
import com.network.db.impl.UserDBImpl;
import com.network.dealer.DealerParamWrap;
import com.network.logic.Logic;
import com.network.logic.LogicResult;
import com.network.modal.User;
import com.network.util.StringHelper;

public class UserLogic extends Logic {
	UserDB userDB = null;
	public String registe(User user){
		String msg = null;
		if(!User.isTrueName(user.getTrueName())){
			msg = "真实姓名的字符数必须在3到50之间！";
		}else if(!User.isProfessional(user.getProfessional())){
			msg = "专业的字符数必须在5到32之间！";
		}else if(!User.isGrade(user.getGrade())){
			msg = "班级输入有误！";
		}else if(!User.isIdentity(user.getIdentity())){
			msg = "身份输入有误！";
		}else if(!User.isNickName(user.getNickName())){
			msg = "昵称的字符数必须在3到50之间！";
		}else if(isExistByNickName(user.getNickName())){
			msg = "该昵称已存在！";
		}else if(!User.isPassword(user.getPassword())){
			msg = "密码输入不正确";
		}
		if(msg==null){
			userDB.addUser(user);
		}
		return msg;
	}
	public boolean isExistByNickName(String nickName){
		return userDB.isExistByNickName(nickName);
	}
	
	public User login(String nickName,String password,StringBuilder msg){
		User user = null;
		if(nickName==null||nickName.equals("")){
			msg.append("请输入昵称");
		}else if(password==null || password.equals("")){
			msg.append("请输入密码");
		}else{
			user = userDB.getUserByNickName(nickName);
			if(user==null){
				msg.append("昵称输入错误");
			}else if(user.getStatus() == User.STATUS_VERIFY_REFUSE){
				msg.append("对不起，此账号未审核通过");
			}else if(user.getStatus() == User.STATUS_WAIT_FOR_VERIFY){
				msg.append("此账号正在审核过程中，请耐心等待");
			}else if(!user.getPassword().equals(password)){
				msg.append("密码输入错误");
			}
		}
		return user;
	}
	public LogicResult getAllUser(String pageIndex,User self){
		LogicResult data = new LogicResult();
		int index = 1;
		if(self == null){
			data.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			data.put("msg", "请登录！");
		}else if(self.getIdentity() != User.IDENTITY_SUPER && !self.getPermissionUser()){
			data.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			data.put("msg", "您没有权限！");
		}else{
			if(StringHelper.isInteger(pageIndex)){
				index = Integer.parseInt(pageIndex);
			}
			if(index<1) index = 1;
			int pageSize = this.getPropertyInteger("USER_PAGE_SIZE", 0);
			int total = userDB.getAllUserCount();
			List<User> users = userDB.getAllUser((index-1)*pageSize, pageSize);
			
			data.put("status", DealerParamWrap.STATUS_SUCCESS);
			data.put("users", users);
			data.put("page_index", index);
			data.put("page_size", pageSize);
			data.put("page_count", total%pageSize==0 ? total/pageSize : (total/pageSize+1));
			data.put("total_count", total);
		} 
		
		return data;
	}
	public Map<String,Object> deleteUserById(String id,User self){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(!ObjectId.isValid(id)){
			dataMap.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			dataMap.put("msg", "错误的参数！");
		}else if(self == null){
			dataMap.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			dataMap.put("msg", "请登录");
		}else if(self.getIdentity()!=User.IDENTITY_SUPER && !self.getPermissionUser()){
			dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			dataMap.put("msg", "您没有权限！");
		}else{
			User user = userDB.getById(new ObjectId(id));
			if(user == null){
				dataMap.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				dataMap.put("msg", "不存在该用户！");
			}else if(self.getId().equals(user.getId()) || (self.getIdentity() != User.IDENTITY_SUPER && (user.getIdentity() == User.IDENTITY_SUPER || user.getPermissionUser()))){
				//自己不是超级管理员，而将要被操作的用户具有和自己同样的权限
				dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				dataMap.put("msg", "具有相同权限，无法操作！");
			}else{
				userDB.deleteUserById(new ObjectId(id));
				dataMap.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return dataMap;
	}
	public Map<String,Object> userPass(String id,User self){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(!ObjectId.isValid(id)){
			dataMap.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			dataMap.put("msg", "错误的参数！");
		}else if(self == null){
			dataMap.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			dataMap.put("msg", "请登录");
		}else if(self.getIdentity()!=User.IDENTITY_SUPER && !self.getPermissionUser()){
			dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			dataMap.put("msg", "您没有权限！");
		}else{
			User user = userDB.getById(new ObjectId(id));
			if(user == null){
				dataMap.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				dataMap.put("msg", "不存在该用户！");
			}else if(self.getId().equals(user.get_id())){
				dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				dataMap.put("msg", "您不能操作自己！");
			}else if(self.getIdentity() != User.IDENTITY_SUPER && (user.getIdentity() == User.IDENTITY_SUPER || user.getPermissionUser())){
				//自己不是超级管理员，而将要被操作的用户具有和自己同样的权限
				dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				dataMap.put("msg", "具有相同权限，无法操作！");
			}else if(user.getStatus() != User.STATUS_VERIFY_REFUSE && user.getStatus() != User.STATUS_WAIT_FOR_VERIFY){
				dataMap.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				dataMap.put("msg", "错误的状态转换！");
			}else{
				userDB.setUserStatus(new ObjectId(id), User.STATUS_VERIFY_PASS);
				dataMap.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return dataMap;
	}
	public Map<String,Object> userRefuse(String id,User self){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		if(!ObjectId.isValid(id)){
			dataMap.put("status", DealerParamWrap.STATUS_PARAMS_ERROR);
			dataMap.put("msg", "错误的参数！");
		}else if(self == null){
			dataMap.put("status", DealerParamWrap.STATUS_NOT_LOGIN);
			dataMap.put("msg", "请登录");
		}else if(self.getIdentity()!=User.IDENTITY_SUPER && !self.getPermissionUser()){
			dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
			dataMap.put("msg", "您没有权限！");
		}else{
			User user = userDB.getById(new ObjectId(id));
			if(user == null){
				dataMap.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				dataMap.put("msg", "不存在该用户！");
			}else if(self.getId().equals(user.get_id())){
				dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				dataMap.put("msg", "您不能操作自己！");
			}else if(self.getIdentity() != User.IDENTITY_SUPER && (user.getIdentity() == User.IDENTITY_SUPER || user.getPermissionUser())){
				//自己不是超级管理员，而将要被操作的用户具有和自己同样的权限
				dataMap.put("status", DealerParamWrap.STATUS_NO_PERMISSION);
				dataMap.put("msg", "具有相同权限，无法操作！");
			}else if(user.getStatus() != User.STATUS_ENABLE && user.getStatus() != User.STATUS_VERIFY_PASS && user.getStatus() != User.STATUS_WAIT_FOR_VERIFY){
				dataMap.put("status", DealerParamWrap.STATUS_COMMON_ERROR);
				dataMap.put("msg", "错误的状态转换！");
			}else{
				userDB.setUserStatus(new ObjectId(id), User.STATUS_VERIFY_REFUSE);
				dataMap.put("status", DealerParamWrap.STATUS_SUCCESS);
			}
		}
		return dataMap;
	}
	
	public User getById(String id,StringBuilder msg){
		User user = null;
		if(!ObjectId.isValid(id)){
			msg.append("非法参数！");
		}else{
			user = userDB.getById(new ObjectId(id));
		}
		return user;
	}
	
	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void initLogic(ServletContext context) {
		// TODO Auto-generated method stub
		userDB = dbManager.getDB(UserDBImpl.class);
		this.setProperty("USER_PAGE_SIZE", 10);
	}

	@Override
	protected void destroyLogic() {
		// TODO Auto-generated method stub

	}

}

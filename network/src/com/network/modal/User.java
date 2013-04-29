package com.network.modal;

import java.nio.charset.Charset;

import com.mongodb.BasicDBObject;
import com.network.util.StringHelper;

public class User implements MongoDBModal{
	private String _id;
	private String trueName;
	private String nickName;
	private String password;
	private String grade;
	private String professional;
	private byte identity;
	private byte status;
	
	private int permission;
	
	public static final int PERMISSION_MANAGE_USER=     			StringHelper.binary2int("0b000001");
	public static final int PERMISSION_MANAGE_FORUM=				StringHelper.binary2int("0b000010");
	public static final int PERMISSION_MANAGE_DOCUMENT=				StringHelper.binary2int("0b000100");
	public static final int PERMISSION_MANAGE_FORWARD_BACK=			StringHelper.binary2int("0b001000");
	public static final int PERMISSION_MANAGE_COURSE_CONSTRUCT=		StringHelper.binary2int("0b010000");
	
	public boolean getPermissionUser(){
		return (this.permission & PERMISSION_MANAGE_USER) == PERMISSION_MANAGE_USER;
	}
	public boolean getPermissionForum(){
		return (this.permission & PERMISSION_MANAGE_FORUM) == PERMISSION_MANAGE_FORUM;
	}
	public boolean getPermissionDocument(){
		return (this.permission & PERMISSION_MANAGE_DOCUMENT) == PERMISSION_MANAGE_DOCUMENT;
	}
	public boolean getPermissionForwardBack(){
		return (this.permission & PERMISSION_MANAGE_FORWARD_BACK) == PERMISSION_MANAGE_FORWARD_BACK;
	}
	public boolean getPermissionCourseStructure(){
		return (this.permission & PERMISSION_MANAGE_COURSE_CONSTRUCT) == PERMISSION_MANAGE_COURSE_CONSTRUCT;
	}
	
	/**
	 * the trueName field need three letters at least,50 letters at most
	 * @param src
	 * @return
	 */
	public static boolean isTrueName(String src){
		if(src!=null && src.length() >= 3 && src.length() <= 50){
			return true;
		}
		return false;
	}
	/**
	 * the nickName field need three letters at least,50 letters at most
	 * @param src
	 * @return
	 */
	public static boolean isNickName(String src){
		if(src!=null && src.length() >= 3 && src.length() <= 50){
			return true;
		}
		return false;
	}
	/**
	 * the password field need five letters at least,32 letters at most
	 * @param src
	 * @return
	 */
	public static boolean isPassword(String src){
		if(src!=null && src.length() >= 5 && src.length() <= 32){
			return true;
		}
		return false;
	}
	/**
	 * 2009 grade 2 class --> 2009.2
	 * @param src
	 * @return
	 */
	public static boolean isGrade(String src){
		if(src==null || src.trim().length()==0) return false;
		int index = src.indexOf(".");
		if(index!=4) return false;
		if(src.length()<6|| src.length()>9) return false;
		return true;
	}
	/**
	 * the professional field need five letters at least,32 letters at most
	 * @param src
	 * @return
	 */
	public static boolean isProfessional(String src){
		if(src!=null && src.length() >= 5 && src.length() <= 32){
			return true;
		}
		return false;
	}
	public static boolean isIdentity(byte src){
		if(src != IDENTITY_OTHER && src!=IDENTITY_STUDENT && src!=IDENTITY_TEACHER) return false;
		return true;
	}
	public static boolean isStatus(byte src){
		if(src != STATUS_ENABLE && src!=STATUS_VERIFY_PASS && src!=STATUS_VERIFY_REFUSE && src!=STATUS_WAIT_FOR_VERIFY) return false;
		return true;
	}
	
	/**
	 * student identity
	 */
	public static final byte IDENTITY_STUDENT=1;
	/**
	 * teacher identity
	 */
	public static final byte IDENTITY_TEACHER=2;
	/**
	 * the super user can execute every operation of this system.
	 */
	public static final byte IDENTITY_SUPER=4; 
	/**
	 * other identity
	 */
	public static final byte IDENTITY_OTHER=3;
	
	/**
	 * wait for verify
	 */
	public static final byte STATUS_WAIT_FOR_VERIFY=1;
	/**
	 * pass
	 */
	public static final byte STATUS_VERIFY_PASS=2;
	/**
	 * refused
	 */
	public static final byte STATUS_VERIFY_REFUSE=3;
	/**
	 * the account is available
	 */
	public static final byte STATUS_ENABLE=4;
	/**
	 * the account is deleted
	 */
	public static final byte STATUS_DELETE=5;
	
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getNickName() {
		return nickName;
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
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getProfessional() {
		return professional;
	}
	public void setProfessional(String professional) {
		this.professional = professional;
	}
	public byte getIdentity() {
		return identity;
	}
	public void setIdentity(byte identity) {
		this.identity = identity;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	
	private String _toMongoDBString(String src){
		return new String(src.getBytes(Charset.forName("ISO-8859-1")),Charset.forName("UTF-8"));
	}
	@Override
	public BasicDBObject toBasicDBObject(){
		BasicDBObject obj = new BasicDBObject();
		obj.append("_id", _id);
		obj.append("true_name", _toMongoDBString(trueName));
		obj.append("nick_name", _toMongoDBString(nickName));
		obj.append("password", password);
		obj.append("grade", grade);
		obj.append("professional", _toMongoDBString(professional));
		obj.append("identity",identity);
		obj.append("status",status);
		obj.append("permission", this.permission);
		return obj;
	}
	@Override
	public boolean parseFromDBObject(BasicDBObject obj){
		if(obj==null) {
			return false;
		}
		
		if(obj.containsField("_id")) _id = String.valueOf(obj.get("_id").toString());
		if(obj.containsField("true_name")) trueName = obj.getString("true_name");
		if(obj.containsField("nick_name")) nickName = obj.getString("nick_name");
		if(obj.containsField("password")) password = obj.getString("password");
		if(obj.containsField("grade")) grade = obj.getString("grade");
		if(obj.containsField("professional")) professional = obj.getString("professional");
		if(obj.containsField("identity")) identity = Byte.parseByte(obj.getString("identity"));
		if(obj.containsField("status")) status = Byte.parseByte(obj.getString("status"));
		if(obj.containsField("permission")) permission = obj.getInt("permission");
		
		return true;
	}
	
	public static String getIdentityName(int identity){
		if(identity == IDENTITY_STUDENT){
			return "学生";
		}
		if(identity == IDENTITY_TEACHER){
			return "教师";
		}
		if(identity == IDENTITY_OTHER){
			return "其他身份";
		}
		if(identity == IDENTITY_SUPER){
			return "超级管理员";
		}
		return "不明身份";
	}
	public static String getStatusString(int status){
		if(status == STATUS_WAIT_FOR_VERIFY){
			return "等待审核";
		}
		if(status == STATUS_VERIFY_PASS){
			return "审核通过";
		}
		if(status == STATUS_VERIFY_REFUSE){
			return "审核拒绝";
		}
		if(status == STATUS_ENABLE){
			return "可用";
		}
		return "未知状态";
	}
}

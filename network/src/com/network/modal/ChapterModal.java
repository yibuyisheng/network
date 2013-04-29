package com.network.modal;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ChapterModal implements MongoDBModal {

	private String _id;
	private String name;
	private List<User> teachers;
	private byte depth;/*树形结构深度层次，1是最高层次*/
	private ChapterModal parent;
	private List<ChapterModal> children;
	private int index;/*在兄弟节点中的位置*/
	private DocumentModal doc;
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getParentId(){
		if(this.parent==null) return null;
		return this.parent.get_id();
	}
	public void setParentId(String id){
		if(this.parent==null) this.parent = new ChapterModal();
		this.parent.setId(id);
	}
	public ChapterModal getParent() {
		return parent;
	}
	public void setParent(ChapterModal parent) {
		this.parent = parent;
	}
	public List<ChapterModal> getChildren() {
		return children;
	}
	public void setChildren(List<ChapterModal> children) {
		this.children = children;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<User> teachers) {
		this.teachers = teachers;
	}

	public byte getDepth() {
		return depth;
	}

	public void setDepth(byte depth) {
		this.depth = depth;
	}

	public String getDocId() {
		return doc==null?null:doc.get_id();
	}
	public DocumentModal getDoc() {
		return doc;
	}

	public void setDocId(String id){
		if(this.doc==null) this.doc = new DocumentModal();
		this.doc.set_id(id);
	}
	public void setDoc(DocumentModal doc) {
		this.doc = doc;
	}

	@Override
	public BasicDBObject toBasicDBObject() {
		// TODO Auto-generated method stub
		BasicDBObject obj = new BasicDBObject();
		if(ObjectId.isValid(this._id))obj.put("_id", new ObjectId(_id));
		obj.put("name", name);
		if(teachers!=null){
			List<DBObject> list = new LinkedList<DBObject>();
			for(User teacher : teachers){
				list.add(new BasicDBObject("_id",new ObjectId(teacher.get_id())));
			}
			obj.put("teachers", list);
		}
		obj.append("depth", depth);
		
		if(ObjectId.isValid(this.getDocId())){
			obj.append("doc_id", new ObjectId(this.getDocId()));
		}
		
		if(parent!=null && ObjectId.isValid(parent.get_id())){
			obj.put("parent_id", new ObjectId(parent.get_id()));
		}
		
		if(children!=null){
			List<DBObject> list = new LinkedList<DBObject>();
			for(ChapterModal child : children){
				list.add(new BasicDBObject("_id",new ObjectId(child.get_id())));
			}
			obj.put("children", list);
		}
		obj.put("index", index);
		return obj;
	}

	@Override
	public boolean parseFromDBObject(BasicDBObject obj) {
		// TODO Auto-generated method stub
		if(obj==null) return false;
		if(obj.containsField("_id")) this._id = obj.getString("_id");
		if(obj.containsField("name")) this.name = obj.getString("name");
		if(obj.containsField("index")) this.index = obj.getInt("index");
		if(obj.containsField("teachers")){
			teachers = new LinkedList<User>();
			List<BasicDBObject> list = (List<BasicDBObject>)obj.get("teachers");
			for(BasicDBObject o : list){
				User user = new User();
				user.parseFromDBObject(o);
				teachers.add(user);
			}
		}
		if(obj.containsField("depth")) this.depth = (byte)obj.getInt("depth");
		if(obj.containsField("doc_id")) {
			this.setDocId(obj.getString("doc_id"));
		}
		
		if(obj.containsField("parent_id")){
			this.parent = new ChapterModal();
			this.parent.setId(obj.getString("parent_id"));
		}
		
		if(obj.containsField("children")){
			this.children = new LinkedList<ChapterModal>();
			List<ObjectId> list = (List<ObjectId>)obj.get("children");
			for(ObjectId o : list){
				ChapterModal chapterModal = new ChapterModal();
				chapterModal.setId(o.toString());
				this.children.add(chapterModal);
			}
		}
		
		return true;
	}
	
	public static boolean isName(String src){
		if(src==null || src.trim().length() == 0) return false;
		
		if(src.length() > 30){
			return false;
		}
		
		return true;
	}

}

package com.network.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.network.modal.ChapterModal;

public interface ChapterDB {
	public void addChild(ObjectId parentId,ObjectId childId);
	public void removeChild(ObjectId parentId,ObjectId childId);
	public void insert(ChapterModal chapter);
	public boolean chapterExist(byte depth,String name);
	public List<ChapterModal> getChaptersByDepth(byte depth);
	public List<ChapterModal> getChaptersByParentId(ObjectId parentId);
	public boolean hasChild(ObjectId parentId,ObjectId childId);
	public boolean chapterExistById(ObjectId id);
	public void updateName(ObjectId id,String name);
	public void setDoc(ObjectId chapterId,ObjectId docId);
	public int getMaxIndexByDepth(int depth);
	public ChapterModal get(ObjectId id);
	public boolean hasChild(ObjectId id);
	public void delete(ObjectId id);
}

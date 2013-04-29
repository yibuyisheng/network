package com.network.modal;

import com.mongodb.BasicDBObject;

public interface MongoDBModal {
	public BasicDBObject toBasicDBObject();
	/**
	 * if obj==null , return false
	 * @param obj
	 * @return
	 */
	public boolean parseFromDBObject(BasicDBObject obj);
}

package com.network.db;


import javax.servlet.ServletContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.network.Base;
import com.network.log.Log;
import com.network.log.Logger;
import com.network.manage.ComponentManager;
import com.network.manage.db.DBManager;

/**
 * @author: zhangli
 * @QQ: 157142884
 * @date: 2012-11-25
 * @brief: the parent class of database component
 */
public abstract class DB implements Base{
	protected Log log = null;
	protected DBCollection collection = null;
	
	private DBManager dbManager = null;
	
	protected void createCollection(String name){
		this.collection = dbManager.createCollection(name);
	}
	@Override
	public void init(ServletContext context) {
		this.log = Logger.getInstance();
		
		ComponentManager componentManager = (ComponentManager)context.getAttribute("manager");
		this.dbManager = componentManager.getDBManager();
		this.initDB(context);
	}
	@Override
	public void destroy() {
		this.destroyDB();
	}
	
	protected long findAvailableId(){
		long collectionCount = collection.getCount();
		if(collectionCount==0) return 1;
		long ret = -1;
		DBCursor cursor = collection.find().sort(new BasicDBObject("id",1));
        try {
        	long preId = -1;
            while(cursor.hasNext()) {
            	DBObject obj = cursor.next();
            	long id = Long.parseLong(obj.get("id").toString());
                if(preId==-1){
                	preId=id;
                	continue;
                }
                if(id-preId>1) {
                	ret = preId + 1;
                	break;
                }
                preId = id;
            }
            
            if(collectionCount==1 && ret==-1 && preId != -1){
            	/*说明只有一条记录*/
            	if(preId==1){
            		ret = 2;
            	}else if(preId>1){
            		ret = 1;
            	}
            }else{
            	ret = preId+1;
            }
        } finally {
            cursor.close();
        }
        return ret;
	}
	
	/**
	 * initialize database operating object
	 * @param context this webapps's context
	 */
	protected abstract void initDB(ServletContext context);
	/**
	 * destroy this object
	 */
	protected abstract void destroyDB();
}

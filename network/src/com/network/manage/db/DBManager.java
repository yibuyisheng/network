package com.network.manage.db;

import java.io.File;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.network.Base;
import com.network.db.DB;
import com.network.manage.Manager;

public class DBManager extends Manager{
	
	private MongoDBSwitch mongodbSwitch = null;
	
	private Mongo mongo = null;
	private com.mongodb.DB db = null;
	
	public DBCollection createCollection(String name){
		return db.getCollection(name);
	}
	@Override
	protected void destroyManager() {
		_destroy();
	}

	@Override
	protected void initManager(ServletContext context) {
		if(context.getInitParameter("mongo-start").equals("false")){
			String mongoRoot = context.getInitParameter("mongo-path");
			if(mongoRoot == null || mongoRoot == ""){
				mongoRoot = context.getRealPath(File.separator)+"mongodb"+File.separator;
			}
			File file = new File(mongoRoot);
			if(!file.exists() || !file.isDirectory()){
				log.error("initialize DB failed!The mongo root path does not exist or is not correct!System will exit!"+System.getProperty("line.separator")+"        "+file.getPath());
				System.exit(0);
			}
			try{
				mongodbSwitch = new MongoDBSwitch(log);
				mongodbSwitch.startMongoDB(mongoRoot);
			}catch(Exception e){
				log.error("start MongoDB failed!System will exit!",e);
				System.exit(0);
			}
		}
		
		try{
			mongo = new Mongo("localhost",27017);
			db = mongo.getDB("network");
			log.info("connected to mongodb on localhost:27017 successfully!");
			log.info("the name of the db is "+db.getName()+"!");
		}catch(Exception e){
			log.error("connect to mongodb failed ! please confirm the mongodb is started on localhost:27017 ! System will exit!",e);
			System.exit(0);
		}
		
		try{
			_init( context);
		}catch(Exception e){
			log.error("initialize DB failed!System will exit!",e);
			System.exit(0);
		}
	}
	
	public <T extends DB> T getDB(Class<T> t){
		return (T)this.get(t);
	}

}

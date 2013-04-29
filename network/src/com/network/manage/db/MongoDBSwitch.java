package com.network.manage.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.network.log.Log;
import com.network.util.SystemHelper;

public class MongoDBSwitch implements Runnable{
	private Log log = null;
	private Thread mongoThread = null;
	private Process mongoProcess = null;
	private String mongoStartCommand = null;
	private boolean mongoServerRun = true;
	
	public MongoDBSwitch(Log log){
		this.log = log;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			mongoProcess = Runtime.getRuntime().exec(mongoStartCommand);
			InputStream is = mongoProcess.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null && mongoServerRun) {
				log.info(line);
			}
			mongoProcess.waitFor();
			is.close();
			reader.close();
			mongoProcess.destroy();
			log.info("mongodb end !");
		} catch (Exception e) {
			log.error("mongodb error!system will exit!",e);
			System.exit(0);
		}
	}
	
	/**
	 * @brief start the mongodb database 
	 * @param mongoRoot : the root path of mongodb
	 * @throws Exception 
	 */
	public void startMongoDB(String mongoRoot) throws Exception{
		/*create the data directory*/
		File file = new File(mongoRoot + "data"+File.separator);
		if(!file.exists()) file.mkdirs();
		
		/*specify the data directory*/
		String mongodName = null;
		if(SystemHelper.os == SystemHelper.OPERATING_SYSTEM_WINDOWS) mongodName = "mongod.exe";
		if(mongodName == null || mongodName.equals("")) throw new Exception("This webapp dose not support your operating system currently!System will exit!");
		file = new File(mongoRoot + "bin" + File.separator + mongodName);
		if(!file.exists() || !file.isFile()) throw new Exception("can not find the "+ file.getAbsolutePath() + " file!");
		/*start mongodb*/
		
		String cmd = file.getPath() + " -dbpath " + mongoRoot + "data"+File.separator; 
		log.info(cmd);
		mongoStartCommand = cmd;
		mongoThread = new Thread(this);
		mongoThread.setDaemon(false);
		mongoThread.start();
	}
	
	public void close(){
		mongoServerRun = false;
	}
}

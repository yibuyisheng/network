package com.network.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

/**
 * single object
 * @author zhangli
 *
 */
public class Logger extends Log{

	private String logDirectory = null;
	private String lineSeparator = System.getProperty("line.separator");
	
	private final int LOG_INFO=1;
	private final int LOG_WARN=2;
	private final int LOG_ERROR=3;
	
	private static Logger logger = null;
	
	private Logger(ServletContext context){
		this.init(context);
	}
	public static Logger initInstance(ServletContext context){
		if(logger==null)logger = new Logger(context);
		return logger;
	}
	public static Logger getInstance(){
		if(logger==null){
			System.out.println("you must initialize the logger when the app initializes!");
		}
		return logger;
	}
	/**
	 * @brief write log information
	 * @param logKind : the kind of this information
	 * @param msg : the message 
	 * @param e : the Exception object
	 * @throws IOException
	 */
	private synchronized void _write(int logKind,String msg,Exception e) throws IOException{
		StringBuilder out = new StringBuilder("Thread:"+Thread.currentThread().getName()+" ");
		
		/*show the log stack trace*/
		StackTraceElement [] stackTraceElements = Thread.currentThread().getStackTrace();
		if(stackTraceElements.length >= 4){
			out.append(" "+stackTraceElements[4].getClassName()+"["+stackTraceElements[4].getLineNumber()+"] ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E ");
		out.append(sdf.format(new Date(System.currentTimeMillis())));
		
		switch(logKind){
		case LOG_INFO:out.append("[INFO] ");break;
		case LOG_WARN:out.append("[WARN] ");break;
		case LOG_ERROR:out.append("[ERROR] ");break;
		}
		
		if(msg != null){
			out.append(msg+lineSeparator);
		}
		
		if(e != null){
			out.append(" "+e.getMessage()+lineSeparator);
			stackTraceElements = e.getStackTrace();
			for(StackTraceElement stackTraceElement : stackTraceElements){
				out.append("        "+stackTraceElement.toString()+lineSeparator);
			}
		}
		
		//if(out.lastIndexOf(lineSeparator) != out.length()-1) out.append(lineSeparator);
		
		System.out.print(out.toString());
		String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))+".log";
		FileWriter fileWriter = new FileWriter(logDirectory+fileName,true);
		fileWriter.write(out.toString());
		fileWriter.close();
	}
	@Override
	public void error(Object msg) {
		try {
			this._write(LOG_ERROR, String.valueOf(msg.toString()), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(Exception e) {
		try {
			this._write(LOG_ERROR, null, e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void error(Object msg, Exception e) {
		try {
			this._write(LOG_ERROR, String.valueOf(msg), e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void info(Object msg) {
		try {
			this._write(LOG_INFO, String.valueOf(msg), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void info(Exception e) {
		try {
			this._write(LOG_INFO, null, e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void info(Object msg, Exception e) {
		// TODO Auto-generated method stub
		try {
			this._write(LOG_INFO, String.valueOf(msg), e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void warn(Object msg) {
		try {
			this._write(LOG_WARN, String.valueOf(msg), null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void warn(Exception e) {
		try {
			this._write(LOG_WARN, null, e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void warn(Object msg, Exception e) {
		try {
			this._write(LOG_WARN, String.valueOf(msg), e);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void destroyLog() {
		
	}
	
	@Override
	public void run(){
		this.info(this.getClass().getName()+" run!");
	}
	@Override
	public long getDelay(){
		return 24 * 60 * 60 * 1000;//24小时后开始运行
	}
	@Override
	public long getPeriod(){
		return 24 * 60 * 60 * 1000;//24小时运行一次
	}

	/**
	 * @brief : initialize the log . Get the log directory on server
	 * @param context : the webapp's context
	 */
	@Override
	public void initLog(ServletContext context) {
		this.logDirectory = context.getRealPath(File.separator)+"log"+File.separator;
		System.out.println("log directory : "+this.logDirectory);
		File f = new File(logDirectory);
		if(!f.exists()) f.mkdirs();
	}
	@Override
	public int getPriority() {
		return 0;
	}
	
}

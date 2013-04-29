package com.network.manage;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;

import com.loader.ClassLoader;
import com.loader.ClassLoaderParams;
import com.network.Base;
import com.network.dealer.Dealer;
import com.network.log.Log;
import com.network.log.Logger;
import com.network.logic.Logic;
import com.network.manage.db.DBManager;
import com.network.manage.dealer.DealerManager;
import com.network.manage.logic.LogicManager;

public class ComponentManager implements Base{
	protected Log log = Logger.getInstance();
	
	private DBManager dbManager = new DBManager();
	private LogicManager logicManager = new LogicManager();
	private DealerManager dealerManager = new DealerManager();
	
	
	private static ComponentManager componentManager = null;
	public static ComponentManager getInstance(){
		if(componentManager == null){
			componentManager = new ComponentManager();
		}
		return componentManager;
	}
	private ComponentManager(){}
	
	@Override
	public void destroy() {
		dbManager.destroy();
		logicManager.destroy();
		dealerManager.destroy();
	}
	@Override
	public void init(ServletContext context) {
		
		//_loadFromXmlFile();
		try {
			_loadDbs();
			_loadLogics();
			_loadDealers();
		} catch (Exception e) {
			log.error("system components are loaded failed ! exit !",e);
			System.exit(0);
		}
		
		dbManager.init(context);
		log.info("logic begin");
		logicManager.init(context);
		log.info("logic end");
		log.info("dealer begin");
		dealerManager.init(context);
		log.info("dealer end");
	}
	
//	private void _loadFromXmlFile() throws ParserConfigurationException, SAXException, IOException{
//		SAXParserFactory saxfac = SAXParserFactory.newInstance();
//		SAXParser saxParser = saxfac.newSAXParser();
//		InputStream in = getClass().getResourceAsStream("/component.xml");
//		saxParser.parse(in, new DefaultHandler(){
//			@Override
//			public void startElement(String uri, String localName,
//					String qName, Attributes attributes) throws SAXException {
//				super.startElement(uri, localName, qName, attributes);
//				try {
//					String className;
//					if(qName.equals("logic")){
//						className = attributes.getValue("class");
//						Base base = (Base)Class.forName(className).newInstance();
//						logicManager.put(className, base);
//					}else if(qName.equals("dealer")){
//						className = attributes.getValue("class");
//						Dealer dealer = (Dealer)Class.forName(className).newInstance();
//						dealer.setDealerPath(attributes.getValue("path"));
//						dealerManager.put(className, dealer);
//					}else if(qName.equals("db")){
//						className = attributes.getValue("class");
//						Base base = (Base)Class.forName(className).newInstance();
//						dbManager.put(className, base);
//					}
//				} catch (InstantiationException e) {
//					Logger.getInstance().error(e);
//				} catch (IllegalAccessException e) {
//					Logger.getInstance().error(e);
//				} catch (ClassNotFoundException e) {
//					Logger.getInstance().error(e);
//				}
//			}
//			@Override
//			public void endElement(String uri, String localName, String qName)
//					throws SAXException {
//				super.endElement(uri, localName, qName);
//			}
//		});
//		in.close();
//	}

	private void _loadDealers() throws UnsupportedEncodingException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		ClassLoaderParams params = new ClassLoaderParams();
		params.setParentClass(Dealer.class);
		dealerManager.putAll(ClassLoader.getInstance().loadClassesMap("com.network.dealer", params));
	}
	private void _loadLogics() throws UnsupportedEncodingException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		ClassLoaderParams params = new ClassLoaderParams();
		params.setParentClass(Logic.class);
		logicManager.putAll(ClassLoader.getInstance().loadClassesMap("com.network.logic", params));
	}
	private void _loadDbs() throws UnsupportedEncodingException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		ClassLoaderParams params = new ClassLoaderParams();
		params.setParentClass(Logic.class);
		dbManager.putAll(ClassLoader.getInstance().loadClassesMap("com.network.db", params));
	}
	
	public DealerManager getDealerManager(){
		return dealerManager;
	}
	public DBManager getDBManager(){
		return this.dbManager;
	}
	public LogicManager getLogicManager(){
		return this.logicManager;
	}
	@Override
	public int getPriority() {
		return 0;
	}
}

package com.loader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassLoader {
	private String srcRoot = null;

	private static ClassLoader classLoader = null;
	public static ClassLoader getInstance() throws UnsupportedEncodingException {
		if (classLoader == null) {
			classLoader = new ClassLoader();
			classLoader.srcRoot = URLDecoder.decode(
					classLoader.getClass().getResource("/").getPath().substring(1),
					"UTF-8").replace('/', File.separatorChar);
		}
		return classLoader;
	}
	private ClassLoader(){
		
	}

	public Object loadClass(String classFullName,ClassLoaderParams params)throws InstantiationException, IllegalAccessException,ClassNotFoundException {
		Class<?> cls = Class.forName(classFullName);
		if(cls.isInterface()) return null;
		
		if(params != null && 
				( (params.getParentClass() != null && !isExtend(cls, params.getParentClass())) || 
				(params.getInterfaces() != null && !hasInterfaces(cls, params.getInterfaces()) ))
				){
			return null;
		}
		return cls.newInstance();
	}

	public List<Object> loadClassesList(String classesPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		return loadClassesList(classesPath,null);
	}
	public List<Object> loadClassesList(String classesPath,ClassLoaderParams params)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		List<Object> objs = null;
		String fullPath = srcRoot
				+ classesPath.replace('.', File.separatorChar);
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new ClassNotFoundException("the path : " + fullPath.toString() + " not exist!");
		}

		if (file.isFile()) {
			if (!file.getName().endsWith(".class")) {
				throw new ClassNotFoundException("the path : "
						+ fullPath.toString()
						+ " does not refer to a class file!");
			} else {
				objs = new LinkedList<Object>();
				Object o = loadClass(classesPath,params);
				if(o!=null) objs.add(o);
			}
		} else {
			objs = new LinkedList<Object>();
			File[] files = file.listFiles();
			for (int i = 0, il = files.length; i < il; i++) {
				String packageName = files[i].getAbsolutePath()
						.replace(srcRoot, "").replace(File.separatorChar, '.');
				if (files[i].isFile() && packageName.endsWith(".class")) {
					Object o = loadClass(packageName.substring(0,packageName.length() - 6),params);
					if(o!=null) objs.add(o);
				} else {
					objs.addAll(loadClassesList(files[i].getAbsolutePath()
							.replace(srcRoot, "")
							.replace(File.separatorChar, '.'),params));
				}
			}
		}

		return objs;
	}
	
	public Map<Class<?>,Object> loadClassesMap(String classesPath,ClassLoaderParams params) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Map<Class<?>,Object> objs = null;
		String fullPath = srcRoot
				+ classesPath.replace('.', File.separatorChar);
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new ClassNotFoundException("the path : " + fullPath.toString() + " not exist!");
		}

		if (file.isFile()) {
			if (!file.getName().endsWith(".class")) {
				throw new ClassNotFoundException("the path : "
						+ fullPath.toString()
						+ " does not refer to a class file!");
			} else {
				objs = new HashMap<Class<?>,Object>();
				Object o = loadClass(classesPath,params);
				if(o!=null) objs.put(o.getClass(), o);
			}
		} else {
			objs = new HashMap<Class<?>,Object>();
			File[] files = file.listFiles();
			for (int i = 0, il = files.length; i < il; i++) {
				String packageName = files[i].getAbsolutePath()
						.replace(srcRoot, "").replace(File.separatorChar, '.');
				if (files[i].isFile() && packageName.endsWith(".class")) {
					Object o = loadClass(packageName.substring(0,packageName.length() - 6),params);
					if(o!=null) objs.put(o.getClass(), o);
				} else {
					objs.putAll(loadClassesMap(files[i].getAbsolutePath().replace(srcRoot, "").replace(File.separatorChar, '.'),params));
				}
			}
		}

		return objs;
	}
	
	
	
	
	
	
	public static boolean hasInterfaces(Class<?> cls,List<Class<?>> interfaces){
		Class<?>[] is = cls.getInterfaces();
		boolean ret = true;
		for(Class<?> itf : interfaces){
			boolean isContain = false;
			for(int i=0,il=is.length;i<il;i++){
				if(itf.getName().equals(is[i].getName())){
					isContain = true;
					break;
				}
			}
			if(!isContain){
				ret = false;
				break;
			}
		}
		return ret;
	}
	public static boolean isExtend(Class<?> clas, Class<?> superClass) {
		boolean flag = false;
		if (clas == null || superClass == null) {
			return flag;
		}
		Class<?> org = clas.getSuperclass();
		if (org == null) {
			return flag;
		} else if (org.getName().equals(superClass.getName())) {
			flag = true;
		} else {
			flag = isExtend(org, superClass);
		}
		return flag;
	}
}

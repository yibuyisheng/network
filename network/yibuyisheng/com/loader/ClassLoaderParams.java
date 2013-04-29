package com.loader;

import java.util.List;

public class ClassLoaderParams {
	private Class<?> parentClass;
	private List<Class<?>> interfaces;
	public Class<?> getParentClass() {
		return parentClass;
	}
	public void setParentClass(Class<?> parentClass) {
		this.parentClass = parentClass;
	}
	public List<Class<?>> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(List<Class<?>> interfaces) {
		this.interfaces = interfaces;
	}
	
}

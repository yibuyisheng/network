package com.network.logic;

import java.util.HashMap;

import com.network.util.StringHelper;

public class LogicResult extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public int getInt(String k,int defaultValue){
		String v = String.valueOf(this.get(k));
		if(StringHelper.isInteger(v)){
			return Integer.parseInt(v);
		}
		return defaultValue;
	}
	public int getInt(String k){
		return this.getInt(k, 0);
	}
	
}

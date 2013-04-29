package com.network.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class HashHelper implements Comparator{
	public static String hashToJson(Map<Object,Object> map){
		StringBuilder ret = new StringBuilder("{");
		for(Map.Entry<Object,Object> entry : map.entrySet()){
			ret.append(entry.getKey());
			ret.append(":");
			if(entry.getValue() instanceof Integer){
				ret.append(entry.getValue());
			}else{
				ret.append("\""+entry.getValue()+"\"");
			}
			ret.append(entry.getKey()+":"+entry.getValue()+",");
		}
		if(ret.length() > 1){
			ret.deleteCharAt(ret.length()-1);
		}
		ret.append("}");
		return ret.toString();
	}
	/**
	 * @brief ： sort the hashtable's key set by key and return the sorted
	 * @param ：h the hashtable will be sorted
	 * @return : the sorted key set 
	 */
	 public static Map.Entry[] getSortedHashtableByKey(Hashtable h,boolean asc) {
	    Set set = h.entrySet();
	    Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
	    Arrays.sort(entries, new HashHelper(asc));
	    return entries;
	  }

	 /**
		 * @brief ： sort the hashtable's key set by value and return the sorted key set
		 * @param ：h the hashtable will be sorted
		 * @return : the sorted key set 
		 */
	  public static Map.Entry[] getSortedHashtableByValue(Hashtable h,boolean asc) {
	    Set set = h.entrySet();
	    Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
	    Arrays.sort(entries, new HashHelper(asc));
	    return entries;
	  }

	
	private boolean asc = true;
	public HashHelper(boolean asc){
		this.asc = asc;
	}
	@Override
	public int compare(Object o1, Object o2) {
		Object key1 = ((Map.Entry) o1).getKey();
        Object key2 = ((Map.Entry) o2).getKey();
        if(asc) return ((Comparable) key1).compareTo(key2);
        else return -((Comparable) key1).compareTo(key2);
	}
}

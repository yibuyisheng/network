package com.network.util;

import java.util.Locale;

public class StringHelper {
	public static boolean isLong(String src){
		if(src==null || src.equals("")) return false;
		try{
			Long.parseLong(src);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static boolean isInteger(String src){
		if(src==null || src.equals("")) return false;
		try{
			Integer.parseInt(src);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static boolean isByte(String src){
		if(src==null || src.equals("")) return false;
		try{
			Byte.parseByte(src);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	public static int binary2int(String src){
		src = src.toLowerCase(Locale.CHINA);
		if(src.startsWith("0b")){
			src = src.substring(2);
			int ret = 0;
			for(int il=src.length(),i=il-1;i>=0;i--){
				char ch = src.charAt(i);
				if(ch == '1'){
					ret = 1 * (int)Math.pow(2, il-i-1);
				}else if(ch == '0'){
				}else{
					throw new IllegalArgumentException("参数必须以0b开头，其余部分只能是0或者1！");
				}
			}
			return ret;
		}else{
			throw new IllegalArgumentException("参数必须以0b开头，其余部分只能是0或者1！");
		}
	}
}

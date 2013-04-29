package com.network.util;

public class SystemHelper {
	public static final int OPERATING_SYSTEM_WINDOWS=1;
	public static final int OPERATING_SYSTEM_LINUX=2;
	public static final int OPERATING_SYSTEM_MACOS=3;
	
	public static int os = OPERATING_SYSTEM_WINDOWS;
	static{
		String osName = System.getProperties().getProperty("os.name").toLowerCase();
		if(osName.indexOf("windows") > -1) os = OPERATING_SYSTEM_WINDOWS;
		else if(osName.indexOf("linux") > -1) os = OPERATING_SYSTEM_LINUX;
		else if(osName.indexOf("mac") > -1) os = OPERATING_SYSTEM_MACOS;
	}
	
}

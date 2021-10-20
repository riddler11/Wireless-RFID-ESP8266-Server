package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//日期格式工具�?
public class StringFormatUtils { 
	public static Date convertStrToDate(String str)
	{
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//2016/12/04 yyyy/MM/dd 2016-12-04 yyyy-MM-dd
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}

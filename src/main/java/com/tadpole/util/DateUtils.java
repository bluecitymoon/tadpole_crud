package com.tadpole.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tadpole.constants.SystemConstants;

public class DateUtils {
	
	public static int minusDate(Date date1,Date date2){
		try{
			return (int)(date1.getTime()-date2.getTime())/(3600*24*1000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return 0;
	}
	
	public static int getGanjiDate(int days){
		if(days>=0&&days<=3)return 1;
		else if(days>3&&days<=5)return 2;
		else if(days>5&&days<=15)return 3;
		else if(days>15&&days<=30)return 4;
		return 0;
	}
	
	public static String getDateFormate(Date date,String formate){
		SimpleDateFormat sf = new SimpleDateFormat(formate);
		return sf.format(date); 
	}
	
	public static String getPostDateParameter(Date startDate, Date endDate) {
		return SystemConstants.POST_DATE_FORMATTER.format(startDate) + "_" + SystemConstants.POST_DATE_FORMATTER.format(endDate);
	}
}

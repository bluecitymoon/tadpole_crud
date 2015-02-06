package com.tadpole.constants;

import java.text.SimpleDateFormat;

public class SystemConstants {

	public static final String FULL_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

	public static final String SIMPLE_DATE_FORMAT_STRING = "yyyy-MM-dd";

	public static final String MONTH_AND_DAY_DATE_FORMAT_STRING = "MM-dd";
	public static final String POST_DATE_FORMAT_STRING = "yyyyMMdd";

	public static final SimpleDateFormat FULL_DATE_FORMATTER = new SimpleDateFormat(FULL_DATE_FORMAT_STRING);

	public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat(SIMPLE_DATE_FORMAT_STRING);
	public static final SimpleDateFormat MONTH_AND_DAY_DATE_FORMATTER = new SimpleDateFormat(MONTH_AND_DAY_DATE_FORMAT_STRING);

	public static final SimpleDateFormat POST_DATE_FORMATTER = new SimpleDateFormat(POST_DATE_FORMAT_STRING);
}

package com.github.btr.base.util;

import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * 日期工具
 * Created by ryze on 2017/6/9.
 */
public class DateUtil
{
	private DateUtil() {}

	private static final String FULLDATE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 年龄
	 * @param birthday 生日
	 * @return
	 */
	public static Integer getAge(final Date birthday)
	{
		val now = DateTime.now().getYear();
		val bir = new DateTime(birthday).getYear();
		return now - bir;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static long timeStamp()
	{
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 默认格式 yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @return
	 */
	public static Date toDate(final String str)
	{
		return toDate(str,FULLDATE);
	}

	/**
	 * 按指定格式解析日期字符串
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date toDate(final String str,final String pattern)
	{
		if(Objects.nonNull(str) && !str.equals(""))return DateTime.parse(str,DateTimeFormat.forPattern(pattern)).toDate();
		return null;
	}
}

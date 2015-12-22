package com.xiankezu.sirceo.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Message;

public class TimeUtils {
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final int SEVEN_DAYS = 7 * 24 * 60 * 60 * 1000;
	public static final int HALFHOUR = 30 * 60 * 1000;

	static boolean stopRunning = false;

	private final static int TIME_CHANGE = 0;
	private final static int TIME_END = 1;


	/**
	 * 判断当前日期是周几
	 * 
	 * @param pTime
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 根据生日获取年龄
	 * 
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	/**
	 * 获取年龄
	 * 
	 * @param birthDay
	 * @param pattern
	 *            传入生日的pattern
	 * @return
	 * @throws Exception
	 */
	public static int getAge(String birthDay, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return getAge(sdf.parse(birthDay));
	}

	/**
	 * 根据时间戳获取当前时间
	 * 
	 * @param time
	 * @param pattern
	 *            es:yyyy年MM月dd日 hh:mm:ss
	 * @return 2007年03月12日05:42:08
	 */
	public static String getDataByLong(long time, String pattern) {
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		String result = f.format(new Date(time));
		return result;
	}

	public static Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);

		return cal.getTime();
	}

	/**
	 * 根据字符串获取时间戳
	 * 
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	public static long getDateByString(String dateString) throws ParseException {
		SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
		return f.parse(dateString).getTime();
	}

	/**
	 * mm:ss形式返回
	 * 
	 * @param seconds
	 * @return
	 */
	private static String getTimeBySeconds(long seconds) {
		int min = (int) (seconds / 60);
		int mill = (int) (seconds % 60);
		String minString = min <= 9 ? "0" + min : min + "";
		String millString = mill <= 9 ? "0" + mill : mill + "";
		return minString + ":" + millString;
	}

	/**
	 * 根据编号获得周几
	 * 
	 * @param i
	 * @return
	 */
	public static String getWeekDay(int i) {
		switch (i) {
		case 1:
			return " 周一 ";
		case 2:
			return " 周二 ";
		case 3:
			return " 周三 ";
		case 4:
			return " 周四 ";
		case 5:
			return " 周五 ";
		case 6:
			return " 周六 ";
		case 7:
			return " 周日 ";
		}

		return "  ";
	}

	/**
	 * 带周的时间信息
	 * 
	 * @param timeStamp
	 * @param pattern
	 *            输出格式 MM.dd E HH:mm
	 * @return 带周的时间信息 08.13 周四 11:44
	 */
	public static String getWeekDayAndDate(long timeStamp, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(sdf.format(new Date(timeStamp)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat sdf_output = new SimpleDateFormat(pattern);
		return sdf_output.format(date);
	}

	/**
	 * 根据时间戳计算距离当前时间的差值
	 * @param time
	 * @return 返回多少天/小时/分钟之前
	 */
	public static String getTimeDifferent(long time){

		Date date=new Date(time);
		Date begin = null,end = null;
		String strs="";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			strs=sdf.format(date);
			begin = sdf.parse(strs);
			end = new Date();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60/60;
		
		if (day > 0) {
			return  day + "天前";
		} else if(hour > 0){
			return  hour + "小时前";
		} else if (minute > 0) {
			return  minute + "分钟前";
		} else if (second > 0) {
			return  second + "秒前";
		}
		return  "刚刚";
	}
}

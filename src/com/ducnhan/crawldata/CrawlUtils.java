package com.ducnhan.crawldata;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

public class CrawlUtils {

	@SuppressWarnings("deprecation")
	public static Time genTimeSchedule(int lessonStart) {
		int baseHours = 6;
		Date date = new Date();
		int hours =  baseHours + (lessonStart >= 5 ? lessonStart + 1 : lessonStart);
		date.setHours(hours);
		date.setMinutes(0);
		date.setSeconds(0);
		return new Time(date.getTime());
	}

	public static String genScheduleId(String data) {
		return DigestUtils.sha256Hex(data);
	}

	public static Date genDateSchedule(String strDayOfWeek, String strWeek) {
		String[] arrDayOfWeek = {"Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật"};
		List<String> listDay = Arrays.asList(arrDayOfWeek);
		strWeek = strWeek.substring(strWeek.indexOf("[")).replaceAll("[^/ 0-9]", "").trim();
		String[] split = strWeek.split("\\s+");
		String strDateStart = split[0];

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dateStart = dateFormat.parse(strDateStart);
			long unixTimeStamp = (dateStart.getTime() / 1000L + 86400*listDay.indexOf(strDayOfWeek));
			return new Date(unixTimeStamp * 1000L);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}

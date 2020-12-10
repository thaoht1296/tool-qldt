package com.ducnhan.crawldata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ducnhan.model.Schedule;

import us.codecraft.xsoup.Xsoup;


/**
 * @author Trần Đức Nhân
 *
 */
public class CrawlData {

	private String strBaseUrl = "http://qldt.ptit.edu.vn/Default.aspx?page=";
	private String strPage = "gioithieu";
	private String strUsername = "B17DCAT136";
	private String strPassword = "matkhaula1006";
	private String[] __EVENTTARGET = { "ctl00$ContentPlaceHolder1$ctl00$ddlTuan",
			"ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK", "ctl00$ContentPlaceHolder1$ctl00$ddlLoai" };		
	private String[] strScheduleType = {"0", "1", "2"};
	private List<String> listWeek = new ArrayList<>();
	private String weeckSelected;
	private String __VIEWSTATE;
	private String strHocKy;
	public CrawlData() {
		super();
	}
	
	public CrawlData(String strUsername, String strPassword) {
		super();
		this.strUsername = strUsername;
		this.strPassword = strPassword;
	}
	
	public Map<String, String> getCookiesLogin() throws IOException {
		String strUrlHome = strBaseUrl+strPage;
		Response response = Jsoup.connect(strUrlHome).userAgent("Mozilla/5.0")
                .timeout(100 * 1000)
                .method(Method.POST)
                .data("__EVENTTARGET", "")
                .data("__EVENTARGUMENT","")
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtTaiKhoa", this.strUsername)
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$txtMatKhau", this.strPassword)              
                .data("ctl00$ContentPlaceHolder1$ctl00$ucDangNhap$btnDangNhap", "Đăng nhập")
                .followRedirects(true)
                .execute();		
		//get cookies
		Map<String, String> mapCookies = response.cookies();
		return mapCookies;
	}
	
	private	String getViewStateAndListWeek(Map<String, String> mapCookies) throws IOException {	
		strPage = "thoikhoabieu";
		String strParam = "&sta="+strScheduleType[0];
		String strUrlHome = strBaseUrl+strPage+strParam;
		Response response = Jsoup.connect(strUrlHome).userAgent("Mozilla/5.0")
                .timeout(100 * 1000)
                .method(Method.GET)                        
                .cookies(mapCookies)               
                .followRedirects(true)
                .execute();		
		Document doc = response.parse();	
		
		Elements listWeekElement = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_ddlTuan\"]/option").evaluate(doc).getElements();
		this.listWeek.clear();
		for (Element element : listWeekElement) {
			String value = element.attr("value").trim();
			if(element.hasAttr("selected"))
				this.weeckSelected = value;
			listWeek.add(value);  
		}
		this.strHocKy = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK\"]/option[@selected]")
				.evaluate(doc).getElements().attr("value");
		Elements viewStateElement = Xsoup.compile("//*[@id=\"__VIEWSTATE\"]").evaluate(doc).getElements();		
		return viewStateElement.attr("value");
	}	
	
	private Response postReqSchedule(Map<String, String> mapCookies, int targetType, int scheduelType, String strHocKy,
			String strWeek) throws IOException {
		strPage = "thoikhoabieu";		
		String strUrlHome = strBaseUrl+strPage;
		Response response = Jsoup.connect(strUrlHome).userAgent("Mozilla/5.0")
                .timeout(100 * 1000)
                .method(Method.POST)
                .postDataCharset("UTF-8")
                .data("__EVENTTARGET", __EVENTTARGET[targetType])
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", this.__VIEWSTATE)
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK", strHocKy )
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlLoai", strScheduleType[scheduelType] )              
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlTuan", strWeek )                
                .cookies(mapCookies)
                .followRedirects(true)
                .execute();
		return response;		
	}
	
	public void changeSemester(Map<String, String> mapCookies, String strHocKy) throws IOException {
		this.__VIEWSTATE = this.getViewStateAndListWeek(mapCookies);
		this.postReqSchedule(mapCookies, 0, 1, strHocKy, "");
	}
	
	public List<Schedule> getSchedulesInWeek(Map<String, String> mapCookies, String strWeek, String strHocKy)
			throws IOException {
		List<Schedule> listSchedules = new ArrayList<Schedule>();
		Response response;
		if(this.weeckSelected.equalsIgnoreCase(strWeek))
			response = this.postReqSchedule(mapCookies, 0, 0, strHocKy, "");
		else 
			response = this.postReqSchedule(mapCookies, 0, 0, strHocKy, strWeek);
		Document doc = response.parse();
		Elements schedulesElements = Xsoup
				.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_Table1\"]/tbody/tr/td[@onmouseover]").evaluate(doc)
				.getElements();
		
		for (Element element : schedulesElements) {
			String data = element.attr("onmouseover");
			data = data.replace("ddrivetip(", "").replace(")", "");
			data = data.substring(0, data.indexOf("Mã Môn")).replace("\'", "");
			String[] splitData = data.split(",");
			
			listSchedules.add(new Schedule(CrawlUtils.genScheduleId(data+strWeek+strHocKy), splitData[1], splitData[2],
					Integer.parseInt(splitData[4]), splitData[3], CrawlUtils.genDateSchedule(splitData[3], strWeek), Integer.parseInt(splitData[6]),
					Integer.parseInt(splitData[7]), CrawlUtils.genTimeSchedule(Integer.parseInt(splitData[6])), splitData[8],
					splitData[0], splitData[5], strWeek, strHocKy));
		}
		return listSchedules;
	}

	public List<Schedule> getSchedulesInWeek(Map<String, String> mapCookies) throws IOException {
		this.__VIEWSTATE = this.getViewStateAndListWeek(mapCookies);
		return this.getSchedulesInWeek(mapCookies, this.weeckSelected, this.strHocKy);
	}

	public List<Schedule> getAllSchedules(Map<String, String> mapCookies, String strHocKy) throws IOException {
		List<Schedule> listSchedules = new ArrayList<Schedule>();
		if (strHocKy != "")
			this.changeSemester(mapCookies, strHocKy);
		this.__VIEWSTATE = getViewStateAndListWeek(mapCookies);

		for (String week : listWeek) {
			listSchedules.addAll(this.getSchedulesInWeek(mapCookies, week, strHocKy));
		}
		return listSchedules;
	}

	public List<Schedule> getAllSchedules(Map<String, String> mapCookies) throws IOException {
		return this.getAllSchedules(mapCookies, this.strHocKy);
	}
}

package com.ducnhan.crawldata;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.codecraft.xsoup.Xsoup;
import com.ducnhan.model.Schedule;
import java.util.HashMap;

/**
 * @author Nhân
 */
public class CrawlScheduleIDOR {

    private String[] __EVENTTARGET = {"ctl00$ContentPlaceHolder1$ctl00$ddlTuan",
        "ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK", "ctl00$ContentPlaceHolder1$ctl00$ddlLoai"};
    private String[] strScheduleType = {"0", "1", "2"};
    private List<String> listWeek = new ArrayList<>();
    private String weeckSelected;

    private String __VIEWSTATE = "";
    private String strHocKy = "";
    private Map<String, String> mapCookies = new HashMap<String, String>();
    ;
    private String strName;
    private Date dateOfSv;
    private String strMaSV;

    public CrawlScheduleIDOR(String strMaSV) {
        this.strMaSV = strMaSV;
    }

    public List<Schedule> getSchedulesInWeek(String strWeek) throws IOException {
        List<Schedule> listSchedules = new ArrayList<Schedule>();        

        getcookiesAndViewState();

        weeckSelected = strWeek;
        Response response = postReqSchedule(mapCookies, 0, 0, strHocKy, weeckSelected);
        Document doc = response.parse();

        // Lấy tên sinh viên
        String strIn4Sv = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_lblContentTenSV\"]").evaluate(doc)
                .getElements().text();
        strName = strIn4Sv.split("-")[0].trim();

        // Lấy ngay sinh sinh viên
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = strIn4Sv.split("-")[1].replaceAll("[^/ 0-9]", "").trim();
            dateOfSv = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Elements schedulesElements = Xsoup
                .compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_Table1\"]/tbody/tr/td[@onmouseover]").evaluate(doc)
                .getElements();

        // Lấy danh sách thời khóa biểu
        for (Element element : schedulesElements) {
            String data = element.attr("onmouseover");
            data = data.replace("ddrivetip(", "").replace(")", "");
            data = data.substring(0, data.indexOf("Mã Môn") != -1 ? data.indexOf("Mã Môn") : 0).replace("'", "");
            String[] splitData = data.split(",");

            listSchedules.add(new Schedule(CrawlUtils.genScheduleId(data + weeckSelected + strHocKy), splitData[1],
                    splitData[2], Integer.parseInt(splitData[4]), splitData[3],
                    CrawlUtils.genDateSchedule(splitData[3], weeckSelected), Integer.parseInt(splitData[6]),
                    Integer.parseInt(splitData[7]), CrawlUtils.genTimeSchedule(Integer.parseInt(splitData[6])),
                    splitData[8], splitData[0], splitData[5], weeckSelected, strHocKy));
        }
        return listSchedules;

    }

    public List<Schedule> getSchedulesInCurrentWeek() throws IOException {
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=thoikhoabieu&id=" + strMaSV;
        List<Schedule> listSchedules = new ArrayList<Schedule>();
        Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100 * 1000).method(Method.GET)
                .followRedirects(true).execute();
        Document doc = response.parse();
        weekHandle(doc);

        // Lấy tên sinh viên
        String strIn4Sv = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_lblContentTenSV\"]").evaluate(doc)
                .getElements().text();
        strName = strIn4Sv.split("-")[0].trim();

        // Lấy ngay sinh sinh viên
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = strIn4Sv.split("-")[1].replaceAll("[^/ 0-9]", "").trim();
            dateOfSv = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Elements schedulesElements = Xsoup
                .compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_Table1\"]/tbody/tr/td[@onmouseover]").evaluate(doc)
                .getElements();

        // Lấy danh sách thời khóa biểu
        for (Element element : schedulesElements) {
            String data = element.attr("onmouseover");
            data = data.replace("ddrivetip(", "").replace(")", "");
            data = data.substring(0, data.indexOf("Mã Môn")).replace("'", "");
            String[] splitData = data.split(",");

            listSchedules.add(new Schedule(CrawlUtils.genScheduleId(data + this.weeckSelected + strHocKy), splitData[1],
                    splitData[2], Integer.parseInt(splitData[4]), splitData[3],
                    CrawlUtils.genDateSchedule(splitData[3], this.weeckSelected), Integer.parseInt(splitData[6]),
                    Integer.parseInt(splitData[7]), CrawlUtils.genTimeSchedule(Integer.parseInt(splitData[6])),
                    splitData[8], splitData[0], splitData[5], this.weeckSelected, strHocKy));
        }
        return listSchedules;
    }

    public List<Schedule> getAllSchedules() throws IOException {
        List<Schedule> listAllSchedule = new ArrayList<>();
        getAllWeek();
        listAllSchedule.clear();
        for (String strWeek : listWeek) {
            listAllSchedule.addAll(getSchedulesInWeek(strWeek));
        }
        return listAllSchedule;

    }

    private void getcookiesAndViewState() throws IOException {
        List<Schedule> listSchedules = new ArrayList<Schedule>();
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=thoikhoabieu&id=" + strMaSV;
        Document doc;
        if (mapCookies.isEmpty()) {
            Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100 * 1000).method(Method.GET).followRedirects(true)
                    .execute();
            // Lấy Cookies
            mapCookies = response.cookies();
            doc = response.parse();
            // Lấy __ViewState
            Elements viewStateElement = Xsoup.compile("//*[@id=\"__VIEWSTATE\"]").evaluate(doc).getElements();
            __VIEWSTATE = viewStateElement.attr("value");

            // lấy danh sách tuần.
            weekHandle(doc);
        } else {
            Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(100 * 1000)
                    .method(Method.GET)
                    .cookies(mapCookies)
                    .followRedirects(true)
                    .execute();
            // Lấy Cookies
            mapCookies = response.cookies();
            doc = response.parse();
            // Lấy __ViewState
            Elements viewStateElement = Xsoup.compile("//*[@id=\"__VIEWSTATE\"]").evaluate(doc).getElements();
            __VIEWSTATE = viewStateElement.attr("value");
            // lấy danh sách tuần.
            weekHandle(doc);
        }

    }

    private void weekHandle(Document doc) {
        if (!listWeek.isEmpty()) {
            return;
        }
        // Lấy danh sách tuần
        Elements listWeekElement = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_ddlTuan\"]/option")
                .evaluate(doc).getElements();
        this.listWeek.clear();
        for (Element element : listWeekElement) {
            String value = element.attr("value").trim();
            // Lấy tuần hiện tại
            if (element.hasAttr("selected")) {
                weeckSelected = value;
            }
            listWeek.add(value);
        }

        // Lấy học kỳ hiện tại
        this.strHocKy = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_ddlChonNHHK\"]/option[@selected]")
                .evaluate(doc).getElements().attr("value");
    }

    public List<String> getAllWeek() throws IOException {
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=thoikhoabieu&id=" + strMaSV;
        Response response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100 * 1000).method(Method.GET)
                .followRedirects(true).execute();
        Document doc = response.parse();
        // Lấy tên sinh viên
        String strIn4Sv = Xsoup.compile("//*[@id=\"ctl00_ContentPlaceHolder1_ctl00_lblContentTenSV\"]").evaluate(doc)
                .getElements().text();
        strName = strIn4Sv.split("-")[0].trim();
        weekHandle(doc);        
        return listWeek;
    }

    private Response postReqSchedule(Map<String, String> mapCookies, int targetType,
            int scheduelType, String strHocKy, String strWeek) throws IOException {
        String url = "http://qldt.ptit.edu.vn/Default.aspx?page=thoikhoabieu&id=" + strMaSV;
        Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(100 * 1000)
                .method(Method.POST)
                .postDataCharset("UTF-8")
                .data("__EVENTTARGET", __EVENTTARGET[targetType])
                .data("__EVENTARGUMENT", "")
                .data("__VIEWSTATE", this.__VIEWSTATE)
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlChonNHHK", strHocKy)
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlLoai", strScheduleType[scheduelType])
                .data("ctl00$ContentPlaceHolder1$ctl00$ddlTuan", strWeek)
                .cookies(mapCookies).followRedirects(true)
                .execute();
        return response;
    }

    public String[] getEVENTTARGET() {
        return __EVENTTARGET;
    }

    public void setEVENTTARGET(String[] __EVENTTARGET) {
        this.__EVENTTARGET = __EVENTTARGET;
    }

    public String[] getStrScheduleType() {
        return strScheduleType;
    }

    public void setStrScheduleType(String[] strScheduleType) {
        this.strScheduleType = strScheduleType;
    }

    public List<String> getListWeek() {
        return listWeek;
    }

    public void setListWeek(List<String> listWeek) {
        this.listWeek = listWeek;
    }

    public String getWeeckSelected() {
        return weeckSelected;
    }

    public void setWeeckSelected(String weeckSelected) {
        this.weeckSelected = weeckSelected;
    }

    public String getVIEWSTATE() {
        return __VIEWSTATE;
    }

    public void setVIEWSTATE(String __VIEWSTATE) {
        this.__VIEWSTATE = __VIEWSTATE;
    }

    public String getStrHocKy() {
        return strHocKy;
    }

    public void setStrHocKy(String strHocKy) {
        this.strHocKy = strHocKy;
    }

    public Map<String, String> getMapCookies() {
        return mapCookies;
    }

    public void setMapCookies(Map<String, String> mapCookies) {
        this.mapCookies = mapCookies;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Date getDateOfSv() {
        return dateOfSv;
    }

    public void setDateOfSv(Date dateOfSv) {
        this.dateOfSv = dateOfSv;
    }

    public String getStrMaSV() {
        return strMaSV;
    }

    public void setStrMaSV(String strMaSV) {
        this.strMaSV = strMaSV;
    }

}

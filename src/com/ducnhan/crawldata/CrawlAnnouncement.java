/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ducnhan.crawldata;

import java.io.IOException;
import com.ducnhan.model.*;
import java.util.ArrayList;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Hoa Nguyen
 */
public class CrawlAnnouncement {
//    private ArrayList<Announcement> listAnn = new ArrayList<>();
	private String url = "http://qldt.ptit.edu.vn/";

	public CrawlAnnouncement(String url) {
		this.url = url;
	}

	public CrawlAnnouncement() {
	}

	// lay toan bo tb tren web
	public ArrayList<Announcement> getAllAnnInPage() throws IOException {
		ArrayList<Announcement> list_ann = new ArrayList<>();
		Document document = Jsoup.connect(url).get();
		Elements elms = document.getElementsByClass("TextTitle");
		Elements elms1 = document.getElementsByClass("NgayTitle");
		int l = 0;
		for (Element elm_row : elms) {
			Elements elm_row1 = elm_row.getElementsByTag("a");
			String link_ann = elm_row1.first().absUrl("href");
			String al = elm_row.text();
			String x = "";

			if (!al.equals(x)) {
				Element p = elms1.get(l);
				String k = p.text();
				l++;
				String dateAnn = k.substring(1, k.length() - 1);
				list_ann.add(eachAnn(link_ann, dateAnn));
			}
		}
		Collections.sort(list_ann, Announcement.compare);
		return list_ann;
	}

	// tung TB cu the (noi dung va tieu de)
	public Announcement eachAnn(String url_eachann, String dateAnn) throws IOException {
		Document document = Jsoup.connect(url_eachann).get();
		Elements elms = document.getElementsByClass("TextThongTin");
		String titleAnn = elms.get(0).text();
		String content = elms.get(1).text();
		Announcement ann = new Announcement(url_eachann, dateAnn, titleAnn, content);

		return ann;
	}
}

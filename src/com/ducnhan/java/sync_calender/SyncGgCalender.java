package com.ducnhan.java.sync_calender;

import com.ducnhan.crawldata.CrawlScheduleIDOR;
import com.ducnhan.crawldata.CrawlUtils;
import static com.ducnhan.display.Display.PROCESS_VALUE;
import com.ducnhan.model.Schedule;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SyncGgCalender {

    private static final String APPLICATION_NAME = "Google Calendar API Java Synchronize QLDT";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public final static int REMIDER_TYPE_ALL = 0;
    public final static int REMIDER_TYPE_POPUP = 1;
    public final static int REMIDER_TYPE_EMAIL = 2;

    /**
     * Global instance of the scopes required by this App. If modifying these
     * scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Calendar service;
    private String strCalenderID;
    private int reminderTime;
    private int reminderType;

    public SyncGgCalender(int reminder, int reminderType) throws GeneralSecurityException, IOException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME).build();
        this.reminderTime = reminder;
        this.reminderType = reminderType;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SyncGgCalender.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(6969).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String createCalender(String strSummary) throws IOException, IOException {
        // Create a new calendar
        // Kiem tra xem co da ton tai hay chua

        if (checkCalenderExist(strSummary)) {
            service.calendars().delete(strCalenderID).execute();
        }
        System.out.println("Calender was cleaned!");

        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        calendar.setSummary(strSummary);
        calendar.setTimeZone("Asia/Ho_Chi_Minh");

        // Insert the new calendar
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();

        System.out.println("New Calender ID: " + createdCalendar.getId()); // Calender Lịch học PTIT ID
        return createdCalendar.getId();

    }

    public void syncSchedule(List<Schedule> listSchedule, String strCalenderName) throws IOException, ParseException {

        // Creat new Calender "Lịch Học PTIT"
        System.out.println("Init Calender: Lịch học PTIT ...");
        strCalenderID = createCalender(strCalenderName);
//                
        // Creat new Event.
        System.out.println("Start Insert Event => Calender: Lịch học PTIT ...");
        for (Schedule schedule : listSchedule) {
//            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(schedule.getDate());
            c.set(java.util.Calendar.HOUR_OF_DAY, schedule.getTimeStart().getHours());
            c.set(java.util.Calendar.MINUTE, schedule.getTimeStart().getMinutes());
            Date startDate = c.getTime();

            c.set(java.util.Calendar.HOUR_OF_DAY,
                    CrawlUtils.genTimeSchedule(schedule.getLessonStart() + schedule.getNumOfLesson()).getHours());
            c.set(java.util.Calendar.MINUTE,
                    CrawlUtils.genTimeSchedule(schedule.getLessonStart() + schedule.getNumOfLesson()).getMinutes());
            Date endDate = c.getTime();

            createEvent(strCalenderID, schedule.getSubjectName(), "PTIT", schedule.getDescription(), startDate, endDate);
        }
        System.out.println("Sync Google Calender Success!");
    }

    private void createEvent(String calendarId, String strSummary, String strLocation, String strDes, Date startDate, Date endDate) throws ParseException, IOException {
        Event newEvent = new Event()
                .setSummary(strSummary)
                .setLocation(strLocation)
                .setDescription(strDes);

        DateTime startDateTime = new DateTime(startDate);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        newEvent.setStart(start);

        DateTime endDateTime = new DateTime(endDate);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        newEvent.setEnd(end);

        // Lua chon kieu thong bao    
        EventReminder[] reminderOverrides;
        if (reminderType == REMIDER_TYPE_ALL) {
            reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(reminderTime),
                new EventReminder().setMethod("popup").setMinutes(reminderTime)};
        } else if (reminderType == REMIDER_TYPE_POPUP) {
            reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("popup").setMinutes(reminderTime)};
        } else {
            reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(reminderTime)};
        }

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        newEvent.setReminders(reminders);

        newEvent = service.events().insert(calendarId, newEvent).execute();
        System.out.printf("Event created: %s\n", newEvent.getHtmlLink());
        PROCESS_VALUE++;
    }

    private boolean checkCalenderExist(String strSumaryCheck) throws IOException {
        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                if (calendarListEntry.getSummary().equals(strSumaryCheck)) {
                    strCalenderID = calendarListEntry.getId();
                    return true;
                }
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return false;
    }

//    public static void main(String[] args) throws GeneralSecurityException, IOException, ParseException {
//        SyncGgCalender sgc = new SyncGgCalender(15, SyncGgCalender.REMIDER_TYPE_ALL);
//        CrawlScheduleIDOR cdidor = new CrawlScheduleIDOR("b17dcat136");
//        sgc.syncSchedule(cdidor.getAllSchedules(), "Lịch học PTIT");
//    }
}

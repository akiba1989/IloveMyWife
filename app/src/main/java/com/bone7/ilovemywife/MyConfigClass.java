package com.bone7.ilovemywife;

import java.util.ArrayList;


/**
 * Created by ducnu on 10/28/2017.
 */

public class MyConfigClass {
    public boolean notification = true;
    public ArrayList<MyEvent> eventList;
    public MyConfigClass ( boolean notification, ArrayList<MyEvent> eventList)
    {
        this.notification = notification;
        this.eventList = eventList;
    }
    public boolean containDate(String date)
    {
        for (int i = 0;i<eventList.size();i++)
        {
            if(eventList.get(i).eventDate.substring(5,10).equals(date.substring(5,10)))
                return true;
        }
        return false;
    }
    public String getEventName (String date)
    {
        for (int i = 0;i<eventList.size();i++)
        {
            if(eventList.get(i).eventDate.substring(5,10).equals(date.substring(5,10)))
                return eventList.get(i).eventName;
        }
        return "";
    }

    public MyConfigClass(){};
    public static class MyEvent
    {
        public String eventName;
        public String eventDate;
        public MyEvent(String name, String date)
        {
            this.eventName = name;
            this.eventDate = date;
        }
    }
}

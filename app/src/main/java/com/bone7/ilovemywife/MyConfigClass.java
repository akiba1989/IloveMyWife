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

package com.bone7.ilovemywife;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;

/**
 * Created by jamijn on 9/22/17.
 */

public class MyAndroidHelper {
    public static String readFromFile(String filename, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

            }
        }
        catch (FileNotFoundException e) {
            //msbox("Error", "Sorry, Unexpected error occurred!");
            return "";
        } catch (IOException e) {
            //msbox("Error", "Sorry, Unexpected error occurred!");
            return "";
        }

        return ret;
    }
    public static String readFromAssetsFile(String filename, Context context) {

        String ret = "";

        try {
            AssetManager am = context.getAssets();
            InputStream inputStream = am.open(filename);


            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            //msbox("Error", "Sorry, Unexpected error occurred!");
            return "";
        } catch (IOException e) {
            //msbox("Error", "Sorry, Unexpected error occurred!");
            return "";
        }

        return ret;
    }
    public static void writeToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            //Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static class ActionComparator implements Comparator<NewsService.News.StoriesBean>
    {
        public int compare(NewsService.News.StoriesBean lhs, NewsService.News.StoriesBean rhs) {
            return String.valueOf(rhs.getChecked()).compareTo(String.valueOf(lhs.getChecked()));
        }
    }

    public static class EventComparator implements Comparator<MyConfigClass.MyEvent>
    {
        public int compare(MyConfigClass.MyEvent lhs, MyConfigClass.MyEvent rhs) {
            return lhs.eventDate.substring(5,10).compareTo(rhs.eventDate.substring(5,10));
        }
    }
    public static String getMonthName(int month)
    {
        switch (month)
        {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JULY";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return "JAN";
    }
    public static String getMonthName(String month)
    {
        switch (month)
        {
            case "01":
                return "JAN";
            case "02":
                return "FEB";
            case "03":
                return "MAR";
            case "04":
                return "APR";
            case "05":
                return "MAY";
            case "06":
                return "JUN";
            case "07":
                return "JULY";
            case "08":
                return "AUG";
            case "09":
                return "SEP";
            case "10":
                return "OCT";
            case "11":
                return "NOV";
            case "12":
                return "DEC";
        }
        return "JAN";
    }

}

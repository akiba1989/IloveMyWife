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

}

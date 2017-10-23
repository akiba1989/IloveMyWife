package com.bone7.ilovemywife;

import com.kelin.calendarlistview.library.BaseCalendarItemModel;

import java.util.List;

/**
 * Created by kelin on 16-7-20.
 */
public class CustomCalendarItemModel  extends BaseCalendarItemModel{
    private int newsCount;
    private boolean isFav;
    private List<String> images;

    public int getNewsCount() {
        return newsCount;
    }

    public void setNewsCount(int newsCount) {
        this.newsCount = newsCount;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public List<String> getListImages() {return images;}

    public void setListImages(List<String> images) {this.images = images;}
}

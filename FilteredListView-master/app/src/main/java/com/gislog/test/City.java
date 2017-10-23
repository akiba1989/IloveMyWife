package com.gislog.test;

/**
 * Created by salih.yalcin on 22.2.2017.
 */

public class City {

    private String cityName;
    private int id;
    private boolean isChecked;
    private boolean check;


    public City(int id , String cityName){
        this.cityName = cityName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}

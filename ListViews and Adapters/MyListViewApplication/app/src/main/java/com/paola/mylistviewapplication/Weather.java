package com.paola.mylistviewapplication;

public class Weather {
    public int icon;
    public String date;
    public String temp;
    public Weather(){
        super();
    }
    
    public Weather(int icon, String date, String temp) {
        super();
        this.icon = icon;
        this.date = date;
        this.temp = temp;
    }
}
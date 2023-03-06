/*
 * Homework 06
 * ContactListFragment
 *Group12_HW06
 * Samba Diagne
 * Chris Overcash
 */
package edu.uncc.weather;

import java.io.Serializable;
import java.security.PublicKey;

public class WeatherForecast implements Serializable {


    public String date;
    public double temp;
    public double tempMax;
    public double tempMin;
    public double humidity;
    public String weatherDesc;
    public String imageLink;
    public WeatherForecast(){
    }

    public WeatherForecast(String date, double temp, double tempMax, double tempMin, double humidity, String weatherDesc, String img) {
        this.date = date;
        this.temp = temp;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.humidity = humidity;
        this.weatherDesc = weatherDesc;
        this.imageLink = img;
    }
}

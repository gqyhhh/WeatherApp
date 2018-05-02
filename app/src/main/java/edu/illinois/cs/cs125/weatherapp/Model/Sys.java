package edu.illinois.cs.cs125.weatherapp.Model;

public class Sys {
    private String country;
    private double sunrise;
    private double sunset;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getSunrise() {
        return sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public double getSunset() {
        return sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }

    public Sys(String country, double sunrise, double sunset) {

        this.country = country;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }
}

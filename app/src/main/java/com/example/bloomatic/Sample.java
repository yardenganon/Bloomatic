package com.example.bloomatic;

public class Sample {
    String soilHumiditySampleValue;
    String humiditySampleValue;
    String temperature;
    String timeStamp;
    String water_level;

    public Sample(String soilHumiditySampleValue, String humiditySampleValue, String temperature, String timeStamp) {
        this.soilHumiditySampleValue = soilHumiditySampleValue;
        this.humiditySampleValue = humiditySampleValue;
        this.temperature = temperature;
        this.timeStamp = timeStamp;
    }
    public  Sample() {}

    public String getSoilHumiditySampleValue() {
        return soilHumiditySampleValue;
    }

    public String getHumiditySampleValue() {
        return humiditySampleValue;
    }

    public void setHumiditySampleValue(String humiditySampleValue) {
        this.humiditySampleValue = humiditySampleValue;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setSoilHumiditySampleValue(String soilHumiditySampleValue) {
        this.soilHumiditySampleValue = soilHumiditySampleValue;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWater_level() {
        return water_level;
    }

    public void setWater_level(String water_level) {
        this.water_level = water_level;
    }

    public static int numberOfSamples = 0;
}

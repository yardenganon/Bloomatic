package com.example.bloomatic;

import java.util.List;

public class Irrigation {
    public String status;
    public double water_amount;
    public String water_level_before;
    public String water_level_after;
    public String timestamp;
    public long epochTime;
    public boolean quickIrrigation;
    public List<String> errors;
    public List<String> logs;


    public Irrigation(String status, double water_amount, String timestamp) {
        this.status = status!=null?status:"";
        this.water_amount = water_amount;
        this.timestamp = timestamp!=null?timestamp:"";
        quickIrrigation = false;

    }

    public Irrigation(String status, double water_amount, long epochTime) {
        this.status = status;
        this.water_amount = water_amount;
        this.epochTime = epochTime;
    }

    Irrigation(double water_amount, String water_level, String timestamp, boolean isQuickIrrigation, List<String> errors, List<String> logs){
        this.water_amount = water_amount;
        this.water_level_before = water_level;
        this.timestamp = timestamp;
        this.quickIrrigation = isQuickIrrigation;
        for (String s : errors)
            this.errors.add(s);
        for (String l : logs)
            this.logs.add(l);
    }

    public boolean isQuickIrrigation() {
        return quickIrrigation;
    }

    public void setQuickIrrigation(boolean quickIrrigation) {
        this.quickIrrigation = quickIrrigation;
    }

    public double getWater_amount() {
        return water_amount;
    }

    public void setWater_amount(double water_amount) {
        this.water_amount = water_amount;
    }

    public String getWater_level_before() {
        return water_level_before;
    }

    public void setWater_level_before(String water_level_before) {
        this.water_level_before = water_level_before;
    }

    public String getWater_level_after() {
        return water_level_after;
    }

    public void setWater_level_after(String water_level_after) {
        this.water_level_after = water_level_after;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

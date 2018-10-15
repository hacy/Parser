package com.ef.configuration;

public enum Period {

    HOURLY("hourly", "HOUR"),
    DAILY("daily", "DAY");

    public final String desc;
    public final String interval;

    Period(String desc, String interval) {
        this.desc = desc;
        this.interval = interval;
    }

}

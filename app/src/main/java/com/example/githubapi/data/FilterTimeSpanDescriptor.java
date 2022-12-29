package com.example.githubapi.data;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FilterTimeSpanDescriptor {


    @Retention(RetentionPolicy.SOURCE)

    @StringDef({DAILY, WEEKLY, MONTHLY})

    public @interface FilterTimeSpanDef {}

    public static final String DAILY = "daily";
    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";


}

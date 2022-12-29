package com.example.githubapi;

import com.example.githubapi.data.FilterTimeSpanDescriptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String formatTimeFilterQuery(@FilterTimeSpanDescriptor.FilterTimeSpanDef String filter) {
        String formattedTime = "";

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        switch (filter) {

            case FilterTimeSpanDescriptor.DAILY:
                c.add(Calendar.HOUR_OF_DAY,-24);

                break;
            case FilterTimeSpanDescriptor.WEEKLY:
                c.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case FilterTimeSpanDescriptor.MONTHLY:
                c.add(Calendar.MONTH, -1);
                break;



        }
        formattedTime = formatDate(c.getTime());

        return formattedTime;


    }


    private static  String formatDate(Date date) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);


    }
}

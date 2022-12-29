package com.example.githubapi;

import com.example.githubapi.data.Filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static String formatTimeFilterQuery(Filter filter) {
        StringBuilder searchBuilder = new StringBuilder();
        searchBuilder.append("created:>");


        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        switch (filter) {

            case DAILY:
                c.add(Calendar.HOUR_OF_DAY,-24);

                break;
            case WEEKLY:
                c.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case MONTHLY:
                c.add(Calendar.MONTH, -1);
                break;



        }
        String formattedTime = formatDate(c.getTime());
        searchBuilder.append(formattedTime);

        return searchBuilder.toString();


    }


    private static  String formatDate(Date date) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);


    }
}

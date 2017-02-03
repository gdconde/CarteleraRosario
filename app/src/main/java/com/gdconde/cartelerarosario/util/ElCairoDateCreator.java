package com.gdconde.cartelerarosario.util;

import java.util.Calendar;

/**
 * Created by gdconde on 2/2/17.
 */

public final class ElCairoDateCreator {

    public static Long stringToTimestamp(String schedule, String month) {

        //General case Miércoles 22 - 21:00 Hs
        String[] scheduleDivided = schedule.split(" ");

        //General case Febrero 2017
        String[] monthDivided = month.split(" ");
        int yearInt = Integer.parseInt(monthDivided[1]);
        int monthInt = Util.stringToIntMonth(monthDivided[0]);
        int dayInt = Integer.parseInt(scheduleDivided[1]);
        int hourInt = Integer.parseInt(scheduleDivided[3].split(":")[0]);
        int minuteInt = Integer.parseInt(scheduleDivided[3].split(":")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(yearInt, monthInt, dayInt, hourInt, minuteInt, 0);
        return calendar.getTimeInMillis() / 1000;
    }

}

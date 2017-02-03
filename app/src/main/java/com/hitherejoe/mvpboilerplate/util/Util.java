package com.hitherejoe.mvpboilerplate.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gdconde on 2/2/17.
 */

public final class Util {

    public static int stringToIntMonth(String month) {
        switch (month) {
            case "Enero": return 0;
            case "Febrero": return 1;
            case "Marzo": return 2;
            case "Abril": return 3;
            case "Mayo": return 4;
            case "Junio": return 5;
            case "Julio": return 6;
            case "Agosto": return 7;
            case "Septiembre": return 8;
            case "Octubre": return 9;
            case "Noviembre": return 10;
            case "Diciembre": return 11;
        }
        return -1;
    }

    public static String formatCalendarDateToString(Calendar date, String format) {
        DateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date.getTime());
    }

}

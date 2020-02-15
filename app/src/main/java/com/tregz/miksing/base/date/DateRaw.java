package com.tregz.miksing.base.date;

import java.util.Calendar;
import java.util.Date;

public final class DateRaw {

    static Date localeToTimeZone(Date date) {
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getRawOffset());
    }

    static Date timeZoneToLocale(Date date) {
        return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getRawOffset());
    }
}

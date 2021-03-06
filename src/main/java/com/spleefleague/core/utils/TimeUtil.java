/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.core.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jonas
 */
public class TimeUtil {

    //second, minute, hour, day, month, year
    private final static int[] seconds = new int[]{1, 60, 60 * 60, 60 * 60 * 24, 60 * 60 * 24 * 30, 60 * 60 * 24 * 365};
    private final static String[] names = new String[]{"second", "minute", "hour", "day", "month", "year"};

    public static String dateToString(Date date, boolean future) {
        long diff = Instant.now().getEpochSecond() - date.toInstant().getEpochSecond();
        if (future) {
            diff *= -1;
        }
        for (int i = 1; i < seconds.length; i++) {
            if (diff < seconds[i]) {
                long result = diff / seconds[i - 1];
                return result + " " + names[i - 1] + ((result != 1) ? "s" : "");
            }
        }
        return "";
    }

    public static String durationToString(Duration d) {
        String s = "";
        boolean started = false;
        if (d.toDays() >= 365) {
            started = true;
            s += d.toDays() / 365 + " year" + ((d.toDays() / 365 != 1) ? "s" : "") + ", ";
            d = d.minus(Duration.ofDays(d.toDays() / 365 * 365));
        }
        if (started || d.toDays() >= 30) {
            started = true;
            s += d.toDays() / 30 + " month" + ((d.toDays() / 30 != 1) ? "s" : "") + ", ";
            d = d.minus(Duration.ofDays(d.toDays() / 30 * 30));
        }
        if (started || d.toDays() >= 7) {
            started = true;
            s += d.toDays() / 7 + " week" + ((d.toDays() / 7 != 1) ? "s" : "") + ", ";
            d = d.minus(Duration.ofDays(d.toDays() / 7 * 7));
        }
        if (started || d.toDays() > 0) {
            started = true;
            s += d.toDays() + " day" + ((d.toDays() != 1) ? "s" : "") + ", ";
            d = d.minus(Duration.ofDays(d.toDays()));
        }
        if (started || d.toHours() > 0) {
            s += d.toHours() + " hour" + ((d.toHours() != 1) ? "s" : "") + ", ";
            started = true;
            d = d.minus(Duration.ofHours(d.toHours()));
        }
        if (started || d.toMinutes() > 0) {
            s += d.toMinutes() + " minute" + ((d.toMinutes() != 1) ? "s" : "") + " and ";
            started = true;
            d = d.minus(Duration.ofMinutes(d.toMinutes()));
        }
        if (started || d.getSeconds() > 0) {
            s += d.getSeconds() + " second" + ((d.getSeconds() != 1) ? "s" : "");
        }
        return s;
    }

    public static Duration parseDurationString(String time) {

        Pattern timePattern = Pattern.compile("[1-9][0-9]*(y|mo|w|h|d|m|s)");
        Matcher matcher = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        while (matcher.find()) {
            String r = matcher.group();
            switch (r.charAt(r.length() - 1)) {
                case 'y': {
                    years = Integer.parseInt(r.replace("y", ""));
                    break;
                }
                case 'o': {
                    months = Integer.parseInt(r.replace("mo", ""));
                    break;
                }
                case 'w': {
                    weeks = Integer.parseInt(r.replace("w", ""));
                    break;
                }
                case 'd': {
                    days = Integer.parseInt(r.replace("d", ""));
                    break;
                }
                case 'h': {
                    hours = Integer.parseInt(r.replace("h", ""));
                    break;
                }
                case 'm': {
                    minutes = Integer.parseInt(r.replace("m", ""));
                    break;
                }
                case 's': {
                    seconds = Integer.parseInt(r.replace("s", ""));
                    break;
                }

            }
        }
        return Duration.ofDays(years * 365 + months * 30 + weeks * 7 + days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }
}

package com.monenco.insights;

import com.monenco.insights.models.Translator;

public class Calander {

    public static String getDate(String gregorianDate, boolean isShort) {
        if (Translator.isPersian()) {
            gregorianDate = gregorian_to_jalali_int(gregorianDate);
        }
        int year = Integer.parseInt(gregorianDate.substring(0,4));
        int month = Integer.parseInt(gregorianDate.substring(5, 7));
        int date = Integer.parseInt(gregorianDate.substring(8,10));
        String monthStr;
        switch (month) {
            case 1:
                if (Translator.isPersian()) {
                    monthStr = "فروردین";
                } else {
                    if (isShort) {
                        monthStr = "JAN";
                    } else {
                        monthStr = "January";
                    }
                }
                break;
            case 2:
                if (Translator.isPersian()) {
                    monthStr = "اردیبهشت";
                } else {
                    if (isShort) {
                        monthStr = "FEB";
                    } else {
                        monthStr = "February";
                    }
                }
                break;
            case 3:
                if (Translator.isPersian()) {
                    monthStr = "خرداد";
                } else {
                    if (isShort) {
                        monthStr = "MAR";
                    } else {
                        monthStr = "March";
                    }
                }
                break;
            case 4:
                if (Translator.isPersian()) {
                    monthStr = "تیر";
                } else {
                    if (isShort) {
                        monthStr = "APR";
                    } else {
                        monthStr = "April";
                    }
                }
                break;
            case 5:
                if (Translator.isPersian()) {
                    monthStr = "مرداد";
                } else {
                    if (isShort) {
                        monthStr = "MAY";
                    } else {
                        monthStr = "May";
                    }
                }
                break;
            case 6:
                if (Translator.isPersian()) {
                    monthStr = "شهریور";
                } else {
                    if (isShort) {
                        monthStr = "JUN";
                    } else {
                        monthStr = "June";
                    }
                }
                break;
            case 7:
                if (Translator.isPersian()) {
                    monthStr = "مهر";
                } else {
                    if (isShort) {
                        monthStr = "JUL";
                    } else {
                        monthStr = "July";
                    }
                }
                break;
            case 8:
                if (Translator.isPersian()) {
                    monthStr = "آبان";
                } else {
                    if (isShort) {
                        monthStr = "AUG";
                    } else {
                        monthStr = "August";
                    }
                }
                break;
            case 9:
                if (Translator.isPersian()) {
                    monthStr = "آذر";
                } else {
                    if (isShort) {
                        monthStr = "SEP";
                    } else {
                        monthStr = "September";
                    }
                }
                break;
            case 10:
                if (Translator.isPersian()) {
                    monthStr = "دی";
                } else {
                    if (isShort) {
                        monthStr = "OCT";
                    } else {
                        monthStr = "October";
                    }
                }
                break;
            case 11:
                if (Translator.isPersian()) {
                    monthStr = "بهمن";
                } else {
                    if (isShort) {
                        monthStr = "NOV";
                    } else {
                        monthStr = "November";
                    }
                }
                break;
            case 12:
                if (Translator.isPersian()) {
                    monthStr = "اسفند";
                } else {
                    if (isShort) {
                        monthStr = "DEC";
                    } else {
                        monthStr = "December";
                    }
                }
                break;
            default:
                monthStr = null;

        }
        if (isShort) {
            return date + " " + monthStr;
        }else {
            if(Translator.isPersian()){
                return date+" "+monthStr+" "+year;
            }else {
                return monthStr+" "+date+"th, "+year;
            }
        }
    }

    public static String gregorian_to_jalali_int(String georgian) {
        String[] splited = georgian.split("-");

        int gy1, gm1, gd1;
        gy1 = Integer.parseInt(splited[0]);
        gm1 = Integer.parseInt(splited[1]);
        gd1 = Integer.parseInt(splited[2]);

        int g_days_in_month[] = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int j_days_in_month[] = new int[]{31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
        int gy = gy1 - 1600;
        int gm = gm1 - 1;
        int gd = gd1 - 1;
        int g_day_no =
                365 * gy + doubleToInt((gy + 3) / 4) - doubleToInt((gy + 99) / 100) +
                        doubleToInt((gy + 399) / 400);
        int i;
        for (i = 0; i < gm; ++i)
            g_day_no += g_days_in_month[i];
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)))
            g_day_no++;
        g_day_no += gd;
        int j_day_no = g_day_no - 79;
        int j_np = doubleToInt(j_day_no / 12053);
        j_day_no = j_day_no % 12053;
        int jy = 979 + 33 * j_np + 4 * doubleToInt(j_day_no / 1461);
        j_day_no %= 1461;
        if (j_day_no >= 366) {
            jy += doubleToInt((j_day_no - 1) / 365);
            j_day_no = (j_day_no - 1) % 365;
        }
        for (i = 0; i < 11 && j_day_no >= j_days_in_month[i]; ++i)
            j_day_no -= j_days_in_month[i];
        int jm = i + 1;

        String d = "";
        d += Integer.toString(jy) + "-";
        d += jm + "-";
        d += Integer.toString(j_day_no + 1);
        return d;
    }

    private static int doubleToInt(double d) {
        Double intOfD = d;
        return intOfD.intValue();
    }

}

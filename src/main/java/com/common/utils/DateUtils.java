package com.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2019/12/12
 *
 * @author connor.chen
 */
public class DateUtils {
    private final SecureRandom random = new SecureRandom();

    public static String formatDate(long ms, String language) {// 将毫秒数换算成x天x时x分x秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (!strDay.equals("00")) {
            if (language.equals("zh_CN")) {
                return strDay + "天" + strHour + "小时" + strMinute + "分" + strSecond + "秒";
            }
            return strDay + "d" + strHour + "h" + strMinute + "m" + strSecond + "s";
        } else if (!strHour.equals("00")) {
            if (language.equals("zh_CN")) {
                return strHour + "小时" + strMinute + "分" + strSecond + "秒";
            }
            return strHour + "h" + strMinute + "m" + strSecond + "s";
        } else if (!strMinute.equals("00")) {
            if (language.equals("zh_CN")) {
                return strMinute + "分" + strSecond + "秒";
            }
            return strMinute + "m" + strSecond + "s";
        } else {
            if (language.equals("zh_CN")) {
                return strSecond + "秒";
            }
            return strSecond + "s";
        }
    }

    public static Date ch2Date(String str, String fmt) {

        DateFormat format = new java.text.SimpleDateFormat(fmt);
        Date data = null;
        try {
            if (!"".equals(str) && str != null) {
                data = format.parse(str);
            }
        } catch (ParseException e) {
            System.err.println("ch2Date出错");
            e.printStackTrace();
        }
        return data;
    }

    public static Map<String, String> getFlyTime(Date arrivalDate, Date deptDate) {
        long time1 = arrivalDate.getTime();
        long time2 = deptDate.getTime();
        long dit = time1 - time2;
        int dite = 0;
        int sont = 0;
        if (dit != 0) {
            dite = (int) (dit / 3600000);
            sont = (int) ((dit % 3600000) / 60000);
        }
        Map<String, String> map = new HashMap<>();
        map.put("h", dite + "");
        map.put("m", sont + "");
        map.put("s", sont + "");
        return map;
    }

    public static String gettadaydate() {
        Date taday = new Date();
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(taday);
    }

    public static String gettadaydate(String forstr) {
        Date taday = new Date();
        DateFormat format = new java.text.SimpleDateFormat(forstr);
        return format.format(taday);
    }

    public static String date2str(Date date) {
        if (date != null) {
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return format.format(date).substring(0, 10);
        } else {
            return null;
        }
    }

    public static String date2str(Date date, String fmt) {
        if (date != null) {
            DateFormat format = new java.text.SimpleDateFormat(fmt);
            return format.format(date);
        } else {
            return null;
        }
    }

    public static String formatDate(Date date) {

        if (date != null) {
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        } else {
            return null;
        }
    }

    public static String formatDatetime(Date date) {

        if (date != null) {
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        } else {
            return null;
        }
    }

    public static String formatDateStr(String dateStr) {
        String result = "";
        if (dateStr != null && !"".equals(dateStr)) {
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                result = format.format(format.parse(dateStr));
            } catch (ParseException e) {
                System.out.println("formatDateStr 出错");
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String formatDateStr(String dateStr, String formatStr) {
        String result = "";
        if (dateStr != null && !"".equals(dateStr)) {
            DateFormat format = new java.text.SimpleDateFormat(formatStr);
            try {
                result = format.format(format.parse(dateStr));
            } catch (ParseException e) {
                System.out.println("formatDateStr 出错");
                e.printStackTrace();
            }
        }
        return result;
    }

    public Date getNeedsDateTime(Date dDate, int iMinute) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dDate);

        calendar.add(Calendar.MINUTE, iMinute);

        String sDate = sdf.format(calendar.getTime());

        return sdf.parse(sDate);
    }

    public Date getNeedDayDateTime(Date dDate, int iDays) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dDate);

        calendar.add(Calendar.DATE, iDays);

        String sDate = sdf.format(calendar.getTime());

        return sdf.parse(sDate);
    }

    public static Date changeDay(String timeStr, int day) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d;
        Calendar cal = Calendar.getInstance();
        d = sdf.parse(timeStr);
        cal.setTime(d);
        cal.add(Calendar.DATE, day); // 减1天
        return cal.getTime();
    }

    /**
     * 生成随机密码 产生一个随机字符串，其中只包含字母数字； len-这个随机字符串的长度；
     * digitalset-必须出现的数字个数，0表示不一定要包含数字，1表示至少包含一个数字，>1表示随便几个；
     * upperset-必须出现的大写字母个数，0表示不一定要包含大写字母，1表示至少包含一个大写字母，>1表示随便几个；
     * lowerset-必须出现的小写字母个数，0表示不一定要包含小写字母，1表示至少包含一个小写字母，>1表示随便几个；
     * removechar-不希望出现的字符组成的字符串，可以多个，也可以一个，若为空串表示没有需要过滤的字符。
     *
     * @param len
     * @param digitalset
     * @param upperset
     * @param lowerset
     * @param removechar
     * @return String
     */
    public String random_string(int len, int digitalset, int upperset, int lowerset, String removechar) {
        //
        int i;
        int c;
        int digital_count;
        int upper_count;
        int lower_count;
        StringBuilder randomstr = new StringBuilder();
        char tmp_c;
        while (randomstr.toString().trim().equals("")) {
            i = 0;
            digital_count = 0;
            upper_count = 0;
            lower_count = 0;

            while (i < len) {
                c = random.nextInt(122);
                tmp_c = (char) c;
                if (((c > 47 && c < 58) || (c > 64 && c < 91) || (c > 96 && c < 123)) && !removechar.trim().equals("") && removechar.indexOf(tmp_c) < 0) {
                    if (c > 47 && c < 58) {
                        digital_count++;
                    }
                    if (c > 64 && c < 91) {
                        upper_count++;
                    }
                    if (c > 96 && c < 123) {
                        lower_count++;
                    }
                    randomstr.append((char) c);
                    i++;
                }
            }
            if ((digitalset > 0 && digital_count == 0) || (upperset > 0 && upper_count == 0) || (lowerset > 0 && lower_count == 0)) {
                randomstr = new StringBuilder();
            }
        }
        return randomstr.toString();
    }

    public String getRandKeys(int intLength) {

        String retStr;
        String strTable = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < intLength; i++) {
                double dblR = random.nextDouble() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    public static boolean ReplaceAllChineseCharacter(String source) {
        if (StringUtils.isNotEmpty(source)) {
            String mre = "[\u4e00-\u9fa5]+";
            Pattern p = Pattern.compile(mre);
            Matcher m = p.matcher(source);
            return m.find();
        } else {
            return false;
        }
    }

    public static String strtodatetostr(String datestr, String fmtstr) {
        if (datestr != null && !"".equals(datestr)) {
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat format1 = new java.text.SimpleDateFormat(fmtstr);
            try {
                return format1.format(format.parse(datestr));
            } catch (ParseException e) {
                System.out.println("strtodatetostr 出错");
                e.printStackTrace();
            }
        }
        return datestr;

    }

    /**
     * 替换换行符，回车符，制表位
     */
    public static String replaceRNT(String mystr) {
        mystr = mystr.replaceAll("\r\n", "<br/>");
        mystr = mystr.replaceAll("\r", "<br/>");
        mystr = mystr.replaceAll("\n", "<br/>");
        mystr = mystr.replaceAll("'", "&quot;");

        return mystr;
    }


    public static String formatDate(Date date, String format) {
        return formatDate(date, format, Locale.ENGLISH);
    }

    public static String formatDate(Date date, String format, Locale locale) {
        String formattedDate = "";
        if (date != null && format != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, locale);
                formattedDate = simpleDateFormat.format(date);
            } catch (Exception e) {
                System.out.println("formatDate 出错");
                e.printStackTrace();
            }
        }
        return formattedDate;
    }

    public static String formatDate(Date date, String format, TimeZone zone) {
        String formattedDate = null;
        if (date != null && format != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                simpleDateFormat.setTimeZone(zone);
                formattedDate = simpleDateFormat.format(date);
            } catch (Exception e) {
                System.out.println("formatDate 出错");
                e.printStackTrace();
            }
        }
        return formattedDate;
    }

    public static Date parseDateByLocale(String dateValue,String dateFmt,Locale locale){
        try{
            SimpleDateFormat sdf=new SimpleDateFormat(dateFmt, locale);
            return sdf.parse(dateValue);
        }catch(Exception e){
            System.out.println("parseDateByLocale 出错");
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String dateValue, String dateFormat) {
        Date parsedDate = null;
        if (dateValue != null && dateFormat != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
                parsedDate = simpleDateFormat.parse(dateValue);
            } catch (ParseException e) {
                System.out.println("Invalid Date value " + dateValue +
                        " for format " + dateFormat);
                System.out.println("getPageDateTime 出错");
            }
        }
        return parsedDate;
    }

    public static Date parseDateSilent(String dateValue) {
        Date result = null;
        try {
            result = org.apache.commons.lang3.time.DateUtils.parseDate(dateValue,
                    DateFormatConsts.DATE_SHORT,
                    DateFormatConsts.DATE_TIME,
                    DateFormatConsts.DATE_SHORT_1,
                    DateFormatConsts.DATE_TIME_2);
        } catch (ParseException e) {
            System.out.println("Date parse error");
            e.printStackTrace();
        }
        return result;
    }

    public static Date parseDateSilent(String dateValue, String format) {
        Date result = null;
        if(StringUtils.isNotBlank(dateValue)){
            try {
                result = org.apache.commons.lang3.time.DateUtils.parseDate(dateValue,
                        format,
                        DateFormatConsts.DATE_SHORT,
                        DateFormatConsts.DATE_TIME,
                        DateFormatConsts.DATE_SHORT_1,
                        DateFormatConsts.DATE_TIME_2);
            } catch (ParseException e) {
                System.out.println("Date parse error");
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String strToDateString(String dateValue, String dateFrom, String dateFormat) {
        String m = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFrom);
            DateFormat df = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

            Date date = formatter.parse(dateValue);
            m = df.format(date);
        } catch (ParseException e) {
            System.out.println("strToDateString 出错");
            e.printStackTrace();
        }
        return m;
    }

    public String convertDateByLanguage(Date date, String lanuage) {
        String needDate = null;
        if ("zh_CN".equals(lanuage)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            needDate = simpleDateFormat.format(date);
        }
        if (!"zh_CN".equals(lanuage)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            needDate = simpleDateFormat.format(date);
        }
        return needDate;
    }

    public static String getMonth(String date) {
        List<String> dayNames = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
        for (int i = 0; i < dayNames.size(); i++) {
            if (date.substring(2, 5).toUpperCase().startsWith(dayNames.get(i))) {
                if (i < 10) {
                    return "0" + i;
                }
                return i + "";
            }
        }
        return -1 + "";
    }

    // 28DEC10 conversion yyyy-MM-dd
    public static String yearFormat2Date(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.valueOf(20 + date.substring(5, 7)));

        c.set(Calendar.MONTH, Integer.valueOf(getMonth(date)));

        c.set(Calendar.DATE, Integer.valueOf(date.substring(0, 2)));

        Date date1 = c.getTime();
        return sdf.format(date1);
    }

    // yyyy-MM-dd conversion 28DEC10
    public static String date2YearFormat(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        final String[] dayNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        int dateTime = c.get(Calendar.DATE);
        String dateTime1;

        if (dateTime < 10) {
            dateTime1 = "0" + dateTime;
        } else {
            dateTime1 = Integer.toString(dateTime);
        }

        return dateTime1 + dayNames[c.get(Calendar.MONTH)] + Integer.toString(c.get(Calendar.YEAR)).substring(2);
    }

    // 时间前后时差问题
    public static List<String> dateTimeRange(String hour, String timeRange) {
        List<String> vector = new ArrayList<>();
        int intTimerange = 0;
        int intHour = 0;
        int add;
        int dec;
        String beforeDate;
        String afterDate;
        if (StringUtils.isNotEmpty(hour)) {
            intHour = Integer.parseInt(hour.replace(":", ""));
        }
        if (StringUtils.isNotEmpty(timeRange)) {
            intTimerange = Integer.parseInt(timeRange);
        }
        add = intHour + intTimerange * 100;
        dec = intHour - intTimerange * 100;
        if (dec <= 0) {
            beforeDate = "0000";
        } else if (dec > 0 && Integer.toString(dec).length() < 4) {
            beforeDate = "0" + dec;
        } else {
            beforeDate = Integer.toString(dec);
        }
        if (add >= 2400) {
            afterDate = "2400";
        } else if (add < 2400 && Integer.toString(add).length() < 4) {
            afterDate = "0" + add;
        } else {
            afterDate = Integer.toString(add);
        }
        vector.add(beforeDate);
        vector.add(afterDate);
        return vector;
    }

    /**
     * 相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int betweenDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        long interval = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        long day = interval / 60 / 60 / 1000 / 24;// 获取剩余天数
        return Math.toIntExact(day);
    }

    /**
     * 相差分钟数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int betweenMinutes(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        long interval = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        long minutes = interval / 1000 / 60 ;// 获取剩余天数
        return Math.toIntExact(minutes);
    }

    /**
     * 指定时间增加minutes分钟
     *
     * @param deptDate
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date deptDate, Long minutes) {
        long orignalTime = deptDate.getTime();
        orignalTime = orignalTime + minutes * 60 * 1000;
        return new Date(orignalTime);
    }

    /**
     * 指定时间增加seconds秒
     *
     * @param deptDate
     * @param seconds
     * @return
     */
    public static Date addSeconds(Date deptDate, Long seconds) {
        long orignalTime = deptDate.getTime();
        orignalTime = orignalTime + seconds * 1000;
        return new Date(orignalTime);
    }


    /**
     * Abacus特殊格式的日期解析，如：09AUG
     *
     * @param date
     * @return
     */
    public static String getAbacusDate(Date date) {
        StringBuilder abacusDate = new StringBuilder();
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        abacusDate.append(fillOrRemove(String.valueOf(c.get(Calendar.DATE)), 2, "0", true));
        switch (c.get(Calendar.MONTH)) {
            case 0:
                abacusDate.append("JAN");
                break;
            case 1:
                abacusDate.append("FEB");
                break;
            case 2:
                abacusDate.append("MAR");
                break;
            case 3:
                abacusDate.append("APR");
                break;
            case 4:
                abacusDate.append("MAY");
                break;
            case 5:
                abacusDate.append("JUN");
                break;
            case 6:
                abacusDate.append("JUL");
                break;
            case 7:
                abacusDate.append("AUG");
                break;
            case 8:
                abacusDate.append("SEP");
                break;
            case 9:
                abacusDate.append("OCT");
                break;
            case 10:
                abacusDate.append("NOV");
                break;
            case 11:
                abacusDate.append("DEC");
                break;
            default:
        }
        return abacusDate.toString();
    }

    /**
     * @param source
     * @param len
     * @param padStr
     * @param left
     * @return
     */
    public static String fillOrRemove(String source, int len, String padStr,
                                      boolean left) {
        source = source == null ? StringUtils.EMPTY : source;
        if (left) {
            return StringUtils.leftPad(StringUtils.left(source, len), len,
                    padStr);
        } else {
            return StringUtils.rightPad(StringUtils.right(source, len), len,
                    padStr);
        }
    }

    /**
     * 添加/减少天数
     *
     * @param date
     * @param days
     * @return 返回新对象,源对象未改变
     */
    public static Date addDays(Date date, int days) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, days);
    }

    public static boolean verifyCreated(Date createdDate, int timeToLive, int futureTimeToLive) {
        if (createdDate == null) {
            return false;
        }
        Date validCreation = new Date();
        long currentTime = validCreation.getTime();
        if (futureTimeToLive > 0) {
            validCreation.setTime(currentTime + ((long) futureTimeToLive * 1000L));
        }
        // Check to see if the created time is in the future
        if (createdDate.after(validCreation)) {
            System.out.println("Validation of Created: The message was created in the future!");
            return false;
        }
        // Calculate the time that is allowed for the message to travel
        currentTime -= ((long) timeToLive * 1000L);
        validCreation.setTime(currentTime);
        // Validate the time it took the message to travel
        if (createdDate.before(validCreation)) {
            System.out.println("Validation of Created: The message was created too long ago");
            return false;
        }
        System.out.println("Validation of Created: Everything is ok");
        return true;
    }
}

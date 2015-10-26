package jp.hubfactory.moco.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.NoArgsConstructor;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

@NoArgsConstructor
public class MocoDateUtils {

    /** 日時フォーマット : yyyy/MM/dd */
    public static final String DATE_FORMAT_yyyyMMdd_SLASH = "yyyy/MM/dd";
    /** 日時フォーマット : yyyy/MM */
    public static final String DATE_FORMAT_yyyyMM_SLASH = "yyyy/MM";
    /** 日時フォーマット : HH:mm:ss */
    public static final String DATE_FORMAT_HHmmss = "HH:mm:ss";


    /** 日時フォーマット : yyyy-MM-dd HH:mm:ss */
    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
    /** 日時フォーマット : yyyy/MM/dd HH:mm:ss */
    public static final String DATE_FORMAT_NORMAL_SLASH = "yyyy/MM/dd HH:mm:ss";
    /** 日時フォーマット : MM/dd HH:mm */
    public static final String DATE_FORMAT_MMDDHHmm = "MM/dd HH:mm";
    /** 日時フォーマット : HH:mm */
    public static final String DATE_FORMAT_HHmm = "HH:mm";
    /** 日時フォーマット : yyyyMMdd */
    public static final String DATE_FORMAT_yyyyMMdd = "yyyyMMdd";
    /** 日時フォーマット : yyyyMM */
    public static final String DATE_FORMAT_yyyyMM = "yyyyMM";
    /** 日時フォーマット : yyyy-MM-dd */
    public static final String DATE_FORMAT_yyyyMMdd_HYPHEN = "yyyy-MM-dd";
    /** 日時フォーマット : MM */
    public static final String DATE_FORMAT_MM = "MM";
    /** 日時フォーマット : dd */
    public static final String DATE_FORMAT_DD = "dd";

    /**
     * 現在日時取得
     * @return
     */
    public static Date getNowDate(){
        return new Date(System.currentTimeMillis());
    }

    /**
     * String型に変換します。
     *
     * @param date 対象日付
     * @param format フォーマット
     * @return
     */
    public static String convertString(Date date, String format) {
        FastDateFormat sdf = FastDateFormat.getInstance(format);
        return sdf.format(date);
    }

    /**
     * Date型に変換します。
     *
     * @param date 対象日付
     * @param format フォーマット
     * @return
     */
    public static Date convertDate(String date, String format) {
        try {
            return DateUtils.parseDate(date, new String[] { format });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertTimeString(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String timeStr = null;

        if (hour > 0) {
            timeStr = String.format("%02d:%02d:%02d", hour, minute, second);
        } else if (minute > 0) {
            timeStr = String.format("%01d:%02d", minute, second);
        } else if (second > 0) {
            timeStr = String.format("%02d", second);
        } else {
            timeStr = "";
        }
        return timeStr;
    }

//    public static String convertAvgTimeString(Date date, String format) {
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//
//        int minute = cal.get(Calendar.MINUTE);
//        int second = cal.get(Calendar.SECOND);
//
//        String timeStr = null;
//
//        if (minute > 0) {
//            timeStr = String.format("%01d'%02d\"", minute, second);
//        } else if (second > 0) {
//            timeStr = String.format("%01d\"", second);
//        } else {
//            timeStr = "";
//        }
//        return timeStr;
//    }

    public static String convertTimeString(int minute, int second) {

        String timeStr = null;

        if (minute > 0) {
            timeStr = String.format("%01d'%02d\"", minute, second);
        } else if (second > 0) {
            timeStr = String.format("%01d\"", second);
        } else {
            timeStr = "";
        }
        return timeStr;

    }

    /**
     * 平均時間取得
     * @param timeStr 時間文字列(hh:mm:ss)
     * @param distance 距離
     * @return 平均時間
     */
    public static String calcAvgTime(String timeStr, Double distance) {
        // 走った時間を秒に変換する
        int second = MocoDateUtils.convertTimeStrToSecond(timeStr);
        // 平均を求める
        return MocoDateUtils.calcAvgTime(second, distance);
    }

    public static int convertTimeStrToSecond(String timeStr) {
        // 走った時間を秒に変換する
        String times[] = timeStr.split(":");
        int hour = Integer.parseInt(times[0]) * 3600;
        int minute = Integer.parseInt(times[1]) * 60;
        int second = Integer.parseInt(times[2]);
        int timeSecond = hour + minute + second;
        return timeSecond;
    }

    public static String calcAvgTime(int second, double distance) {
        // 平均時間(秒)を求める
        int avgTimeSecond = Double.valueOf(Math.ceil(second / distance)).intValue();
        int avgMinute = avgTimeSecond % 3600 / 60;
        int avgSecond = avgTimeSecond % 60;
        // 平均時間文字列
        String avgTime = MocoDateUtils.convertTimeString(avgMinute, avgSecond);
        return avgTime;
    }

    /**
     * 指定された日付が、指定された開始日時、終了日時の期間内かどうかを判定する
     *
     * <pre>
     * 秒単位まで確認し、境界を含む(start <= now <= end の場合true).
     * 開始日時が nullの場合、最小値。 終了日時が nullの場合、最大値として判断する
     * </pre>
     *
     * @param startDate 開始日時(任意)
     * @param endDate 終了日時(任意)
     * @param targetDate ターゲット日時
     * @return 判定結果
     */
    public static boolean isWithin(Date startDate, Date endDate, Date targetDate) {
        Validate.notNull(targetDate);
        if ((startDate == null) && (endDate == null)) {
            return true;
        }
        long start = startDate != null ? startDate.getTime() / 1000L : Long.MIN_VALUE;
        long end = endDate != null ? endDate.getTime() / 1000L : Long.MAX_VALUE;
        long now = targetDate.getTime() / 1000L;
        return start <= now && now <= end;
    }

    public static boolean isWithin(Date startDate, Date endDate) {
        return isWithin(startDate, endDate, getNowDate());
    }

    public static String convertSecToHMS(int sec) {

        int HH = sec / 3600;
        int mm = sec % 3600 / 60;
        int ss = sec % 60;

        String timeStr = null;

        if (HH > 0) {
            timeStr = String.format("%01d:%02d:%02d", HH, mm, ss);
        } else if (mm > 0) {
            timeStr = String.format("%02d:%02d", mm, ss);
        } else if (ss > 0) {
            timeStr = String.format("0'%02d", ss);
        } else {
            timeStr = "";
        }

        return timeStr;
    }

    public static String convertSecToLapTimeString(int sec) {

        int second = sec < 0 ? sec * -1 : sec;

        int HH = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;

        String timeStr = null;

        if (HH > 0) {
            timeStr = String.format("%01'%02d'%02d\"", HH, mm, ss);
        } else if (mm > 0) {
            timeStr = String.format("%01d'%02d\"", mm, ss);
        } else if (ss > 0) {
            timeStr = String.format("0'%02d\"", ss);
        } else {
            timeStr = "";
        }

        return timeStr;
    }

    public static Date getTimeZeroDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * startDateからendDateまでの日数を返す
     *
     * @param startDate 開始時間
     * @param endDate 終了時間
     * @return Long
     */
    public static Long getDiffereceDays(Date startDate, Date endDate) {
        long diffTime = endDate.getTime() - startDate.getTime();
        if (diffTime > 0) {
            // 秒/分/時/日
            long second = diffTime / 1000;
            long minute = second / 60;
            long hour = minute / 60;
            long day = hour / 24;

            return day;
        }
        return null;
    }

    /**
     * 曜日取得
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 日付の加減算を行います。
     *
     * @param target 対象日付
     * @param addNum 加減日数
     * @param addKind 年、月、日、等々（Calendarのフィールド）
     * @return 加減算の結果
     */
    public static Date add(Date target, int addNum, int addKind) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(target);
        cal.add(addKind, addNum);
        return new Date(cal.getTimeInMillis());
    }
}

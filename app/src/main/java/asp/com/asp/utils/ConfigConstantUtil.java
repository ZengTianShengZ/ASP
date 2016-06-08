package asp.com.asp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ConfigConstantUtil {

    public static final int loadingNotNew = 110;
    public static final int loadingSuccess = 111;
    public static final int loadingFault = 112;
    public static final int loadingNotOld = 113;


    public static final int sendSuccess = 114;
    public static final int sendFault = 115;

    public static final int commentSuccess = 116;
    public static final int commentFault = 117;

    public static final String intentDtat_Author = "intentDtat_Author";

    public static final String UserName = "UserName";
    public static final String UserLogStr = "UserLogStr";
    public static final String UserPassword = "UserPassword";
    //"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
    public static final String isTelePhoneNumber = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    public static final class Dates {
        public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        @SuppressWarnings("deprecation")
        public static final Date birthday = new java.util.Date(113, 4, 19); // May 19th, 2013
    }
}

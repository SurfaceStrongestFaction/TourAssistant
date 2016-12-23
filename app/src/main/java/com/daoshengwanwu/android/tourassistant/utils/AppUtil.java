package com.daoshengwanwu.android.tourassistant.utils;


import android.graphics.Bitmap;

public class AppUtil {
    public static final class SharingServer {
        public static final String HOST = "123.206.14.122";//192.168.43.14
        public static final int PORT = 80;
        public static final String COMMAND_SET_LOCATION = "set_location";
        public static final String REQUEST_MEMBER_LOCATION = "request_location";
        public static final String REQUEST_STOP = "over";
        public static final String COMMAND_SET_USERID = "userId";
        public static final String COMMAND_SET_GROUPID = "groupId";
        public static final String SEPARATOR_LOCATION_DIFFER_MEMBER = "#";
        public static final String SEPARATOR_LOCATION_LAT_LON = ",";
        public static final String SEPARATOR_LOCATION_ID_LOC = "->";
        public static final String SEPARATOR_COMMAND_CONTENT = ":";
        public static final String RECEIVED_MEMBER_LOCATIONS = "member_locations";
    }

    public static final class JFinalServer {
        public static final String HOST = "123.206.14.122";
        public static final int PORT = 80;
        public static final String xyurl = "http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/user/getInformation";
        public static final String xyurl2 = "http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/team/getInformation";
        public static final String xyurl3 = "http://"+AppUtil.JFinalServer.HOST+":"+AppUtil.JFinalServer.PORT+ "/team/joinTeam";
    }

    public static final class User {
        public static String USER_ID = "";
        public static String USER_NAME = "";
        public static Bitmap USER_IMG;
        public static String USER_GENDER = "男";
    }

    public static final class Group {
        public static String GROUP_ID = "";
        public static String GROUP_NAME;
        public static String GROUP_CAPTIAN = "4b9a29a5-9fc9-48db-9486-79353106a599";
    }

}

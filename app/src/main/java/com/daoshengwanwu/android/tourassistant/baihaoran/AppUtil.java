package com.daoshengwanwu.android.tourassistant.baihaoran;


import java.util.UUID;


public class AppUtil {
    public static final class SharingServer {
        public static final String HOST = "123.206.81.40";
        public static final int PORT = 9638;
        public static final String COMMAND_SET_LOCATION = "set_location";
        public static final String REQUEST_MEMBER_LOCATION = "request_location";
        public static final String REQUEST_STOP = "over";
        public static final String COMMAND_SET_USERID = "userId";
        public static final String SEPARATOR_LOCATION_DIFFER_MEMBER = "#";
        public static final String SEPARATOR_LOCATION_LAT_LON = ",";
        public static final String SEPARATOR_LOCATION_ID_LOC = "->";
        public static final String RECEIVED_MEMBER_LOCATIONS = "member_locations";
    }

    public static final class User {
        public static String USER_ID = "";
        public static String USER_NAME = "白浩然";
    }
}
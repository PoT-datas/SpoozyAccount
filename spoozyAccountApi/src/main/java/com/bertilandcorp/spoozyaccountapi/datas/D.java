package com.bertilandcorp.spoozyaccountapi.datas;

public class D {

    public static String HOST = "https://auth.spoozy.bertilandweb.com/";
    private static String API_DIR = "api/";
    private static String API_VERSION = "v1/";

    public static String getHOST(){
        return HOST;
    }

    public static String getApiLink(){
        return HOST+API_DIR+API_VERSION;
    }
}

package com.museumsystem.museumserver.config;


public class ServerPaths {
    public static final String SERVER_PROTOCOL = "https://";
    public static final String SERVER_IP = "145.239.93.41";
    public static final String SERVER_PORT = "8443";
    public static final String SERVER_URL =  SERVER_PROTOCOL + SERVER_IP + ":" + SERVER_PORT;
    public static final String SERVER_REGISTER_URL = SERVER_URL + "/user/add";
    public static final String SERVER_LOGIN_URL = SERVER_URL + "/oauth/token";
    public static final String SERVER_ACTIVATE_ACCOUNT_URL = SERVER_URL + "/account/activate";
}

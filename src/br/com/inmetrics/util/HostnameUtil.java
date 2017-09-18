package br.com.inmetrics.util;


public class HostnameUtil {

    public static String removeDomain (String hostname){
        
        if (hostname.split(".").length >= 1){
            hostname = hostname.split(".")[0];
        }
        
        return hostname;
    }
}
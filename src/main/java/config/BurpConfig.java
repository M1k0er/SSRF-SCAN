package config;

import burp.*;

public class BurpConfig {
    private static final String DNSLOGURL = "dnslog_url";
    private static final String DNSLOGTOKEN = "dnslog_token";

    public static String getDnslogService(String name){
        return BurpExtender.callback.loadExtensionSetting(name);
    }

    public static boolean getBoolean(String name){
        String var = BurpExtender.callback.loadExtensionSetting(name);
        if(var == null){
            return false;
        }else{
            return true;
        }
    }

    public static void setDnslogService(String name,String value){
        BurpExtender.callback.saveExtensionSetting(name,value);
    }

}

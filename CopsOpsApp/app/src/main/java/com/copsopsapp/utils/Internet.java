package com.copsopsapp.utils;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Internet {
    private static Internet INT;

    public static Internet getInstance() {
        if (INT == null)
            INT = new Internet();
        return INT;
    }

    public Boolean internetConnectivity(Context ctx) {
        if (ctx != null) {
            ConnectivityManager conmgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiinfo = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobiledata = conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifiinfo.isConnected() || mobiledata.isConnected();
        }
        return false;
    }


    //////// Ip Adresss code
    public static String checkAvailableConnection(Context ctx) {
        int ipAddress = 0;
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo network = connMgr.getActiveNetworkInfo();

        if (network.getType() == ConnectivityManager.TYPE_WIFI) {

            WifiManager myWifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
            ipAddress = myWifiInfo.getIpAddress();
            System.out.println("WiFi address is " + android.text.format.Formatter.formatIpAddress(ipAddress));

        } else if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
            ipAddress = GetLocalIpAddress();
            if (ipAddress != 0)
                System.out.println("WiFi address is " + android.text.format.Formatter.formatIpAddress(ipAddress));
        }

        return android.text.format.Formatter.formatIpAddress(ipAddress);
    }

    private static int GetLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.hashCode();
                    }
                }
            }
        } catch (SocketException ex) {
            return 0;
        }
        return 0;
    }

    public static int getDisplayWidth(Activity context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

//////// Ip Adresss end
}


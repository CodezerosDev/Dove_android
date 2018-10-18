package com.darpysolutions.dove.wifi;


import com.darpysolutions.Utils.Constants;

public class ServerBean {

    String servername;
    public String Type = Constants.HOTSPOT;

    public String IP;

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

}

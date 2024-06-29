package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.entity.inf.IData;

public class RemoteIpaddressData implements IData {
    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    private String ipaddress;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public RemoteIpaddressData(String ipaddress, String port) {
        this.ipaddress = ipaddress;
        this.port = port;
    }

    private String port;
}

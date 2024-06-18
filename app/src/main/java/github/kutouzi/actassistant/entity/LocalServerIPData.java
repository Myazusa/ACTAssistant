package github.kutouzi.actassistant.entity;

public class LocalServerIPData {
    private String localServerIPAddress;
    private String localServerPort;

    public LocalServerIPData(String localServerIPAddress, String localServerPort) {
        this.localServerIPAddress = localServerIPAddress;
        this.localServerPort = localServerPort;
    }

    public String getLocalServerIPAddress() {
        return localServerIPAddress;
    }

    public void setLocalServerIPAddress(String localServerIPAddress) {
        this.localServerIPAddress = localServerIPAddress;
    }

    public String getLocalServerPort() {
        return localServerPort;
    }

    public void setLocalServerPort(String localServerPort) {
        this.localServerPort = localServerPort;
    }
}

package github.kutouzi.actassistant.network;

public class JsonFileNIO {
    static {
        System.loadLibrary("LibJsonFileNIO");
    }
    public static native void sendSwipeUpJsonToQt(String jsonData);

    public static native String receiveSwipeUpJsonFromQt();

}

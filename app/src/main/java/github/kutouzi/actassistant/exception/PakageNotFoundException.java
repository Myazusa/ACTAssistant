package github.kutouzi.actassistant.exception;

public class PakageNotFoundException extends Exception{
    public PakageNotFoundException(String message){
        super("PakageNotFoundException: Please check if the application '"+  message +  "' is installed");
    }
}

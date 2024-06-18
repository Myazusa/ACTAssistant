package github.kutouzi.actassistant.exception;

public class FailedTaskException  extends Exception{

    public FailedTaskException(String message) {
        super("任务执行失败，原因："+message);
    }
}

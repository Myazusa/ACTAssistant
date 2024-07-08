package github.kutouzi.actassistant.command;

import org.json.JSONObject;

import github.kutouzi.actassistant.MainActivity;

/**
 * 传输的json命令要求如下格式，且一个命令一个json
 * 直接用String commandString = "{\"action\":\"click\",\"parameters\":{\"x\":100,\"y\":200}}";构造
 * {
 *     "action": "click",
 *     "parameters": {
 *         "x": 100,
 *         "y": 200
 *     }
 * }
 */
public class Command {
    private JSONObject parameters;
    private String action;
    public Command(String action, JSONObject parameters) {
        this.action = action;
        this.parameters = parameters;
    }
    public String getAction() {
        return action;
    }
    public JSONObject getParameters() {
        return parameters;
    }

    public void execute() {
        switch (action) {
            // 截图命令
            case "captureScreen":
                MainActivity.getInstance().captureScreen();
                break;
            case "clickScreen":
                int x = parameters.optInt("x");
                int y = parameters.optInt("y");
                MainActivity.getInstance().requestClickCommandACTFloatingWindow("clickScreen",x,y);
            default:
                System.out.println("未知指令: " + action);
        }
    }
}

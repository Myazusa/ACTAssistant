package github.kutouzi.actassistant.network;

import org.json.JSONObject;

import github.kutouzi.actassistant.command.Command;

public class CommandJsonNIO {
    // TODO: 实现命令的网络交互
    public static Command parseCommand(String commandString) {
        try {
            JSONObject jsonObject = new JSONObject(commandString);
            String action = jsonObject.getString("action");
            JSONObject parameters = jsonObject.getJSONObject("parameters");
            return new Command(action, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package github.kutouzi.actassistant.io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import github.kutouzi.actassistant.config.AutoSettingDefaultConfig;
import github.kutouzi.actassistant.config.DataDefaultConfig;
import github.kutouzi.actassistant.config.FloatWindowDefaultConfig;
import github.kutouzi.actassistant.config.HelpDefaultConfig;
import github.kutouzi.actassistant.config.ServerDefaultConfig;
import github.kutouzi.actassistant.entity.AutoSettingData;
import github.kutouzi.actassistant.entity.HelpData;
import github.kutouzi.actassistant.entity.RemoteIpaddressData;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.LocalServerIPData;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.entity.SwitchApplicationData;
import github.kutouzi.actassistant.entity.TimedTaskData;
import github.kutouzi.actassistant.entity.inf.IData;

public class JsonFileIO extends FileIO {
    private static final String _TAG = JsonFileIO.class.getName();
    public static final Gson _gson = new Gson();

    public static void writeJson(Context context, String jsonFileName, IData data){
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultJson(context,jsonFileName, data.getClass());
        }
        String jsonString = _gson.toJson(data);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static <T> IData readJson(Context context, String jsonFileName, Class<T> dataType){
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultJson(context,jsonFileName,dataType);
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(jsonFileName))){
            Object object = _gson.fromJson(inputStreamReader, dataType);
            if(object instanceof IData){
                return (IData) object;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    private static <T> void writeDefaultJson(Context context, String jsonFileName, Class<T> dataType) {
        String jsonString = null;

        if(dataType == SwipeUpData.class){
            SwipeUpData swipeUpData = new SwipeUpData(FloatWindowDefaultConfig.defaultRandomMaxSwipeupValue, FloatWindowDefaultConfig.defaultRandomMinSwipeupValue,
                    FloatWindowDefaultConfig.defaultRandomMaxDelayValue, FloatWindowDefaultConfig.defaultRandomMinDelayValue);
            jsonString = _gson.toJson(swipeUpData);
        }else if (dataType == KeyWordData.class){
            KeyWordData keyWordData = new KeyWordData(DataDefaultConfig.defaultPingduoduoClickableKeyWordList, DataDefaultConfig.defaultMeituanClickableKeyWordList,
                    DataDefaultConfig.defaultPingduoduoCancelableKeyWordList, DataDefaultConfig.defaultMeituanCancelableKeyWordList);
            jsonString = _gson.toJson(keyWordData);
        }else if (dataType == SwitchApplicationData.class){
            SwitchApplicationData switchApplicationData = new SwitchApplicationData(FloatWindowDefaultConfig.defaultSwitchApplicationTime);
            jsonString = _gson.toJson(switchApplicationData);
        }else if(dataType == AutoSettingData.class){
            AutoSettingData autoSettingData = new AutoSettingData(AutoSettingDefaultConfig.autoScanApplicationButtonDefaultState);
            jsonString = _gson.toJson(autoSettingData);
        }else if(dataType == HelpData.class){
            HelpData helpData = new HelpData(HelpDefaultConfig.autoCheckUpdateSwitchDefaultState);
            jsonString = _gson.toJson(helpData);
        }else if(dataType == TimedTaskData.class){
            TimedTaskData helpData = new TimedTaskData(FloatWindowDefaultConfig.defaultTimedTaskDelayValue);
            jsonString = _gson.toJson(helpData);
        }else if(dataType == RemoteIpaddressData.class){
            RemoteIpaddressData remoteIpaddressData = new RemoteIpaddressData(ServerDefaultConfig.defaultLocalServerIPAddress, ServerDefaultConfig.defaultLocalServerPort);
            jsonString = _gson.toJson(remoteIpaddressData);
        }
        if(jsonString != null){
            try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
                fileOutputStream.write(jsonString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static File getLocalJsonFile(Context context,String jsonFileName){
        File[] jsonFiles = getInternalStorageFiles(context);
        for (File file : jsonFiles) {
            if (file.isFile()) {
                if (file.getName().contains(jsonFileName)){
                    return file;
                }
            }
        }
        return null;
    }
}

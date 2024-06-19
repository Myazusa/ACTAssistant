package github.kutouzi.actassistant.io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Objects;

import github.kutouzi.actassistant.config.AutoSettingDefaultConfig;
import github.kutouzi.actassistant.config.DataDefaultConfig;
import github.kutouzi.actassistant.entity.AutoSettingData;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.entity.SwitchApplicationData;
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
        if(dataType == SwipeUpData.class){
            SwipeUpData swipeUpData = new SwipeUpData(DataDefaultConfig.defaultRandomMaxSwipeupValue, DataDefaultConfig.defaultRandomMinSwipeupValue,
                    DataDefaultConfig.defaultRandomMaxDelayValue, DataDefaultConfig.defaultRandomMinDelayValue);
            String jsonString = _gson.toJson(swipeUpData);
            try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
                fileOutputStream.write(jsonString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (dataType == KeyWordData.class){
            KeyWordData keyWordData = new KeyWordData(DataDefaultConfig.defaultPingduoduoClickableKeyWordList, DataDefaultConfig.defaultMeituanClickableKeyWordList,
                    DataDefaultConfig.defaultPingduoduoCancelableKeyWordList, DataDefaultConfig.defaultMeituanCancelableKeyWordList);
            String jsonString = _gson.toJson(keyWordData);
            try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
                fileOutputStream.write(jsonString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (dataType == SwitchApplicationData.class){
            SwitchApplicationData switchApplicationData = new SwitchApplicationData(DataDefaultConfig.defaultSwitchApplicationTime);
            String jsonString = _gson.toJson(switchApplicationData);
            try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
                fileOutputStream.write(jsonString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(dataType == AutoSettingData.class){
            AutoSettingData autoSettingData = new AutoSettingData(AutoSettingDefaultConfig.autoScanApplicationButtonDefaultState);
            String jsonString = _gson.toJson(autoSettingData);
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

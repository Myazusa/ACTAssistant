package github.kutouzi.actassistant.io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import github.kutouzi.actassistant.config.ConfigDefaultData;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.SwipeUpData;

public class JsonFileIO extends FileIO {
    private static final String _TAG = JsonFileIO.class.getName();
    private static final Gson _gson = new Gson();
    public static boolean writeSwipeUpDataJson(Context context, String jsonFileName, SwipeUpData swipeUpData) {
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultSwipeUpDataJson(context,jsonFileName);
        }
        String jsonString = _gson.toJson(swipeUpData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean writeDefaultSwipeUpDataJson(Context context, String jsonFileName){
        SwipeUpData swipeUpData = new SwipeUpData(ConfigDefaultData.defaultRandomMaxSwipeupValue, ConfigDefaultData.defaultRandomMinSwipeupValue,
                ConfigDefaultData.defaultRandomMaxDelayValue, ConfigDefaultData.defaultRandomMinDelayValue);
        String jsonString = _gson.toJson(swipeUpData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static SwipeUpData readSwipeUpDataJson(Context context, String jsonFileName){
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultSwipeUpDataJson(context,jsonFileName);
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(jsonFileName))){
            return _gson.fromJson(inputStreamReader, SwipeUpData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static KeyWordData readKeyWordDataJson(Context context,String jsonFileName){
        try (InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(jsonFileName))){
            return _gson.fromJson(inputStreamReader, KeyWordData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeKeyWordDataJson(Context context, String jsonFileName, KeyWordData keyWordData) {
        String jsonString = _gson.toJson(keyWordData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

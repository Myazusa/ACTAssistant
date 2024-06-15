package github.kutouzi.actassistant.io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import github.kutouzi.actassistant.config.DataDefaultConfig;
import github.kutouzi.actassistant.entity.LocalServerIPData;

public class LocalServerIPFileIO extends FileIO{
    private static final String _TAG = LocalServerIPFileIO.class.getName();
    public static final Gson _gson = new Gson();

    public static boolean writeLocalServerIPDataJson(Context context, String jsonFileName, LocalServerIPData localServerIPData) {
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultLocalServerIPDataJson(context,jsonFileName);
        }
        String jsonString = _gson.toJson(localServerIPData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static boolean writeDefaultLocalServerIPDataJson(Context context,String jsonFileName){
        LocalServerIPData localServerIPData = new LocalServerIPData(DataDefaultConfig.defaultLocalServerIPAddress, DataDefaultConfig.defaultLocalServerPort);
        String jsonString = _gson.toJson(localServerIPData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static LocalServerIPData readLocalServerIPDataJson(Context context, String jsonFileName){
        if (!isFileExists(context,jsonFileName)){
            // 如果文件不存在就先写入默认值
            writeDefaultLocalServerIPDataJson(context,jsonFileName);
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(jsonFileName))){
            return _gson.fromJson(inputStreamReader, LocalServerIPData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

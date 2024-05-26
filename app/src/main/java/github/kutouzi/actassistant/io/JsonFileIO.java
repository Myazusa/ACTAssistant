package github.kutouzi.actassistant.io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.util.ActionUtil;

public class JsonFileIO extends FileIO {
    private static final String _TAG = JsonFileIO.class.getName();
    private static final Gson _gson = new Gson();
    public static boolean writeJson(Context context, String jsonFileName, SwipeUpData swipeUpData) {
        String jsonString = _gson.toJson(swipeUpData);
        try (FileOutputStream fileOutputStream = context.openFileOutput(jsonFileName, Context.MODE_PRIVATE)) {
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static SwipeUpData readJson(Context context, String jsonFileName){
        try (InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(jsonFileName))){
            return _gson.fromJson(inputStreamReader, SwipeUpData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

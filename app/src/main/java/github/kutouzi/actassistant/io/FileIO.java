package github.kutouzi.actassistant.io;

import android.content.Context;

import java.io.File;
import java.nio.file.Files;

abstract class  FileIO {
    public static File[] getInternalStorageFiles(Context context){
        return context.getFilesDir().listFiles();
    }
    public static boolean isFileExists(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }
}

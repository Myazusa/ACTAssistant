package github.kutouzi.actassistant.io;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

abstract class  FileIO {
    public static File[] getInternalStorageFiles(Context context){
        return context.getFilesDir().listFiles();
    }
    public static boolean isFileExists(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }
    public static boolean isFileEmpty(File file) {
        if (file.exists() && file.isFile()) {
            return file.length() == 0;
        }
        return true;
    }

    public static String readFileContent(Context context, String fileName) {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(fileName);
            int character;
            while ((character = fis.read()) != -1) {
                sb.append((char) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}

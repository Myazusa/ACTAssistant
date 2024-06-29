package github.kutouzi.actassistant.util;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import github.kutouzi.actassistant.R;

public class AddViewUtil {
    public static CircularProgressIndicator addCircularProgressIndicator(LayoutInflater inflater, ViewGroup rootLayout){
        View progressBar = inflater.inflate(R.layout.circular_progress_view, rootLayout, false);
        rootLayout.addView(progressBar);
        CircularProgressIndicator linearProgressIndicator = progressBar.findViewById(R.id.circularProgressIndicator);
        linearProgressIndicator.setProgress(0);
        return linearProgressIndicator;
    }
    public static void removeCircularProgressIndicator(ViewGroup rootLayout,View circularProgressIndicator){
        if(circularProgressIndicator != null){
            rootLayout.removeView(circularProgressIndicator);
        }
    }
}

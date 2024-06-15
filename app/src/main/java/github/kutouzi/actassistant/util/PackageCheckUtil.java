package github.kutouzi.actassistant.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.service.BaidujisuService;
import github.kutouzi.actassistant.service.DouyinjisuService;
import github.kutouzi.actassistant.service.KuaishoujisuService;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.service.XiaohongshuService;

public class PackageCheckUtil {
    private static final String _TAG = PackageCheckUtil.class.getName();
    private static final List<String> defualtPackageList = Stream.of(BaidujisuService.PACKAGE_NAME,
            DouyinjisuService.PACKAGE_NAME,
            KuaishoujisuService.PACKAGE_NAME,
            MeituanService.PACKAGE_NAME,
            PinduoduoService.PACKAGE_NAME,
            XiaohongshuService.PACKAGE_NAME
    ).collect(Collectors.toList());
    public static List<String> getInstalledPackageList(Context context) {
        List<String> installedPackageList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        for (String packageName : defualtPackageList) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                if (packageInfo != null) {
                    installedPackageList.add(packageName);
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(_TAG,"没有安装："+packageName);
            }
        }
        return installedPackageList;
    }
}

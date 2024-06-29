package github.kutouzi.actassistant.view.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.gsls.gt.GT;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.HelpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.util.AddViewUtil;

public class HelpFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private MaterialButton _checkUpdateButton;
    public MaterialSwitch _autoCheckUpdateSwitch;
    private static final String APP_GITHUB_API_URL = "https://api.github.com/repos/Myazusa/ACTAssistant/releases/latest";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _checkUpdateButton = _layout.findViewById(R.id.checkUpdateButton);
        _checkUpdateButton.setOnClickListener(v -> {
            GT.Thread.getInstance(0).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(APP_GITHUB_API_URL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                        connection.setConnectTimeout(5000);
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String inputLine;
                            StringBuilder response = new StringBuilder();

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                            postResult(response.toString());
                        } else {
                            GT.toast_time("错误，响应码：" + responseCode,5000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        GT.toast_time("远端服务无响应",5000);
                    }
                    postResult(null);
                }
                private void postResult(String result){
                    if (result != null) {
                        try {
                            JSONObject release = new JSONObject(result);
                            String latestVersion = release.getString("tag_name");
                            String currentVersion = getCurrentVersion();

                            if (isNewVersionAvailable(currentVersion, latestVersion)) {
                                GT.toast_time("检测到最新版本",5000);
                                GT.HttpUtil.downloadFile("https://github.com/Myazusa/ACTAssistant/releases/download/1.0.2/ACTAssistant.apk",
                                        GT.FileUtils.getAppDirectory(getContext()) + "/ACTAssistant.apk", new GT.HttpUtil.OnLoadData() {
                                            private CircularProgressIndicator circularProgressIndicator = null;
                                            @Override
                                            public void onDownloadStart(File file) {
                                                super.onDownloadStart(file);
                                                if(circularProgressIndicator == null){
                                                    circularProgressIndicator = AddViewUtil.addCircularProgressIndicator(inflater,_layout.findViewById(R.id.fragmentHelp));
                                                }
                                            }

                                            @Override
                                            public void onDownloading(int progress) {
                                                super.onDownloading(progress);
                                                circularProgressIndicator.setProgress(progress);
                                            }

                                            @Override
                                            public void onDownloadSuccess(File file) {
                                                super.onDownloadSuccess(file);
                                                AddViewUtil.removeCircularProgressIndicator(_layout.findViewById(R.id.fragmentHelp),circularProgressIndicator);
                                                GT.AppIteration.UpdateApp.installNewApk(getActivity(), file.getPath(), "");
                                            }

                                            @Override
                                            public void onDownloadFailed(Exception e) {
                                                super.onDownloadFailed(e);
                                                AddViewUtil.removeCircularProgressIndicator(_layout.findViewById(R.id.fragmentHelp),circularProgressIndicator);
                                                GT.toast_time("下载失败",5000);
                                            }
                                        });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                private boolean isNewVersionAvailable(String currentVersion, String latestVersion) {
                    String[] currentParts = currentVersion.split("\\.");
                    String[] latestParts = latestVersion.split("\\.");

                    for (int i = 0; i < Math.min(currentParts.length, latestParts.length); i++) {
                        int currentPart = Integer.parseInt(currentParts[i]);
                        int latestPart = Integer.parseInt(latestParts[i]);
                        if (latestPart > currentPart) {
                            return true;
                        } else if (latestPart < currentPart) {
                            return false;
                        }else {
                            GT.toast_time("版本已为最新",5000);
                        }
                    }
                    return latestParts.length > currentParts.length;
                }
                private String getCurrentVersion() {
                    try {
                        PackageManager pm = requireContext().getPackageManager();
                        PackageInfo pInfo = pm.getPackageInfo(requireContext().getPackageName(), 0);
                        return pInfo.versionName;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        });
        _autoCheckUpdateSwitch = _layout.findViewById(R.id.autoCheckUpdateSwitch);
        HelpData helpData = (HelpData) JsonFileIO.readJson(getContext(), JsonFileDefinition.HELP_JSON_NAME, HelpData.class);
        _autoCheckUpdateSwitch.setChecked(helpData.getAutoCheckUpdateSwitchState());
        _autoCheckUpdateSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if(isChecked){
                _autoCheckUpdateSwitch.setTextColor( getResources().getColor(R.color.my_app_accent));
                JsonFileIO.writeJson(getContext(), JsonFileDefinition.HELP_JSON_NAME, new HelpData(true));
            }else {
                _autoCheckUpdateSwitch.setTextColor(getResources().getColor(R.color.my_app_on_primary));
                JsonFileIO.writeJson(getContext(), JsonFileDefinition.HELP_JSON_NAME, new HelpData(false));
            }
        });
        return _layout;
    }


}

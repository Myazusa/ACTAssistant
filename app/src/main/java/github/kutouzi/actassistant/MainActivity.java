package github.kutouzi.actassistant;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gsls.gt.GT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.adapter.ClientViewAdapter;
import github.kutouzi.actassistant.adapter.KeyWordJsonSpinnerAdapter;
import github.kutouzi.actassistant.adapter.KeyWordViewAdapter;
import github.kutouzi.actassistant.databinding.ActivityMainBinding;
import github.kutouzi.actassistant.entity.ClientViewData;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.enums.KeyWordListDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.service.ACTFloatingWindowService;

public class MainActivity extends AppCompatActivity  {

    static {
        System.loadLibrary("actassistant");
    }

    public static final String CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE = "github.kutouzi.actassistant.CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE";

    private static final String _TAG = MainActivity.class.getName();
    private ActivityMainBinding _binding;

    // 除服务开关外，其他所有按钮的状态，false表示全部禁用
    private boolean _allButtonInWindowStartedState = true;

    private ImageButton _settingButton;
    private ImageButton _addClientButton;
    private ImageButton _startACTFloatingWindowServiceButton;
    private RecyclerView _clientRecyclerView;
    private ScrollView _optionView;

    private final int _recyclerViewSpanCount = 3;
    private boolean _isStartACTFloatingWindowServiceButtonPressed = false;
    public static String listName = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        createClientView();

        createSettingSwitch();
        createAddClientSwitch();
        createStartFloatingServiceWindowSwitch();
        createOptionView();

    }

    private void createClientView(){
        // 寻找clientRecyclerView的xml资源
        _clientRecyclerView = findViewById(R.id.clientView);

        // 设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(this, _recyclerViewSpanCount, GridLayoutManager.VERTICAL, false);
        _clientRecyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        List<ClientViewData> clientViewData = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            clientViewData.add(new ClientViewData("192.168.1." + i, R.drawable.connection_lost));
        }

        // 创建adapter，准备为视图提供数据
        ClientViewAdapter adapter = new ClientViewAdapter(clientViewData);

        // 通过adapter把数据集绑定到clientRecyclerView以显示
        _clientRecyclerView.setAdapter(adapter);
    }

    private void createStartFloatingServiceWindowSwitch(){
        // 创建开启ACT悬浮窗的开关按钮
        _startACTFloatingWindowServiceButton = findViewById(R.id.serviceWindowButton);

        // 根据按钮状态打开或移除悬浮窗
        _startACTFloatingWindowServiceButton.setOnClickListener(v -> {
            // 判断用户有无悬浮窗权限
            if (!Settings.canDrawOverlays(v.getContext())) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                Log.i(_TAG,"用户没有悬浮窗权限");
            }else {
                Log.i(_TAG, "用户已获取悬浮窗权限");
                // 判断此应用有没有在设置里获得无障碍模式权限
                AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
                List<AccessibilityServiceInfo> enabledAccessibilityServiceList = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
                boolean isAccessibilityEnabled = isAccessibilityServiceEnabled(getPackageName(), enabledAccessibilityServiceList);
                if (!isAccessibilityEnabled) {
                    // 权限未开启，引导用户去开启
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    Log.i(_TAG, "用户没有无障碍权限");
                } else {
                    Log.i(_TAG, "用户已获取无障碍权限");
                    if (_isStartACTFloatingWindowServiceButtonPressed) {
                        // 向服务请求销毁悬浮窗
                        _isStartACTFloatingWindowServiceButtonPressed = false;
                    } else {
                        // 向服务请求开启悬浮窗
                        startService(new Intent(this, ACTFloatingWindowService.class));
                        requestCreateACTFloatingWindow();
                        // 隐藏此activity
                        moveTaskToBack(true);
                        _isStartACTFloatingWindowServiceButtonPressed = true;
                    }
                    switchOtherButtonStates();
                }
            }
        });
    }

    private void switchOtherButtonStates(){
        if(!_allButtonInWindowStartedState){
            _settingButton.setEnabled(true);
            _addClientButton.setEnabled(true);
            _allButtonInWindowStartedState = true;
            Log.i(_TAG,"主菜单其他按钮已启用");
        }else {
            _settingButton.setEnabled(false);
            _addClientButton.setEnabled(false);
            _allButtonInWindowStartedState = false;
            Log.i(_TAG,"主菜单其他按钮已禁用");
        }

    }
    private void createSettingSwitch(){
        _settingButton = findViewById(R.id.settingButton);
        _settingButton.setOnClickListener(v->{
            if(_optionView.getVisibility() == View.GONE){
                _optionView.setVisibility(View.VISIBLE);
            }
            else {
                _optionView.setVisibility(View.GONE);
            }
        });
    }

    private void createOptionView(){
        _optionView = findViewById(R.id.optionLayout);
        EditText maxSwipeupTimeEditText = findViewById(R.id.maxSwipeupTimeEditText);
        EditText minSwipeupTimeEditText = findViewById(R.id.minSwipeupTimeEditText);
        EditText maxDelayTimeEditText = findViewById(R.id.maxDelayTimeEditText);
        EditText minDelayTimeEditText = findViewById(R.id.minDelayTimeEditText);
        SwipeUpData swipeUpData = JsonFileIO.readSwipeUpDataJson(this,JsonFileDefinition.SWIPEUP_JSON_NAME);
        Optional.ofNullable(swipeUpData).ifPresent(s -> {
            maxSwipeupTimeEditText.setText(String.valueOf(s.getRandomMaxSwipeupValue()));
            minSwipeupTimeEditText.setText(String.valueOf(s.getRandomMinSwipeupValue()));
            maxDelayTimeEditText.setText(String.valueOf(s.getRandomMaxDelayValue()));
            minDelayTimeEditText.setText(String.valueOf(s.getRandomMinDelayValue()));
        });
        maxSwipeupTimeEditText.setOnFocusChangeListener((v,hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(maxSwipeupTimeEditText.getText().toString()) >= s.getRandomMinSwipeupValue()){
                        // 检测是否大于等于当前最小值
                        s.setRandomMaxSwipeupValue(Integer.parseInt(maxSwipeupTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(this,JsonFileDefinition.SWIPEUP_JSON_NAME,s);
                    }else {
                        // 小于的话就设置回去
                        s.setRandomMaxSwipeupValue(s.getRandomMaxSwipeupValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        minSwipeupTimeEditText.setOnFocusChangeListener((v,hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(minSwipeupTimeEditText.getText().toString()) <= s.getRandomMaxSwipeupValue()){
                        s.setRandomMinSwipeupValue(Integer.parseInt(minSwipeupTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(this,JsonFileDefinition.SWIPEUP_JSON_NAME,s);
                    }else {
                        s.setRandomMinSwipeupValue(s.getRandomMinSwipeupValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        maxDelayTimeEditText.setOnFocusChangeListener((v,hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(maxDelayTimeEditText.getText().toString()) >= s.getRandomMinDelayValue()) {
                        s.setRandomMaxDelayValue(Integer.parseInt(maxDelayTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(this, JsonFileDefinition.SWIPEUP_JSON_NAME, s);
                    }else {
                        s.setRandomMaxDelayValue(s.getRandomMaxDelayValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        minDelayTimeEditText.setOnFocusChangeListener((v,hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(minDelayTimeEditText.getText().toString()) <= s.getRandomMaxDelayValue()) {
                        s.setRandomMinDelayValue(Integer.parseInt(minDelayTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(this, JsonFileDefinition.SWIPEUP_JSON_NAME, s);
                    }else {
                        s.setRandomMinDelayValue(s.getRandomMinDelayValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });

        //KeyWordData keyWordData = JsonFileIO.readKeyWordDataJson(this,JsonFileDefinition.KEYWORD_JSON_NAME);
        Spinner keyWordJsonSpinner = findViewById(R.id.keyWordJsonSpinner);
        SpinnerAdapter spinnerAdapter = new KeyWordJsonSpinnerAdapter(this, Stream.of(KeyWordListDefinition.PINGDUODUO_CLICKABLE_KEYWORD_LIST,
                KeyWordListDefinition.PINGDUODUO_CANCELABLE_KEYWORD_LIST,KeyWordListDefinition.MEITUAN_CLICKABLE_KEYWORD_LIST,KeyWordListDefinition.MEITUAN_CANCELABLE_KEYWORD_LIST).collect(Collectors.toList()));
        keyWordJsonSpinner.setAdapter(spinnerAdapter);
        keyWordJsonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listName = (String) parent.getItemAtPosition(position);
                if (listName.equals(KeyWordListDefinition.PINGDUODUO_CLICKABLE_KEYWORD_LIST)) {

                }else if(listName.equals(KeyWordListDefinition.PINGDUODUO_CANCELABLE_KEYWORD_LIST)){
                    RecyclerView keyWordRecyclerView = findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(JsonFileIO.readKeyWordDataJson(getApplication(), JsonFileDefinition.KEYWORD_JSON_NAME)).getPingduoduoCancelableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }else if(listName.equals(KeyWordListDefinition.MEITUAN_CLICKABLE_KEYWORD_LIST)){

                }else if(listName.equals(KeyWordListDefinition.MEITUAN_CANCELABLE_KEYWORD_LIST)){
                    RecyclerView keyWordRecyclerView = findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(JsonFileIO.readKeyWordDataJson(getApplication(), JsonFileDefinition.KEYWORD_JSON_NAME)).getMeituanCancelableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _optionView.setVisibility(View.GONE);

    }
    private void createAddKeyWordItem(KeyWordViewAdapter keyWordViewAdapter){
        ImageButton addKeyWordButton = findViewById(R.id.addKeyWordButton);
        EditText addKeyWordEditText = findViewById(R.id.addKeyWordEditText);
        addKeyWordButton.setOnClickListener(v -> {
            String s = addKeyWordEditText.getText().toString();
            if (!s.isEmpty()){
                keyWordViewAdapter.addItem(s,v);
            }else {
                GT.toast_time("未输入任何东西",3000);
            }
        });
    }

    private void createAddClientSwitch(){
        _addClientButton = findViewById(R.id.addClientButton);
    }
    private void requestCreateACTFloatingWindow() {
        sendBroadcast(new Intent(CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE).putExtra("key","Create"));
    }

    private boolean isAccessibilityServiceEnabled(String serviceName, List<AccessibilityServiceInfo> enabledAccessibilityServiceList) {
        for (AccessibilityServiceInfo info : enabledAccessibilityServiceList) {
            if (info.getResolveInfo().serviceInfo.packageName.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
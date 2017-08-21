package com.project.equipmanagement.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.project.equipmanagement.R;
import com.project.equipmanagement.bean.InfoCount;
import com.project.equipmanagement.bean.MobUserInfo;
import com.project.equipmanagement.constant.Constants;
import com.project.equipmanagement.http.MobApi;
import com.project.equipmanagement.http.MyCallBack;
import com.project.equipmanagement.ui.view.MainItemView;
import com.project.equipmanagement.ui.view.TopBarView;
import com.project.equipmanagement.ui.view.library.zxing.android.CaptureActivity;
import com.project.equipmanagement.utils.DialogUtils;
import com.project.equipmanagement.utils.UserUtils;
import com.project.equipmanagement.utils.Utils;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements TopBarView.onTitleBarClickListener{

    @Bind(R.id.top_bar_main)
    TopBarView topBarMain;
    @Bind(R.id.main_my_task)
    MainItemView mainMyTask;
    @Bind(R.id.main_launch_process)
    MainItemView mainLaunchProcess;
    @Bind(R.id.main_process_launched)
    MainItemView mainProcessLaunched;
    @Bind(R.id.main_my_approval)
    MainItemView mainMyApproval;
    @Bind(R.id.main_check_device)
    MainItemView mainCheckDevice;

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        topBarMain.setClickListener(this);

        MobUserInfo mobUserInfo = UserUtils.getUserCache();
        topBarMain.setTitle(mobUserInfo.getUserName());
        topBarMain.setRightImage(ContextCompat.getDrawable(this, R.drawable.icon_logout));
    }

    @OnClick({R.id.main_my_task, R.id.main_launch_process, R.id.main_process_launched, R.id.main_my_approval, R.id.main_check_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_my_task:
                loadWebpage(Constants.FlagMyTask, "flowHistory/myTasking?isFinished=1&");
                break;
            case R.id.main_my_approval:
                loadWebpage(Constants.FlagMyApproval, "flowHistory/myCurrentFlow?isFinished=1&");
                break;
            case R.id.main_process_launched:
                loadWebpage(Constants.FlagProcessLaunched, "flowHistory/myInitiatorFlow?isFinished=1&");
                break;
            case R.id.main_launch_process:
                loadWebpage(Constants.FlagLaunchProcess, "flowBase/user-list?");
                break;
            case R.id.main_check_device:
                loadWebpage(Constants.FlagCheckDevice, "equip/myList?");
            default:
                break;
        }
    }

    private void loadWebpage(String titleContent, String partUrl) {
        String userName = UserUtils.getUserCache().getUserName();
        String url = Constants.BASEURL + partUrl + "user_name=" + userName + "&" + "user_msg=" + getUserMsg();
        Log.e("URL", url);
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.INTENT_TITLE_WEB_PAGE, titleContent);
        intent.putExtra(Constants.INTENT_URL_WEB_PAGE, url);
        startActivity(intent);
    }

    @Override
    public void onBackClick() {
        //开启扫一扫
        Intent intent = new Intent(MainActivity.this,
                CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRightClick() {
        if (isLogin()) {
            //退出登录
            DialogUtils.showMyDialog(mContext, "退出提示", "确定要退出当前用户吗?", "退出", "取消", new DialogUtils.OnDialogClickListener() {
                @Override
                public void onConfirm() {
                    quitLogin();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onCloseClick() {

    }

    //退出登录
    private void quitLogin() {
        UserUtils.quitLogin();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFlowCount();
    }

    private void getFlowCount() {
        MobUserInfo mobUserInfo = UserUtils.getUserCache();
        String username = mobUserInfo.getUserName();
        String userMsg = getUserMsg();

        MobApi.getFlowCount(username, userMsg, Constants.CONSTANT_GET_PROCESS_COUNT, new MyCallBack() {
            @Override
            public void onSuccess(int what, Object result) {
                if (result != null) {
                    InfoCount infoCount = (InfoCount) result;
                    mainMyApproval.setContentText(infoCount.getMyInitiatorFlow() + "条");
                    mainMyTask.setContentText(infoCount.getMyTasking() + "条");
                    mainProcessLaunched.setContentText(infoCount.getMyCurrentFlow() + "条");
                }
            }

            @Override
            public void onSuccessList(int what, List results) {

            }

            @Override
            public void onFail(int what, String result) {
                mainMyApproval.setContentText("0条");
                mainMyTask.setContentText("0条");
                mainProcessLaunched.setContentText("0条");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Log.e(MainActivity.class.getName(), content);
                String decodeResult = "";
                Map<String, Object> map = null;
                try {
                    decodeResult = new String(Base64.decode(content, 0x005));
                    map = Utils.getMapForJson(decodeResult);
                } catch (Exception e) {
                    Toast.makeText(this, "请扫描编号二维码", Toast.LENGTH_SHORT).show();
                    Log.e(MainActivity.class.getName(), e.toString());
                }
                Log.e(MainActivity.class.getName(), content + "==============" + decodeResult);

                if (null != map && map.containsKey("id") && map.containsKey("equipNo")) {
                    String url = Constants.BASEURL + "equip/form?" + "id=" + map.get("id") + "&" + "equipNo＝" + map.get("equipNo") + "&" + "user_msg=" + getUserMsg();
                    Log.e("URL", url);
                    Intent intent = new Intent(this, WebActivity.class);
                    intent.putExtra(Constants.INTENT_TITLE_WEB_PAGE, "设备详情");
                    intent.putExtra(Constants.INTENT_URL_WEB_PAGE, url);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请扫描编号二维码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

package com.project.equipmanagement.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.project.equipmanagement.R;
import com.project.equipmanagement.bean.MobUserInfo;
import com.project.equipmanagement.constant.Constants;
import com.project.equipmanagement.http.MobApi;
import com.project.equipmanagement.http.MyCallBack;
import com.project.equipmanagement.ui.view.TopBarView;
import com.project.equipmanagement.utils.KeyboardUtils;
import com.project.equipmanagement.utils.MySnackbar;
import com.project.equipmanagement.utils.UserUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.top_bar_login)
    TopBarView topBarLogin;
    @Bind(R.id.et_user_name)
    EditText etUserName;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        etUserName.setText(sharedPreferences.getString("userName", ""));
        etPassword.setText(sharedPreferences.getString("password",""));

        topBarLogin.setTitle("登录");
    }

    @OnClick(R.id.btn_login)
    public void Loging() {

        //隐藏输入法
        KeyboardUtils.hideSoftInput(this);

        //获取数据
        final String userName = etUserName.getText().toString();
        final String userPsd = md5(etPassword.getText().toString());


        //判空
        if (TextUtils.isEmpty(userName)) {
            MySnackbar.makeSnackBarRed(topBarLogin, "用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(userPsd)) {
            MySnackbar.makeSnackBarRed(topBarLogin, "密码不能为空");
            return;
        }

        showProgressDialog("正在登录...");
        MobApi.userLogin(userName, userPsd, Constants.CONSTANT_LOGIN, new MyCallBack() {
            @Override
            public void onSuccess(int what, Object result) {
                dissmissProgressDialog();
                showProgressSuccess("登录成功!");
                MobUserInfo userInfo = (MobUserInfo) result;
                userInfo.setUserName(userName);
                userInfo.setUserPsd(userPsd);
                Log.e("TEST",userName+"========"+userPsd+"------"+userInfo.toString());

                //保存用户信息
                UserUtils.saveUserCache(userInfo);

                //关闭当前页面。
                closeActivity();
            }

            @Override
            public void onSuccessList(int what, List results) {

            }

            @Override
            public void onFail(int what, String result) {
                dissmissProgressDialog();
                MySnackbar.makeSnackBarRed(topBarLogin, result);
            }
        });
    }

    private void closeActivity(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", etUserName.getText().toString());
        editor.putString("password", etPassword.getText().toString());
        editor.commit();

        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
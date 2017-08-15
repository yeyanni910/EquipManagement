package com.project.equipmanagement.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.equipmanagement.R;
import com.project.equipmanagement.app.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.tv_app_version)
    TextView tvAppVersion;
    @Bind(R.id.shade_bg)
    TextView shadeBg;
    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin()){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, 2000);

        tvAppVersion.setText(String.valueOf("V " + MyApplication.getVersionName()));
    }
}

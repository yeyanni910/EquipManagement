package com.project.equipmanagement.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.equipmanagement.R;
import com.project.equipmanagement.ui.view.TopBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class LaunchProcessActivity extends AppCompatActivity implements TopBarView.onTitleBarClickListener{

    @Bind(R.id.top_bar_launch_process)
    TopBarView topBarLaunchProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_process);
        ButterKnife.bind(this);

        topBarLaunchProcess.setTitle("发起流程");
        topBarLaunchProcess.setClickListener(this);
    }

    @Override
    public void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onCloseClick() {

    }
}

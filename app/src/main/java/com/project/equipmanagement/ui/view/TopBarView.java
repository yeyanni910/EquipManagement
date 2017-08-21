package com.project.equipmanagement.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.equipmanagement.R;

/**
 * Created by AnnieYe on 17/8/2 18:27.
 * email:15191755477@163.com
 */
public class TopBarView extends RelativeLayout implements View.OnClickListener {

    private Button btnClose;
    private ImageView backView;
    private ImageView rightView;
    private TextView titleView;


    private Drawable leftImage;
    private Drawable rightImage;
    private boolean isShow;
    private int leftResourceId;

    private onTitleBarClickListener onMyClickListener;

    public TopBarView(Context context) {
        this(context, null);
    }


    public TopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.AppTheme);


    }

    public TopBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getConfig(context, attrs);
        initView(context);
    }

    /**
     * 从xml中获取配置信息
     */
    private void getConfig(Context context, AttributeSet attrs) {
        //TypedArray是一个数组容器用于存放属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBarView);

        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.TopBarView_leftBtn:
                    leftImage = ta.getDrawable(R.styleable.TopBarView_leftBtn);
                    break;
                case R.styleable.TopBarView_leftBtnResource:
                    leftResourceId = ta.getResourceId(R.styleable.TopBarView_leftBtnResource,0);
                    break;
                case R.styleable.TopBarView_rightBtn:
                    rightImage = ta.getDrawable(R.styleable.TopBarView_rightBtn);
                    break;
                case R.styleable.TopBarView_closeBtnShow:
                    isShow = ta.getBoolean(R.styleable.TopBarView_closeBtnShow,false);
                    break;
            }
        }
        //用完务必回收容器
        ta.recycle();
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.layout_topbar_view,
                this, true);

        backView = (ImageView) layout.findViewById(R.id.back_image);
        titleView = (TextView) layout.findViewById(R.id.text_title);
        rightView = (ImageView) layout.findViewById(R.id.right_image);
        btnClose = (Button) layout.findViewById(R.id.btn_close);
        backView.setOnClickListener(this);
        rightView.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        if (null != leftImage)
            backView.setImageDrawable(leftImage);
//            backView.setImageResource(leftResourceId);
        if (null != rightImage)
            rightView.setImageDrawable(rightImage);
        if (0 != leftResourceId){
            backView.setImageResource(leftResourceId);
        }
        if (isShow){
            btnClose.setVisibility(VISIBLE);
        }else {
            btnClose.setVisibility(INVISIBLE);
        }
    }

    public void setTitle(String strTitle) {
        titleView.setText(strTitle);
    }

    public void setRightImage(Drawable rightDrawable) {
        rightView.setImageDrawable(rightDrawable);
    }

    /**
     * 设置按钮点击监听接口
     *
     * @param listener
     */
    public void setClickListener(onTitleBarClickListener listener) {
        this.onMyClickListener = listener;
    }

    /**
     * 导航栏点击监听接口
     */
    public static interface onTitleBarClickListener {
        /**
         * 点击返回按钮回调
         */
        void onBackClick();

        void onRightClick();

        void onCloseClick();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_image:
                if (null != onMyClickListener)
                    onMyClickListener.onBackClick();
                break;
            case R.id.right_image:
                if (null != onMyClickListener)
                    onMyClickListener.onRightClick();
                break;
            case R.id.btn_close:
                if (null != onMyClickListener)
                    onMyClickListener.onCloseClick();
                break;
        }
    }
}

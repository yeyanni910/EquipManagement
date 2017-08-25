package com.project.equipmanagement.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.equipmanagement.R;

/**
 * Created by AnnieYe on 17/8/3 14:42.
 * email:15191755477@163.com
 */
public class MainItemView extends FrameLayout {

    ImageView ivMainItemIcon;
    TextView tvMainItemTitle;
    TextView tvMainItemContent;
    ImageView ivMainItemArrows;

    private Drawable iconImage;
    private Drawable arrowImage;

    private String strTitle;

    private String strContent;


    public MainItemView(@NonNull Context context) {
        this(context,null);
    }

    public MainItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.style.AppTheme);
    }

    public MainItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getConfig(context,attrs);
        initView(context);
    }

    private void getConfig(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainItemView);

        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MainItemView_mainTitleText:
                    strTitle = typedArray.getString(R.styleable.MainItemView_mainTitleText);
                    break;
                case R.styleable.MainItemView_mainLeftImage:
                    iconImage = typedArray.getDrawable(R.styleable.MainItemView_mainLeftImage);
                    break;
                case R.styleable.MainItemView_mainRightImage:
                    arrowImage = typedArray.getDrawable(R.styleable.MainItemView_mainRightImage);
                    break;
                case R.styleable.MainItemView_mainContentText:
                    strContent = typedArray.getString(R.styleable.MainItemView_mainContentText);
                    break;
            }
        }
        //用完回收容器
        typedArray.recycle();
    }

    private void initView(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.main_item_view, this, true);
        tvMainItemTitle = (TextView) layout.findViewById(R.id.tv_main_item_title);
        tvMainItemContent = (TextView) layout.findViewById(R.id.tv_main_item_content);
        ivMainItemIcon = (ImageView) layout.findViewById(R.id.iv_main_item_icon);
        ivMainItemArrows = (ImageView) layout.findViewById(R.id.iv_main_item_arrows);

        if (null != iconImage){
            ivMainItemIcon.setImageDrawable(iconImage);
        }
        if (null != strTitle){
            tvMainItemTitle.setText(strTitle);
        }

        if (null != strContent){
            tvMainItemContent.setText(strContent);
        }

        if (null != arrowImage){
            ivMainItemArrows.setImageDrawable(arrowImage);
        }
    }

    public void setContentText(String contentText){
        if (!contentText.isEmpty()){
            tvMainItemContent.setText(contentText);
            tvMainItemContent.setTextColor(getResources().getColor(R.color.main_color));
        }
    }
}

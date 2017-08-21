package com.project.equipmanagement.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;


/**
 * Created by maning on 16/6/22.
 */
public class DialogUtils {

    public static MaterialDialog showMyDialog(final Context context, String title, String content, String positiveBtnText, String negativeBtnText, final OnDialogClickListener onDialogClickListener) {

        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveBtnText)
                .negativeText(negativeBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onConfirm();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogClickListener != null) {
                            onDialogClickListener.onCancel();
                        }
                    }
                })
                .show();
        materialDialog.setCancelable(false);
        return materialDialog;
    }

    public interface OnDialogClickListener {

        void onConfirm();

        void onCancel();
    }

    public static MaterialDialog showMyListDialog(final Context context, String title, String positiveBtnText,String negativeBntText,List<String> searchResult, final OnDialogListCallback onDialogListCallback){
        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .title(title)
                .items(searchResult)
                .positiveText(positiveBtnText)
                .negativeText(negativeBntText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogListCallback != null){
                            onDialogListCallback.onPritln();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (onDialogListCallback != null){
                            onDialogListCallback.onDisconnect();
                            dialog.dismiss();
                        }
                    }
                })
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        onDialogListCallback.onSelection(dialog,itemView,position,text);
                    }
                })
                .show();
        materialDialog.setCanceledOnTouchOutside(false);
        return materialDialog;
    }

    public interface OnDialogListCallback {

        void onPritln();
        void onDisconnect();
        void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text);
    }

}

package com.project.equipmanagement.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.lvrenyang.io.Label;
import com.lvrenyang.io.Pos;
import com.project.equipmanagement.bean.DeviceInfo;
import com.project.equipmanagement.ui.view.library.zxing.encode.CodeCreator;

import java.util.List;

/**
 * Created by AnnieYe on 17/8/16 17:24.
 * email:15191755477@163.com
 */
public class Prints {

    public static boolean PrintLabel(Context ctx, Label label, int nPrintWidth, int nPrintHeight, List<DeviceInfo> deviceInfos){
        boolean bPrintResult = false;

        int w = nPrintWidth;
        int h = nPrintHeight;

        Pos pos = new Pos();
        pos.Set(label.GetIO());
        pos.POS_S_Align(1);		//居中对齐
        for (DeviceInfo deviceInfo :deviceInfos) {
            label.PageBegin(0, 5, w, h, 0);
            pos.POS_S_TextOut("编号："+ deviceInfo.getEquipNo()+"\n", 0, 0, 0, 0, 0x100);
            String json = "{\"id\":\"" + deviceInfo.getId() + "\",\"equipNo\":" + deviceInfo.getEquipNo()+ "}";
            String strDeviceInfo = Base64.encodeToString(json.getBytes(),0x005);
            Bitmap bitmap = CodeCreator.createQRCode(strDeviceInfo,280,280,null);

            pos.POS_PrintPicture(bitmap,215,0,0);               //205
            pos.POS_S_TextOut("出厂编号："+deviceInfo.getFactoryNo()+"\n",0,0,0,0,0x100);
            label.PageEnd();
            label.PageFeed();
            byte[] status = new byte[4];
            pos.POS_QueryStatus(status, 3000, 2);
            bPrintResult = label.GetIO().IsOpened();
            if (!bPrintResult)
                break;
            }
        bPrintResult = label.GetIO().IsOpened();
        return bPrintResult;
    }

}

package com.project.equipmanagement.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.lvrenyang.io.Label;
import com.lvrenyang.io.Page;
import com.lvrenyang.io.Pos;
import com.project.equipmanagement.bean.DeviceInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by AnnieYe on 17/8/16 17:24.
 * email:15191755477@163.com
 */
public class Prints {

    public static boolean PrintTicket(Context ctx, Page page, int nPrintWidth, int nPrintHeight) {
        boolean bPrintResult = false;

        int w = nPrintWidth;
        int h = nPrintHeight;

        page.PageEnter();

        page.SetPrintArea(0, 0, w, h, Page.DIRECTION_LEFT_TO_RIGHT);
        page.DrawText("打印测试", Page.HORIZONTALALIGNMENT_CENTER, 10, 1, 1, Page.FONTTYPE_STANDARD, Page.FONTSTYLE_BOLD);

        page.PagePrint();
        page.PageExit();

        bPrintResult = page.GetIO().IsOpened();
        return bPrintResult;
    }

    public static boolean PrintLabel(Context ctx, Label label, int nPrintWidth, int nPrintHeight, int nPrintCount, List<Bitmap> bitmaps){
        boolean bPrintResult = false;

        int w = nPrintWidth;
        int h = nPrintHeight;
//        int w = 76*8;
//        int h = nPrintHeight;

//        Bitmap logo = getImageFromAssetsFile(ctx, "Kitty.bmp");

        Pos pos = new Pos();
        pos.Set(label.GetIO());
        pos.POS_S_Align(1);		//居中对齐
        pos.POS_S_TextOut("扫二维码测试\r\n", 0, 0, 0, 0, 0x100);
//        int marginLeft = (w-bitmap.getWidth())/2;
        for (Bitmap bitmap :bitmaps) {
            label.PageBegin(0, 0, w, h, 1);
//			label.DrawBox(0, 0, w-1, h-1, 1, 1);
            label.DrawBitmap(100, 60, bitmap.getWidth(), bitmap.getHeight()-20, 0,bitmap , 0);
//			try {
//				label.DrawPlainText(10, 50, 24, 0, "扫描下方二维码".getBytes("GBK"));
////				label.DrawPlainText(10, 80, 24, 0, "版本：V4.0".getBytes("GBK"));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			label.DrawBarcode(10, 150, 8, 50, 2, 0, "No.14580384".getBytes());
//			label.DrawQRCode(200, 50, 0, 1, 4, 0, "http://baidu.com".getBytes());

            label.PageEnd();
            label.PagePrint(1);
            byte[] status = new byte[4];
            pos.POS_QueryStatus(status, 3000, 2);


            bPrintResult = label.GetIO().IsOpened();
            if (!bPrintResult)
                break;
        }

        bPrintResult = label.GetIO().IsOpened();
        return bPrintResult;
    }

    public static boolean PrintLabel(Context ctx, Label label, int nPrintWidth, int nPrintHeight, List<DeviceInfo> deviceInfos){
        boolean bPrintResult = false;

        int w = nPrintWidth;
        int h = nPrintHeight;
//        int w = 76*8;
//        int h = nPrintHeight;

//        Bitmap logo = getImageFromAssetsFile(ctx, "Kitty.bmp");

        Pos pos = new Pos();
        pos.Set(label.GetIO());
        pos.POS_S_Align(1);		//居中对齐
        for (DeviceInfo deviceInfo :deviceInfos) {
            label.PageBegin(0, 0, w, h, 1);
            pos.POS_S_TextOut("编号："+ deviceInfo.getEquipNo()+"\r\n", 0, 0, 0, 0, 0x100);
            String json = "{\"id\":\"" + deviceInfo.getId() + "\",\"equipNo\":" + deviceInfo.getEquipNo()+ "}";
            String strDeviceInfo = Base64.encodeToString(json.getBytes(),0x005);
            Bitmap bitmap = null;
            try {
                bitmap = getBitmap("http://qr.liantu.com/api.php?text="+strDeviceInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
            label.DrawBitmap(100, 60, bitmap.getWidth(), bitmap.getHeight()-20, 0,bitmap , 0);
            label.PageEnd();
            label.PagePrint(1);
            byte[] status = new byte[4];
            pos.POS_QueryStatus(status, 3000, 2);
            bPrintResult = label.GetIO().IsOpened();
            if (!bPrintResult)
                break;
        }

        bPrintResult = label.GetIO().IsOpened();
        return bPrintResult;
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssetsFile(Context ctx, String fileName) {
        Bitmap image = null;
        AssetManager am = ctx.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;
    }
}

package chairyfish.test.Util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by chairyfish on 2017/2/20.
 */

public class PictureUtil {

    public static Bitmap zoomBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int w = 400;
        int h = 400;
        Log.i("scale", String.valueOf(width));
        Log.i("scale", String.valueOf(height));
        float bitScale = (float) w / h;
        float viewScale = (float) width / height;
        Log.i("scale", String.valueOf(bitScale) + "," + String.valueOf(viewScale));
        float scaleWidth, scaleHeight;
        Matrix matrix = new Matrix();
        if (bitScale > viewScale) {
            scaleWidth = ((float) w / width);
            scaleHeight = ((float) h / width * w / h);
        } else {
            scaleWidth = ((float) w / height * h / w);
            scaleHeight = ((float) h / height);
        }
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    public static String transToBase64(Bitmap bitmap){
        String file="";
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] appicon = baos.toByteArray();// 转为byte数组
            file = Base64.encodeToString(appicon, Base64.DEFAULT);
            return file;
        }else {
            return file;
        }
    }


    /**
     * Created by chairyfish on 2017/2/19.
     */


}

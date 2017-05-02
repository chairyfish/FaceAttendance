package chairyfish.test.Camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;

/**
 * Created by chairyfish on 2017/2/19.
 */

public class DisplayUtil {
    private static final String TAG = "DisplayUtil";
    /**
     * dip转px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi);
        return new Point(w_screen, (int)(w_screen*(float)1.33));

    }

    /**
     * 获取屏幕长宽比
     * @param context
     * @return
     */
    public static float getScreenRate(Context context){
        Point P = getScreenMetrics(context);

        float H = P.y;
        float W = P.x;
        return (H/W);
    }


    public static Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution){
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float)screenResolution.x / (float)screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for(Camera.Size s : supportedPreviewSizes){
            tmp = Math.abs(((float)s.height/(float)s.width)-x_d_y);
            if(tmp<mindiff){
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

}

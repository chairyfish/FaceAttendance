package chairyfish.test.Camera;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by chairyfish on 2017/2/19.
 */

public class ImageUtil {
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}

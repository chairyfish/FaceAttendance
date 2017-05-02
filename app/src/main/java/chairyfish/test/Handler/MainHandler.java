package chairyfish.test.Handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import chairyfish.test.Camera.CameraInterface;
import chairyfish.test.Camera.GoogleFaceDetect;
import chairyfish.test.Util.EventUtil;
import chairyfish.test.Util.PictureUtil;

import static chairyfish.test.Util.EventUtil.PHOTO_HAS_TAKE;


/**
 * Created by chairyfish on 2017/2/20.
 */

public class MainHandler extends Handler {


    @Override
    public void handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case EventUtil.UPDATE_FACE_RECT:
                Log.i("Detecting","检测到了脸");
                GoogleFaceDetect.stopGoogleFaceDetect();
                CameraInterface.getInstance().doTakePicture();
                break;
            case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
                GoogleFaceDetect.startGoogleFaceDetect();
                break;
            case PHOTO_HAS_TAKE:
                Log.i("takephotoResult", "success");
                break;
            case EventUtil.COMPARE_SUCCESS:
                Log.i("CompareAction","success");
                break;
        }
        super.handleMessage(msg);
    }

}

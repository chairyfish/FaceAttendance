package chairyfish.test.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Message;
import android.util.Log;

import chairyfish.test.Handler.MainHandler;
import chairyfish.test.Util.EventUtil;
import chairyfish.test.Util.PictureUtil;

/**
 * Created by chairyfish on 2017/2/20.
 */

public class GoogleFaceDetect implements Camera.FaceDetectionListener {
    private static final String TAG = "FaceDetect";
    private Context mContext;
    private MainHandler mHandler;
    public static GoogleFaceDetect googleFaceDetect;

    public GoogleFaceDetect(Context c, MainHandler handler) {
        mContext = c;
        mHandler = handler;
    }

//    private Handler mHander = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case EventUtil.UPDATE_FACE_RECT:
//                    Log.i("FaceDetect", "success");
//                    break;
//            }
//        }
//
//        ;




    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        // TODO Auto-generated method stub

        Log.i(TAG, "onFaceDetection...");
        if (faces.length!=0) {
//            Message m = mHander.obtainMessage();
//            m.what = EventUtil.UPDATE_FACE_RECT;
//            m.obj = faces;
//            m.sendToTarget();
            Message m=new Message();
            m.what= EventUtil.UPDATE_FACE_RECT;
            mHandler.sendMessage(m);
        }
    }

    public static void startGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
            CameraInterface.getInstance().getCameraDevice().startFaceDetection();
        }
    }

    public static void stopGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();

        }
    }


}


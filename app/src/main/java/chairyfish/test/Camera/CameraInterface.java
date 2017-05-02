package chairyfish.test.Camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Activity.CheckActivity;
import chairyfish.test.Handler.MainHandler;
import chairyfish.test.Util.PictureUtil;
import chairyfish.test.Connect.ServerConnect;

import static chairyfish.test.Util.EventUtil.COMPARE_SUCCESS;
import static chairyfish.test.Util.EventUtil.PHOTO_HAS_TAKE;


/**
 * Created by chairyfish on 2017/2/19.
 */

public class CameraInterface {
    private static final String TAG = "Attd";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private int mCameraId = -1;
    private boolean isGoogleFaceDetectOn = false;
    static Bitmap rotaBitmap;
    private static CameraInterface mCameraInterface;

    String file=null;
    String result=null;

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    private CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开Camera
     *
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback, int cameraId) {
        Log.i(TAG, "Camera open....");
        mCamera = Camera.open(cameraId);
        mCameraId = cameraId;
        if (callback != null) {
            callback.cameraHasOpened();
        }
    }

    /**
     * 开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //设置PreviewSize和PictureSize
            Camera.Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Camera.Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setDisplayOrientation(90);
            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters(); //重新get一次
            Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width
                    + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width
                    + "Height = " + mParams.getPictureSize().height);
        }
    }



    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /**
     * 获取Camera.Parameters
     *
     * @return
     */
    public Camera.Parameters getCameraParams() {
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            return mParams;
        }
        return null;
    }


    /**
     * 获取Camera实例
     *
     * @return
     */
    public Camera getCameraDevice() {
        return mCamera;
    }


    public int getCameraId() {
        return mCameraId;
    }


    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };

    Camera.PictureCallback mRawCallback = new Camera.PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");
        }
    };

    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //保存图片到sdcard
            if (null != b) {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
                rotaBitmap = ImageUtil.getRotateBitmap(b, 270.0f);
                if(rotaBitmap==null){
                    Log.i("doTakePhoto：","照片为空");
                }else {
                    Log.i("doTakePhoto：","照片不为空");

                    Message msg=Message.obtain();
                    CheckActivity.bitmap=PictureUtil.zoomBitmap(rotaBitmap);
                    //发送消息
                    msg.what=PHOTO_HAS_TAKE;
                    handler.sendMessage(msg);
                }
            }
            //再次进入预览
   //         mCamera.startPreview();
   //         isPreviewing = true;
        }
    };

    MainHandler handler=new MainHandler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public  void doCompareFace(){
        System.out.println("doCompare start");
        rotaBitmap= PictureUtil.zoomBitmap(rotaBitmap);
        BitmaptoBase64();
        new Thread(new CompareAction()).run();
    }



    public class CompareAction extends Thread{
        public void run(){
            System.out.println("doComparing");
            List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("file",file));
            try {
                result= ServerConnect.getInstance().getResult("Compare",param);
                Message msg=Message.obtain();
                //发送消息
                msg.what=COMPARE_SUCCESS;
                handler.sendMessage(msg);
            } catch (Exception e) {
                System.out.println("Compare.action"+e.getMessage());
                e.printStackTrace();
            }
        }

    }


    public void BitmaptoBase64() {
        if(rotaBitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
            rotaBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] appicon = baos.toByteArray();// 转为byte数组
            file = Base64.encodeToString(appicon, Base64.DEFAULT);

        }
    }
}

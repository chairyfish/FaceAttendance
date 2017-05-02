package chairyfish.test.Activity;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Camera.CameraInterface;
import chairyfish.test.Camera.CameraSurfaceView;
import chairyfish.test.Camera.DisplayUtil;

import chairyfish.test.Camera.GoogleFaceDetect;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.Dialog.SignDialog;
import chairyfish.test.Handler.MainHandler;
import chairyfish.test.R;
import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Util.EventUtil;
import chairyfish.test.Util.PictureUtil;

import static chairyfish.test.Camera.GoogleFaceDetect.googleFaceDetect;
import static chairyfish.test.Util.EventUtil.SIGN_SUCCESS;


public class CheckActivity extends Activity {
    private static final String TAG = "Attd";
    CameraSurfaceView surfaceView = null;
    Button shutterBtn;
    float previewRate = -1f;
    public static Bitmap bitmap;
    MainHandler mMainHandler;
    SignDialog signdialog;
    private  EditText TeacherName,Code;

    private String a_name,code;

    String result;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SIGN_SUCCESS:
                    if (result == null) {
                        Toast.makeText(CheckActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            JSONObject stateMap = obj.getJSONObject("stateMap");
                            String signState = stateMap.getString("signState");
                            System.out.println("*****************签到结果*****************");
                            System.out.println(signState);
                            if (signState.equals("success")) {
                                JSONArray stateInfo = stateMap.getJSONArray("stateInfo");
                                UserInfo.setCHECKSTATE(stateInfo);

                                //写入缓存
                                SharedPreferences sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("stateMap",stateMap.toString());
                                editor.commit();

                                Intent intentMain = new Intent();
                                intentMain.setClass(CheckActivity.this, ContainerActivity.class);
                                Bundle bundle=new Bundle();
                                intentMain.putExtra("PAGE","0");
                                intentMain.putExtra("bundle",bundle);
                                startActivity(intentMain);
                                finish();
                            } else if (signState.equals("fail")) {
//                                Intent intent=new Intent();
//                                intent.setClass(CheckActivity.this,ContainerActivity.class);
//                                Bundle bundle=new Bundle();
//                                intent.putExtra("PAGE","0");
//                                intent.putExtra("bundle",bundle);
//                                startActivity(intent);
                                Toast.makeText(CheckActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;

            }
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check);
        initUI();

        initViewParams();
        mMainHandler = new MainHandler();
        googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);
        shutterBtn.setOnClickListener(new BtnListeners());
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
        Toast.makeText(this, "检测到人脸后将自动签到，请稳定相机", Toast.LENGTH_SHORT).show();
    }



    private void initUI(){
        surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
        shutterBtn = (Button)findViewById(R.id.btn_shutter);
    }

    private void initViewParams(){
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this);            //设置画面比例
        surfaceView.setLayoutParams(params);
    }

    private class BtnListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_shutter:
                    showdialog();
                    break;
                default:break;
            }
        }

    }



    private void takePicture(){
        CameraInterface.getInstance().doTakePicture();
        Log.i("All Action:","finish");
    }







    private void showdialog(){


        signdialog = new SignDialog(this,R.style.SignDialog);
        signdialog.setCanceledOnTouchOutside(false);

        TeacherName=(EditText)signdialog.getTeacherName();
        Code=(EditText)signdialog.getCheckCode();
        signdialog.setOnYesListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null) {
                    a_name = TeacherName.getText().toString();
                    code = Code.getText().toString();
                    new Thread(new SignAction()).start();
                }
            }
        });

        signdialog.setOnNoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signdialog.dismiss();
            }
        });

        signdialog.show();


//                if(bitmap!=null){
//                    Log.i("signdialog","bitmap is not null");
//                    Log.i("bitmapSize",String.valueOf(bitmap.getByteCount()));
//                    new Thread(new SignAction()).start();
//                    //   signdialog.dismiss();
//                }

    }




    public class SignAction extends Thread{
        public void run(){

                Log.i("password",UserInfo.getPASSWORD());


                String file= PictureUtil.transToBase64(bitmap);
                List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                param.add(new BasicNameValuePair("u_name", UserInfo.getUSERNAME()));
                param.add(new BasicNameValuePair("password",UserInfo.getPASSWORD()));
                param.add(new BasicNameValuePair("a_name", a_name));
                param.add(new BasicNameValuePair("code", code));
                param.add(new BasicNameValuePair("file",file));
                param.add(new BasicNameValuePair("photoFileName", UserInfo.getPASSWORD()+String.valueOf(System.currentTimeMillis()/1000)+".JPG"));
                param.add(new BasicNameValuePair("photoContentType","image/jpeg"));
                try {
                    result= ServerConnect.getInstance().getResult("MSign",param);
                } catch (Exception e) {
                    System.out.println("MSign.action"+e.getMessage());
                    e.printStackTrace();
                }
                Message msg=Message.obtain();
                //发送消息
                msg.what= EventUtil.SIGN_SUCCESS;
                handler.sendMessage(msg);
            }
        }






}
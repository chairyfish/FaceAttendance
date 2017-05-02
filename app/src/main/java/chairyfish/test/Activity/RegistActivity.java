package chairyfish.test.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chairyfish.test.R;
import chairyfish.test.Util.PictureUtil;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.Util.TouchUtil;

import static chairyfish.test.Util.EventUtil.REGIST_SUCCESS;


public class RegistActivity extends AppCompatActivity {

    EditText Newusername,Newpassword;
    TextView Title;
    Button Regist;
    RelativeLayout Notice;
    ImageButton BackToLogin;
    String name,passwd,file;
    String result;
    ImageView Registphoto;
    Drawable NewUsernameDraw,NewPasswordDraw;
    Bitmap mBitmap;


    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;


    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            // TODO Auto-generated method stub
            switch (msg.what){
                case REGIST_SUCCESS:
                    if (result == null) {
                        Toast.makeText(RegistActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(result);
                        if(result.equals("\"success\"")){
                            Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                            Intent loginIntent=new Intent();
                            loginIntent.setClass(RegistActivity.this,LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }else if(result.equals("\"connectfail\"")){
                            Toast.makeText(RegistActivity.this,"发送信息失败",Toast.LENGTH_LONG).show();
                        }else if(result.equals("\"fail\"")){
                            Registphoto.setImageBitmap(null);
                            Toast.makeText(RegistActivity.this,"人脸识别失败,请重新拍摄清晰图片",Toast.LENGTH_LONG).show();
                        }else {
                            Log.i("Result","注册失败:"+result);
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
        setContentView(R.layout.activity_regist);

        init();

    }

    private void init() {
        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("注 册");
        BackToLogin=(ImageButton)findViewById(R.id.top_back);
        Newusername=(EditText)findViewById(R.id.newusername);
        Newpassword=(EditText)findViewById(R.id.newpassword);
        Registphoto=(ImageView)findViewById(R.id.registphoto);
        Notice=(RelativeLayout)findViewById(R.id.notice_takephoto);
        Regist=(Button)findViewById(R.id.regist);
        TouchUtil.expandViewTouchDelegate(Regist,50,50,50,50);
        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RegistActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Newusername.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                NewUsernameDraw = Newusername.getCompoundDrawables()[2];
                if (NewUsernameDraw == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > Newusername.getWidth()
                        - Newusername.getPaddingRight()
                        - NewUsernameDraw.getIntrinsicWidth()) {
                    Newusername.setText("");
                }
                return false;
            }
        });
        Newpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                NewPasswordDraw = Newpassword.getCompoundDrawables()[2];
                if (NewPasswordDraw == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > Newpassword.getWidth()
                        - Newpassword.getPaddingRight()
                        - NewPasswordDraw.getIntrinsicWidth()) {
                    Newpassword.setText("");
                }
                return false;
            }
        });
        Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=Newusername.getText().toString();
                passwd=Newpassword.getText().toString();
                new Thread(new RegistAction()).start();
            }
        });
        Registphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ExistSDCard()) {
                    Toast.makeText(RegistActivity.this,"没有SD卡",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageUri = Uri.fromFile(getTempImage());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Notice.setVisibility(View.GONE);
                    mBitmap = getScaleBitmap(this, getTempImage().getPath());
                    file=PictureUtil.transToBase64(mBitmap);
                    if (null != mBitmap) {
                        Registphoto.setImageBitmap(mBitmap);
                    }
                }break;
        }
    }

    public static File getTempImage() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return tempFile;
        }
        return null;
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > screenWidth)
                opt.inSampleSize = bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = bmpHeght / screenHeight;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        bmp= PictureUtil.zoomBitmap(bmp);
        System.out.println(bmp.getAllocationByteCount());
        return bmp;
    }
    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    public class RegistAction extends Thread{
        public void run(){

            if(name==null||passwd==null|file==null) {
                Toast.makeText(RegistActivity.this,"请将信息补充完整",Toast.LENGTH_SHORT).show();
            }else{
                List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
                param.add(new BasicNameValuePair("name", name));
                param.add(new BasicNameValuePair("password", passwd));
                param.add(new BasicNameValuePair("file",file));
                param.add(new BasicNameValuePair("photoFileName",name+".JPG"));
                param.add(new BasicNameValuePair("photoContentType","image/jpeg"));
                try {
                    result= ServerConnect.getInstance().getResult("MRegist",param);
                } catch (Exception e) {
                    System.out.println("MRegist.action"+e.getMessage());
                    e.printStackTrace();
                }
                Message msg=Message.obtain();
                //发送消息
                msg.what=REGIST_SUCCESS;
                handler.sendMessage(msg);
            }
        }

    }


}

package chairyfish.test.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import chairyfish.test.R;
import chairyfish.test.Bean.UserInfo;

public class SplashActivity extends AppCompatActivity {

    private static final int MODE_UNLOGIN=1;
    private static final int MODE_ISLOGIN=2;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MODE_ISLOGIN:
                    Intent intentMain=new Intent().setClass(SplashActivity.this,ContainerActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("PAGE","0");
                    intentMain.putExtra("bundle", bundle);
                    startActivity(intentMain);
                    finish();
                    break;
                case MODE_UNLOGIN:
                    Intent intentLogin=new Intent().setClass(SplashActivity.this,LoginActivity.class);
                    startActivity(intentLogin);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取缓存的用户登录信息
        new Thread(new SharedThread()).start();
    }


    class SharedThread extends Thread{
        public void run() {
            //启动页2秒
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //读取缓存的用户信息
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            String password = sharedPreferences.getString("password", null);
            Message msg = Message.obtain();
            if (username != null) {
                UserInfo.setUSERNAME(username);
                UserInfo.setPASSWORD(password);
                msg.what = MODE_ISLOGIN;
                handler.sendMessage(msg);
            } else {
                msg.what = MODE_UNLOGIN;
                handler.sendMessage(msg);
            }
        }
    }
}

package chairyfish.test.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Bean.UserInfo;

import static chairyfish.test.Util.EventUtil.LOGIN_SUCCESS;

public class LoginActivity extends AppCompatActivity {

    EditText Username,Password;
    Button Regist,Login;
    Drawable UsernameDraw,PasswordDraw;

    private String username=null,password=null;
    String result=null;


    Handler handler=new Handler(){
        public void  handleMessage(Message msg){
            if(msg.what == LOGIN_SUCCESS){
                if(result==null){
                    Toast.makeText(LoginActivity.this,"请检查网络服务",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JSONObject obj=new JSONObject(result);
                        JSONObject stateMap=obj.getJSONObject("stateMap");
                        String loginState=stateMap.getString("loginState");
                        System.out.println("*****************登录结果*****************");
                        System.out.println(loginState);
                        if(loginState.equals("success")){
                            //写入全局变量
                            UserInfo.setUSERNAME(username);
                            UserInfo.setPASSWORD(password);
                            //写入缓存
                            SharedPreferences sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("username",username);
                            editor.putString("password",password);
                            JSONArray stateInfo=stateMap.getJSONArray("stateInfo");
                            UserInfo.setCHECKSTATE(stateInfo);
                            editor.putString("stateMap",stateMap.toString());
                            Log.i("Login",stateInfo.toString());
                            editor.commit();
                            //跳转
                            Intent intentMain=new Intent();
                            intentMain.setClass(LoginActivity.this,ContainerActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("PAGE","0");
                            intentMain.putExtra("bundle", bundle);
                            startActivity(intentMain);
                            finish();
                        }else if(loginState.equals("fail")) {
                            Username.setText("");
                            Password.setText("");
                            Toast.makeText(LoginActivity.this,"帐号或密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        Login.setOnClickListener(new SimpleListener());
        Regist.setOnClickListener(new SimpleListener());

    }

    private void init() {
        Username=(EditText)findViewById(R.id.username);
        Password=(EditText)findViewById(R.id.password);
        Regist=(Button)findViewById(R.id.regist);
        Login=(Button)findViewById(R.id.login);
        Username.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                UsernameDraw = Username.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (UsernameDraw == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > Username.getWidth()
                        - Username.getPaddingRight()
                        - UsernameDraw.getIntrinsicWidth()){
                    Username.setText("");
                    Password.setText("");
                }
                return false;
            }
        });
        Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PasswordDraw = Password.getCompoundDrawables()[2];
                if (PasswordDraw == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > Password.getWidth()
                        - Password.getPaddingRight()
                        - PasswordDraw.getIntrinsicWidth()) {
                    Password.setText("");
                }
                return false;
            }
        });
    }

    class SimpleListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.regist:
                    Intent intentRegist=new Intent();
                    intentRegist.setClass(LoginActivity.this,RegistActivity.class);
                    startActivity(intentRegist);
                    finish();
                    break;
                case  R.id.login:
                    username=Username.getText().toString();
                    password=Password.getText().toString();
                    new Thread(new LoginAction()).start();
                    break;
                default:
                    break;
            }
        }
    }

    public class LoginAction extends Thread{
        public void run(){
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("username",username));
            param.add(new BasicNameValuePair("password",password));
            try {
                result=ServerConnect.getInstance().getResult("MLogin",param);
            } catch (Exception e) {
                System.out.println("MLogin.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what=LOGIN_SUCCESS;
            handler.sendMessage(msg);
        }

    }

}

package chairyfish.test.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static chairyfish.test.Util.EventUtil.SIGN_SUCCESS;


public class CodeActivity extends AppCompatActivity {


    EditText Checkcode,Teachername;
    Button Checkstate;
    String code,a_name;
    String result;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SIGN_SUCCESS:
                    if (result == null) {
                        Toast.makeText(CodeActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            JSONObject stateMap = obj.getJSONObject("stateMap");
                            String loginState = stateMap.getString("signState");
                            System.out.println("*****************签到结果*****************");
                            System.out.println(loginState);
                            if (loginState.equals("success")) {
                                JSONArray stateInfo = stateMap.getJSONArray("stateInfo");
                                UserInfo.setCHECKSTATE(stateInfo);
                                Intent intentMain = new Intent();
                                intentMain.setClass(CodeActivity.this, MainActivity.class);
                                startActivity(intentMain);
                                finish();
                            } else if (loginState.equals("fail")) {
                                Checkcode.setText("");
                                Teachername.setText("");
                                Toast.makeText(CodeActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_code);

        init();
    }

    private void init() {
        Checkcode=(EditText)findViewById(R.id.checkcode);
        Teachername=(EditText)findViewById(R.id.teachername);
        Checkstate=(Button)findViewById(R.id.checkstate);
        Checkstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=Checkcode.getText().toString();
                a_name=Teachername.getText().toString();
                new Thread(new SignAction()).start();
            }
        });
    }

    class SignAction extends Thread{
        public  void run(){
            List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", UserInfo.getUSERNAME()));
            param.add(new BasicNameValuePair("a_name" ,a_name));
            param.add(new BasicNameValuePair("code",code));
            try {
                result= ServerConnect.getInstance().getResult("Sign",param);
                Message msg=Message.obtain();
                //发送消息
                msg.what=SIGN_SUCCESS;
                handler.sendMessage(msg);
            } catch (Exception e) {
                System.out.println("Compare.action"+e.getMessage());
                e.printStackTrace();
            }
        }
    }


}

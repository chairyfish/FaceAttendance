package chairyfish.test.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Util.TouchUtil;

import static chairyfish.test.Util.EventUtil.JOIN_SUCCESS;
import static chairyfish.test.Util.EventUtil.LOGIN_SUCCESS;


public class JoinActivity extends AppCompatActivity {

    TextView Title;
    ImageButton BackToAttd;
    EditText JoinCode;
    Button Join;

    String code;
    String result;

    Handler handler=new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == JOIN_SUCCESS) {
                if (result == null) {
                    Toast.makeText(JoinActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONObject stateMap = obj.getJSONObject("stateMap");
                        String joinState = stateMap.getString("flag");
                        if(joinState.equals("true")) {
                            Toast.makeText(JoinActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                            Intent intentAttd=new Intent();
                            intentAttd.setClass(JoinActivity.this,ContainerActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("PAGE","0");
                            intentAttd.putExtra("bundle",bundle);
                            startActivity(intentAttd);
                        }else if(joinState.equals("fail")){
                            Toast.makeText(JoinActivity.this, "失败了，检查一下课程码？", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(JoinActivity.this, "失败了，检查一下课程码？", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();

    }

    private void init() {
        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("新增课堂");
        BackToAttd=(ImageButton)findViewById(R.id.top_back);
        JoinCode=(EditText)findViewById(R.id.join_code);
        Join=(Button)findViewById(R.id.btn_join);
        TouchUtil.expandViewTouchDelegate(BackToAttd,50,50,50,50);
        BackToAttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack=new Intent();
                intentBack.setClass(JoinActivity.this,ContainerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("PAGE","0");
                intentBack.putExtra("bundle",bundle);
                startActivity(intentBack);
                finish();
            }
        });
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=JoinCode.getText().toString().trim();
                if(code.length()!=5){
                    Toast.makeText(JoinActivity.this,"请输入正确的课堂码",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new JoinAction()).start();
                }
            }
        });
    }


    public class JoinAction extends Thread{
        public void run(){
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", UserInfo.getUSERNAME()));
            param.add(new BasicNameValuePair("c_code",code));
            try {
                result= ServerConnect.getInstance().getResult("MJoin",param);
            } catch (Exception e) {
                System.out.println("MJoin.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what=JOIN_SUCCESS;
            handler.sendMessage(msg);
        }

    }


}

package chairyfish.test.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Util.EventUtil;
import chairyfish.test.Util.TouchUtil;

import static chairyfish.test.Util.EventUtil.EACH_RECORD_SUCCESS;
import static chairyfish.test.Util.EventUtil.SIGN_SUCCESS;

public class EachDetailActivity extends AppCompatActivity {
    TextView Title;
    ImageButton BackToAttd;
    TextView ShouldTime,ActualTime;


    String courseName,teacherName;
    String result;
    String shouldTime,actualTime;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EACH_RECORD_SUCCESS:
                    if (result == null) {
                        Toast.makeText(EachDetailActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            JSONObject stateMap = obj.getJSONObject("stateMap");
                            String accessState=stateMap.getString("stateInfo");
                            if(accessState.equals("success")){
                                shouldTime=stateMap.getString("should_checked");
                                actualTime=stateMap.getString("has_checked");
                                ShouldTime.setText(shouldTime);
                                ActualTime.setText(actualTime);
                            }else if(accessState.equals("fail")){
                                Toast.makeText(EachDetailActivity.this,"查询出错",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_each_detail);

        Intent intent=getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        courseName=bundle.getString("CourseName");
        teacherName=bundle.getString("TeacherName");
        Log.i("coursename",courseName+teacherName);

        init();

        new Thread(new SignTimeAction()).start();
    }

    private void init() {
        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("签到情况");
        BackToAttd=(ImageButton)findViewById(R.id.top_back);
        ShouldTime=(TextView)findViewById(R.id.should_time);
        ActualTime=(TextView)findViewById(R.id.actual_time);

        TouchUtil.expandViewTouchDelegate(BackToAttd,50,50,50,50);
        BackToAttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack=new Intent();
                intentBack.setClass(EachDetailActivity.this,ChosenCourseActivity.class);
                startActivity(intentBack);
                finish();
            }
        });


        }

    public class SignTimeAction extends Thread{
        public void run(){
            String u_name= UserInfo.getUSERNAME();
            Log.i("getUsername",u_name);
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", u_name));
            param.add(new BasicNameValuePair("a_name",teacherName));
            param.add(new BasicNameValuePair("c_name",courseName));
            try {
                result= ServerConnect.getInstance().getResult("MCheckTimes",param);
            } catch (Exception e) {
                System.out.println("MCheckTimes.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what= EventUtil.EACH_RECORD_SUCCESS;
            handler.sendMessage(msg);
        }




    }






}

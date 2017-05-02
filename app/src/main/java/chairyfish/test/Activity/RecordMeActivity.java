package chairyfish.test.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Adapter.ChosenCourseAdapter;
import chairyfish.test.Adapter.FinishCourseAdapter;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.Bean.ChosenCourse;
import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Util.ReadStateInfoC;
import chairyfish.test.Util.ReadStateInfoX;
import chairyfish.test.Util.TimeFormat;
import chairyfish.test.Util.TouchUtil;

import static chairyfish.test.Util.EventUtil.RECORDME_SUCCESS;

public class RecordMeActivity extends AppCompatActivity {

    TextView Title;
    ImageButton BackToMe;

    JSONArray listCode;
    ArrayList<CheckingCourse> finishCourses;
    FinishCourseAdapter finishCourseAdapter;
    ListView lvFinish;


    String result;

    Handler handler=new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == RECORDME_SUCCESS) {
                if (result == null) {
                    Toast.makeText(RecordMeActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONObject stateMap = obj.getJSONObject("stateMap");
                        String chosenState=stateMap.getString("stateInfo");
                        if(chosenState.equals("success")) {
                            System.out.println("*****************获取结果*****************");
                            listCode = stateMap.getJSONArray("list_code");
                            finishCourses.clear();
                            for(int i=0;i<listCode.length();i++){
                                JSONObject courseobj= null;
                                try {
                                    courseobj = listCode.getJSONObject(i);
                                    ReadStateInfoX rsx=new ReadStateInfoX(courseobj);
                                    CheckingCourse course=new CheckingCourse();
                                    course.setCoursename(rsx.getC_name());
                                    course.setTeachername(rsx.getA_name());
                                    course.setCoursestart(TimeFormat.DateTimetoStr(rsx.getTime1()));
                                    course.setCourseend(TimeFormat.DateTimetoStr(rsx.getTime2()));
                                    course.setCoursestate(rsx.getState());
                                    finishCourses.add(course);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("course",String.valueOf(finishCourses.size()));
                            if(finishCourseAdapter==null) {
                                finishCourseAdapter = new FinishCourseAdapter(RecordMeActivity.this, finishCourses);
                            }
                            lvFinish.setAdapter(finishCourseAdapter);





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
        setContentView(R.layout.activity_record_me);
        initView();
        new Thread(new AccessRecoedAction()).start();
    }

    private void initView() {
        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("已选课程");
        BackToMe=(ImageButton)findViewById(R.id.top_back);

        finishCourses=new ArrayList<CheckingCourse>();
        lvFinish=(ListView)findViewById(R.id.listview_finish_record);
        if(finishCourseAdapter==null) {
            finishCourseAdapter = new FinishCourseAdapter(this, finishCourses);
        }

        TouchUtil.expandViewTouchDelegate(BackToMe,50,50,50,50);
        BackToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RecordMeActivity.this,ContainerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("PAGE","1");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }
    public class AccessRecoedAction extends Thread{
        public void run(){
            String u_name= UserInfo.getUSERNAME();
            Log.i("getUsername",u_name);
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", u_name));
            try {
                result= ServerConnect.getInstance().getResult("MState02",param);
            } catch (Exception e) {
                System.out.println("MState02.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what=RECORDME_SUCCESS;
            handler.sendMessage(msg);
        }
    }


}

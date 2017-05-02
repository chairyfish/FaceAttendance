package chairyfish.test.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.service.chooser.ChooserTarget;
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

import chairyfish.test.Adapter.CheckingCourseAdapter;
import chairyfish.test.Adapter.ChosenCourseAdapter;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.Bean.ChosenCourse;
import chairyfish.test.Bean.PageState;
import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Util.EventUtil;
import chairyfish.test.Util.ReadStateInfoC;
import chairyfish.test.Util.ReadStateInfoX;
import chairyfish.test.Util.TimeFormat;
import chairyfish.test.Util.TouchUtil;

public class ChosenCourseActivity extends AppCompatActivity {
    TextView Title;
    ImageButton BackToAttd;
    JSONArray listCode;
    ArrayList<ChosenCourse> chosenCourses;
    ChosenCourseAdapter courseAdapter;
    ListView lvChosen;

    String result;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EventUtil.CHOSEN_SUCCESS:
                    if (result == null) {
                        Toast.makeText(ChosenCourseActivity.this, "请检查网络服务", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println(result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            JSONObject stateMap = obj.getJSONObject("stateMap");
                            String chosenState=stateMap.getString("stateInfo");
                            if(chosenState.equals("success")) {
                                System.out.println("*****************获取结果*****************");
                                listCode = stateMap.getJSONArray("list_code");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        chosenCourses.clear();
                        for(int i=0;i<listCode.length();i++){
                            JSONObject courseobj= null;
                            try {
                                courseobj = listCode.getJSONObject(i);
                                ReadStateInfoC rsc=new ReadStateInfoC(courseobj);
                                ChosenCourse course=new ChosenCourse();
                                course.setCoursename(rsc.getC_name());
                                course.setTeachername(rsc.getA_name());
                                chosenCourses.add(course);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("course",String.valueOf(chosenCourses.size()));
                        if(courseAdapter==null) {
                            courseAdapter = new ChosenCourseAdapter(ChosenCourseActivity.this, chosenCourses);
                        }
                        lvChosen.setAdapter(courseAdapter);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_course);

        init();
        new Thread(new AccessChosenCourceAction()).start();
    }

    private void init() {
        chosenCourses=new ArrayList<ChosenCourse>();
        lvChosen=(ListView)findViewById(R.id.listview_chosen_course);

        if(courseAdapter==null) {
            courseAdapter = new ChosenCourseAdapter(this, chosenCourses);
        }

        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("已选课程");
        BackToAttd=(ImageButton)findViewById(R.id.top_back);
        TouchUtil.expandViewTouchDelegate(BackToAttd,50,50,50,50);
        BackToAttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBack=new Intent();
                intentBack.setClass(ChosenCourseActivity.this,ContainerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("PAGE", PageState.PAGE);
                intentBack.putExtra("bundle",bundle);
                startActivity(intentBack);
                finish();
            }
        });
    }


    public class AccessChosenCourceAction extends Thread{
        public void run(){
            String u_name= UserInfo.getUSERNAME();
            Log.i("getUsername",u_name);
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", u_name));
            try {
                result= ServerConnect.getInstance().getResult("MHas_check",param);
            } catch (Exception e) {
                System.out.println("MHas_check.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what= EventUtil.CHOSEN_SUCCESS;
            handler.sendMessage(msg);
        }
    }
}

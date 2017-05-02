package chairyfish.test.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chairyfish.test.Activity.ChosenCourseActivity;
import chairyfish.test.Activity.JoinActivity;
import chairyfish.test.Adapter.CheckingCourseAdapter;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.Connect.ServerConnect;
import chairyfish.test.R;
import chairyfish.test.Bean.UserInfo;
import chairyfish.test.Util.ReadStateInfoX;
import chairyfish.test.Util.TimeFormat;

import static chairyfish.test.Util.EventUtil.ACCESS_CHECKING_SUCCESS;


/**
 * Created by chairyfish on 2017/4/25.
 */

public class AttendanceFragment extends Fragment{
    String result;
    ListView lvChecking;
    JSONArray listCode=null;
    ArrayList<CheckingCourse> courses;
    CheckingCourseAdapter courseAdapter;
    RelativeLayout NoticeEmpty;

    LinearLayout ChosenCourse,JoinCourse;

    Handler handler=new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == ACCESS_CHECKING_SUCCESS) {
                if (result == null) {
                    Toast.makeText(getActivity(), "请检查网络服务", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONObject stateMap=obj.getJSONObject("stateMap");
                        String checkingState=stateMap.getString("stateInfo");
                        System.out.println("*****************查询结果*****************");
                        if(checkingState.equals("success")) {
                        listCode = stateMap.getJSONArray("list_code");
                        Log.i("list_code",listCode.toString());
                            //System.out.println(new ReadStateInfoX(listCode.getJSONObject(0)).getA_name());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    courses.clear();
                    for(int i=0;i<listCode.length();i++){
                        Log.e("解析开始..."," ");
                        try {
                            JSONObject courseobj=listCode.getJSONObject(i);
                            ReadStateInfoX rsx=new ReadStateInfoX(courseobj);
                            CheckingCourse course=new CheckingCourse();
                            course.setCoursename(rsx.getC_name());
                            course.setTeachername(rsx.getA_name());
                            course.setCoursestate(rsx.getState());
                            course.setCoursestart(TimeFormat.DateTimetoStr(rsx.getTime1()));
                            course.setCourseend(TimeFormat.DateTimetoStr(rsx.getTime2()));
      //                      course.setCoursestart(rsx.getTime1());

                            courses.add(course);
                        } catch (JSONException e) {
                            Log.e("catch","反正出错就对了！！！！！！");
                            e.printStackTrace();
                        }
                    }
                    Log.e("courses",String.valueOf(courses.size()));
                    if(courses.size()==0) {
                        NoticeEmpty.setVisibility(View.VISIBLE);
                    }
                    if(courseAdapter==null) {
                        courseAdapter = new CheckingCourseAdapter(getActivity(), courses);
                    }
                    lvChecking.setAdapter(courseAdapter);
                }
            }
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        init(view);

            new Thread(new AccessCheckingCourceAction()).start();

        return view;

    }

    private void init(View view) {
        courses=new ArrayList<CheckingCourse>();
        lvChecking=(ListView)view.findViewById(R.id.listview_checking_course);
        ChosenCourse=(LinearLayout)view.findViewById(R.id.chosen_course);
        JoinCourse=(LinearLayout)view.findViewById(R.id.join_course);
        NoticeEmpty=(RelativeLayout)view.findViewById(R.id.notice_emptylist);
        if(courseAdapter==null) {
            courseAdapter = new CheckingCourseAdapter(getActivity(), courses);
        }
        ChosenCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChosen=new Intent();
                intentChosen.setClass(getActivity(),ChosenCourseActivity.class);
                startActivity(intentChosen);
            }
        });
        JoinCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentJoin=new Intent();
                intentJoin.setClass(getActivity(),JoinActivity.class);
                startActivity(intentJoin);
            }
        });

    }

    public class AccessCheckingCourceAction extends Thread{
        public void run(){
            String u_name=UserInfo.getUSERNAME(getActivity());
            Log.i("getUsername",u_name);
            List<BasicNameValuePair> param=new ArrayList<BasicNameValuePair>();
            param.add(new BasicNameValuePair("u_name", u_name));
            try {
                result= ServerConnect.getInstance().getResult("MState",param);
            } catch (Exception e) {
                System.out.println("MState.action"+e.getMessage());
                e.printStackTrace();
            }
            Message msg=Message.obtain();
            //发送消息
            msg.what=ACCESS_CHECKING_SUCCESS;
            handler.sendMessage(msg);
        }
    }

}

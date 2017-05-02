package chairyfish.test.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import chairyfish.test.Activity.ChosenCourseActivity;
import chairyfish.test.Activity.LessonMeActivity;
import chairyfish.test.Activity.LoginActivity;
import chairyfish.test.Activity.MainActivity;
import chairyfish.test.Activity.RecordMeActivity;
import chairyfish.test.Bean.ChosenCourse;
import chairyfish.test.R;
import chairyfish.test.Bean.UserInfo;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chairyfish on 2017/4/25.
 */

public class MeFragment extends Fragment {


    Button LogOut;
    TextView LessonRecord,CheckRecord;
    TextView UserName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        initView(view);
        initData();

        return view;

    }



    private void initView(View view) {
        LessonRecord=(TextView)view.findViewById(R.id.lesson_record);
        CheckRecord=(TextView)view.findViewById(R.id.check_record);
        LogOut=(Button)view.findViewById(R.id.btn_logout);
        UserName=(TextView)view.findViewById(R.id.txv_setting_username);
        UserName.setText(UserInfo.getUSERNAME(getActivity()));
        LessonRecord.setOnClickListener(new MyOnClick());
        CheckRecord.setOnClickListener(new MyOnClick());
        LogOut.setOnClickListener(new MyOnClick());
    }

    private void initData() {

        UserInfo.setUSERNAME(UserInfo.getUSERNAME(getActivity()));


    }


    class MyOnClick implements View.OnClickListener{
        public void onClick(View view){
            switch (view.getId()){
                case R.id.lesson_record:
                    Intent intentLesson=new Intent();
                    intentLesson.setClass(getActivity(), ChosenCourseActivity.class);
                    startActivity(intentLesson);
//                    Bundle bundle=new Bundle();
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent,0);
                    break;
                case R.id.check_record:
                    Intent intentRecord=new Intent();
                    intentRecord.setClass(getActivity(), RecordMeActivity.class);
                    startActivity(intentRecord);
                    break;
                case R.id.btn_logout:
                    SharedPreferences userSettings = getActivity().getSharedPreferences("UserInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = userSettings.edit();
                    editor.remove("username");
                    editor.remove("password");
                    editor.remove("stateMap");
                    editor.commit();
                    Intent intentLogin=new Intent();
                    intentLogin.setClass(getActivity(),LoginActivity.class);
                    startActivity(intentLogin);
                    getActivity().finish();
                    break;
            }

        }


    }
}

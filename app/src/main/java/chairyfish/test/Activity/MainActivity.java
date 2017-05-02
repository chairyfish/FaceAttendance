package chairyfish.test.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chairyfish.test.R;
import chairyfish.test.Util.ReadStateInfo;
import chairyfish.test.Util.TimeFormat;
import chairyfish.test.Bean.UserInfo;

public class MainActivity extends AppCompatActivity {

    Button Check,LogOut;
    TableLayout Checkstate;
    JSONArray checkstatearray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(UserInfo.getCHECKSTATE().length()!=0) {
            checkstatearray=UserInfo.getCHECKSTATE();
        }else {
            SharedPreferences sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);
            String stateMap=sharedPreferences.getString("stateMap",null);
            try {
                JSONObject stateInfoObj=new JSONObject(stateMap);
                checkstatearray=stateInfoObj.getJSONArray("stateInfo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<checkstatearray.length();i++){
            try {
                JSONObject stateobj=checkstatearray.getJSONObject(i);
                setCheckState(stateobj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCheckState(JSONObject info) {
        ReadStateInfo readstateinfo=new ReadStateInfo(info);
        TableRow tableRow=new TableRow(this);
        TextView aname=new TextView(this);
        aname.setText(readstateinfo.getA_name());
        aname.setGravity(Gravity.CENTER);
        aname.setHeight(50);
        TextView time=new TextView(this);
        time.setText(TimeFormat.DateTimetoStr(readstateinfo.getTime()));
        time.setGravity(Gravity.CENTER);
        time.setHeight(50);
        TextView code=new TextView(this);
        code.setText(readstateinfo.getCode());
        code.setGravity(Gravity.CENTER);
        code.setHeight(50);
        TextView state=new TextView(this);
        state.setText(readstateinfo.getState());
        state.setGravity(Gravity.CENTER);
        state.setHeight(50);
        tableRow.addView(aname);
        tableRow.addView(time);
        tableRow.addView(code);
        tableRow.addView(state);
        Checkstate.addView(tableRow);
    }

    private void init() {
        Check=(Button)findViewById(R.id.check);
        LogOut=(Button)findViewById(R.id.btn_logout);
        Checkstate=(TableLayout)findViewById(R.id.checkstate);
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,CheckActivity.class);
                startActivity(intent);
                finish();
            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userSettings = getSharedPreferences("UserInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.remove("username");
                editor.remove("password");
                editor.remove("stateMap");
                editor.commit();
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

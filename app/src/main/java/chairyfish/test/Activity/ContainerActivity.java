package chairyfish.test.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import chairyfish.test.Bean.PageState;
import chairyfish.test.Fragment.AttendanceFragment;
import chairyfish.test.Fragment.MeFragment;
import chairyfish.test.R;
import chairyfish.test.Util.TouchUtil;


/**
 * Created by chairyfish on 2017/4/25.
 */

public class ContainerActivity extends FragmentActivity{

    private FragmentManager fm;
    private FragmentTransaction ft;
    private TextView title;
    ImageButton[] tabs = new ImageButton[2];
    private String page="0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Intent intent=getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        page = bundle.getString("PAGE");

        initView();

    }

    private void initView() {
        title=(TextView)findViewById(R.id.top_title);
        title.setText("签到面板");
        tabs[0]=(ImageButton) findViewById(R.id.id_tab_attd_img);
        tabs[1]=(ImageButton)findViewById(R.id.id_tab_settings_img);
        tabs[0].setOnClickListener(new MenuClick());
        tabs[1].setOnClickListener(new MenuClick());
        TouchUtil.expandViewTouchDelegate(tabs[0],200,200,200,200);//扩大判定范围
        TouchUtil.expandViewTouchDelegate(tabs[1],200,200,200,200);
        fm = getFragmentManager();
        ft = fm.beginTransaction();
        if(page.equals("0")) {

            tabs[0].setSelected(true);
            tabs[1].setSelected(false);
            tabs[0].setImageDrawable(getResources().getDrawable(R.mipmap.attendance_checked));
            tabs[1].setImageDrawable(getResources().getDrawable(R.mipmap.setting_unchecked));
            ft.replace(R.id.layout_fragment, new AttendanceFragment());
        }else if(page.equals("1")){

            tabs[0].setSelected(false);
            tabs[1].setSelected(true);
            tabs[0].setImageDrawable(getResources().getDrawable(R.mipmap.attendance_unchecked));
            tabs[1].setImageDrawable(getResources().getDrawable(R.mipmap.setting_checked));
            ft.replace(R.id.layout_fragment, new MeFragment());
        }else {

            ft.replace(R.id.layout_fragment, new AttendanceFragment());
        }
        ft.commit();
    }

    private void setSelected() {
        tabs[0].setSelected(false);
        tabs[1].setSelected(false);
    }

    class MenuClick implements View.OnClickListener{
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.id_tab_attd_img:
                    PageState.PAGE="0";
                    title.setText("签到面板");
                    tabs[0].setImageDrawable(getResources().getDrawable(R.mipmap.attendance_checked));
                    tabs[1].setImageDrawable(getResources().getDrawable(R.mipmap.setting_unchecked));
                    setSelected();
                    tabs[0].setSelected(true);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.layout_fragment, new AttendanceFragment());
                    ft.commit();
                    break;
                case R.id.id_tab_settings_img:
                    PageState.PAGE="1";
                    title.setText("我");
                    tabs[0].setImageDrawable(getResources().getDrawable(R.mipmap.attendance_unchecked));
                    tabs[1].setImageDrawable(getResources().getDrawable(R.mipmap.setting_checked));
                    setSelected();
                    tabs[1].setSelected(true);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.layout_fragment, new MeFragment());
                    ft.commit();
                    break;
            }
        }

    }


}

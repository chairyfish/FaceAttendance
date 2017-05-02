package chairyfish.test.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import chairyfish.test.R;
import chairyfish.test.Util.TouchUtil;

public class LessonMeActivity extends AppCompatActivity {

    TextView Title;
    ImageButton BackToMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_me);


        initView();
    }

    private void initView() {

        Title=(TextView)findViewById(R.id.top_title_back);
        Title.setText("已选课程");
        BackToMe=(ImageButton)findViewById(R.id.top_back);
        TouchUtil.expandViewTouchDelegate(BackToMe,50,50,50,50);
        BackToMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LessonMeActivity.this,ContainerActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("PAGE","1");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }
}

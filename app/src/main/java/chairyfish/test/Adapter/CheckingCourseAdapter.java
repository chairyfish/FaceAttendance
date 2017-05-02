package chairyfish.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import chairyfish.test.Activity.CheckActivity;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.R;
import chairyfish.test.Util.TimeFormat;

/**
 * Created by chairyfish on 2017/4/26.
 */

public class CheckingCourseAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<CheckingCourse> checkingItems;    //商品信息集合
    private LayoutInflater listContainer;           //视图容器

    public final class CheckingCell{                //自定义控件集合
        public TextView CourseName;
        public TextView TeacherName;
        public TextView CourseTime;
        public Button StartCheck;

        public CheckingCell(TextView courseName, TextView teacherName, TextView courseTime, Button startCheck) {
            CourseName = courseName;
            TeacherName = teacherName;
            CourseTime = courseTime;
            StartCheck = startCheck;
        }

        public TextView getTeacherName() {
            return TeacherName;
        }

        public TextView getCourseName() {
            return CourseName;
        }

        public TextView getCourseTime() {
            return CourseTime;
        }

        public Button getStartCheck() {
            return StartCheck;
        }
    }

    public CheckingCourseAdapter(Context context, List<CheckingCourse> checkingItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.checkingItems = checkingItems;
    }

    @Override
    public int getCount() {
        return checkingItems.size();
    }

    @Override
    public CheckingCourse getItem(int position) {
        return checkingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_checkingcourse, null);
            convertView.setTag(new CheckingCell((TextView) convertView.findViewById(R.id.checking_course_name),
                    (TextView) convertView.findViewById(R.id.checking_course_teacher),
                    (TextView) convertView.findViewById(R.id.checking_time),
                    (Button) convertView.findViewById(R.id.checking_enter)));
        }

        final CheckingCell Cell = (CheckingCell) convertView.getTag();
        TextView CellCourseName = Cell.getCourseName();
        TextView CellTeacherName = Cell.getTeacherName();
        TextView CellCourseTime=Cell.getCourseTime();
        final Button CellStartCheck=Cell.getStartCheck();
        CellCourseName.setText(getItem(position).getCoursename());
        CellTeacherName.setText(getItem(position).getTeachername());
        CellCourseTime.setText(getItem(position).getCoursestart()+"  至  "+
        getItem(position).getCourseend());
        if(getItem(position).getCoursestate().equals("是")){
            //已经签到的按钮样式
            CellStartCheck.setText("你已签到");
            CellStartCheck.setBackgroundColor(Color.GRAY);
            CellStartCheck.setBackground(context.getResources().getDrawable(R.drawable.rect_button_unable));
        }else if(getItem(position).getCoursestate().equals("否")){
            CellStartCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(context,CheckActivity.class);
                    context.startActivity(intent);

                }
            });
        }
        return convertView;
    }

    public void clear() {
        checkingItems.clear();
        notifyDataSetChanged();
    }
}

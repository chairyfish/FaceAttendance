package chairyfish.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chairyfish.test.Activity.CheckActivity;
import chairyfish.test.Activity.ChosenCourseActivity;
import chairyfish.test.Activity.EachDetailActivity;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.Bean.ChosenCourse;
import chairyfish.test.R;

/**
 * Created by chairyfish on 2017/4/27.
 */

public class ChosenCourseAdapter extends BaseAdapter{
    private Context context;              //运行上下文
    private List<ChosenCourse> chosenItems;    //商品信息集合
    private LayoutInflater listContainer;

    public final class ChosenCell{
        public TextView CourseName;
        public TextView TeacherName;
        public TextView Detail;

        public ChosenCell(TextView courseName, TextView teacherName, TextView detail) {
            CourseName = courseName;
            TeacherName = teacherName;
            Detail = detail;
        }

        public TextView getCourseName() {
            return CourseName;
        }

        public TextView getTeacherName() {
            return TeacherName;
        }

        public TextView getDetail() {
            return Detail;
        }
    }
    public ChosenCourseAdapter(Context context, List<ChosenCourse> chosenItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.chosenItems = chosenItems;
    }

    @Override
    public int getCount() {
        return chosenItems.size();
    }

    @Override
    public ChosenCourse getItem(int position) {
        return chosenItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_chosen_course, null);
            convertView.setTag(new ChosenCourseAdapter.ChosenCell((TextView) convertView.findViewById(R.id.chosen_course_name),
                    (TextView) convertView.findViewById(R.id.chosen_course_teacher),
                    (TextView) convertView.findViewById(R.id.chosen_course_detail)));
        }

        final ChosenCourseAdapter.ChosenCell Cell = (ChosenCourseAdapter.ChosenCell) convertView.getTag();
        TextView CellCourseName = Cell.getCourseName();
        TextView CellTeacherName = Cell.getTeacherName();
        TextView CellDetial=Cell.getDetail();
        CellCourseName.setText(getItem(position).getCoursename());
        CellTeacherName.setText(getItem(position).getTeachername());

        CellDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context, EachDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("CourseName",getItem(position).getCoursename());
                bundle.putString("TeacherName",getItem(position).getTeachername());
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public void clear() {
        chosenItems.clear();
        notifyDataSetChanged();
    }

}

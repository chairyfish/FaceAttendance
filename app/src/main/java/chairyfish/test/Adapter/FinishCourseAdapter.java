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

import chairyfish.test.Activity.CheckActivity;
import chairyfish.test.Bean.CheckingCourse;
import chairyfish.test.R;

/**
 * Created by chairyfish on 2017/4/27.
 */

public class FinishCourseAdapter extends BaseAdapter {

    private Context context;                        //运行上下文
    private List<CheckingCourse> fishishItems;    //课程信息集合
    private LayoutInflater listContainer;           //视图容器

    public final class FinishCell{                //自定义控件集合
        public TextView CourseName;
        public TextView TeacherName;
        public TextView CourseStart;
        public TextView CourseEnd;
        public TextView CourseState;

        public FinishCell(TextView courseName, TextView teacherName, TextView courseStart, TextView courseEnd, TextView courseState) {
            CourseName = courseName;
            TeacherName = teacherName;
            CourseStart = courseStart;
            CourseEnd = courseEnd;
            CourseState = courseState;
        }

        public TextView getTeacherName() {
            return TeacherName;
        }

        public TextView getCourseName() {
            return CourseName;
        }

        public TextView getCourseStart() {
            return CourseStart;
        }

        public TextView getCourseEnd() {
            return CourseEnd;
        }

        public TextView getCourseState() {
            return CourseState;
        }
    }

    public FinishCourseAdapter(Context context, List<CheckingCourse> fishishItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.fishishItems = fishishItems;
    }

    @Override
    public int getCount() {
        return fishishItems.size();
    }

    @Override
    public CheckingCourse getItem(int position) {
        return fishishItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_checked_record, null);
            convertView.setTag(new FinishCourseAdapter.FinishCell((TextView) convertView.findViewById(R.id.finish_course_name),
                    (TextView) convertView.findViewById(R.id.finish_course_teacher),
                    (TextView) convertView.findViewById(R.id.finish_course_start),
                    (TextView) convertView.findViewById(R.id.finish_course_end),
                    (TextView) convertView.findViewById(R.id.finish_course_state)));
        }

        final FinishCourseAdapter.FinishCell Cell = (FinishCourseAdapter.FinishCell) convertView.getTag();
        TextView CellCourseName = Cell.getCourseName();
        TextView CellTeacherName = Cell.getTeacherName();
        TextView CellCourseStart=Cell.getCourseStart();
        TextView CellCourseEnd=Cell.getCourseEnd();
        TextView CellCourseState=Cell.getCourseState();
        CellCourseName.setText(getItem(position).getCoursename());
        CellTeacherName.setText(getItem(position).getTeachername());
        CellCourseStart.setText(getItem(position).getCoursestart());
        CellCourseEnd.setText(getItem(position).getCourseend());
        CellCourseState.setText(getItem(position).getCoursestate());


        return convertView;
    }

    public void clear() {
        fishishItems.clear();
        notifyDataSetChanged();
    }
}

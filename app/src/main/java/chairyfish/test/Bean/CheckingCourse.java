package chairyfish.test.Bean;

import java.util.Date;

/**
 * Created by chairyfish on 2017/4/26.
 */

public class CheckingCourse {
    private String coursename,teachername;
    private String coursestart,courseend;
    private String coursestate;
    public CheckingCourse(){};

    public CheckingCourse(String coursename, String teachername, String coursestart, String courseend, String coursestate) {
        this.coursename = coursename;
        this.teachername = teachername;
        this.coursestart = coursestart;
        this.courseend = courseend;
        this.coursestate = coursestate;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getCoursestart() {
        return coursestart;
    }

    public void setCoursestart(String coursestart) {
        this.coursestart = coursestart;
    }

    public String getCourseend() {
        return courseend;
    }

    public void setCourseend(String courseend) {
        this.courseend = courseend;
    }

    public String getCoursestate() {
        return coursestate;
    }

    public void setCoursestate(String coursestate) {
        this.coursestate = coursestate;
    }
}

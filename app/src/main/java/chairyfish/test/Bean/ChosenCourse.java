package chairyfish.test.Bean;

/**
 * Created by chairyfish on 2017/4/27.
 */

public class ChosenCourse {

    private String coursename;
    private String teachername;
    public ChosenCourse(){};
    public ChosenCourse(String coursename, String teachername) {
        this.coursename = coursename;
        this.teachername = teachername;
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
}

package chairyfish.test.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chairyfish on 2017/4/27.
 */

public class ReadStateInfoC {
    private JSONObject stateInfo;
    private String a_name=null;
    private String c_name=null;
    public ReadStateInfoC(JSONObject stateInfo){
        this.stateInfo=stateInfo;
    }

    public String getA_name() {
        try {
            a_name=stateInfo.getString("a_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return a_name;
    }
    public String getC_name() {
        try {
            c_name=stateInfo.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c_name;
    }
}

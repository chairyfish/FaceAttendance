package chairyfish.test.Connect;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by chairyfish on 2017/2/14.
 */

public class ServerConnect {
    private static ServerConnect httpConnect;

    private ServerConnect(){};

    public static ServerConnect getInstance(){
        if(httpConnect == null){
            synchronized (ServerConnect.class) {
                if(httpConnect == null){
                    httpConnect = new ServerConnect();
                }
            }
        }
        return httpConnect;
    }
    public String getResult(String actionName,List<BasicNameValuePair> params)throws Exception{
        HttpClient httpClient = new DefaultHttpClient();
        String url="http://www.jiaxiaojun.cn/Student_Attdence_System3.0/"+actionName+".action";
        HttpPost request = new HttpPost(url);
        //设置上传的数据，上传的数据封装到list集合中,并转换为UTF-8字符集
        System.out.println(url);
        System.out.println(request.toString());
 //       System.out.println(actionName+params.toString());
        request.addHeader("Accept","text/json");
        request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        //执行请求，并返回响应对象
        System.out.println("发送请求");
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            System.out.println("服务器未响应:"+e.getMessage());
            e.printStackTrace();
        // TODO: handle exception
        }
        //判断服务器响应状态
        System.out.println("发送数据");
        System.out.println(response.getStatusLine().getStatusCode());
        if(response.getStatusLine().getStatusCode() == 200){
        //获取服务器返回信息
            System.out.println("**************result****************");
            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity,"UTF-8");
            System.out.println(json);
            return json;
        }
        return null;

    }

}

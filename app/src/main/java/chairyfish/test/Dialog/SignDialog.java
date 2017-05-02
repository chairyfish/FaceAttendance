package chairyfish.test.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chairyfish.test.R;

/**
 * Created by chairyfish on 2017/4/16.
 */

public class SignDialog extends Dialog{

    private EditText teachername,code;
    private Button yesButton, noButton;
    Context context;


    public SignDialog (Context context){
        super(context);
        this.context = context;
    }
    public SignDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initUI();

    }

    private void initUI() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_sign, null);
        this.setContentView(layout);
        teachername=(EditText)layout.findViewById(R.id.Ed_teacher);
        code=(EditText)layout.findViewById(R.id.Ed_code);
        Log.i("控件绑定成功",teachername.getText().toString()+"_____________________________________");
        yesButton = (Button)layout.findViewById(R.id.sign_dialog_yes);
        noButton=(Button)layout.findViewById(R.id.sign_dialog_no);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public EditText getTeacherName(){
        return teachername;
    }

    public EditText getCheckCode(){
        return code;
    }
    public void setOnYesListener(View.OnClickListener listener){
        yesButton.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNoListener(View.OnClickListener listener){
        noButton.setOnClickListener(listener);
    }
}

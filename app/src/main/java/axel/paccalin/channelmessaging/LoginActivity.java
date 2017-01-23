package axel.paccalin.channelmessaging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnDownloadCompleteListener{
    private Button btn_login;
    private TextView lbl_password;
    private TextView lbl_userName;
    private EditText txt_userName;
    private EditText txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        lbl_userName = (TextView) findViewById(R.id.lbl_userName);
        lbl_password = (TextView) findViewById(R.id.lbl_password);

        txt_userName = (EditText) findViewById(R.id.txt_userName);
        txt_password = (EditText) findViewById(R.id.txt_password);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login)
        {
            HashMap<String, String> connectInfo = new HashMap<>();
            connectInfo.put("id",txt_userName.getText().toString());
            connectInfo.put("mdp", txt_password.getText().toString());
            Async Async = new Async(getApplicationContext(), connectInfo);
            Async.setOnDownloadCompleteListener((OnDownloadCompleteListener) this);
            Async.execute();
        }
    }

    @Override
    public void onDownloadComplete(String result) {

        Gson gson = new Gson();
        Callback obj = new Callback();
        String json = gson.toJson(obj);

    }



}
package axel.paccalin.channelmessaging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, OnDownloadCompleteListener{
    public static final String PREFS_NAME = "stockage";
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
            connectInfo.put("username",txt_userName.getText().toString());
            connectInfo.put("password", txt_password.getText().toString());
            Async Async = new Async(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=connect", connectInfo);
            Async.setOnDownloadCompleteListener((OnDownloadCompleteListener) this);
            Async.execute();
        }
    }

    @Override
    public void onDownloadComplete(String result) {

        Gson gson = new Gson();

        CallbackLogin r = gson.fromJson(result, CallbackLogin.class);
        if(r.code==200){
            Toast.makeText(this, "Vous êtes connecté ! ", Toast.LENGTH_SHORT).show();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("accesstoken", r.accesstoken);
            editor.commit();
            Intent ChannelList = new Intent(getApplicationContext(),ChannelListActivity.class);
            startActivity(ChannelList);
        }
        else{
            Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
        }
    }
}
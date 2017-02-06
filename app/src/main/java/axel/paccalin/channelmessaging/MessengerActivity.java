package axel.paccalin.channelmessaging;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MessengerActivity extends AppCompatActivity implements OnDownloadCompleteListener {

    private ListView lvMsg;
    private ArrayList<Message> messageList = new ArrayList<>();
    private String accesstoken;
    private int channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        this.accesstoken = settings.getString("accesstoken", "error");

        settings = getSharedPreferences(ChannelListActivity.PREFS_NAME, 0);
        this.channelId = settings.getInt("channelid", 0);

        setContentView(R.layout.activity_messenger);
        this.lvMsg = (ListView)findViewById(R.id.lvMsg);

        LoadMsg();
    }

    public void LoadMsg(){
        HashMap<String, String> connectInfo = new HashMap<>();

        connectInfo.put("accesstoken",this.accesstoken.toString());
        connectInfo.put("channelid", Integer.toString(this.channelId));

        Async Async = new Async(getApplicationContext(), "http://www.joomlaworks.net/images/demos/galleries/abstract/7.jpg", connectInfo);
        Async.setOnDownloadCompleteListener((OnDownloadCompleteListener) this);
        Async.execute();
    }

    public void ShowMsg(){

    }

    @Override
    public void onDownloadComplete(String result) {

        Gson gson = new Gson();

        CallbackMsg r = gson.fromJson(result, CallbackMsg.class);
        if(/*r.code == 200*/r.messages.size() == 0){
            Toast.makeText(this, "Erreur lors de la récupération des channels: Erreur " + r.code, Toast.LENGTH_SHORT).show();
        }
        this.messageList = r.messages;
        ShowMsg();
    }
}
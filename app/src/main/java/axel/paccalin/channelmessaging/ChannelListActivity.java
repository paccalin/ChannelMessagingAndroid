package axel.paccalin.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnDownloadCompleteListener{

    private ListView lvChannels;
    private ArrayList<Channel> channelList = new ArrayList<>();
    private String accesstoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        this.accesstoken = settings.getString("accessToken", "error");
        setContentView(R.layout.activity_channel_list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "J'ai sélectionné l'item " + position, Toast.LENGTH_SHORT).show();
        Log.d("monApplication", "J'ai sélectionné l'item " + position);
    }

    public void loadChannels(){
        HashMap<String, String> connectInfo = new HashMap<>();
        connectInfo.put("accesstoken",this.accesstoken.toString());
        Async Async = new Async(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=connect", connectInfo);
        Async.setOnDownloadCompleteListener((OnDownloadCompleteListener) this);
        Async.execute();
    }

    @Override
    public void onDownloadComplete(String result) {

        Gson gson = new Gson();

        CallbackChannels r = gson.fromJson(result, CallbackChannels.class);
        if(r.channels.size() != 0){
            this.channelList = r.channels;
        }
        else{
            Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowChannels(){
        this.lvChannels = (ListView)findViewById(R.id.lvChannels);
        this.lvChannels.setAdapter(new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, channelList));
        this.lvChannels.setOnItemClickListener(this);
    }
}

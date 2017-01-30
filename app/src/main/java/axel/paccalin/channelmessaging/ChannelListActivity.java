package axel.paccalin.channelmessaging;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, OnDownloadCompleteListener{

    private ListView lvChannels;
    private ArrayList<Channel> channelList = new ArrayList<>();
    private String accesstoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        this.accesstoken = settings.getString("accesstoken", "error");
        Toast.makeText(this, "AT2: " + accesstoken, Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_channel_list);
        this.lvChannels = (ListView)findViewById(R.id.lvChannels);

        loadChannels();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "J'ai sélectionné l'item " + position, Toast.LENGTH_SHORT).show();
        Log.d("monApplication", "J'ai sélectionné l'item " + position);
    }

    public void loadChannels(){
        HashMap<String, String> connectInfo = new HashMap<>();
        connectInfo.put("accesstoken",this.accesstoken.toString());
        Async Async = new Async(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=getchannels", connectInfo);
        Async.setOnDownloadCompleteListener((OnDownloadCompleteListener) this);
        Async.execute();
    }

    @Override
    public void onDownloadComplete(String result) {

        Gson gson = new Gson();

        CallbackChannels r = gson.fromJson(result, CallbackChannels.class);
        if(/*r.code == 200*/r.channels.size() == 0){
            Toast.makeText(this, "Erreur lors de la récupération des channels: Erreur " + r.code, Toast.LENGTH_SHORT).show();
        }
        this.channelList = r.channels;
        ShowChannels();
    }

    public void ShowChannels(){
        this.lvChannels.setAdapter(new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, channelList));
        this.lvChannels.setOnItemClickListener(this);


        //Toast.makeText(getApplicationContext(),Integer.toString(r.code),Toast.LENGTH_SHORT).show();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Channel aChannel : this.channelList) {
            Map<String, String> hashedChannel = new HashMap<String, String>(2);
            hashedChannel.put("name", aChannel.name);
            hashedChannel.put("connectedusers", "Utilisateur(s) connecté(s) : " + Integer.toString(aChannel.connectedusers));
            data.add(hashedChannel);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "connectedusers"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        this.lvChannels.setAdapter(adapter);
    }
}

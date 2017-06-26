package vazquez.rodrigo.realm;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vazquez.rodrigo.realm.Adapters.GithubAdapter;
import vazquez.rodrigo.realm.Models.GitHub;
import vazquez.rodrigo.realm.Services.PollingService;

public class ThreadExampleActivity extends AppCompatActivity {

    private static final String URL = "https://api.github.com/repos/RodryVazquez/realm";
    private RealmChangeListener<RealmResults<GitHub>> listener;
    Realm realm;
    private ListView lstGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_example);
        realm = Realm.getDefaultInstance();

        listener = new RealmChangeListener<RealmResults<GitHub>>() {
            @Override
            public void onChange(RealmResults<GitHub> element) {
                lstGithub.setAdapter(new GithubAdapter(element));
            }
        };
        setUpListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void setUpListView() {
        lstGithub = (ListView) findViewById(R.id.lstGithub);
        RealmResults<GitHub> results = realm.where(GitHub.class).findAllAsync();
        results.addChangeListener(listener);
    }

    public void startIntent() {
        Intent intent = new Intent(this, PollingService.class);
        intent.putExtra("url", URL);
        startService(intent);
    }
}

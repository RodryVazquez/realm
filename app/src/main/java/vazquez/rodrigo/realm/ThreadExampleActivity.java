package vazquez.rodrigo.realm;

import android.app.IntentService;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vazquez.rodrigo.realm.Adapters.GithubAdapter;
import vazquez.rodrigo.realm.Models.Car;
import vazquez.rodrigo.realm.Models.GitHub;
import vazquez.rodrigo.realm.Services.PollingService;

public class ThreadExampleActivity extends AppCompatActivity {

    private static final String URL = "https://api.github.com/repos/RodryVazquez/realm";
    private RealmChangeListener<RealmResults<GitHub>> listener;
    Realm realm;
    private ListView lstGithub;
    private GithubAdapter githubAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_example);

        //Leemos el objeto Parcelable
        Car car = getIntent().getParcelableExtra("ParcelableCar");
        if(car != null){

            StringBuilder builder = new StringBuilder();
            builder.append("Id " + car.getCarId());
            builder.append(" Brand " + car.getCarName());
            builder.append(" State " + car.getRun());

            Toast.makeText(this, builder.toString(), Toast.LENGTH_SHORT).show();
        }

        realm = Realm.getDefaultInstance();

        listener = new RealmChangeListener<RealmResults<GitHub>>() {
            @Override
            public void onChange(RealmResults<GitHub> element) {
                lstGithub.setAdapter(githubAdapter = new GithubAdapter(element));
            }
        };

        setUpListView();

        lstGithub.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final GitHub gitHub = (GitHub) githubAdapter.getItem(position);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(GitHub.class).equalTo("id", gitHub.getId())
                                .findAll()
                                .deleteAllFromRealm();
                    }
                });
                return true;
            }
        });
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

    /*  Async Task & Intent Service
    *   http://android-cuu-api.herokuapp.com/api/movies
    *   If you need to use Realm in either of these methods (doInBackground(T),onHandleIntent(T))
    *   you should open the Realm, perform your work and then
    *   close the Realm before exiting
    * */

    private class DownloadFilesTask extends AsyncTask<String, Integer, String> {

        //Main thread Updates
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e("Progress", String.valueOf(values[0]));
        }

        //Background thread
        @Override
        protected String doInBackground(String... params) {
            Realm realm = Realm.getDefaultInstance();
            String json = null;
            try {
                for (int i = 10; i <= 100; i += 10) {
                    Thread.sleep(1000);
                    //Progress
                    publishProgress(i);
                }
            } catch (Exception ex) {
                Log.e("TAG", ex.getMessage());
                realm.close();
            } finally {
                realm.close();
            }
            return json;
        }

        //Main thread
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Save and process Json String
        }
    }

    private class DownloadFilesIntentService extends IntentService {

        public DownloadFilesIntentService() {
            super("DownloadFiles");
        }

        //Worker Thread (another thread)
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            Realm realm = Realm.getDefaultInstance();
            try {

            } catch (Exception ex) {
                Log.e("TAG", ex.getMessage());
                realm.close();
            } finally {
                realm.close();
            }
        }
    }
}

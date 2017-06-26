package vazquez.rodrigo.realm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vazquez.rodrigo.realm.Adapters.GithubAdapter;
import vazquez.rodrigo.realm.Models.GitHub;

public class ThreadExampleActivity extends AppCompatActivity {

    Realm realm;
    private RealmChangeListener realmChangeListener;
    private ListView lstGithub;
    private GithubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_example);
        realm = Realm.getDefaultInstance();
        setUpListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void setUpListView(){
        lstGithub = (ListView)findViewById(R.id.lstGithub);
        RealmResults<GitHub> results = realm.where(GitHub.class).findAllAsync();
        if(results.isLoaded()){
            adapter = new GithubAdapter(results);
            lstGithub.setAdapter(adapter);
        }
    }
}

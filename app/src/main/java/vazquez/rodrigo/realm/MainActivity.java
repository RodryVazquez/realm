package vazquez.rodrigo.realm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vazquez.rodrigo.realm.Adapters.MyAdapter;
import vazquez.rodrigo.realm.Models.DogModel;
import vazquez.rodrigo.realm.Models.PersonModel;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Variables globales
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Al cargar la actividad
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(new String[]{"Rodrigo","Kristel"});
        mRecyclerView.setAdapter(mAdapter);

        testRealmData(this);
    }

    /**
     *
     * @param mContext
     */
    public void  testRealmData(Context mContext){

        DogModel dogModel =  new DogModel();
        dogModel.setName("krispy");
        dogModel.setAge(11);

        Realm.init(mContext);
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<DogModel> dogModelRealmResults = realm
                                                            .where(DogModel.class)
                                                            .lessThan("age",2)
                                                            .findAll();
        final int dsSize = dogModelRealmResults.size();

        realm.beginTransaction();
        final DogModel managedDog = realm.copyToRealm(dogModel);
        PersonModel personModel = realm.createObject(PersonModel.class);
        personModel.getDogs().add(managedDog);

        realm.commitTransaction();

        dogModelRealmResults.addChangeListener(new RealmChangeListener<RealmResults<DogModel>>() {
            @Override
            public void onChange(RealmResults<DogModel> element) {
              int  ds = dogModelRealmResults.size();
            }
        });


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DogModel dog = realm.where(DogModel.class).equalTo("age",11).findFirst();
                dog.setAge(12);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                int newDs = dogModelRealmResults.size();
                int dogAge = managedDog.getAge();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("RealmError",error.getMessage());
            }
        });
    }
}

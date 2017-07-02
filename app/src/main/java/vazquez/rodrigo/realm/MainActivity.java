package vazquez.rodrigo.realm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import vazquez.rodrigo.realm.Adapters.MyAdapter;
import vazquez.rodrigo.realm.Models.Car;
import vazquez.rodrigo.realm.Models.DogModel;
import vazquez.rodrigo.realm.Models.PersonModel;
import vazquez.rodrigo.realm.Models.SocialAccount;
import vazquez.rodrigo.realm.Models.User;

/**
 * Actividad principal
 */
public class MainActivity extends AppCompatActivity {

    //TAG
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Referencias layout
    private EditText etPersonName, etAge, etSocialAccount, etStatus;

    //Realm Object y RealAsyncTask (util para transacciones asincronas)
    private Realm myRealm;
    private RealmAsyncTask realAsyncTask;

    RealmResults<User>  userList;


    /**
     * /**
     * Al cargar la actividad
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_form);

        etPersonName = (EditText) findViewById(R.id.etPersonName);
        etAge = (EditText) findViewById(R.id.etAge);
        etSocialAccount = (EditText) findViewById(R.id.etSocialAccount);
        etStatus = (EditText) findViewById(R.id.etStatus);

        //Obtenemos la instancia default
        myRealm = Realm.getDefaultInstance();

        /*mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(new String[]{"Rodrigo","Kristel"});
        mRecyclerView.setAdapter(mAdapter);
        testRealmData(this);*/

        Log.i(TAG,"Current Version" + myRealm.getVersion());
    }

    /**
     * Ejecuta una transaccion de manera sincrona
     * @param view
     */
    public void addUserToRealm_Synchronously(View view) {

        final String id = UUID.randomUUID().toString();
        final String name = etPersonName.getText().toString();
        final int age = Integer.valueOf(etAge.getText().toString());
        final String socialAccountName = etSocialAccount.getText().toString();
        final String status = etStatus.getText().toString();

        //Manual
        /*
        try{
            myRealm.beginTransaction();

            SocialAccount socialAccount = myRealm.createObject(SocialAccount.class);
            socialAccount.setName(socialAccountName);
            socialAccount.setStatus(status);


            User user = myRealm.createObject(User.class, id);
            user.setName(name);
            user.setAge(age);
            user.setSocialAccount(socialAccount);

            myRealm.commitTransaction();

        }catch(Exception ex){
             myRealm.cancelTransaction();
        }*/

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Creamos el objeto social account
                SocialAccount socialAccount = realm.createObject(SocialAccount.class);
                socialAccount.setName(socialAccountName);
                socialAccount.setStatus(status);

                //Creamos el objeto user
                User user = realm.createObject(User.class, id);
                user.setName(name);
                user.setAge(age);
                user.setSocialAccount(socialAccount);

                Toast.makeText(MainActivity.this, R.string.added_successfully, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Ejecuta una transaccion de manera asincrona
     * @param view
     */
    public void addUserToRealm_Asynchronously(View view) {

        final String id = UUID.randomUUID().toString();
        final String name = etPersonName.getText().toString();
        final int age = Integer.valueOf(etAge.getText().toString());
        final String socialAccountName = etSocialAccount.getText().toString();
        final String status = etStatus.getText().toString();

        //Guardamos la referencia en el objeto RealmAsyncTask ya que
        //Necesitamos saber si hay que cancelar la transaccion por importancia
        //de procesos (Jerarquia de Importancia)
        realAsyncTask = myRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SocialAccount socialAccount = realm.createObject(SocialAccount.class);
                socialAccount.setName(socialAccountName);
                socialAccount.setStatus(status);

                User user = realm.createObject(User.class, id);
                user.setName(name);
                user.setAge(age);
                user.setSocialAccount(socialAccount);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, R.string.added_successfully, Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, R.string.error_data_not_save, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Lista todos los registros
     * @param view
     */
    public void displayAllUsers(View view) {

        RealmResults<User> userList = myRealm.where(User.class).findAll();
        displayQueriedUsers(userList);
    }

    /**
     * Mustra el ds de RealmResults
     * @param userList
     */
    private void displayQueriedUsers(RealmResults<User> userList){
        StringBuilder builder = new StringBuilder();

        for (User user : userList) {
            builder.append("ID: ").append(user.getId());
            builder.append("\nName: ").append(user.getName());
            builder.append(", Age: ").append(user.getAge());


            SocialAccount socialAccount = user.getSocialAccount();
            builder.append("\nS'Account: ").append(socialAccount.getName());
            builder.append(", Status: ").append(socialAccount.getStatus()).append(" .\n\n");
        }

        Log.i(TAG + "Lists Results", builder.toString());
    }

    /**
     * Uso de Fluid Interface y Predicates
     * @param view
     */
    public void sampleQueryExample(View view){
        //Uso de Fluid Interface
        RealmResults<User> userList2 = myRealm.where(User.class)
                .greaterThan("age",28)
                .contains("name","rodrigo", Case.INSENSITIVE)
                .findAll();
        displayQueriedUsers(userList2);
    }

    /**
     * Delete, Update, Sort, Operators
     */
    public void groupData(){

        //AND OR operators
        RealmResults<User> users =  myRealm.where(User.class)
                .contains("name", "Rodry", Case.INSENSITIVE) //AND
                .beginGroup()
                .lessThan("age",50)
                .or() //Explicity OR
                .greaterThan("age",40)
                .endGroup()
                .findAll();


        //Ordenamos con base al nombre de la Social Account
        users = myRealm.where(User.class)
                .findAll()
                .sort("socialAccount.name");


        //Actualizar un registro
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).findFirst();
                user.setName("Cochinilla");
                SocialAccount socialAccount = user.getSocialAccount();
                if(socialAccount != null){
                    socialAccount.setName("New Name");
                    socialAccount.setStatus("New Status");
                }
            }
        });

        //Tipos de borrado
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Borrar elemento especifico
                User user = realm.where(User.class).findFirst();
                user.deleteFromRealm();

                //Borrar de una coleccion
                RealmResults<User> userRealmResults = realm.where(User.class).findAll();
                userRealmResults.deleteFirstFromRealm(); //Borra el primer elemento de la lista
                userRealmResults.deleteLastFromRealm(); //Borra el ultimo elemento de la lista
                userRealmResults.deleteFromRealm(3); // Borra el elemento con base su posicion
                userRealmResults.deleteAllFromRealm(); //Borra todos los datos
            }
        });
    }

    //
    public void testRealmData(Context mContext) {

        DogModel dogModel = new DogModel();
        dogModel.setName("krispy");
        dogModel.setAge(11);

        Realm.init(mContext);
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<DogModel> dogModelRealmResults = realm
                .where(DogModel.class)
                .lessThan("age", 2)
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
                int ds = dogModelRealmResults.size();
            }
        });


        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DogModel dog = realm.where(DogModel.class).equalTo("age", 11).findFirst();
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
                Log.e("RealmError", error.getMessage());
            }
        });
    }


    /**
     * Cancelamos transacciones asincronas (Jerarquia de Importancia 'procesos')
     */
    @Override
    protected void onStop() {
        super.onStop();

        //Removemos los listeners
        if(userList != null){
            userList.removeChangeListener(userListListener); // remueve un listener especifico
            //userList.removeChangeListeners(); remueve todos los listeners

            /*
              listeners estan disponibles para
              Realm Instance
              Realm Object
              Realm Result<E>
              Realm List<E>
            */
        }

        if (realAsyncTask != null && !realAsyncTask.isCancelled()) {
            realAsyncTask.cancel();
        }
    }

    /**
     * Cerramos la instancia de realm
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }

    /**
     * Inicia un intent para mostrar la vista con el Recycler View
     * @param view
     */
    public void openDisplayActivity(View view) {
        Intent intent = new Intent(this, DisplayActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void exploreMiscConcepts(View view) {

        userList = myRealm.where(User.class).findAllAsync();
        userList.addChangeListener(userListListener);

        //Si la consulta ya termino
        //if(userList.isLoaded())
        //    userList.deleteFirstFromRealm();
    }

    /**
     * Listener se ejecuta cada vez que existe un cambio en el objeto que lo implementa
     */
    RealmChangeListener<RealmResults<User>> userListListener = new RealmChangeListener<RealmResults<User>>() {
        @Override
        public void onChange(RealmResults<User> element) {
            displayQueriedUsers(element);
        }
    };

    public void jsonExample(View view) {
        startActivity(JsonActivity.class);
    }

    public void threadExample(View view) {
        Car car = new Car(1289,"ChevyMonza", true);
        launchParcelObject(car);
    }


    private void launchParcelObject(Car model){
        Intent intent = new Intent(this, ThreadExampleActivity.class);
        intent.putExtra("ParcelableCar", model);
        startActivity(intent);
    }

    void startActivity(Class<? extends Activity> activityClass){
        startActivity(new Intent(this,activityClass));
    }
}

package vazquez.rodrigo.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *Inicializamos Realm
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        //Configuration Realm DB
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myFirstRealm.realm") //By default the name is default.realm
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}

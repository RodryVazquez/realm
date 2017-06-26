package vazquez.rodrigo.realm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;
import vazquez.rodrigo.realm.Models.City;
import vazquez.rodrigo.realm.Models.Company;
import vazquez.rodrigo.realm.Models.GitHub;
import vazquez.rodrigo.realm.Models.SocialAccount;
import vazquez.rodrigo.realm.Models.User;

/**
 *Inicializamos Realm
 */

public class MyApplication extends Application {

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        //Configuration Realm DB
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myFirstRealm.realm") //By default the name is default.realm
                .modules(new MyCustomModule())
                .schemaVersion(4) //por default es 0
                .migration(new MyMigration())
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    /**
     * Modulo custom
     * TODO Aplicar Migracion
     */
    @RealmModule (classes = {User.class, SocialAccount.class, Company.class, City.class, GitHub.class})
    public class MyCustomModule{}

    /**
     * Instancia dummy
     * @return
     */
    public static  Realm getAnotherInstance(){

        RealmConfiguration otherConfig = new RealmConfiguration.Builder()
                .name("another.realm")
                .build();
        return Realm.getInstance(otherConfig);
    }
}

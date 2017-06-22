package vazquez.rodrigo.realm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Rodrigo Vazquez on 22/06/2017.
 */

public class MyMigration implements RealmMigration {


    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        //Realizamos la migracion
        if(oldVersion == 0){
            RealmObjectSchema userSchema1 = schema.get("User");
            userSchema1.addField("hobby", String.class, FieldAttribute.REQUIRED);
            oldVersion++;
        }

        if(oldVersion == 1){

            //Agregamos a Company
            RealmObjectSchema companySchema = schema.create("Company");
            companySchema.addField("name",String.class);
            //Indicamos el nuevo objeto company en users
            RealmObjectSchema userSchema = schema.get("User");
            userSchema.addRealmObjectField("company", companySchema);

            oldVersion++;
        }
    }
}

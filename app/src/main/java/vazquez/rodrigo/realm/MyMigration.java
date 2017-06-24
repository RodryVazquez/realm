package vazquez.rodrigo.realm;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Clase para manejar las migraciones de la bd
 * Created by Rodrigo Vazquez on 22/06/2017.
 */

public class MyMigration implements RealmMigration {


    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        //Migramos de la version 0 a la 1
        if(oldVersion == 0){
            RealmObjectSchema userSchema1 = schema.get("User");
            userSchema1.addField("hobby", String.class, FieldAttribute.REQUIRED);
            oldVersion++;
        }

        //Migramos de la version 1 a la 2
        if(oldVersion == 1){

            //Agregamos a Company
            RealmObjectSchema companySchema = schema.create("Company");
            companySchema.addField("name",String.class);
            //Indicamos el nuevo objeto company en users
            RealmObjectSchema userSchema = schema.get("User");
            userSchema.addRealmObjectField("company", companySchema);

            oldVersion++;
        }

        //Migramos de la version 2 a 3
        if(oldVersion == 2){

            //Agregamos la tabla de city
            RealmObjectSchema citySchema = schema.create("City");
            citySchema.addField("name",String.class);
            citySchema.addField("votes", long.class);

            oldVersion++;
        }
        /*************************************************
        //Migramos de la version 3 a la 4
        if (oldVersion == 3) {
            RealmObjectSchema userSchema = schema.get("User");

            // Combine 'firstName' and 'lastName' in a new field called 'fullName'
            userSchema
                    .addField("fullName", String.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("fullName", obj.getString("firstName") + " " + obj.getString("lastName"));
                        }
                    })
                    .removeField("firstName")
                    .removeField("lastName");
            oldVersion++;
        }
        ****************************************************/
    }
}

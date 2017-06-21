package vazquez.rodrigo.realm.Models;

import io.realm.RealmObject;

/**
 * Created by Rodrigo Vazquez on 21/06/2017.
 */

public class Company extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

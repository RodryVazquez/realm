package vazquez.rodrigo.realm.Models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Rodrigo Vazquez on 04/03/2017.
 */

public class PersonModel extends RealmObject {

    private long id;
    private String name;
    private RealmList<DogModel>dogs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<DogModel> getDogs() {
        return dogs;
    }

    public void setDogs(RealmList<DogModel> dogs) {
        this.dogs = dogs;
    }
}

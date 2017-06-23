package vazquez.rodrigo.realm.Models;

import io.realm.RealmObject;

/**
 * Created by Rodry on 6/22/2017.
 */

public class City extends RealmObject {

    private String name;
    private long votes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
}

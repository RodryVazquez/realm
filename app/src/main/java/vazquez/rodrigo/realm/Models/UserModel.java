package vazquez.rodrigo.realm.Models;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Rodrigo Vazquez on 05/03/2017.
 */

public class UserModel extends RealmObject {

    private String name;
    private int age;

    @Ignore
    private int sessionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public boolean hasLongName(){
        return this.name.length() > 87;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

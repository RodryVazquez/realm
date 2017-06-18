package vazquez.rodrigo.realm;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;
import vazquez.rodrigo.realm.Models.SocialAccount;
import vazquez.rodrigo.realm.Models.User;

public class EditActivity extends AppCompatActivity {

    private EditText etPersonName, etAge, etSocialAccountName, etStatus;
    private Realm myRealm;

    Bundle bundle;
    int position;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");
        }

        myRealm = Realm.getDefaultInstance();
        RealmResults<User> userList = myRealm.where(User.class).findAll();
        user = userList.get(position);

        etPersonName = (EditText) findViewById(R.id.etPersonName);
        etAge = (EditText) findViewById(R.id.etAge);
        etSocialAccountName = (EditText) findViewById(R.id.etSocialAccount);
        etStatus = (EditText) findViewById(R.id.etStatus);

        setupViews(user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViews(User user) {

        etPersonName.setText(user.getName());
        etAge.setText(String.valueOf(user.getAge()));

        SocialAccount socialAccount = user.getSocialAccount();
        if (socialAccount != null) {
            etSocialAccountName.setText(socialAccount.getName());
            etStatus.setText(socialAccount.getStatus());
        }
    }

    public void update(View view) {
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SocialAccount socialAccount = user.getSocialAccount();
                if (socialAccount != null) {
                    socialAccount.setName(etSocialAccountName.getText().toString().trim());
                    socialAccount.setStatus(etStatus.getText().toString().trim());
                }

                user.setName(etPersonName.getText().toString().trim());
                user.setAge(Integer.valueOf(etAge.getText().toString()));
                user.setSocialAccount(socialAccount);
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}

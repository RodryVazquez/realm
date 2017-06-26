package vazquez.rodrigo.realm.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vazquez.rodrigo.realm.Models.GitHub;

/**
 * Created by Rodrigo Vazquez on 26/06/2017.
 */

public class PollingService extends IntentService {

    public PollingService() {
        super("PollingService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Realm realm = Realm.getDefaultInstance();
        try {
            String url = intent.getExtras().getString("url");
            final String response = fetchData(new OkHttpClient(), url);
            if (response != null && response.length() > 0) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObjectFromJson(GitHub.class, response);
                    }
                });
            }
        } catch (IOException ex) {
            Log.e("PollingService", ex.getMessage());
        } finally {
            realm.close();
        }
    }

    private String fetchData(OkHttpClient client, String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

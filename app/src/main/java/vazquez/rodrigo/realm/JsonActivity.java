package vazquez.rodrigo.realm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.internal.IOException;
import vazquez.rodrigo.realm.Adapters.CityAdapter;
import vazquez.rodrigo.realm.Models.City;

public class JsonActivity extends AppCompatActivity {

    private static final String TAG = JsonActivity.class.getName();

    private GridView citiesList;
    private CityAdapter cityAdapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(cityAdapter == null){
            List<City> cities = null;
            try{

                cities = loadCities();
                cityAdapter = new CityAdapter(this);
                cityAdapter.setData(cities);

                citiesList = (GridView)findViewById(R.id.cities_list);
                citiesList.setAdapter(cityAdapter);
                cityAdapter.notifyDataSetChanged();
                citiesList.invalidate();

            } catch (java.io.IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private List<City> loadCities() throws java.io.IOException{

        loadJsonFromString();
        loadJsonFromJsonObject();
        loadJsonFromStream();

        return  realm.where(City.class).findAll();
    }

    private void loadJsonFromString(){
        final String json = "{ name: \"Cochinilla City\", votes: 89}";
        //Guardamos la cadena JSON
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class,json);
            }
        });
    }

    private void loadJsonFromJsonObject(){
        Map<String, String> city = new HashMap<String, String>();
        city.put("name","Cochinilla City 2");
        city.put("votes","9");

        final JSONObject jsonObject = new JSONObject(city);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class, jsonObject);
            }
        });
    }

    private void loadJsonFromStream() throws java.io.IOException {

        InputStream inputStream = getAssets().open("cities.json");

        realm.beginTransaction();
        try{
            realm.createAllFromJson(City.class, inputStream);
            realm.commitTransaction();
        }catch (IOException ex){
            realm.cancelTransaction();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }
}

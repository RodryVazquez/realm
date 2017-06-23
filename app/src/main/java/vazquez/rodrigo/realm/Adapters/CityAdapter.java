package vazquez.rodrigo.realm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import vazquez.rodrigo.realm.Models.City;
import vazquez.rodrigo.realm.R;

/**
 * Base Adapter
 * Created by Rodry on 6/22/2017.
 */

public class CityAdapter extends BaseAdapter {

    private static final String TAG = CityAdapter.class.getName();

    private LayoutInflater inflater;

    private List<City> cities = null;

    public CityAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(cities == null){
            return 0;
        }
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        if(cities == null || cities.get(i) == null){
            return null;
        }
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.city_list_item, viewGroup, false);
        }
        
        City city = (City) getItem(i);
        if(city != null){
            ((TextView) view.findViewById(R.id.name)).setText(city.getName());
            ((TextView) view.findViewById(R.id.votes)).setText(String.valueOf(city.getVotes()));
        }
        return view;
    }


    /**
     * Asignamos el ds
     * @param cities
     */
    public void setData(List<City> cities){
        this.cities = cities;
    }
}

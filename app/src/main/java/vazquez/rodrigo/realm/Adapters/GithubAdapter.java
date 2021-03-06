package vazquez.rodrigo.realm.Adapters;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmModel;
import vazquez.rodrigo.realm.Models.GitHub;
import vazquez.rodrigo.realm.R;

/**
 * Created by Rodry on 6/25/2017.
 */

public class GithubAdapter extends RealmBaseAdapter {

    public GithubAdapter(@Nullable OrderedRealmCollection data) {
        super(data);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public RealmModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_adapter, viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.txtItem = (TextView)view.findViewById(R.id.txtName);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        if(adapterData != null){
            final GitHub item = (GitHub) getItem(i);
            viewHolder.txtItem.setText(item.getName());
        }
        return view;
    }

    private static class ViewHolder{
        TextView txtItem;
    }
}

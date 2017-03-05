package vazquez.rodrigo.realm.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vazquez.rodrigo.realm.R;

/**
 *
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    /**
     *
     */
    public String[] mDataSet;

    /**
     *
     * @param mDataset
     */
    public MyAdapter(String[] mDataset) {
        this.mDataSet = mDataset;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    /**
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    /**
     *
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}

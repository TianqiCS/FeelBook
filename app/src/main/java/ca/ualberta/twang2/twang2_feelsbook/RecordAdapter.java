package ca.ualberta.twang2.twang2_feelsbook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * the adapter used to control recycleView for history list
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder> {
    private static ArrayList<Record> records;

    // Provide a reference to the views for each data item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView record;
        MyViewHolder(TextView v) {
            super(v);
            record = v;
        }
    }

    // Provide a suitable constructor
    RecordAdapter(RecordHistory recordHistory) {
        records = recordHistory.getRecords();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_record, parent, false);
        v.setMaxLines(6);  // not show all the information for a subscription
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // get element from the dataset at this position and replace the contents of the view with that element
        holder.record.setText(records.get(position).toString());

    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return records.size();
    }

}
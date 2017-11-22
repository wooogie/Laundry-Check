package ch.meienberger.android.laundrycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.meienberger.android.laundrycheck.R;
import ch.meienberger.android.laundrycheck.Washorder;

/**
 * This adapter makes the onClick-listener on a RecyclerView possible
 */

public class WashorderRecyclerViewContentAdapter extends RecyclerView.Adapter<WashorderRecyclerViewContentAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Washorder item);
    }

    private final List<Washorder> items;
    private final OnItemClickListener listener;

    public WashorderRecyclerViewContentAdapter(List<Washorder> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.washorder_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


        static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.washorderRowName);
            }

            public void bind(final Washorder item, final OnItemClickListener listener) {

                name.setText(item.getName());
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item);
                    }
                });
            }
        }

}

package ch.meienberger.android.laundrycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import ch.meienberger.android.laundrycheck.adapter.WashorderAdapter;

/**
 * Created by studio1 on 10.06.2016.
 */
public class WashorderTouchCallback extends ItemTouchHelper.SimpleCallback {


    private WashorderAdapter mWashorderAdapter;

    public WashorderTouchCallback(WashorderAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
        mWashorderAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mWashorderAdapter.removeItemAt(viewHolder.getAdapterPosition());
    }
}

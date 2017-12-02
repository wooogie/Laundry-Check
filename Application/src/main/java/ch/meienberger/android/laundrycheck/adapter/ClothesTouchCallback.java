package ch.meienberger.android.laundrycheck.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by studio1 on 10.06.2016.
 */
public class ClothesTouchCallback extends ItemTouchHelper.SimpleCallback {


    private ClothesAdapter mClothesAdapter;

    public ClothesTouchCallback(ClothesAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
        mClothesAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mClothesAdapter.removeItemAt(viewHolder.getAdapterPosition());
    }
}

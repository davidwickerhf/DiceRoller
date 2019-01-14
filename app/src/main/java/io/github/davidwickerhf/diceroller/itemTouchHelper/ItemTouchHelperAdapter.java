package io.github.davidwickerhf.diceroller.itemTouchHelper;


import java.util.ArrayList;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onClearView();
}

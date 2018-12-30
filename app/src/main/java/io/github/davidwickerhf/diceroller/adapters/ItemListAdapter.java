package io.github.davidwickerhf.diceroller.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.davidwickerhf.diceroller.AddSettingActivity;
import io.github.davidwickerhf.diceroller.R;
import io.github.davidwickerhf.diceroller.itemTouchHelper.ItemTouchHelperAdapter;
import io.github.davidwickerhf.diceroller.itemTouchHelper.ItemTouchHelperViewHolder;

public class ItemListAdapter  extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private OnListItemClickListener itemClickListener;
    public interface OnListItemClickListener {
        void onItemClick(int position, View itemView);
    }
    public void setOnItemClickLister(OnListItemClickListener listener){
        itemClickListener = listener;
    }

    public ItemListAdapter(){
    }


    //todo Views
    public View itemView;

    //todo Variables
    ArrayList<String> items = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_item, parent, false);


        return new ItemViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String currentItem = items.get(position);
        holder.textViewItemString.setText(currentItem);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String prev = items.remove(fromPosition);
        items.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    //todo GET ITEM COUNT  to return to setting holder as Max Number
    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<String> getItems(){
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    //todo ITEM HOLDER
    class ItemViewHolder extends RecyclerView.ViewHolder  implements ItemTouchHelperViewHolder {
        private TextView textViewItemString;


        public ItemViewHolder(@NonNull final View itemView, final OnListItemClickListener itemClickListener) {
            super(itemView);
            textViewItemString = itemView.findViewById(R.id.text_view_item_string);

            Log.d("Dicee", "Item view; " + itemView);

            //On Item View Bring String to Edit Text
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {

                            itemClickListener.onItemClick(position, itemView);
                            //itemView.setBackgroundResource(R.drawable.list_item_selected);

                        }
                    }
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}

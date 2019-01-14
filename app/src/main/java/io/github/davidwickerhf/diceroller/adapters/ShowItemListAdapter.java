package io.github.davidwickerhf.diceroller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.davidwickerhf.diceroller.R;

public class ShowItemListAdapter extends RecyclerView.Adapter<ShowItemListAdapter.ItemHolder>{

    //Variables
    ArrayList<String> items = new ArrayList<>();
    // Views
    public View itemView;


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_item_list_item, parent, false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        String currentItem = items.get(position);
        holder.textViewItemString.setText(currentItem);
        holder.itemPositionTextView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    //todo ITEM HOLDER
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewItemString;
        private TextView itemPositionTextView;

        public ItemHolder(@NonNull final View itemView) {
            super(itemView);
            textViewItemString = itemView.findViewById(R.id.show_item_list_recycler_view_title_text);
            itemPositionTextView = itemView.findViewById(R.id.item_number_text_view);
        }

    }
}


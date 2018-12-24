package io.github.davidwickerhf.diceroller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemListAdapter  extends RecyclerView.Adapter<ItemListAdapter.ItemHolder> {

    private OnListItemClickListener itemClickListener;
    public interface OnListItemClickListener {
        void onItemClick(int position, View itemView);
    }
    public void setOnItemClickLister(OnListItemClickListener listener){
        itemClickListener = listener;
    }

    //todo Views
    public View itemView;

    //todo Variables
    List<String> items = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_item, parent, false);

        return new ItemHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        String currentItem = items.get(position);
        holder.textViewItemString.setText(currentItem);
    }

    //todo GET ITEM COUNT  to return to setting holder as Max Number
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


        public ItemHolder(@NonNull final View itemView, final OnListItemClickListener itemClickListener) {
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

    }
}

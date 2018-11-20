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


    private OnDeleteItemClickListener deleteListener;
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(int position);
    }
    public void setOnDeleteItemClickLister(OnDeleteItemClickListener listener){
        deleteListener = listener;
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

        return new ItemHolder(itemView, itemClickListener, deleteListener);
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
        private ImageView deleteItemButtonView;


        public ItemHolder(@NonNull final View itemView, final OnListItemClickListener itemClickListener, final OnDeleteItemClickListener deleteListener) {
            super(itemView);
            textViewItemString = itemView.findViewById(R.id.text_view_item_string);
            deleteItemButtonView = itemView.findViewById(R.id.delete_item_button);

            Log.d("Dicee", "Item view; " + itemView);

            //Create Delete Item Button
            deleteItemButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener != null) {
                        int position = getAdapterPosition();
                        Log.d("Dicee", "position: " + position);

                        if(position != RecyclerView.NO_POSITION) {
                            deleteListener.onDeleteItemClick(position);
                        }
                    }
                }
            });

            //On Item View Bring String to Edit Text
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            //todo Setting Resources
                            itemView.setBackgroundResource(R.drawable.recycler_view_selected_background);

                            itemClickListener.onItemClick(position, itemView);


                        }
                    }
                }
            });
        }

    }
}

package io.github.davidwickerhf.diceroller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemListAdapter  extends RecyclerView.Adapter {

    //todo Views
    public View itemView;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_item, parent, false);

        return new ItemListAdapter.ItemHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewMaxNum;
        private ImageView editButtonView;


        public ItemHolder(@NonNull final View itemView, final SettingAdapter.OnItemClickListener deleteListener, final SettingAdapter.OnEditItemClickListener deleteListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewMaxNum = itemView.findViewById(R.id.text_view_maxnumber);
            editButtonView = itemView.findViewById(R.id.edit_setting_button);

            Log.d("Dicee", "Item view; " + itemView);

            editSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener != null) {
                        int position = getAdapterPosition();
                        Log.d("Dicee", "position: " + position);

                        if(position != RecyclerView.NO_POSITION) {
                            Log.d("Dicee", "setting: " + settings.get(position));
                            deleteListener.onEditItemClick(position);
                        }
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            //todo Setting Resources
                            itemView.setBackgroundResource(R.drawable.recycler_view_selected_background);

                            deleteListener.onItemClick(position, itemView);


                        }
                    }
                }
            });
        }

    }
}

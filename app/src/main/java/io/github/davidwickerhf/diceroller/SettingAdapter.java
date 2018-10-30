package io.github.davidwickerhf.diceroller;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;



public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingHolder> {
    
    private int LAYOUT_ONE = 1;
    private int LAYOUT_TWO = 2;
    
    private List<Setting> settings = new ArrayList<>();
    private ImageView editSettingButton;
    
    private OnItemClickListener mListener;
    
    public View itemView;
    private int selectedItemPosition;
    private DashboardFragment dashboard;
    
    public interface OnItemClickListener {
        void onItemClick(int position, View itemView, TextView textViewTitle, TextView textViewMaxNum, ImageView editButtonView);
    }
    
    public void setOnItemClickLister(OnItemClickListener listener){
        mListener = listener;
    }
    
    
    
    private OnEditItemClickListener editListener;
    
    public interface OnEditItemClickListener {
        void onEditItemClick(int position);
    }
    
    public void setOnEditItemClickLister(OnEditItemClickListener listener){
        editListener = listener;
    }
    
    
    
    @NonNull
    @Override
    public SettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        
        dashboard = new DashboardFragment();
        selectedItemPosition = dashboard.selectedItemPosition;
        
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_item, parent, false);
        itemView.setBackgroundResource(R.drawable.recycler_view_background);
        editSettingButton = itemView.findViewById(R.id.edit_setting_button);
    
        return new SettingHolder(itemView, mListener, editListener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SettingHolder holder, int position) {
        Setting currentSetting = settings.get(position);
        holder.textViewTitle.setText(currentSetting.getTitle());
        holder.textViewMaxNum.setText(String.valueOf(currentSetting.getMaxDiceSum()));
        holder.editButtonView.setImageResource(R.drawable.ic_edit);
    
    }
    
    @Override
    public int getItemCount() {
        return settings.size();
    }
    
    //todo This function will change from notifyDataSetChanged
    public void setSettings (List<Setting> settings){
        this.settings = settings;
        notifyDataSetChanged();
    }
    
    public List<Setting> getSettings(){
        return settings;
    }
    
    public Setting getSettingAt(int position){
        return settings.get(position);
    }
    
    class SettingHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewMaxNum;
        private ImageView editButtonView;
    
    
        public SettingHolder(@NonNull final View itemView, final OnItemClickListener listener,final OnEditItemClickListener editListener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewMaxNum = itemView.findViewById(R.id.text_view_maxnumber);
            editButtonView = itemView.findViewById(R.id.edit_setting_button);
    
            Log.d("Dicee", "Item view; " + itemView);
    
            editSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(editListener != null) {
                        int position = getAdapterPosition();
                        Log.d("Dicee", "position: " + position);
                
                        if(position != RecyclerView.NO_POSITION) {
                            Log.d("Dicee", "setting: " + settings.get(position));
                            editListener.onEditItemClick(position);
                        }
                    }
                }
            });
            
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            //todo Setting Resources
                            itemView.setBackgroundResource(R.drawable.recycler_view_selected_background);
                            textViewTitle.setTextColor(Color.parseColor("#ffffff"));
                            textViewMaxNum.setTextColor(Color.parseColor("#ffffff"));
                            editButtonView.setImageResource(R.drawable.ic_edit_selected);
                            
                            listener.onItemClick(position, itemView, textViewTitle, textViewMaxNum, editButtonView);
                            
                            
                        }
                    }
                }
            });
        }
        
    }
    
    
    
    
    
    
    
}

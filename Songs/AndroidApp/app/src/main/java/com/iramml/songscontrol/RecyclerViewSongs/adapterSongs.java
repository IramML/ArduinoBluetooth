package com.iramml.songscontrol.RecyclerViewSongs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iramml.songscontrol.R;

import java.util.ArrayList;

public class adapterSongs extends RecyclerView.Adapter<adapterSongs.ViewHolder>{
    Context context;
    ArrayList<String> items;
    ClickListener listener;

    public adapterSongs(Context context, ArrayList<String> items, ClickListener listener){
        this.context=context;
        this.items=items;
        this.listener=listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.template_songs,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;
        ClickListener listener;
        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            this.listener=listener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            this.listener.onClick(view, getAdapterPosition());
        }
    }
}
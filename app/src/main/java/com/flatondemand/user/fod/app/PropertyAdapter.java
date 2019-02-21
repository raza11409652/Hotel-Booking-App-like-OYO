package com.flatondemand.user.fod.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flatondemand.user.fod.R;

import java.util.List;

public class PropertyAdapter  extends RecyclerView.Adapter<PropertyAdapter.MyViewHolder>{
    private Context context;
    private List<Property> propertyList;
    public PropertyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
           name = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }
    @NonNull
    @Override
    public PropertyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.store_propety_item,viewGroup, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Property property = propertyList.get(position);
        holder.name.setText(property.getTitle());
        holder.price.setText(property.getPrice());

        Glide.with(context)
                .load(property.getImage())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }
}

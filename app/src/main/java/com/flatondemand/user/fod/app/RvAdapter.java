package com.flatondemand.user.fod.app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flatondemand.user.fod.PropertyView;
import com.flatondemand.user.fod.R;
import com.flatondemand.user.fod.model.PropertyList;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    RequestOptions options ;
    private Context mContext ;
    private List<PropertyList> mData;
   SQLiteHandler sqLiteOpenHelper;
    public RvAdapter(Context mContext, List lst) {


        this.mContext = mContext;
        this.mData = lst;
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.store_propety_item,parent,false);
        // click listener here
        final MyViewHolder viewHolder = new MyViewHolder(view) ;

       /* viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent propertyView = new Intent(mContext , PropertyView.class);
                //sendind data
                propertyView.putExtra("propertyName",mData.get(viewType).getProperty());
                propertyView.putExtra("propeertyPrice", mData.get(viewType).getPrice());
                propertyView.putExtra("propertyFullAdd" , mData.get(viewType).getFulladdress());

                mContext.startActivity(propertyView);
            }
        });
        */
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       // Intent intent;//= getIntent();
      //  intent=mContext.getApplicationContext().getI
        holder.propertyName.setText(mData.get(position).getProperty());
        holder.propertyPrice.setText(mData.get(position).getPrice());
      //  holder.tvstudio.setText(mData.get(position).getStudio());
      //  holder.propertyImage.setText(mData.get(position).getCategorie());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * public void addProperty(String propertyName , String propertyPrice , String propertyUid ,
                            String propertyCoverImage , String propertyAdd)
                * */
                sqLiteOpenHelper= new SQLiteHandler(mContext);
                sqLiteOpenHelper.addProperty(mData.get(position).getProperty() ,mData.get(position).getPrice(),
                        mData.get(position).getUid() ,mData.get(position).getCoverImage(), mData.get(position).getAddress());
                Intent propertyView = new Intent(mContext , PropertyView.class);
                //sendind data
                propertyView.putExtra("propertyName",mData.get(position).getProperty());
                propertyView.putExtra("propeertyPrice", mData.get(position).getPrice());
                propertyView.putExtra("propertyFullAdd" , mData.get(position).getFulladdress());
                propertyView.putExtra("adress",mData.get(position).getAddress());
                propertyView.putExtra("coverImage",mData.get(position).getCoverImage());
                propertyView.putExtra("uid",mData.get(position).getUid());
                propertyView.putExtra("price",mData.get(position).getPrice());
                mContext.startActivity(propertyView);
            }
        });
        // load image from the internet using Glide
        Glide.with(mContext).load(mData.get(position).getCoverImage()).apply(options).into(holder.propertyImage);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView propertyName,propertyPrice;
        ImageView propertyImage;
        CardView view_container;



        public MyViewHolder(View itemView) {
            super(itemView);
            view_container=itemView.findViewById(R.id.card_view);
            propertyName = itemView.findViewById(R.id.title);
           propertyPrice=itemView.findViewById(R.id.price);
           propertyImage=itemView.findViewById(R.id.thumbnail);


        }
    }
}

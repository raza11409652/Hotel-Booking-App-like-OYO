package com.flatondemand.user.fod.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.flatondemand.user.fod.Dash;
import com.flatondemand.user.fod.R;
import com.flatondemand.user.fod.SearchForProperty;

import java.util.ArrayList;
import java.util.List;

public class LocationActivityAdapter extends RecyclerView.Adapter<LocationActivityAdapter.HolderView>   {
    List<Location> list= new ArrayList<>();
    private Context context;
SQLiteHandler sqLiteHandler;

    public LocationActivityAdapter(List<Location>list , Context context)
    {
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_location,viewGroup ,false);
//sqLiteHandler= new SQLiteHandler()
        return new HolderView(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderView holderView, final int i) {

        holderView.textView.setText(list.get(i).getName());
        holderView.imageView.setImageResource(list.get(i).getImage());
        holderView.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goto_main= new Intent(context , SearchForProperty.class);
               // goto_main.set
                //set location in database;
                sqLiteHandler= new SQLiteHandler(context);
                sqLiteHandler.addLocation(list.get(i).getName());
                goto_main.putExtra("Location" , list.get(i).getName());
                context.startActivity(goto_main);

                //Toast.makeText(context , "Clikc On"+list.get(i).getName() , Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(List<Location> locations){
        list= new ArrayList<>();
        list.addAll(locations);
        notifyDataSetChanged();
    }
    class  HolderView extends RecyclerView.ViewHolder{

       ImageView imageView;
       TextView textView;


        public HolderView(@NonNull View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.locationImage);
            textView=(TextView)itemView.findViewById(R.id.locationTitle);

        }
    }
}

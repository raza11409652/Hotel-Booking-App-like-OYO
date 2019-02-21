package com.flatondemand.user.fod;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.flatondemand.user.fod.app.Location;
import com.flatondemand.user.fod.app.LocationActivityAdapter;

import java.util.ArrayList;
import java.util.List;

public class LocationSearch extends AppCompatActivity {
    SearchView searchView;

    LocationActivityAdapter activityAdapter;
    RecyclerView recyclerView;
    List<Location> list= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        list.add(new Location("Deep Nagar" , R.drawable.ic_location_on_black_24dp));
        list.add(new Location("Law gate " ,R.drawable.ic_location_on_black_24dp));
        list.add(new Location("jalandhar" , R.drawable.ic_location_on_black_24dp));
        list.add(new Location("Phagwara" , R.drawable.ic_location_on_black_24dp));
        list.add(new Location("Jalandhar Cant" , R.drawable.ic_location_on_black_24dp));
        recyclerView = (RecyclerView)findViewById(R.id.locationList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        activityAdapter=new LocationActivityAdapter(list , LocationSearch.this);
        recyclerView.setAdapter(activityAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_file , menu);
        final MenuItem menuItem=menu.findItem(R.id.search);
        searchView=(SearchView) menuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchView.isIconified())
                {
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                final  List<Location> filtermodelist=filter(list,s);
                activityAdapter.setFilter(filtermodelist);
                return true;

            }

        });


        return true;
    }
    private List<Location> filter(List<Location> pl,String query)
    {
        query=query.toLowerCase();
        final List<Location> filteredModeList=new ArrayList<>();
        for (Location model:pl)
        {
            final String text=model.getName().toLowerCase();
            if (text.startsWith(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    private void changeSearchViewTextColor(View view) {
        if(view !=null)
        {
            if(view instanceof TextView)
            {
                ((TextView) view).setTextColor(Color.WHITE);
            }else if(view instanceof ViewGroup){
                ViewGroup viewGroup= (ViewGroup)view;
                for(int i=0; i <viewGroup.getChildCount() ; i++)
                {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

}

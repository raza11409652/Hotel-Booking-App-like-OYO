package com.flatondemand.user.fod;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import com.karan.churi.PermissionManager.PermissionManager;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.ContextCompat.getSystemService;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment  {

    private EditText locationInput;
    Button btnSearch , btn_location_gps;
    Intent intent;//= getIntent();
    String Location="" , errorMessage="";
    PermissionManager permissionManager;

    public HomeFrag() {
        // Required empty public constructor
    }
    public static HomeFrag newInstance(String param1, String param2) {
       HomeFrag fragment = new HomeFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.checkResult(requestCode , permissions ,grantResults);
        ArrayList<String>granted= permissionManager.getStatus().get(0).granted;
        ArrayList<String>denied= permissionManager.getStatus().get(0).denied;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view= inflater.inflate(R.layout.fragment_home, container, false);
    locationInput=(EditText)view.findViewById(R.id.locationBox);

     //geocoder=   new Geocoder(getContext(), Locale.getDefault());
    //btn_location_gps=(Button)view.findViewById(R.id.find_property_by_location);


        intent=getActivity().getIntent();
        if(intent.hasExtra("Location"))
        {
            Location=intent.getStringExtra("Location");
        }else{
            Location="";
        }
        locationInput.setText(Location);
        locationInput.setSelected(false);
       // locationInput.setFocusable(getActivit.NOT_FOCUSABLE);
        locationInput.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent locationSearch= new Intent(getContext() , LocationSearch.class);
            startActivity(locationSearch);
            getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in , android.support.v7.appcompat.R.anim.abc_fade_out);
        }
    });




        return view;

    }

    private void locationPermission() {
        permissionManager= new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(getActivity());
       // if(permissionManager.getStatus().get(0).)
    }






    private void searchProperty(String location) {
        Intent searchProperty= new Intent(getContext() , SearchForProperty.class );
        searchProperty.putExtra("Location",location);
        startActivity(searchProperty);

        getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_popup_enter ,android.support.v7.appcompat.R.anim.abc_popup_exit );
    }





}

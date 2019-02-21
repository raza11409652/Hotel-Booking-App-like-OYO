package com.flatondemand.user.fod.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flatondemand.user.fod.BookingAll;
import com.flatondemand.user.fod.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bookings extends Fragment {

View view;
String UserMobile;
SessionManager sessionManager;
ProgressDialog progressDialog;
TextView countBooking , booking;
     public Bookings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_bookings, container, false);
       //ssseion manager
        sessionManager= new SessionManager(getContext());
        UserMobile=sessionManager.getLoggedInMobile();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        countBooking=(TextView)view.findViewById(R.id.countBooking);
        booking = (TextView)view.findViewById(R.id.booingView);
        //progressDialog.setTitle("U");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        fetchDetails(UserMobile);
        //bookingView
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingIntent=new Intent(getContext()  , BookingAll.class);
                getActivity().startActivity(bookingIntent);
                getActivity().overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
            }
        });
       return  view;
    }

    private void fetchDetails(final String userMobile) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.USER_DETAILS_COUNT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if (error==false){
                        String bookingCount=jsonObject.getString("booking");
                        countBooking.setText(bookingCount);
                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String>map=new HashMap<>();
                map.put("mobile" , userMobile);
                return  map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}

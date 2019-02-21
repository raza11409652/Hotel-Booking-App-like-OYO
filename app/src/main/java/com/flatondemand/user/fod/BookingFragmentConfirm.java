package com.flatondemand.user.fod;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flatondemand.user.fod.app.BookingsAdapter;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.SessionManager;
import com.flatondemand.user.fod.model.BookingList;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragmentConfirm extends Fragment {
    List<BookingList> list = new ArrayList<BookingList>();
    SessionManager sessionManager ;
    BookingsAdapter bookingsAdapter;
    String userMobile;
    View view;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    public BookingFragmentConfirm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_booking_fragment_confirm, container, false);
        sessionManager=new SessionManager(getContext());
        userMobile=sessionManager.getLoggedInMobile();
        recyclerView=(RecyclerView)view.findViewById(R.id.bookingRecyclerView);
        recyclerView.setHasFixedSize(true);
        progressDialog= new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        fetchBookings(userMobile);
        return  view;
    }



    private void fetchBookings(final String userMobile) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.FETCH_BOOKINGS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG " , response);
                progressDialog.dismiss();

                try {
                   JSONObject jsonObject = new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if(error==false){
                        JSONArray jsonArray= new JSONArray();
                        jsonArray=jsonObject.getJSONArray("booking");
                        for (int i=0;i<jsonArray.length() ; i++){
                            JSONObject booking= new JSONObject();
                            BookingList bookingList= new BookingList();
                            booking=jsonArray.getJSONObject(i);
                            String bookingID =booking.getString("id");
                            String bookedOn= booking.getString("bookedOn");
                            String bookingNo=booking.getString("bookingNo");
                            String dateFrom=booking.getString("dateFrom");
                            String dateTo=booking.getString("dateTo");
                            String propertyName=booking.getString("propertyName");
                            bookingList.setId(bookingID);
                            bookingList.setBookedOn(bookedOn);
                            bookingList.setPropertyName(propertyName);
                            bookingList.setBookingNo(bookingNo);
                            bookingList.setDateFrom(dateFrom);
                            bookingList.setDateTo(dateTo);
                            list.add(bookingList);
                        }
                        setAdapter(list);
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
                HashMap <String , String> map =  new HashMap<>();
                map.put("mobile" , userMobile);
                return map;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setAdapter(List<BookingList> list) {
/*
*  bookingAdapter= new BookingAdapter(list , getApplicationContext());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        bookingList.setLayoutManager(linearLayoutManager);
        bookingList.setAdapter(bookingAdapter);
* */
        bookingsAdapter=new BookingsAdapter(getContext() , list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bookingsAdapter);
    }

}

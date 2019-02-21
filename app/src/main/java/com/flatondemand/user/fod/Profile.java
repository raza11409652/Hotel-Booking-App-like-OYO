package com.flatondemand.user.fod;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.app.SessionManager;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    Button logout;
    SessionManager sessionManager;
    SQLiteHandler sqLiteHandler;
    AlertDialog alertDialog;
    FancyAlertDialog fancyAlertDialog;
    String userName , userMobile , userEmail;
    TextView nameView, mobileView,emailView , fatherName , address, fatherMobile ;
    TextView fodName , fodAddress , fodContactPerson , fodContactMobile;
    RelativeLayout relativeLayout , noBooking;
    HashMap<String  , String> map= new HashMap<>();
    private ShimmerFrameLayout mShimmerViewContainer ,shimmerFrameLayout;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_profile, container, false);
        mShimmerViewContainer =view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container_fod);
        sessionManager = new SessionManager(view.getContext());
        sqLiteHandler= new SQLiteHandler(view.getContext());
        logout=(Button)view.findViewById(R.id.logout_btn);
        map=sqLiteHandler.getUserDetails();
        userName="Mr."+map.get("name");
        userEmail=map.get("email");
        //Toast.makeText(getContext() , ""+map ,Toast.LENGTH_LONG).show();
        relativeLayout=(RelativeLayout)view.findViewById(R.id.no_detailsFoundsLayout);
        noBooking=(RelativeLayout)view.findViewById(R.id.no_fodFoundsLayout);
        //Text view
        nameView=(TextView)view.findViewById(R.id.name);
        mobileView=(TextView)view.findViewById(R.id.mobile);
        emailView=(TextView)view.findViewById(R.id.emailUser);
        fatherName=(TextView)view.findViewById(R.id.fatherName);
        address=(TextView)view.findViewById(R.id.address);
        fatherMobile=(TextView)view.findViewById(R.id.fatherMobile);
        fodName=(TextView)view.findViewById(R.id.fod_name);
        fodAddress=(TextView)view.findViewById(R.id.fod_address);
        fodContactPerson=(TextView)view.findViewById(R.id.fod_contact_name);
        fodContactMobile=(TextView)view.findViewById(R.id.fod_contact_mobile);
        userMobile=sessionManager.getLoggedInMobile();
        alertDialog= new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert !!");
        alertDialog.setMessage("You will be logout from this device");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessionManager.setLogout(false);
                Intent go_to= new Intent(getContext() , MainActivity.class);
                startActivity(go_to);
                getActivity().finish();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        try {
            mobileView.setText("+91-"+userMobile);
            nameView.setText(userName);
            emailView.setText(userEmail);
            mShimmerViewContainer.startShimmerAnimation();
        }catch (Exception e){
            e.printStackTrace();
        }
        fetchUserDetails(userMobile);
        fetchFodDetails(userMobile);

       // mShimmerViewContainer.setVisibility(View.GONE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext() , ""+logout ,Toast.LENGTH_SHORT).show();
                alertDialog.show();
            }
        });

    return  view;
    }

    private void fetchFodDetails(final String userMobile) {
        shimmerFrameLayout.startShimmerAnimation();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.CURRENT_FOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.d("TAG" , response);
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if (error == false){
                        shimmerFrameLayout.stopShimmerAnimation();
                        String propertyName = jsonObject.getString("propertyName");
                        String propertyAddress=jsonObject.getString("propertyAddress");
                        String caretaker=jsonObject.getString("propertyCareTakerName");
                        String mobile=jsonObject.getString("propertyCareTakerMobile");
                        fodName.setBackgroundColor(Color.parseColor("#ffffff"));
                        fodAddress.setBackgroundColor(Color.parseColor("#ffffff"));
                       fodContactPerson.setBackgroundColor(Color.parseColor("#ffffff"));
                       fodContactMobile.setBackgroundColor(Color.parseColor("#ffffff"));
                        fodName.setText(propertyName);
                        fodAddress.setText(propertyAddress);
                        fodContactPerson.setText(caretaker);
                        fodContactMobile.setText("+91-"+mobile);
                    }else{
                        shimmerFrameLayout.setVisibility(View.GONE);
                        noBooking.setVisibility(View.VISIBLE);
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
                HashMap<String , String> map= new HashMap<>();
                map.put("mobile" , userMobile);
                return map;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void fetchUserDetails(final String userMobile) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.USER_COMPLETE_DETAILS, new Response.Listener<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(String response) {
                Log.d("TAG" , response);
                mShimmerViewContainer.stopShimmerAnimation();
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if (error == false){
                        fatherName.setBackgroundColor(Color.parseColor("#ffffff"));
                        fatherMobile.setBackgroundColor(Color.parseColor("#ffffff"));
                        address.setBackgroundColor(Color.parseColor("#ffffff"));
                        String fatherNAme = jsonObject.getString("fatherName");
                        String father_Mobile = jsonObject.getString("fatherMobile");
                        String addressV=jsonObject.getString("address");
                        fatherName.setText(fatherNAme);
                        fatherMobile.setText(father_Mobile);
                        address.setText(addressV);
                    }else{
                        mShimmerViewContainer.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
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
                HashMap<String , String>map= new HashMap<>();
                map.put("mobile" , userMobile);
                return map;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}

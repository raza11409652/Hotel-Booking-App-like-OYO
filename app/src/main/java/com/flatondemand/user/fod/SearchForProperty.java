package com.flatondemand.user.fod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.RvAdapter;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.model.PropertyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchForProperty extends AppCompatActivity {
    Intent intent;//= getIntent();
    String Location="";
    ProgressDialog progressDialog;


    private JsonArrayRequest ArrayRequest ;
    private RequestQueue requestQueue ;
    private List<PropertyList> propertyLists= new ArrayList<>();
    private RecyclerView myrv ;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout , linearLayout1;
SQLiteHandler sqLiteHandler;
HashMap<String ,String> map= new HashMap<String ,String >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_property);
        intent= getIntent();
        sqLiteHandler= new SQLiteHandler(this);
        map=sqLiteHandler.getLocation();
      // Toast.makeText(getApplicationContext() , ""+map,Toast.LENGTH_SHORT).show();
        Location=map.get("location");

        Location=Location.toUpperCase();
        relativeLayout= (RelativeLayout)findViewById(R.id.emptyIconwrapper);
        relativeLayout.setVisibility(View.GONE);
        linearLayout=(LinearLayout)findViewById(R.id.propertyListwrapper);
        linearLayout1=(LinearLayout)findViewById(R.id.listProerty);
        progressDialog= new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Searching for "+Location);
        progressDialog.show();
        myrv = findViewById(R.id.recycler_view);
        myrv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        setTitle(Location);
        //myrv.setLayoutManager(linearLayoutManager);

        ///fetchdata(Location);
        jsoncall(Location);


    }

    private void jsoncall(final String location) {


        StringRequest request= new StringRequest(Request.Method.POST, Constant.PROPERTY_LOCATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    String count=jsonObject.getString("count");
                    progressDialog.dismiss();
                    int counter=Integer.parseInt(count);
                    if(counter>0){
                        relativeLayout.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                       // linearLayout1.setGravity(View.T);

                        JSONArray arrayList= new JSONArray();
                        arrayList=jsonObject.getJSONArray("records");
                        for(int i=0;i<arrayList.length() ; i++){
                            JSONObject jsonObjectProperty= new JSONObject();
                            PropertyList propertyList= new PropertyList();
                           jsonObjectProperty= arrayList.getJSONObject(i);
                           String name=jsonObjectProperty.getString("property");
                           String price=jsonObjectProperty.getString("price");
                           String coverImage=jsonObjectProperty.getString("coverImage");
                           String address=jsonObjectProperty.getString("adress");
                           String uid=jsonObjectProperty.getString("uid");
                            propertyList.setProperty(name);
                            propertyList.setPrice(price);
                            propertyList.setCoverImage(Constant.ROOT_IMAGE_URL+coverImage);
                            propertyList.setAddress(address);
                            propertyList.setUid(uid);
                           propertyLists.add(propertyList);


                        }
                        //Toast.makeText(getApplicationContext() ,)

                    }else{
                        relativeLayout.setVisibility(View.VISIBLE);
                    }

                    setRvadapter(propertyLists);
                    //Toast.makeText(getApplicationContext() , count.toString() , Toast.LENGTH_SHORT).show();
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
                Map<String , String> map= new HashMap<>();
                map.put("location", location);
                return map;
            }
        };

        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(request);
        //requestQueue.add(ArrayR);



    }

    private void setRvadapter(List<PropertyList> propertyLists) {

        RvAdapter myAdapter = new RvAdapter(this,propertyLists) ;
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);
     //   myrv.setAdapter(myAdapter);

    }
}







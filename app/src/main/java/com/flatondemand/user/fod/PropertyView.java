package com.flatondemand.user.fod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.InclusionAdapter;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.model.InclusionProperty;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyView extends AppCompatActivity {
    Intent intent;//= getIntent();
    SQLiteHandler sqLiteHandler;
    ImageView imageView;
    String CoverImage , adress, fulladdress , uid , price;
    RequestOptions options ;
    TextView locationWrapper , securityMoney , priceView;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    Button bookNow;
    List<InclusionProperty> inclusionProperties = new ArrayList<>();
    HashMap<String , String>map= new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_view);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        sqLiteHandler= new SQLiteHandler(this);

        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        intent= getIntent();
        map= sqLiteHandler.getProperty();

        imageView=(ImageView)findViewById(R.id.imageView);
        String name=map.get("propertyName");
        uid=map.get("propertyUid");

        name=name.toLowerCase();
       name= StringUtils.capitalize(name);
        CoverImage=map.get("propertyCoverImage");
        adress=map.get("propertyAdd");
        price=map.get("propertyPrice");
        ///recycler view
        recyclerView=(RecyclerView)findViewById(R.id.inclusion_grid);
        bookNow=(Button)findViewById(R.id.book_now_initiate);
        recyclerView.setHasFixedSize(true);

        locationWrapper=(TextView)findViewById(R.id.locationWrapper);
        securityMoney=(TextView)findViewById(R.id.security_money);
        priceView=(TextView)findViewById(R.id.monthly_payment);
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_bg_light)
                .error(R.drawable.placeholder_bg_light);
       // Toast.makeText(getApplicationContext() , ""+adress,Toast.LENGTH_SHORT).show();

        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(name);

        setSupportActionBar(toolbar);
       if (getSupportActionBar() !=null){
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       }
        imageView.setContentDescription(name);
       // Glide.with(mContext).load(mData.get(position).getCoverImage()).apply(options).into(holder.propertyImage);
        Glide.with(getApplicationContext()).load(CoverImage).apply(options).into(imageView);
        locationWrapper.setText(adress);
        securityMoney.setText("Rs. "+price);
        priceView.setText("Rs. "+price);

        getInclusion(uid);

        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext() , upload_documents.class);
                startActivity(intent);
            }
        });
    }

    private void getInclusion(final String uid) {

        //Toast.makeText(getApplicationContext() , uid.toString() , Toast.LENGTH_SHORT).show();
        StringRequest stringRequest= new StringRequest(com.android.volley.Request.Method.POST, Constant.INCLUSION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
               // Toast.makeText(getApplicationContext() , response , Toast.LENGTH_SHORT).show();
                Log.d("response",response);
               // Toast.makeText(getApplicationContext() , ""+map,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray arrayList= new JSONArray();
                    arrayList=jsonObject.getJSONArray("records");
                   // Toast.makeText(getApplicationContext() , ""+arrayList.length(),Toast.LENGTH_SHORT).show();
                    //JSONObject jsonObjectProperty= new JSONObject();
                    for (int i=0;i<arrayList.length() ; i++){
                        JSONObject jsonObjectProperty= new JSONObject();
                        InclusionProperty inclusionProperty= new InclusionProperty();
                        jsonObjectProperty= arrayList.getJSONObject(i);
                        String text=jsonObjectProperty.getString("text");
                        String image=jsonObjectProperty.getString("image");
                        inclusionProperty.setText(text);
                        inclusionProperty.setImage(Constant.ROOT_IMAGE_URL+image);
                        inclusionProperties.add(inclusionProperty);
                    }

                    setInclusionAdapter(inclusionProperties);
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
                Map<String , String>map= new HashMap<>();
                map.put("uid" , uid);
                return map;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setInclusionAdapter(List<InclusionProperty> inclusionProperties) {
        /*
        *  RvAdapter myAdapter = new RvAdapter(this,propertyLists) ;
        myrv.setLayoutManager(new LinearLayoutManager(this));
        myrv.setAdapter(myAdapter);*/
        InclusionAdapter inclusionAdapter= new InclusionAdapter(this , inclusionProperties);
        recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(inclusionAdapter);
    }


}

package com.flatondemand.user.fod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.app.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileCreation extends AppCompatActivity {
    Button save;
    EditText emailInput , nameInput;
    String name , email;
    ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);
        save=(Button)findViewById(R.id.save);
        emailInput=(EditText)findViewById(R.id.email);
        nameInput=(EditText)findViewById(R.id.name);
        final String phonenumber = getIntent().getStringExtra("mobile");
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        progressDialog= new ProgressDialog(this);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            name=nameInput.getText().toString().trim();
            email=emailInput.getText().toString().trim();
            if(TextUtils.isEmpty(name)){
                nameInput.setError("Name is required");
                return;
            }else if(TextUtils.isEmpty(email)){
                emailInput.setError("Email is required");
            }else if(isValidEmail(email)){
                createProfile(name , email , phonenumber);
            }else{
                Toast.makeText(getApplicationContext() , "Email is invalid",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void createProfile(final String name, final String email , final String phone) {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait...");
        Toast.makeText(getApplicationContext() , name +email + phone , Toast.LENGTH_SHORT).show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.USER_PROFILE_CREATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if(error==false)
                    {
                    //user has successuflly created and logged in
                        //now go to dash
                        Intent dash= new Intent(getApplicationContext() , Dash.class);
                        dash.putExtra("userMobile",phone);
                        dash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dash);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext() , jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Map<String , String>param=new HashMap<>();
                param.put("mobile",phone);
                param.put("name",name);
                param.put("email", email);
                return  param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}

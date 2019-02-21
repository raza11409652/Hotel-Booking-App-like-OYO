package com.flatondemand.user.fod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.pd.chocobar.ChocoBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    Button login;
    EditText inputMobile;
    String mobileNumber;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        login=(Button) findViewById(R.id.login_btn);
        inputMobile=(EditText)findViewById(R.id.inputmobile);
        progressDialog= new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNumber= inputMobile.getText().toString().trim();
                //Toast.makeText(getApplicationContext() , mobileNumber , Toast.LENGTH_SHORT).show();
                if(isValidPhone(mobileNumber)){
                    startOTPSender(mobileNumber);
                }else{
                    //Toast.makeText(getApplicationContext() , R.string.invalidMobile, Toast.LENGTH_SHORT).show();
                    showWarningToast("Invalid Mobile Number Format" , v);
                }
            }
        });

    }

    private void startOTPSender(final String mobileNumber) {
       // String stringRequest= new
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.SEND_OTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(getApplicationContext() , ""+response , Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject= new JSONObject(response);
                  boolean error=jsonObject.getBoolean("error");
                  String msg=jsonObject.getString("message");

                    if(error == false){
                        Intent otp= new Intent(getApplicationContext() , OTPVERIFY.class);
                        otp.putExtra("mobile", mobileNumber);
                        otp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity(otp);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext() , msg , Toast.LENGTH_SHORT).show();
                    }
                 // Toast.makeText(getApplicationContext() ,"" +error,Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext() ,""+ error.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params= new HashMap<>();
                params.put("mobile",mobileNumber);
              return  params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }
    private void showWarningToast(String msg , View v){
        ChocoBar.builder().setView(v)
                .setText(msg)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .orange()
                .show();
    }
}

package com.flatondemand.user.fod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.pd.chocobar.ChocoBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import online.devliving.passcodeview.PasscodeView;

public class OTPVERIFY extends AppCompatActivity {
    Button verify;
    PasscodeView passcodeView;
    TextView mobilenumber , edit_button;
    ProgressDialog progressDialog;

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final String phonenumber = getIntent().getStringExtra("mobile");
        setContentView(R.layout.activity_otpverify);
        verify=(Button)findViewById(R.id.btn_verify);
        passcodeView=(PasscodeView)findViewById(R.id.passcode_view);
        mobilenumber=(TextView)findViewById(R.id.mobile_number_wrapper);
        edit_button=(TextView)findViewById(R.id.edit);
        mobilenumber.setText("On +91-"+phonenumber);
        progressDialog= new ProgressDialog(this);
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());
        passcodeView.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
            @Override
            public void onPasscodeEntered(String passcode) {

                startVerifyOtp(phonenumber , passcode);
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoHome= new Intent(getApplicationContext() , Home.class);
                startActivity(gotoHome);

                finish();
                overridePendingTransition(R.anim.no_animation  , R.anim.no_animation);
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP=passcodeView.getText().toString();
                startVerifyOtp(phonenumber , OTP);
            }
        });
    }


    private void startVerifyOtp(final String phonenumber, final String otp) {
        progressDialog.setMessage("Validating OTP .. ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
       // Toast.makeText(getApplicationContext(), "Passcode entered: " + otp, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.VERIFY_OTP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                if(error == false){
                        isUserProfileisCompleted(phonenumber);
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
                Map<String,String> params= new HashMap<>();
                params.put("otp",otp);
                params.put("mobile",phonenumber);
                return params;
             //   return super.getParams();
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void isUserProfileisCompleted(final String phonenumber) {

            StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.USER_PROFILE_COMPLETE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject= new JSONObject(response);
                        Boolean error=jsonObject.getBoolean("error");
                        if(error ==false)
                        {
                            Boolean isProfile=jsonObject.getBoolean("isProfileComplete");
                            if(isProfile == true)
                            {
                                String name=jsonObject.getString("name");
                                String email=jsonObject.getString("email");
                                /* $response['name']=$objUSER->name;
            $response['email']=$objUSER->email;*/
                                //before going to dash board
                                String tag_string_req = "req_login";
                                session.setLogin(true);
                                session.setLoggedInMobile(phonenumber);
                                db.addUser(name ,email ,phonenumber ,"");

                                //go to dash
                                Intent dash= new Intent(getApplicationContext() , Dash.class);
                                dash.putExtra("userMobile",phonenumber);
                                dash.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(dash);
                                finish();
                            }
                            else{
                                //go to profile creation
                                Intent profileCreation = new Intent(getApplicationContext() , ProfileCreation.class);
                                profileCreation.putExtra("mobile",phonenumber);
                                profileCreation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(profileCreation);
                            }
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
                    Map<String , String> param=new HashMap<>();
                    param.put("mobile",phonenumber);
                    return  param;
                }
            };
            RequestQueue requestQueue=Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

    }
    private void showWarningToast(String msg , View v){
        ChocoBar.builder().setView(v)
                .setText(msg)
                .setDuration(ChocoBar.LENGTH_SHORT)
                .orange()
                .show();
    }
}

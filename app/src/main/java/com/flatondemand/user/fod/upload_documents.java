package com.flatondemand.user.fod;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flatondemand.user.fod.app.Constant;
import com.flatondemand.user.fod.app.SQLiteHandler;
import com.flatondemand.user.fod.app.SessionManager;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.michaelbel.bottomsheet.BottomSheet;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class upload_documents extends AppCompatActivity implements SlyCalendarDialog.Callback , PaymentResultListener {
        ImageButton frontFace ,backFace;
        AlertDialog alertDialog ,alertDialog1;
        LayoutInflater inflater ,inflater_back;
    private SessionManager session;
    Double priceDouble;
    CheckBox checkBox;
    Boolean isForntUpload=false , isBackUpload=false;
       // View customView ;
    static final int CAMERA_REQUEST=1888 ,CAMERA_REQUEST_back=1999;
    static final  int MY_CAMERA_PERMISION=1,MY_CAMERA_PERMISSION_2=2;
    Button checkout;
    RadioGroup radioGroup;
    String documentsType=null;
    private RadioButton adhaar , passport;
    private SQLiteHandler sqLiteHandler;
    String CoverImage , adress, fulladdress , uid , price;
    String UserName , UserEmail , FirstDate="" , SecondDate="" , BookingType="";
    TextView BookingFor,monthlyMoney,securityMoney , userNameTextView,userEmailTextView;
    EditText dateFrom , dateTo;
ProgressDialog progressDialog;
    HashMap<String , String>map= new HashMap<>();

    HashMap<String , String>user= new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_documents);
        frontFace=(ImageButton)findViewById(R.id.front_face);
        backFace=(ImageButton)findViewById(R.id.back_face);
        checkout=(Button)findViewById(R.id.checkout);
        inflater= LayoutInflater.from(this);
        inflater_back=LayoutInflater.from(this);
        radioGroup=(RadioGroup)findViewById(R.id.radioDoc);
        adhaar=(RadioButton)findViewById(R.id.radioAdhaar);
        passport=(RadioButton)findViewById(R.id.radioPassport);
        BookingFor =(TextView)findViewById(R.id.header);
        checkBox=(CheckBox)findViewById(R.id.advanceCheckBox);
        monthlyMoney=(TextView)findViewById(R.id.monthly_payment);
        securityMoney=(TextView)findViewById(R.id.security_money);
        userNameTextView=(TextView)findViewById(R.id.booking_person_name);
        userEmailTextView=(TextView)findViewById(R.id.booking_person_email);
        dateFrom=(EditText)findViewById(R.id.date_from);
        Checkout.preload(getApplicationContext());
        progressDialog= new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        final View customView=inflater.inflate(R.layout.custom_dialog,null,false);
        final View CustomViewBAck=inflater_back.inflate(R.layout.custom_dialog , null ,false);
        alertDialog= new AlertDialog.Builder(upload_documents.this).create();
        alertDialog1= new AlertDialog.Builder(upload_documents.this).create();
        alertDialog1.setTitle("Please Read this");
        alertDialog.setTitle("Please read this");
        session = new SessionManager(getApplicationContext());
       // alertDialog.setContentView(R.layout.custom_location);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(customView);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.setView(CustomViewBAck);
        sqLiteHandler= new SQLiteHandler(this);
        //sql lite data from database
        map= sqLiteHandler.getProperty();
        user=sqLiteHandler.getUserDetails();

        UserName=user.get("name");
        UserEmail=user.get("email");
        String name=map.get("propertyName");
        uid=map.get("propertyUid");

        name=name.toLowerCase();
        name= StringUtils.capitalize(name);
        CoverImage=map.get("propertyCoverImage");
        adress=map.get("propertyAdd");
        price=map.get("propertyPrice");
        Toast.makeText(getApplicationContext() , ""+user, Toast
        .LENGTH_SHORT).show();
       try {
           BookingFor.setText(name+","+adress);
           securityMoney.setText("Rs. "+price);
           monthlyMoney.setText("Rs. "+price);
           userNameTextView.setText(UserName);
           userEmailTextView.setText(UserEmail);

           priceDouble=0.0;
           priceDouble= Double.valueOf(Integer.parseInt(price));

           checkBox.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  if(checkBox.isChecked()){
                      priceDouble=1000.00;
                      checkout.setText("Pay ("+priceDouble+")");
                      BookingType="Advance Payment Booking";
                  }else{
                      priceDouble=Double.valueOf(Integer.parseInt(price));
                      checkout.setText("Pay ("+priceDouble *2+")");
                      BookingType="Booking ";
                  }
               }
           });
           String textCheckOutButton=null;
           textCheckOutButton="Pay ("+priceDouble * 2+")";
           checkout.setText(textCheckOutButton);
       }catch (Exception e){
           e.printStackTrace();
       }


       dateFrom.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   genaretedate();
               } catch (ParseException e) {
                   e.printStackTrace();
               }
           }
       });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioAdhaar){
                    //Toast.makeText(getApplicationContext() , ""+checkedId ,Toast.LENGTH_SHORT).show();
                    documentsType="Adhaar Card";
                }else if(checkedId==R.id.radioPassport){
                    documentsType="Passport";
                  //  Toast.makeText(getApplicationContext() , ""+checkedId,Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog1.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_PERMISION);
                }else{
                    Intent cameraIntent= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent , CAMERA_REQUEST_back);
                }
            }
        });
       // alertDialog.setButton("Yes",);
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkSelfPermission(Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISION);
                }else{
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        frontFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    alertDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        backFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                   // alertDialog.show();
                    alertDialog1.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TextUtils.isEmpty(documentsType)){
                   if (isForntUpload == true && isBackUpload ==true){
                       progressDialog.setMessage("Please Wait");
                       progressDialog.show();
                     initiate(documentsType);
                   }else{
                       Toast.makeText(getApplicationContext(), "Upload  document ", Toast.LENGTH_LONG).show();
                   }
               }else{
                   Toast.makeText(getApplicationContext(), "select document ", Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private void genaretedate() throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String str1=sdf1.format(cal.getTime());
        String str2=sdf2.format(cal.getTime());
        Date startDate = sdf1.parse(str1);
        Date endDate = sdf2.parse(str2);
        new SlyCalendarDialog()
                .setSingle(false)
                .setFirstMonday(false)
                .setSelectedColor(R.color.themeColor)
                .setHeaderColor(R.color.themeColor)
                .setStartDate(startDate)
                .setCallback(upload_documents.this)
                .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
    }

    private void initiate(final String documentsType) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constant.START_BOOKING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    Boolean error=jsonObject.getBoolean("error");
                    if(error == false){
                        String bookingId=jsonObject.getString("booking_id");
                        String payAmount=jsonObject.getString("payment");
                        progressDialog.dismiss();
                       startPayment(bookingId , payAmount);
                    }else{
                        Toast.makeText(getApplicationContext() , ""+jsonObject.getString("error_msg") , Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext() , response , Toast.LENGTH_SHORT).show();
              //  JsonObject jsonObject= new JsonObject(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String , String>map= new HashMap<>();
                map.put("mobile" , session.getLoggedInMobile());
                map.put("document",documentsType);
                map.put("startDate",FirstDate);
                map.put("endDate",SecondDate);
                map.put("property",uid);
                map.put("bookingAmount", priceDouble.toString());
                map.put("bookingType" , BookingType);

                //map.put("document",)
               // return super.getParams();
                return  map;

            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void startPayment(String bookingId , String payAmount) {

        final Activity activity = this;

        final Checkout co = new Checkout();
       // String pay=priceDoubl;

        Integer payamount=priceDouble.intValue();


        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", payamount*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", UserEmail);
            preFill.put("contact", session.getLoggedInMobile());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_CAMERA_PERMISION ){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
               // Toast.makeText(getApplicationContext() , "Camera Permission Denied",Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }else{
            Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
           // startActivityForResult();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //photo=(Bitmap)data.getData().get
            frontFace.setImageBitmap(photo);
            frontFace.setEnabled(false);
            upload_documentsImage(photo ,Constant.DOC_UPLOAD_FRONT ,R.id.front_face);

        }else if(requestCode == CAMERA_REQUEST_back && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            backFace.setImageBitmap(photo);
            backFace.setEnabled(false);
            upload_documentsImage(photo ,Constant.DOC_UPLOAD_BACK  ,R.id.back_face);
        }

    }

    private void upload_documentsImage(final Bitmap photo, String docUpload , final int  button ) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, docUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Toast.makeText(getApplicationContext() , response , Toast.LENGTH_SHORT).show();
              //  button.setBackground(R.drawable.cam);
                if(button == R.id.front_face){
                    isForntUpload=true;
                }else if(button ==R.id.back_face){
                    isBackUpload=true;
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
                //return super.getParams();
                map.put("image",getStringImage(photo));
                map.put("mobile",session.getLoggedInMobile());
                //map.put("")
                return  map;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, 400, 580, false);
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onCancelled() {
//
    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
            if (firstDate != null){
                if (secondDate ==null){
                    firstDate.set(Calendar.HOUR_OF_DAY, hours);
                    firstDate.set(Calendar.MINUTE, minutes);
                   // Toast.makeText(getApplicationContext() , new SimpleDateFormat(getString(R.string.timeFormat),Locale.getDefault()).format(firstDate.getTime()) , Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext() , "Please Select Last date ",Toast.LENGTH_SHORT).show();
                }else{
                  /*  Toast.makeText(
                            this,
                            getString(
                                    R.string.period,
                                    new SimpleDateFormat(getString(R.string.dateFormat), Locale.getDefault()).format(firstDate.getTime()),
                                    new SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault()).format(secondDate.getTime())
                            ),
                            Toast.LENGTH_LONG

                    ).show();*/
                    String date = null;
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    String str1=sdf1.format(firstDate.getTime());
                    date= sdf1.format(secondDate.getTime());
                    dateFrom.setText( str1 +" to "+date);
                   FirstDate=str1;
                   SecondDate=date;

                }
            }
    }

    @Override
    public void onPaymentSuccess(String s) {
        try {
            Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }
}

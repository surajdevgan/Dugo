package com.android.parteek.dugo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
TextView t;
    EditText textemail,textpass;
    Button loginBtn;
    RequestQueue requestQueue;
    ProgressDialog pd;
    UserBean userBean;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String login_phone,login_password;

    void views(){
        t=(TextView)findViewById(R.id.link_signup);
        t.setOnClickListener(this);
        textemail=(EditText)findViewById(R.id.input_email);
        textpass=(EditText)findViewById(R.id.input_password);
        loginBtn=(Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        requestQueue= Volley.newRequestQueue(this);
        pd=new ProgressDialog(this);
        pd.setMessage("Signing in.....");
        pd.setCancelable(false);
        userBean=new UserBean();
        preferences=getSharedPreferences(Util.pref_name1,MODE_PRIVATE);
        editor=preferences.edit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        views();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.link_signup) {
            Intent i = new Intent(this, Register.class);
            startActivity(i);
            finish();
        }else if(id==R.id.btn_login){
            login_phone=textemail.getText().toString().trim();
            login_password=textpass.getText().toString().trim();

            login();
            /*if(!userBean.getPhone().equals("123456")&&!userBean.getPassword().equals("admin")) {

            }else{
                Intent intent=new Intent(Login.this,Adminstrator.class);
                startActivity(intent);
                finish();
            }*/
        }
    }
    void login(){
        final String token= FirebaseInstanceId.getInstance().getToken();
        Log.e("token ",token);
        pd.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("students");
                    int id=0;
                    String ag="",bl="",n="",p="",e="",pass="",ge="",st="",ci="",da="",ti="";
                    for(int j=0;j<jsonArray.length();j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        id = jsonObject.getInt("ID");
//These are coloumn name in database table
                        n = jsonObject.getString("NAME");
                        p = jsonObject.getString("PHONE");
                        ge = jsonObject.getString("GENDER");
                        ci = jsonObject.getString("CITY");
                        pass = jsonObject.getString("PASSWORD");
                        da = jsonObject.getString("DATE");
                        ti = jsonObject.getString("TIME");
                         ag = jsonObject.getString("AGE");
                        bl = jsonObject.getString("BLOOD_GROUP");

                    }

                    userBean = new UserBean(id,n,p,ge,ci,pass,da,ti,ag,bl);

                    String mess=object.getString("message");
                    if(mess.contains("Login Sucessful")) {
                        editor.putString(Util.key_phone, userBean.getPhone());
                        editor.putString("name",userBean.getName());
                        editor.putString("age",userBean.getAge());
                        editor.putString("blood",userBean.getBlooddgroup());
                        editor.putString("gender",userBean.getGender());
                        editor.putString("city",userBean.getCity());
                        editor.putString("token",token);


                        editor.commit();
                        Intent i = new Intent(Login.this, Home.class);
                        startActivity(i);
                        finish();
                        pd.dismiss();
                        Toast.makeText(Login.this, mess, Toast.LENGTH_SHORT).show();
                    }else{
                        pd.dismiss();
                        Toast.makeText(Login.this, mess, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    pd.dismiss();
                    Toast.makeText(Login.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Login.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("phone",login_phone);
                map.put("password",login_password);
                map.put("token",token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}

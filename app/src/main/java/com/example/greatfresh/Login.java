package com.example.greatfresh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Login extends AppCompatActivity {

    String url = "https://greatfresh.in/api/login.php";
    private EditText email;
    private EditText mobile;
    Button login;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    public static final String ID = "1";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobilePattern = "^{10,13}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);

        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        getdata();
    }

    private void getdata() {


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailId = email.getText().toString().trim();
                final String mobileId = mobile.getText().toString().trim();

              /*  email.setText("greatfresh01@gmail.com");
                mobile.setText("9426339292");*/

                if (TextUtils.isEmpty(emailId)) {
                    Toast.makeText(Login.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                } else if (!emailId.matches(emailPattern)) {
                    Toast.makeText(Login.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mobileId)) {
                    Toast.makeText(Login.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (!(mobileId.length() <= 13)) {
                    Toast.makeText(Login.this, "You must have 10 characters in your number", Toast.LENGTH_SHORT).show();
                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String userId = object.getString(Constant.USER_ID);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Constant.EMAIL, emailId);
                                    editor.putString(Constant.MOBILE, mobileId);
                                    editor.putString(Constant.USER_ID, userId);
                                    editor.apply();


                                    if (userId.matches(ID)) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra(Constant.USER_ID, userId);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {

                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        //intent.putExtra(Constant.OTHER, userId);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //  pd.dismiss();
                            Toast.makeText(Login.this, "Please Check Internet Connection ", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<>();
                            param.put("email", email.getText().toString().trim());
                            param.put("mobile", mobile.getText().toString().trim());
                            return param;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(stringRequest);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


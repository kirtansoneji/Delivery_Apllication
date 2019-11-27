package com.example.greatfresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String url = "https://greatfresh.in/api/allorder.php";
    ListView lv;
    List<DataModel> list = new ArrayList<>();
    List<DataModel> list1 = new ArrayList<>();
    Spinner sp, spinner1;
    Button submit;
    ImageView logout;
    public static final String ID = "1";
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String pending_order = "https://greatfresh.in/api/pending_deliveries.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        lv = findViewById(R.id.lv);
        sp = (Spinner) this.findViewById(R.id.spinner);
        spinner1 = this.findViewById(R.id.spinner1);
        submit = findViewById(R.id.submit);
        logout = findViewById(R.id.logout);

        listData();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
            }
        });

    }

    private void listData() {

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String userId = sharedPreferences.getString(Constant.USER_ID, "");
        editor.apply();

        if (!userId.matches(ID)){


            StringRequest request = new StringRequest(Request.Method.POST, Url.pending_order, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("response",response);

                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i <= array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);

                            String item = object1.getString("item");
                            String order_id = object1.getString("order_id");
                            String customer_name = object1.getString("customer_name");
                            String phone = object1.getString("phone");
                            String payment_gateway = object1.getString("payment_gateway");
                            String address = object1.getString("address");
                            String total_cost = object1.getString("total_cost");

                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constant.ORDER_ID, order_id);
                            editor.apply();


                            DataModel model = new DataModel();
                            model.setItem(item);
                            model.setOrder_id(order_id);
                            model.setCustomer_name(customer_name);
                            model.setPhone(phone);
                            model.setTotal_cost(total_cost);

                            if (payment_gateway.equals("null")) {
                                model.setPayment_gateway("Cash");
                            } else {
                                model.setPayment_gateway("Paytm");
                            }

                            model.setAddress(address);
                            list.add(model);
                            Base_Adapter adapter = new Base_Adapter(MainActivity.this, list);
                            lv.setAdapter(adapter);
                            // adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put(Constant.USER_ID, userId);

                    return param;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(request);

//            StringRequest request = new StringRequest( pending_order,
//                    new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONArray array = new JSONArray(response);
//
//                        for (int i = 0; i <= array.length(); i++) {
//                            JSONObject object = array.getJSONObject(i);
//
//                            String item = object.getString("item");
//                            String order_id = object.getString("order_id");
//                            String customer_name = object.getString("customer_name");
//                            String phone = object.getString("phone");
//                            String payment_gateway = object.getString("payment_gateway");
//                            String address = object.getString("address");
//                            String total_cost = object.getString("total_cost");
//
//                            DataModel model = new DataModel();
//                            model.setItem(item);
//                            model.setOrder_id(order_id);
//                            model.setCustomer_name(customer_name);
//                            model.setPhone(phone);
//                            model.setTotal_cost(total_cost);
//
//                            if (payment_gateway.equals("null")) {
//                                model.setPayment_gateway("Cash");
//                            } else {
//                                model.setPayment_gateway("Paytm");
//                            }
//
//                            model.setAddress(address);
//                            list.add(model);
//                            Base_Adapter adapter = new Base_Adapter(MainActivity.this, list);
//                            lv.setAdapter(adapter);
//
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });
//            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//            queue.add(request);

        }
        else {

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("ddd", response);
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i <= array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);

                            String item = object1.getString("item");
                            String order_id = object1.getString("order_id");
                            String customer_name = object1.getString("customer_name");
                            String phone = object1.getString("phone");
                            String payment_gateway = object1.getString("payment_gateway");
                            String address = object1.getString("address");
                            String total_cost = object1.getString("total_cost");

                            final SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constant.ORDER_ID, order_id);
                            editor.apply();


                            DataModel model = new DataModel();
                            model.setItem(item);
                            model.setOrder_id(order_id);
                            model.setCustomer_name(customer_name);
                            model.setPhone(phone);
                            model.setTotal_cost(total_cost);

                            if (payment_gateway.equals("null")) {
                                model.setPayment_gateway("Cash");
                            } else {
                                model.setPayment_gateway("Paytm");
                            }

                            model.setAddress(address);
                            list.add(model);
                            Base_Adapter adapter = new Base_Adapter(MainActivity.this, list);
                            lv.setAdapter(adapter);
                           // adapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(request);
        }

    }
}



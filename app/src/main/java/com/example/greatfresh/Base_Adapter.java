package com.example.greatfresh;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import androidx.core.app.NotificationCompat;

public class Base_Adapter extends BaseAdapter {
    private Context context;
    private List<DataModel> list = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();
    private LayoutInflater inflater;
    String[] name = {"process", "out_to_deliver", "delivered", "cancelled"};
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private List<SpinnerModelOne> listSpinner=new ArrayList<>();
    private String d_id,process_id;
    public Base_Adapter(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final String userId = sharedPreferences.getString(Constant.USER_ID, "");
        //final String orderId = sharedPreferences.getString(Constant.ORDER_ID, "");


        // Intent intent = ((Activity) context).getIntent();
        //final String userId = intent.getStringExtra(Constant.USER_ID);

        view = inflater.inflate(R.layout.item, null);
        TextView tvv1 = view.findViewById(R.id.tvv1);
        TextView tvv2 = view.findViewById(R.id.tvv2);
        TextView tvv3 = view.findViewById(R.id.tvv3);
        TextView tvv6 = view.findViewById(R.id.tvv6);
        TextView tvv5 = view.findViewById(R.id.tvv5);
        TextView tvv7 = view.findViewById(R.id.tvv7);
        TextView tvpayment = view.findViewById(R.id.tvpayment);
        final EditText et = view.findViewById(R.id.editText);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;

                }
            }
        });        final Spinner sp = view.findViewById(R.id.spinner);
        final Spinner spinner1 = view.findViewById(R.id.spinner1);
        Button submit = view.findViewById(R.id.submit);


        final DataModel model = list.get(position);
        tvv1.setText(model.getOrder_id());
        tvv2.setText(model.getCustomer_name());
        tvv3.setText(model.getPhone());
        tvv6.setText(model.getItem());
        tvv5.setText(model.getPayment_gateway());
        tvv7.setText(model.getAddress());
        tvpayment.setText(model.getTotal_cost());

        // model.setMsgEt(et.getText().toString().trim());

        final ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, name);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner1.setAdapter(aa);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                process_id=name[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        StringRequest request1 = new StringRequest(Url.fetch_delivery,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("ddd", response);
                        try {
                            JSONArray array = new JSONArray(response);


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String username = object.getString("username");
                                String id = object.getString("id");

                                SpinnerModelOne spinnerModelOne=new SpinnerModelOne();
                                spinnerModelOne.setId(id);
                                spinnerModelOne.setUsername(username);
                                listSpinner.add(spinnerModelOne);

                                //DataModel model1 = new DataModel();
                                model.setId(id);
                                //model.setUsername(username);

                                list2.add(username);
                                HashSet<String> hashSet = new HashSet<String>(list2);
                                list2.clear();
                                list2.addAll(hashSet);
                                ArrayAdapter<String> dataadapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list2);
                                dataadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp.setAdapter(dataadapter);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue1 = Volley.newRequestQueue(context);
        queue1.add(request1);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 d_id = listSpinner.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (userId.matches("1")) {
            sp.setVisibility(View.VISIBLE);
            spinner1.setVisibility(View.VISIBLE);
            et.setVisibility(View.VISIBLE);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String order_id = list.get(position).getOrder_id();

                    if (!TextUtils.isEmpty(order_id)) {


                        StringRequest request = new StringRequest(Request.Method.POST, Url.update_order, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = (JSONObject) array.getJSONObject(i);

                                        String orderId = object.getString("Order_No");

                                        final String d_id = list.get(position).getId();

                                        if (orderId.matches(order_id)) {

                                            list.remove(list.get(position));
                                            notifyDataSetChanged();
                                        }
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
                                param.put(Constant.ORDER_ID, order_id);
                                param.put(Constant.D_ID, d_id);
                                param.put("order_status", process_id);
                                param.put("message", et.getText().toString());

                                return param;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(request);

                    }
                }
            });


        } else {
            sp.setVisibility(View.GONE);
            et.setVisibility(View.GONE);
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    StringRequest request = new StringRequest(Url.update_order,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//
//                                    try {
//
//                                        JSONArray array = new JSONArray();
//
//                                        for (int i = 0; i < array.length(); i++) {
//
//                                            JSONObject object1 = array.getJSONObject(i);
//                                            /*String msg = object.getString("message");
//                                            String order_sts = object.getString("order_status");*/
//
//
//                                        }
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    });
//                    RequestQueue queue = Volley.newRequestQueue(context);
//                    queue.add(request);
//                }
//            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String order_id = list.get(position).getOrder_id();

                    if (!TextUtils.isEmpty(order_id)) {


                        StringRequest request = new StringRequest(Request.Method.POST, Url.update_order, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = (JSONObject) array.getJSONObject(i);

                                        String orderId = object.getString("Order_No");

                                        final String d_id = list.get(position).getId();

                                        if (orderId.matches(order_id)) {

                                            list.remove(list.get(position));
                                            notifyDataSetChanged();
                                        }
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
                                param.put(Constant.ORDER_ID, order_id);
                                param.put("order_status", process_id);
                                param.put("message", et.getText().toString());

                                return param;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(request);

                    }
                }
            });


        }


        return view;
    }

    public void setData(List<DataModel> datalist) {

        list = datalist;
        notifyDataSetChanged();
    }


    private void addNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.icon) //set icon for notification
                        .setContentTitle("Notifications From Great Fresh") //set title of notification
                        .setContentText("This is a notification For Get Ready To Delivery")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(context, NotificationClass.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification For Get Ready To Delivery");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}

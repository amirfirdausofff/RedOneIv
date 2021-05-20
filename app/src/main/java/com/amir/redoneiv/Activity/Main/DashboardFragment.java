package com.amir.redoneiv.Activity.Main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amir.redoneiv.Activity.LoginActivity;
import com.amir.redoneiv.Adapter.UsageAdapter;
import com.amir.redoneiv.Class.UsageClass;
import com.amir.redoneiv.Common.PreferenceManagerLogin;
import com.amir.redoneiv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    static TextView textView_phone_number,textView_name,textView_status;
    static RecyclerView rv;

    static List<UsageClass> usageClassList;
    static UsageAdapter usageAdapter;
    static SwipyRefreshLayout swipe_container;
    static Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        swipe_container = v.findViewById(R.id.swipe_container);
        textView_phone_number = v.findViewById(R.id.textView_phone_number);
        textView_name = v.findViewById(R.id.textView_name);
        textView_status = v.findViewById(R.id.textView_status);
        rv = v.findViewById(R.id.rv);
        activity = getActivity();

        getProfile();
        getUsage();

        swipe_container.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipe_container.setRefreshing(false);
                getProfile();
                getUsage();

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static void getProfile(){
        StringRequest stringRequest = new StringRequest(GET, "https://devmobileapp.redone.com.my/mockconsumer/User/user-info",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = new JSONObject(object.getString("result"));
                            textView_phone_number.setText("Phone Number : "+result.getString("phoneNumber"));
                            textView_name.setText(result.getString("name"));

                            if(result.getBoolean("isActive")){
                                textView_status.setText("Active");
                            }else {
                                textView_status.setText("Not Active");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                PreferenceManagerLogin session = new PreferenceManagerLogin(activity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+session.getUserDetails().get(PreferenceManagerLogin.TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public static void getUsage(){

        usageClassList = new ArrayList<>();
        usageAdapter = new UsageAdapter(usageClassList, activity, new UsageAdapter.onClickJobByMonth() {
            @Override
            public void onClick(UsageClass jobByMonthClass) {

            }

        });
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(horizontalLayoutManager);
        rv.setAdapter(usageAdapter);

        StringRequest stringRequest = new StringRequest(GET, "https://devmobileapp.redone.com.my/mockconsumer/Usage/usages",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray result = new JSONArray(object.getString("result"));

                            for (int i = 0 ; i < result.length(); i++){
                                JSONObject object1 = result.getJSONObject(i);

                                UsageClass usageClass = new UsageClass(
                                        object1.getString("title"),
                                        object1.getString("usageLabel"),
                                        object1.getString("usagePercent")
                                );
                                usageClassList.add(usageClass);
                            }
                            usageAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                PreferenceManagerLogin session = new PreferenceManagerLogin(activity);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+session.getUserDetails().get(PreferenceManagerLogin.TOKEN));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}

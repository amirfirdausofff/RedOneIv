package com.amir.redoneiv.Activity.Main;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amir.redoneiv.Adapter.RewardAdapter;
import com.amir.redoneiv.Adapter.UsageAdapter;
import com.amir.redoneiv.Class.RewardClass;
import com.amir.redoneiv.Class.UsageClass;
import com.amir.redoneiv.Common.PreferenceManagerLogin;
import com.amir.redoneiv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardFragment extends Fragment {

    static TextView textView_rewards_status,textView_rewards_points;
    static RecyclerView rv;
    static SwipyRefreshLayout swipe_container;
    static Activity activity;
    static String membership_point = "",membership_level = "";

    static List<RewardClass> rewardClassList;
    static RewardAdapter rewardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reward, container, false);

        activity = getActivity();

        textView_rewards_status = v.findViewById(R.id.textView_rewards_status);
        textView_rewards_points = v.findViewById(R.id.textView_rewards_points);
        rv = v.findViewById(R.id.rv);
        swipe_container = v.findViewById(R.id.swipe_container);



        swipe_container.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipe_container.setRefreshing(false);
                getUserRewardStatus();
                getRewardList();

            }
        });
        return v;
    }

    public static void getUserRewardStatus(){
        StringRequest stringRequest = new StringRequest(GET, "https://devmobileapp.redone.com.my/mockconsumer/Rewards/user-reward-info",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = new JSONObject(object.getString("result"));
                            textView_rewards_status.setText("Status Membership : "+result.getString("memberLevelText"));
                            membership_level = result.getString("memberLevel");
                            membership_point = result.getString("totalPoints");
                            textView_rewards_points.setText("Total Points : "+membership_point+" pts");
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

    public static void getRewardList(){

        rewardClassList = new ArrayList<>();
        rewardAdapter = new RewardAdapter(rewardClassList, activity);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(horizontalLayoutManager);
        rv.setAdapter(rewardAdapter);

        StringRequest stringRequest = new StringRequest(GET, "https://devmobileapp.redone.com.my/mockconsumer/Rewards/rewards-list",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray result = new JSONArray(object.getString("result"));

                            for (int i = 0 ; i < result.length(); i++){
                                JSONObject object1 = result.getJSONObject(i);

                                RewardClass usageClass = new RewardClass(
                                        object1.getString("uniqueId"),
                                        object1.getString("rewardId"),
                                        object1.getString("rewardName"),
                                        object1.getString("rewardDescription"),
                                        object1.getString("pointRequired"),
                                        object1.getString("validUntilDate"),
                                        object1.getString("minimumMemberLevelRequired")
                                );
                                rewardClassList.add(usageClass);
                            }
                            rewardAdapter.notifyDataSetChanged();
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

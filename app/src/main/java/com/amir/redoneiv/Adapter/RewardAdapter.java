package com.amir.redoneiv.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.amir.redoneiv.Activity.Main.RewardFragment;
import com.amir.redoneiv.Class.RewardClass;
import com.amir.redoneiv.Class.UsageClass;
import com.amir.redoneiv.Common.PreferenceManagerLogin;
import com.amir.redoneiv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.MyViewHolder>{

    private List<RewardClass> menuList;
    Activity activity;
    android.app.AlertDialog rewardDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title,textView_details,textView_duedate,textView_points;
        Button button_redeem;

        public MyViewHolder(View view) {
            super(view);

            textView_title = view.findViewById(R.id.textView_title);
            textView_details = view.findViewById(R.id.textView_details);
            textView_duedate = view.findViewById(R.id.textView_duedate);
            textView_points = view.findViewById(R.id.textView_points);
            button_redeem = view.findViewById(R.id.button_redeem);
        }
    }


    public RewardAdapter(List<RewardClass> menuList, Activity activity) {
        this.menuList = menuList;
        this.activity = activity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reward, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RewardClass menu = menuList.get(position);

        holder.textView_title.setText(menu.getRewardName());
        holder.textView_details.setText(menu.getRewardDescription());
        holder.textView_duedate.setText("Valid until "+menu.getValidUntilDate());
        holder.textView_points.setText("Points required "+menu.getPointRequired());

        holder.button_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder rewardBuilder = new android.app.AlertDialog.Builder(activity,R.style.dialogReward);
                ViewGroup viewGroups = v.findViewById(android.R.id.content);
                View dialogViews = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_reward, viewGroups, false);
                dialogViews.setVisibility(View.VISIBLE);
                rewardBuilder.setView(dialogViews);
                rewardDialog = rewardBuilder.create();
                rewardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                rewardDialog.setCancelable(false);

                TextView textView_title = dialogViews.findViewById(R.id.textView_title);
                TextView textView_details = dialogViews.findViewById(R.id.textView_details);
                TextView textView_duedate = dialogViews.findViewById(R.id.textView_duedate);
                TextView textView_points = dialogViews.findViewById(R.id.textView_points);
                Button button_redeems = dialogViews.findViewById(R.id.button_redeems);
                ImageView imageView_back = dialogViews.findViewById(R.id.imageView_back);

                textView_title.setText(menu.getRewardName());
                textView_details.setText(menu.getRewardDescription());
                textView_duedate.setText("Valid until "+menu.getValidUntilDate());
                textView_points.setText("Points required "+menu.getPointRequired());

                button_redeems.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(menu.getRewardId(),menu.getRewardName());
                    }
                });

                imageView_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rewardDialog.dismiss();
                    }
                });
                rewardDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

    private void showDialog(final String rewardId, String rewardName){
        new AlertDialog.Builder(activity)
                .setTitle("Redeem Reward")
                .setMessage("Are you sure want to redeem "+rewardName +" ?")
                .setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redeem(rewardId);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void redeem(String rewardId){
        StringRequest stringRequest = new StringRequest(POST, "https://devmobileapp.redone.com.my/mockconsumer/Rewards/redeem-reward/"+rewardId,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            if(object.getString("status").equals("1")){
                                Toast.makeText(activity,"Reward successfully redeemed",Toast.LENGTH_SHORT).show();
                                rewardDialog.dismiss();
                                RewardFragment.getRewardList();
                                RewardFragment.getUserRewardStatus();
                            }else {
                                Toast.makeText(activity,"Reward cannot redeemed",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,"Reward cannot redeemed",Toast.LENGTH_SHORT).show();
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

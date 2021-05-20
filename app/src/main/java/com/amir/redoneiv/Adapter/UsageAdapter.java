package com.amir.redoneiv.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amir.redoneiv.Class.UsageClass;
import com.amir.redoneiv.R;

import java.util.List;

public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.MyViewHolder>{

    private List<UsageClass> menuList;
    Activity activity;
    private onClickJobByMonth mListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title,textView_percent,textView_usage;
        ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);

            textView_title = view.findViewById(R.id.textView_title);
            textView_percent = view.findViewById(R.id.textView_percent);
            textView_usage = view.findViewById(R.id.textView_usage);
            progressBar = view.findViewById(R.id.progressBar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClick(menuList.get(getAdapterPosition()));
                }
            });
        }
    }


    public UsageAdapter(List<UsageClass> menuList, Activity activity, onClickJobByMonth mListener) {
        this.menuList = menuList;
        this.activity = activity;
        this.mListener = mListener;
    }

    public interface onClickJobByMonth {
        void onClick(UsageClass jobByMonthClass);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_usage, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UsageClass menu = menuList.get(position);

        holder.textView_title.setText(menu.getTitle());
        holder.textView_percent.setText(menu.getUsagePercent()+"%");
        holder.textView_usage.setText(menu.getUsageLabel());

        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(Integer.parseInt(menu.getUsagePercent()));
    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

}

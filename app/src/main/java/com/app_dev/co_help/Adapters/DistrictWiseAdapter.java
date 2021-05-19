package com.app_dev.co_help.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_dev.co_help.EachDistrictDataActivity;
import com.app_dev.co_help.Models.DistrictWiseModel;
import com.app_dev.co_help.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistrictWiseAdapter extends RecyclerView.Adapter<DistrictWiseAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;

    private String searchText="";
    private SpannableStringBuilder sb;

    public static final String DISTRICT_NAME = "districtName";
    public static final String DISTRICT_CONFIRMED = "districtConfirmed";
    public static final String DISTRICT_CONFIRMED_NEW = "districtConfirmedNew";
    public static final String DISTRICT_ACTIVE = "districtActive";
    public static final String DISTRICT_DEATH = "districtDeath";
    public static final String DISTRICT_DEATH_NEW = "districtDeathNew";
    public static  final String DISTRICT_RECOVERED = "districtRecovered";
    public static final String DISTRICT_RECOVERED_NEW = "districtRecoveredNew";

    public DistrictWiseAdapter(Context mContext, ArrayList<DistrictWiseModel> districtWiseModelArrayList) {
        this.mContext = mContext;
        this.districtWiseModelArrayList = districtWiseModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_state_wise, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        DistrictWiseModel currentItem = districtWiseModelArrayList.get(position);
        String districtName = currentItem.getDistrict();
        String districtTotal = currentItem.getConfirmed();
        holder.tv_districtTotalCases.setText(NumberFormat.getInstance().format(Integer.parseInt(districtTotal)));




        if(searchText.length()>0){

            int index = districtName.indexOf(searchText);
            sb = new SpannableStringBuilder(districtName);
            Pattern word = Pattern.compile(searchText.toLowerCase());
            Matcher match = word.matcher(districtName.toLowerCase());
            while(match.find()){
                ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(52, 195, 235));
                sb.setSpan(fcs, match.start(), match.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);


            }
            holder.tv_districtName.setText(sb);

        }else{
            holder.tv_districtName.setText(districtName);
        }

        holder.lin_district_wise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistrictWiseModel clickedItem = districtWiseModelArrayList.get(position);
                Intent perDistrictIntent = new Intent(mContext, EachDistrictDataActivity.class);
                perDistrictIntent.putExtra(DISTRICT_NAME, clickedItem.getDistrict());
                perDistrictIntent.putExtra(DISTRICT_CONFIRMED, clickedItem.getConfirmed());
                perDistrictIntent.putExtra(DISTRICT_CONFIRMED_NEW, clickedItem.getNewConfirmed());
                perDistrictIntent.putExtra(DISTRICT_ACTIVE, clickedItem.getActive());
                perDistrictIntent.putExtra(DISTRICT_DEATH, clickedItem.getDeceased());
                perDistrictIntent.putExtra(DISTRICT_DEATH_NEW, clickedItem.getNewDeceased());
                perDistrictIntent.putExtra(DISTRICT_RECOVERED, clickedItem.getRecovered());
                perDistrictIntent.putExtra(DISTRICT_RECOVERED_NEW, clickedItem.getNewRecovered());
                mContext.startActivity(perDistrictIntent);
            }
        }
        );

    }

    @Override
    public int getItemCount() {
        return districtWiseModelArrayList==null ? 0 : districtWiseModelArrayList.size();
    }

    public void filterList(ArrayList<DistrictWiseModel> filteredList, String search) {
        districtWiseModelArrayList = filteredList;
        this.searchText = search;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_districtName, tv_districtTotalCases;
        LinearLayout lin_district_wise;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_districtName = itemView.findViewById(R.id.statewise_layout_name_textview);
            tv_districtTotalCases = itemView.findViewById(R.id.statewise_layout_confirmed_textview);
            lin_district_wise = itemView.findViewById(R.id.layout_state_wise_lin);
        }
    }

}


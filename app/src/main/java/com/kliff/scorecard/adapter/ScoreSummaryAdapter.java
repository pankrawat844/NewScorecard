package com.kliff.scorecard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kliff.scorecard.R;
import com.kliff.scorecard.model.ScoreSummaryModel;

import java.util.List;

public class ScoreSummaryAdapter extends RecyclerView.Adapter<ScoreSummaryAdapter.ViewHolder> {

    List<ScoreSummaryModel> modelList;

    public ScoreSummaryAdapter(List<ScoreSummaryModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_summery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreSummaryModel currentData = modelList.get(position);
        holder.ball_text.setText(currentData.getBall_text());
        if (currentData.getCms_text().equals("")) {
            holder.cms_text.setVisibility(View.GONE);
        } else
            holder.cms_text.setText(currentData.getCms_text());

        holder.full_commentary.setText(currentData.getFull_commentary());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView ball_text;
        TextView cms_text;
        TextView full_commentary;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ball_text = itemView.findViewById(R.id.ball_text);
            cms_text = itemView.findViewById(R.id.cms_text);
            full_commentary = itemView.findViewById(R.id.full_commentary);
        }
    }
}

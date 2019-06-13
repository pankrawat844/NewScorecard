package com.kliff.scorecard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kliff.scorecard.R;
import com.kliff.scorecard.model.MatchList;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.Viewholder> {

    List<MatchList> list;

    public  MatchListAdapter(List<MatchList> list){
        this.list=list;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matchlist1,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position)
    {
        holder.team1.setText(list.get(position).getTeam1());
        holder.team2.setText(list.get(position).getTeam2());
        holder.result.setText(list.get(position).getResult());
        holder.venue.setText(list.get(position).getVenue());
        holder.score1.setText(list.get(position).getScore1());
        holder.score2.setText(list.get(position).getScore2());
        holder.wicket1.setText(list.get(position).getWicket1());
        holder.wicket2.setText(list.get(position).getWicket2());
        Picasso.get().load(list.get(position).getTeam1_img()).into(holder.team1_img);
        Picasso.get().load(list.get(position).getTeam2_img()).into(holder.team2_img);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {

        TextView team1,team2,result,venue,score1,score2,wicket1,wicket2;
        ImageView team1_img,team2_img;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            team1=itemView.findViewById(R.id.team1);
            team2=itemView.findViewById(R.id.team2);
            result=itemView.findViewById(R.id.result);
            venue=itemView.findViewById(R.id.venue);
            score1=itemView.findViewById(R.id.score1);
            score2=itemView.findViewById(R.id.score2);
            team1_img=itemView.findViewById(R.id.team1_img);
            team2_img=itemView.findViewById(R.id.team2_img);
            wicket1=itemView.findViewById(R.id.wicket1);
            wicket2=itemView.findViewById(R.id.wicket2);
        }
    }
}

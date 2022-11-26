package ca.georgebrown.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private ArrayList<MatchModal> matchModalArrayList;
    private Context context;
    int lastPosition = -1;
    private MatchClickInterface matchClickInterface;

    //Constructor
    public MatchAdapter(ArrayList<MatchModal> matchModalArrayList, Context context, MatchClickInterface matchClickInterface) {
        this.matchModalArrayList = matchModalArrayList;
        this.context = context;
        this.matchClickInterface = matchClickInterface;
    }

    @NonNull
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating layout
        View view = LayoutInflater.from(context).inflate(R.layout.match_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //setting data
        MatchModal matchModal = matchModalArrayList.get(position);
        holder.homeTeamName.setText(matchModal.getHomeTeam());
        holder.awayTeamName.setText(matchModal.getAwayTeam());
        //Image
        Picasso.get().load(matchModal.getMatchImage()).into(holder.homeViewImage);
        //Adding Animation
        setAnimation(holder.itemView,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchClickInterface.onMatchClick(position);
            }
        });
    }

    //Animation
    private void setAnimation(View itemView,int postion){
        if(postion > lastPosition){
            //Setting animation
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPosition = postion;
        }
    }

    @Override
    public int getItemCount() {
        return matchModalArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //Creating variables
        private TextView homeTeamName, awayTeamName;
        private ImageView homeViewImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTeamName = itemView.findViewById(R.id.idHomeTName);
            awayTeamName = itemView.findViewById(R.id.idAwayTName);
            homeViewImage = itemView.findViewById(R.id.idHomeViewImg);
        }
    }

    //Interface
    public interface MatchClickInterface{
        void onMatchClick(int position);
    }



}

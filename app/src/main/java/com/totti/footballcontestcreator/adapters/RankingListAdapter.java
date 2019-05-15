package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Ranking;

import java.util.ArrayList;
import java.util.List;

public class RankingListAdapter extends RecyclerView.Adapter<RankingListAdapter.RankingViewHolder> {

	private List<Ranking> rankings;

	public RankingListAdapter() {
		rankings = new ArrayList<>();
	}

	class RankingViewHolder extends RecyclerView.ViewHolder {

		TextView rankingPlaceTextView;
		TextView rankingTeamNameTextView;
		TextView rankingPointsTextView;
		TextView rankingWinsTextView;
		TextView rankingDrawsTextView;
		TextView rankingLossesTextView;
		TextView rankingGoalDifferenceTextView;

		Ranking ranking;

		RankingViewHolder(final View rankingView) {
			super(rankingView);

			rankingPlaceTextView = rankingView.findViewById(R.id.ranking_place_textView);
			rankingTeamNameTextView = rankingView.findViewById(R.id.ranking_team_name_textView);
			rankingPointsTextView = rankingView.findViewById(R.id.ranking_points_textView);
			rankingWinsTextView = rankingView.findViewById(R.id.ranking_wins_textView);
			rankingDrawsTextView = rankingView.findViewById(R.id.ranking_draws_textView);
			rankingLossesTextView = rankingView.findViewById(R.id.ranking_losses_textView);
			rankingGoalDifferenceTextView = rankingView.findViewById(R.id.ranking_goal_difference_textView);
		}
	}

	@NonNull
	@Override
	public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View rankingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_list_item, parent, false);
		return new RankingViewHolder(rankingView);
	}

	@Override
	public void onBindViewHolder(@NonNull final RankingViewHolder holder, int position) {
		Ranking ranking = rankings.get(position);

		String place = position + 1 + ".";
		holder.rankingPlaceTextView.setText(place);
		if(position == 0) {
			holder.rankingPlaceTextView.setBackgroundResource(R.color.colorGold);
		}
		else if(position == 1) {
			holder.rankingPlaceTextView.setBackgroundResource(R.color.colorSilver);
		}
		else if(position == 2) {
			holder.rankingPlaceTextView.setBackgroundResource(R.color.colorBronze);
		}
		else if(position == (getItemCount() - 1)) {
			holder.rankingPlaceTextView.setBackgroundResource(R.color.colorLast);
		}
		else {
			holder.rankingPlaceTextView.setBackgroundResource(0);
		}

		holder.rankingTeamNameTextView.setText(ranking.getTeam_name());

		holder.rankingPointsTextView.setText(Integer.toString(ranking.getPoints()));

		holder.rankingWinsTextView.setText(Integer.toString(ranking.getWins()));

		holder.rankingDrawsTextView.setText(Integer.toString(ranking.getDraws()));

		holder.rankingLossesTextView.setText(Integer.toString(ranking.getLosses()));

		holder.rankingGoalDifferenceTextView.setText(Integer.toString(ranking.getGoal_difference()));

		holder.ranking = ranking;
	}

	@Override
	public int getItemCount() {
		return rankings.size();
	}

	public void setRankings(List<Ranking> rankings) {
		this.rankings.clear();
		this.rankings.addAll(rankings);
		notifyDataSetChanged();
	}
}

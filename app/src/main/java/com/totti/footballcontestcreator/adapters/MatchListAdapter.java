package com.totti.footballcontestcreator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchViewHolder> {

	private List<Match> matches;

	public interface OnMatchClickedListener {
		void onMatchClicked(Match match);
	}

	private OnMatchClickedListener listener;

	public MatchListAdapter(OnMatchClickedListener listener) {
		matches = new ArrayList<>();
		this.listener = listener;
	}

	class MatchViewHolder extends RecyclerView.ViewHolder {

		TextView matchDayTextView;
		TextView matchHomeTeamTextView;
		TextView matchVisitorTeamTextView;
		TextView matchHomeScoreTextView;
		TextView matchVisitorScoreTextView;

		Match match;

		MatchViewHolder(final View matchView) {
			super(matchView);

			matchDayTextView = matchView.findViewById(R.id.match_day_textView);
			matchHomeTeamTextView = matchView.findViewById(R.id.match_home_team_textView);
			matchVisitorTeamTextView = matchView.findViewById(R.id.match_visitor_team_textView);
			matchHomeScoreTextView = matchView.findViewById(R.id.match_home_score_textView);
			matchVisitorScoreTextView = matchView.findViewById(R.id.match_visitor_score_textView);

			matchView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(match != null) {
						listener.onMatchClicked(match);
					}
				}
			});
		}
	}

	@NonNull
	@Override
	public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View matchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
		return new MatchViewHolder(matchView);
	}

	@Override
	public void onBindViewHolder(@NonNull final MatchViewHolder holder, int position) {
		Match match = matches.get(position);

		String matchDay = "Matchday #" + match.getMatch_day();
		holder.matchDayTextView.setText(matchDay);

		holder.matchHomeTeamTextView.setText(match.getHome_name());

		holder.matchVisitorTeamTextView.setText(match.getVisitor_name());

		if((match.getHome_score() != 0) || (match.getVisitor_score() != 0) || (match.getFinal_score())) {
			holder.matchHomeScoreTextView.setText(Integer.toString(match.getHome_score()));

			holder.matchVisitorScoreTextView.setText(Integer.toString(match.getVisitor_score()));
		}
		else {
			holder.matchHomeScoreTextView.setText(null);

			holder.matchVisitorScoreTextView.setText(null);
		}

		holder.match = match;
	}

	@Override
	public int getItemCount() {
		return matches.size();
	}

	public void setMatches(List<Match> matches) {
		this.matches.clear();
		this.matches.addAll(matches);
		notifyDataSetChanged();
	}
}

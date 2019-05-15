package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.adapters.MatchListAdapter;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;

import java.util.List;

public class MatchListFragment extends Fragment implements MatchListAdapter.OnMatchClickedListener {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_list_fragment, container, false);

		long id = this.getArguments().getLong("id");
		String type = this.getArguments().getString("type");
		String tab = this.getArguments().getString("tab");

		final MatchListAdapter matchListAdapter = new MatchListAdapter(this);

		MatchViewModel matchViewModel = ViewModelProviders.of(getActivity()).get(MatchViewModel.class);
		if(type != null && tab != null) {
			if(type.equals("team")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, false).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTeamAndFinalScore(id, true).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
			else if(type.equals("tournament")) {
				if(tab.equals("matches")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, false).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
				else if(tab.equals("results")) {
					matchViewModel.getAllMatchesByTournamentAndFinalScore(id, true).observe(this, new Observer<List<Match>>() {
						@Override
						public void onChanged(@Nullable List<Match> matches) {
							matchListAdapter.setMatches(matches);
						}
					});
				}
			}
		}

		RecyclerView recyclerView = rootView.findViewById(R.id.match_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(matchListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		return rootView;
	}

	@Override
	public void onMatchClicked(Match match) {
		Bundle args = new Bundle();
		args.putLong("id", match.getId());
		args.putLong("tournament_id", match.getTournament_id());
		args.putString("tournament_name", match.getTournament_name());
		args.putInt("match_day", match.getMatch_day());
		args.putLong("home_id", match.getHome_id());
		args.putString("home_name", match.getHome_name());
		args.putInt("home_score", match.getHome_score());
		args.putLong("visitor_id", match.getVisitor_id());
		args.putString("visitor_name", match.getVisitor_name());
		args.putInt("visitor_score", match.getVisitor_score());
		args.putString("comments", match.getComments());
		args.putBoolean("final_score", match.getFinal_score());

		if(match.getFinal_score()) {
			ResultDetailsDialogFragment resultDetailsDialogFragment = new ResultDetailsDialogFragment();
			resultDetailsDialogFragment.setArguments(args);
			resultDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), ResultDetailsDialogFragment.TAG);
		}
		else {
			MatchDetailsDialogFragment matchDetailsDialogFragment = new MatchDetailsDialogFragment();
			matchDetailsDialogFragment.setArguments(args);
			matchDetailsDialogFragment.show(getActivity().getSupportFragmentManager(), MatchDetailsDialogFragment.TAG);
		}
	}
}

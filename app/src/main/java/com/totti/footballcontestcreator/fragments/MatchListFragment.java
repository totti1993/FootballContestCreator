package com.totti.footballcontestcreator.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

	private MatchViewModel matchViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.match_list_fragment, container, false);

		String type = this.getArguments().getString("type");
		long id = this.getArguments().getLong("id");
		String tab = this.getArguments().getString("tab");

		final MatchListAdapter matchListAdapter = new MatchListAdapter(this, getActivity());

		matchViewModel = ViewModelProviders.of(getActivity()).get(MatchViewModel.class);
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

		RecyclerView recyclerView = rootView.findViewById(R.id.match_recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(matchListAdapter);

		return rootView;
	}

	@Override
	public void onMatchClicked(Match match) {
		// ilyen lesz a meccskezelő
		// new NewTeamDialogFragment().show(getActivity().getSupportFragmentManager(), NewTeamDialogFragment.TAG);
	}
}

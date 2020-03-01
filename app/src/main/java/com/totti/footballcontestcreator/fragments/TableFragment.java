package com.totti.footballcontestcreator.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.adapters.RankingListAdapter;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;

import java.util.List;

public class TableFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.ranking_list_fragment, container, false);

		String id = this.getArguments().getString("id");
		String tournamentType = this.getArguments().getString("tournamentType");

		final RankingListAdapter rankingListAdapter = new RankingListAdapter(tournamentType);

		RankingViewModel rankingViewModel = ViewModelProviders.of(getActivity()).get(RankingViewModel.class);
		rankingViewModel.getAllRankingsByTournamentOrdered(id).observe(this, new Observer<List<Ranking>>() {
			@Override
			public void onChanged(@Nullable List<Ranking> rankings) {
				rankingListAdapter.setRankings(rankings);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.ranking_recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(rankingListAdapter);

		return rootView;
	}
}

package com.totti.footballcontestcreator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.totti.footballcontestcreator.adapters.RankingListAdapter;
import com.totti.footballcontestcreator.database.Ranking;
import com.totti.footballcontestcreator.R;
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

		// Keep the table up to date
		RankingViewModel rankingViewModel = new ViewModelProvider(requireActivity()).get(RankingViewModel.class);
		rankingViewModel.getAllRankingsByTournamentOrdered(id).observe(getViewLifecycleOwner(), new Observer<List<Ranking>>() {
			@Override
			public void onChanged(List<Ranking> rankings) {
				rankingListAdapter.setRankings(rankings);
			}
		});

		RecyclerView recyclerView = rootView.findViewById(R.id.ranking_recyclerView);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setAdapter(rankingListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);

		return rootView;
	}
}

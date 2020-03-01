package com.totti.footballcontestcreator.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

	private PieChart statsPieChart;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.statistics_fragment, container, false);

		String id = this.getArguments().getString("id");

		statsPieChart = rootView.findViewById(R.id.stats_pieChart);

		TeamViewModel teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
		teamViewModel.getTeamById(id).observe(this, new Observer<Team>() {
			@Override
			public void onChanged(Team team) {
				List<PieEntry> entries = new ArrayList<>();

				entries.add(new PieEntry(team.getAll_time_wins(), "Wins"));
				entries.add(new PieEntry(team.getAll_time_draws(), "Draws"));
				entries.add(new PieEntry(team.getAll_time_losses(), "Losses"));

				PieDataSet dataSet = new PieDataSet(entries, "(All Time Stats)");
				dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

				PieData data = new PieData(dataSet);
				statsPieChart.setData(data);
				statsPieChart.invalidate();
			}
		});

		/*new AsyncTask<Void, Void, Team>() {
			@Override
			protected Team doInBackground(Void... voids) {
				return teamViewModel.getTeamById(id);
			}

			@Override
			protected void onPostExecute(Team team) {
				List<PieEntry> entries = new ArrayList<>();

				entries.add(new PieEntry(team.getAll_time_wins(), "Wins"));
				entries.add(new PieEntry(team.getAll_time_draws(), "Draws"));
				entries.add(new PieEntry(team.getAll_time_losses(), "Losses"));

				PieDataSet dataSet = new PieDataSet(entries, "(All Time Stats)");
				dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

				PieData data = new PieData(dataSet);
				statsPieChart.setData(data);
				statsPieChart.invalidate();
			}
		}.execute();*/

		return rootView;
	}
}

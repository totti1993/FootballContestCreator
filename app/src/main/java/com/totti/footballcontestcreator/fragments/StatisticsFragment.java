package com.totti.footballcontestcreator.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.R;
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

		final ValueFormatter formatter = new ValueFormatter() {
			@Override
			public String getFormattedValue(float value) {
				return String.valueOf((int) value);
			}
		};

		statsPieChart = rootView.findViewById(R.id.stats_pieChart);
		statsPieChart.getDescription().setEnabled(false);
		statsPieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
		statsPieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

		// Keep the statistics (pie chart) up to date
		TeamViewModel teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
		teamViewModel.getTeamById(id).observe(getViewLifecycleOwner(), new Observer<Team>() {
			@Override
			public void onChanged(Team team) {
				List<PieEntry> entries = new ArrayList<>();

				entries.add(new PieEntry(team.getAll_time_wins(), "Wins"));
				entries.add(new PieEntry(team.getAll_time_draws(), "Draws"));
				entries.add(new PieEntry(team.getAll_time_losses(), "Losses"));

				PieDataSet dataSet = new PieDataSet(entries, "(All-time Stats)");
				dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
				dataSet.setSliceSpace(2f);
				dataSet.setValueFormatter(formatter);
				dataSet.setValueTextColor(Color.WHITE);
				dataSet.setValueTextSize(20f);

				PieData data = new PieData(dataSet);
				statsPieChart.setData(data);
				statsPieChart.invalidate();
			}
		});

		return rootView;
	}
}

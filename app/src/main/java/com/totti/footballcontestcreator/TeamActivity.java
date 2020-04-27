package com.totti.footballcontestcreator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.fragments.CommentsFragment;
import com.totti.footballcontestcreator.fragments.MatchListFragment;
import com.totti.footballcontestcreator.fragments.StatisticsFragment;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

public class TeamActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private String id;      // ID of the team

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);

		Toolbar toolbar = findViewById(R.id.shared_toolbar);
		this.setSupportActionBar(toolbar);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		BottomNavigationView navigationView = findViewById(R.id.shared_bottom_navigation_view);
		navigationView.inflateMenu(R.menu.team_navigation_bar);
		navigationView.setOnNavigationItemSelectedListener(this);
		navigationView.setSelectedItemId(R.id.team_nav_statistics);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));

		TeamViewModel teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
		teamViewModel.getTeamById(id).observe(this, new Observer<Team>() {
			@Override
			public void onChanged(Team team) {
				setTitle(team.getName());
			}
		});
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Bundle args = new Bundle();
		args.putString("id", id);
		args.putString("type", "team");

		switch(item.getItemId()) {
			case R.id.team_nav_statistics:
				StatisticsFragment statisticsFragment = new StatisticsFragment();
				statisticsFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, statisticsFragment).commit();
				break;
			case R.id.team_nav_results:
				args.putString("tournamentType", "Mixed");
				args.putString("tab", "results");
				MatchListFragment resultListFragment = new MatchListFragment();
				resultListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, resultListFragment).commit();
				break;
			case R.id.team_nav_matches:
				args.putString("tournamentType", "Mixed");
				args.putString("tab", "matches");
				MatchListFragment matchListFragment = new MatchListFragment();
				matchListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, matchListFragment).commit();
				break;
			case R.id.team_nav_comments:
				CommentsFragment commentsFragment = new CommentsFragment();
				commentsFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, commentsFragment).commit();
				break;
			default:
				break;
		}

		return true;
	}
}

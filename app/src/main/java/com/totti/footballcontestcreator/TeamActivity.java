package com.totti.footballcontestcreator;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.fragments.CommentsFragment;
import com.totti.footballcontestcreator.fragments.MatchListFragment;
import com.totti.footballcontestcreator.fragments.StatisticsFragment;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

public class TeamActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);

		Toolbar toolbar = findViewById(R.id.shared_toolbar);
		this.setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		id = intent.getLongExtra("id", 0);

		BottomNavigationView navigationView = findViewById(R.id.shared_bottom_navigation_view);
		navigationView.inflateMenu(R.menu.team_navigation_bar);
		navigationView.setOnNavigationItemSelectedListener(this);
		navigationView.setSelectedItemId(R.id.team_nav_statistics);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));

		final TeamViewModel teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
		new AsyncTask<Void, Void, Team>() {
			@Override
			protected Team doInBackground(Void... voids) {
				return teamViewModel.getTeamById(id);
			}

			@Override
			protected void onPostExecute(Team team) {
				setTitle(team.getName());
			}
		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_action_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_search) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Bundle args = new Bundle();
		args.putLong("id", id);
		args.putString("type", "team");

		switch(item.getItemId()) {
			case R.id.team_nav_statistics:
				StatisticsFragment statisticsFragment = new StatisticsFragment();
				statisticsFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, statisticsFragment).commit();
				break;
			case R.id.team_nav_results:
				args.putString("tab", "results");
				MatchListFragment resultListFragment = new MatchListFragment();
				resultListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, resultListFragment).commit();
				break;
			case R.id.team_nav_matches:
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

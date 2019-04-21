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

import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.fragments.MatchListFragment;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class TournamentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private long tournamentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);

		Toolbar toolbar = findViewById(R.id.shared_toolbar);
		this.setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		BottomNavigationView navigationView = findViewById(R.id.shared_bottom_navigation_view);
		navigationView.inflateMenu(R.menu.tournament_navigation_bar);
		navigationView.setOnNavigationItemSelectedListener(this);

		Intent intent = getIntent();
		tournamentId = intent.getLongExtra("tournamentId", 0);
		final TournamentViewModel tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel.class);
		new AsyncTask<Void, Void, Tournament>() {
			@Override
			protected Tournament doInBackground(Void... voids) {
				return tournamentViewModel.getTournamentById(tournamentId);
			}

			@Override
			protected void onPostExecute(Tournament tournament) {
				setTitle(tournament.getName());
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
		MatchListFragment matchListFragment = new MatchListFragment();
		Bundle args = new Bundle();

		switch(item.getItemId()) {
			case R.id.tournament_nav_table:
				break;
			case R.id.tournament_nav_results:
				args.putString("type", "tournament");
				args.putLong("id", tournamentId);
				args.putString("tab", "results");
				matchListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, matchListFragment).commit();
				break;
			case R.id.tournament_nav_matches:
				args.putString("type", "tournament");
				args.putLong("id", tournamentId);
				args.putString("tab", "matches");
				matchListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, matchListFragment).commit();
				break;
			case R.id.tournament_nav_comments:
				break;
			default:
				break;
		}

		return true;
	}
}

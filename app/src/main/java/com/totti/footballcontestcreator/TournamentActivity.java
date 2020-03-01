package com.totti.footballcontestcreator;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.fragments.CommentsFragment;
import com.totti.footballcontestcreator.fragments.MatchListFragment;
import com.totti.footballcontestcreator.fragments.TableFragment;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class TournamentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private String id;
	private String tournamentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);

		Toolbar toolbar = findViewById(R.id.shared_toolbar);
		this.setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tournamentType = intent.getStringExtra("tournamentType");

		BottomNavigationView navigationView = findViewById(R.id.shared_bottom_navigation_view);
		navigationView.inflateMenu(R.menu.tournament_navigation_bar);
		navigationView.setOnNavigationItemSelectedListener(this);
		navigationView.setSelectedItemId(R.id.tournament_nav_table);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));

		TournamentViewModel tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel.class);
		tournamentViewModel.getTournamentById(id).observe(this, new Observer<Tournament>() {
			@Override
			public void onChanged(Tournament tournament) {
				setTitle(tournament.getName());
			}
		});

		/*new AsyncTask<Void, Void, Tournament>() {
			@Override
			protected Tournament doInBackground(Void... voids) {
				return tournamentViewModel.getTournamentById(id);
			}

			@Override
			protected void onPostExecute(Tournament tournament) {
				setTitle(tournament.getName());
			}
		}.execute();*/
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
		args.putString("id", id);
		args.putString("tournamentType", tournamentType);
		args.putString("type", "tournament");

		switch(item.getItemId()) {
			case R.id.tournament_nav_table:
				TableFragment tableFragment = new TableFragment();
				tableFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, tableFragment).commit();
				break;
			case R.id.tournament_nav_results:
				args.putString("tab", "results");
				MatchListFragment resultListFragment = new MatchListFragment();
				resultListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, resultListFragment).commit();
				break;
			case R.id.tournament_nav_matches:
				args.putString("tab", "matches");
				MatchListFragment matchListFragment = new MatchListFragment();
				matchListFragment.setArguments(args);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.shared_fragment_content, matchListFragment).commit();
				break;
			case R.id.tournament_nav_comments:
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

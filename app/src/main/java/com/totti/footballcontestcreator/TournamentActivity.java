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

import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.fragments.CommentsFragment;
import com.totti.footballcontestcreator.fragments.MatchListFragment;
import com.totti.footballcontestcreator.fragments.TableFragment;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class TournamentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private String id;              // ID of the tournament
	private String tournamentType;  // Type of the tournament: "Championship" or "Elimination"

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared);

		Toolbar toolbar = findViewById(R.id.shared_toolbar);
		this.setSupportActionBar(toolbar);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tournamentType = intent.getStringExtra("tournamentType");

		BottomNavigationView navigationView = findViewById(R.id.shared_bottom_navigation_view);
		navigationView.inflateMenu(R.menu.tournament_navigation_bar);
		navigationView.setOnNavigationItemSelectedListener(this);
		navigationView.setSelectedItemId(R.id.tournament_nav_table);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));

		TournamentViewModel tournamentViewModel = new ViewModelProvider(this).get(TournamentViewModel.class);
		tournamentViewModel.getTournamentById(id).observe(this, new Observer<Tournament>() {
			@Override
			public void onChanged(Tournament tournament) {
				setTitle(tournament.getName());
			}
		});
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Bundle args = new Bundle();
		args.putString("id", id);
		args.putString("type", "tournament");
		args.putString("tournamentType", tournamentType);

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

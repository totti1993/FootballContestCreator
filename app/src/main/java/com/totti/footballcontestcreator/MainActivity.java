package com.totti.footballcontestcreator;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.totti.footballcontestcreator.adapters.TeamListAdapter;
import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.fragments.NewTeamDialogFragment;
import com.totti.footballcontestcreator.fragments.NewTournamentDialogFragment;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,
				   TournamentListAdapter.OnTournamentSelectedListener,
				   TeamListAdapter.OnTeamSelectedListener,
				   NewTournamentDialogFragment.NewTournamentDialogListener,
				   NewTeamDialogFragment.NewTeamDialogListener {

	private RecyclerView recyclerView;

	private TournamentListAdapter tournamentListAdapter;
	private TeamListAdapter teamListAdapter;

	private TournamentViewModel tournamentViewModel;
	private TeamViewModel teamViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setTheme(R.style.AppTheme_NoActionBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		recyclerView = findViewById(R.id.recycler_view_main);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		tournamentListAdapter = new TournamentListAdapter(this);
		teamListAdapter = new TeamListAdapter(this);

		tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel.class);
		tournamentViewModel.getAllTournaments().observe(this, new Observer<List<Tournament>>() {
			@Override
			public void onChanged(@Nullable List<Tournament> tournaments) {
				tournamentListAdapter.setTournaments(tournaments);
			}
		});
		teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
		teamViewModel.getAllTeams().observe(this, new Observer<List<Team>>() {
			@Override
			public void onChanged(@Nullable List<Team> teams) {
				teamListAdapter.setTeams(teams);
			}
		});

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (getTitle().equals("Tournaments")) {
					new NewTournamentDialogFragment().show(getSupportFragmentManager(), NewTournamentDialogFragment.TAG);
				}
				else if (getTitle().equals("Teams")) {
					new NewTeamDialogFragment().show(getSupportFragmentManager(), NewTeamDialogFragment.TAG);
				}
			}
		});

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if(drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if(id == R.id.action_search) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch(id) {
			case R.id.nav_tournaments:
				this.setTitle("Tournaments");
				recyclerView.setAdapter(tournamentListAdapter);
				break;
			case R.id.nav_teams:
				this.setTitle("Teams");
				recyclerView.setAdapter(teamListAdapter);
				break;
			case R.id.nav_favorites:
				this.setTitle("Favorites");
				break;
			case R.id.nav_settings:
				this.setTitle("Settings");
				break;
			default:
				break;
		}

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onTournamentCreated(Tournament newTournament, List<Team> selectedTeams) {
		tournamentViewModel.insert(newTournament);

		// insert ranking table here
		Toast.makeText(this, "Tournament \"" + newTournament.getName() + "\" created with " + newTournament.getTeams().toString() + " team(s)!", Toast.LENGTH_SHORT).show();

		for(Team team : selectedTeams) {
			team.setSelected(false);
			teamViewModel.update(team);
		}
	}

	@Override
	public void onTournamentClicked(Tournament tournament) {
		Toast.makeText(this, "Tournament \"" + tournament.getName() + "\" clicked!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTournamentLongClicked(final Tournament tournament) {
		new AlertDialog.Builder(this).setMessage("Are you sure you want to delete tournament \"" + tournament.getName() + "\"?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								tournamentViewModel.delete(tournament);
								Toast.makeText(MainActivity.this, "Tournament \"" + tournament.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("No", null)
						.show();
	}

	@Override
	public void onTournamentStarClicked(Tournament tournament) {
		tournamentViewModel.update(tournament);
		if(tournament.getFavorite()) {
			Toast.makeText(this, "Tournament \"" + tournament.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this, "Tournament \"" + tournament.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onTeamCreated(Team newTeam) {
		teamViewModel.insert(newTeam);
		Toast.makeText(this, "Team \"" + newTeam.getName() + "\" created!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTeamClicked(Team team) {
		Toast.makeText(this, "Team \"" + team.getName() + "\" clicked!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onTeamLongClicked(final Team team) {
		new AlertDialog.Builder(this).setMessage("Are you sure you want to delete team \"" + team.getName() + "\"?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								teamViewModel.delete(team);
								Toast.makeText(MainActivity.this, "Team \"" + team.getName() + "\" deleted!", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("No", null)
						.show();
	}

	@Override
	public void onTeamStarClicked(Team team) {
		teamViewModel.update(team);
		if(team.getFavorite()) {
			Toast.makeText(this, "Team \"" + team.getName() + "\" added to favorites!", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(this, "Team \"" + team.getName() + "\" removed from favorites!", Toast.LENGTH_SHORT).show();
		}
	}
}

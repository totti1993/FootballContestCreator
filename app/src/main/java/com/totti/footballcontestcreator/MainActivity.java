package com.totti.footballcontestcreator;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.totti.footballcontestcreator.adapters.TournamentListAdapter;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.fragments.NewTournamentDialogFragment;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, NewTournamentDialogFragment.NewTournamentDialogListener {

	private TournamentViewModel tournamentViewModel;

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

		RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
		final TournamentListAdapter adapter = new TournamentListAdapter(this);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel.class);

		tournamentViewModel.getAllTournaments().observe(this, new Observer<List<Tournament>>() {
			@Override
			public void onChanged(@Nullable List<Tournament> tournaments) {
				adapter.setTournaments(tournaments);
			}
		});

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new NewTournamentDialogFragment().show(getSupportFragmentManager(), NewTournamentDialogFragment.TAG);
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
				break;
			case R.id.nav_teams:
				this.setTitle("Teams");
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
	public void onTournamentCreated(Tournament newTournament) {
		tournamentViewModel.insert(newTournament);
	}
}

package com.totti.footballcontestcreator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;

import com.totti.footballcontestcreator.fragments.TeamListFragment;
import com.totti.footballcontestcreator.fragments.TournamentListFragment;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawer;

	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

		//FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		matchViewModel= ViewModelProviders.of(this).get(MatchViewModel.class);
		rankingViewModel = ViewModelProviders.of(this).get(RankingViewModel.class);
		teamViewModel = ViewModelProviders.of(this).get(TeamViewModel.class);
		tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel.class);

		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				matchViewModel.deleteAll();
				rankingViewModel.deleteAll();
				teamViewModel.deleteAll();
				tournamentViewModel.deleteAll();
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				setTheme(R.style.AppTheme);
			}
		}.execute();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.main_toolbar);
		this.setSupportActionBar(toolbar);

		drawer = findViewById(R.id.main_drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.main_navigation_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setCheckedItem(R.id.nav_tournaments);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));
	}

	@Override
	public void onBackPressed() {
		if(drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
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
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		int id = menuItem.getItemId();
		switch(id) {
			case R.id.nav_tournaments:
				this.setTitle("Tournaments");
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TournamentListFragment()).commit();
				break;
			case R.id.nav_teams:
				this.setTitle("Teams");
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TeamListFragment()).commit();
				break;
			/*
			case R.id.nav_favorites:
				this.setTitle("Favorites");
				break;
			case R.id.nav_settings:
				this.setTitle("Settings");
				break;
			*/
			default:
				break;
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}

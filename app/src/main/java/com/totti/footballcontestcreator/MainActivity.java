package com.totti.footballcontestcreator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

		setTheme(R.style.AppTheme);
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

		matchViewModel= new ViewModelProvider(this).get(MatchViewModel.class);
		rankingViewModel = new ViewModelProvider(this).get(RankingViewModel.class);
		teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
		tournamentViewModel = new ViewModelProvider(this).get(TournamentViewModel.class);

		DatabaseReference onlineConnected = FirebaseDatabase.getInstance().getReference(".info/connected");
		onlineConnected.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				boolean connected = dataSnapshot.getValue(Boolean.class);

				if(connected) {
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... voids) {
							matchViewModel.deleteAll();
							rankingViewModel.deleteAll();
							teamViewModel.deleteAll();
							tournamentViewModel.deleteAll();
							return null;
						}
					}.execute();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
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
/*
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
*/
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

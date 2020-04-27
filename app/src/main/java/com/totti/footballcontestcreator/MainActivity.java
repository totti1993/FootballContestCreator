package com.totti.footballcontestcreator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.totti.footballcontestcreator.fragments.SignInOutFragment;
import com.totti.footballcontestcreator.fragments.TeamListFragment;
import com.totti.footballcontestcreator.fragments.TournamentListFragment;
import com.totti.footballcontestcreator.viewmodels.FavoriteViewModel;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;
import com.totti.footballcontestcreator.viewmodels.RankingViewModel;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawer;    // Navigation drawer

	private FavoriteViewModel favoriteViewModel;
	private MatchViewModel matchViewModel;
	private RankingViewModel rankingViewModel;
	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.main_toolbar);
		this.setSupportActionBar(toolbar);

		drawer = findViewById(R.id.main_drawer_layout);
		final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		final NavigationView navigationView = findViewById(R.id.main_navigation_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setCheckedItem(R.id.nav_tournaments);
		onNavigationItemSelected(navigationView.getMenu().getItem(0));

		favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
		matchViewModel = new ViewModelProvider(this).get(MatchViewModel.class);
		rankingViewModel = new ViewModelProvider(this).get(RankingViewModel.class);
		teamViewModel = new ViewModelProvider(this).get(TeamViewModel.class);
		tournamentViewModel = new ViewModelProvider(this).get(TournamentViewModel.class);

		// When the connection is restored all local data must be synchronized with the online changes
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

						@Override
						protected void onPostExecute(Void aVoid) {
							teamViewModel.addListenerToOnlineDatabase();
							tournamentViewModel.addListenerToOnlineDatabase();
							rankingViewModel.addListenerToOnlineDatabase();
							matchViewModel.addListenerToOnlineDatabase();
						}
					}.execute();
				} else {
					matchViewModel.removeListenerFromOnlineDatabase();
					rankingViewModel.removeListenerFromOnlineDatabase();
					teamViewModel.removeListenerFromOnlineDatabase();
					tournamentViewModel.removeListenerFromOnlineDatabase();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

		// When the user changes all local "favorites" data must be synchronized with the online changes
		FirebaseAuth authentication = FirebaseAuth.getInstance();
		authentication.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
				TextView username = navigationView.getHeaderView(0).findViewById(R.id.username);

				if(firebaseAuth.getCurrentUser() != null) {
					username.setText(firebaseAuth.getCurrentUser().getEmail());

					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... voids) {
							favoriteViewModel.deleteAll();
							return null;
						}

						@Override
						protected void onPostExecute(Void aVoid) {
							favoriteViewModel.addListenerToOnlineDatabase(firebaseAuth.getCurrentUser().getEmail().replace(".", "(dot)"));
						}
					}.execute();
				} else {
					username.setText(R.string.default_username);

					// Delete is necessary, because the "favorites" table needs to be empty when no user is logged in
					favoriteViewModel.removeListenerFromOnlineDatabase();
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... voids) {
							favoriteViewModel.deleteAll();
							return null;
						}
					}.execute();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if(drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		int id = menuItem.getItemId();
		switch(id) {
			case R.id.nav_tournaments:
				this.setTitle(R.string.tournaments);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TournamentListFragment()).commit();
				break;
			case R.id.nav_teams:
				this.setTitle(R.string.teams);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TeamListFragment()).commit();
				break;
			case R.id.nav_sign_in_out:
				this.setTitle(R.string.sign_in_out);
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new SignInOutFragment()).commit();
				break;
			default:
				break;
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}

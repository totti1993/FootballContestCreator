package com.totti.footballcontestcreator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.totti.footballcontestcreator.fragments.TeamListFragment;
import com.totti.footballcontestcreator.fragments.TournamentListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setTheme(R.style.AppTheme);

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
		navigationView.setCheckedItem(R.id.nav_favorites);
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
			case R.id.nav_favorites:
				this.setTitle("Favorites");
				break;
			case R.id.nav_tournaments:
				this.setTitle("Tournaments");
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TournamentListFragment()).commit();
				break;
			case R.id.nav_teams:
				this.setTitle("Teams");
				this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_content, new TeamListFragment()).commit();
				break;
			case R.id.nav_settings:
				this.setTitle("Settings");
				break;
			default:
				break;
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}

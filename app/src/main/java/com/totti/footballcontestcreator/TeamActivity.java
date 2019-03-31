package com.totti.footballcontestcreator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class TeamActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);

		Toolbar toolbar = findViewById(R.id.team_toolbar);
		this.setSupportActionBar(toolbar);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String teamName = intent.getStringExtra("teamName");
		this.setTitle(teamName);

		BottomNavigationView navigationView = findViewById(R.id.team_bottom_navigation_view);
		navigationView.setOnNavigationItemSelectedListener(this);
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
		switch(item.getItemId()) {
			case R.id.team_nav_statistics:
				break;
			case R.id.team_nav_results:
				break;
			case R.id.team_nav_matches:
				break;
			case R.id.team_nav_comments:
				break;
			default:
				break;
		}

		return true;
	}
}

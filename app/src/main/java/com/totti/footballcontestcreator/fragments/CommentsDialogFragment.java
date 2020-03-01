package com.totti.footballcontestcreator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class CommentsDialogFragment extends DialogFragment {

	public static final String TAG = "CommentsDialogFragment";

	private TextView nameTextView;
	private EditText commentsEditText;

	private String id;
	private String type;

	//private Team team;
	//private Tournament tournament;

	private DatabaseReference onlineTeams;
	private DatabaseReference onlineTournaments;

	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = this.getArguments().getString("id");
		type = this.getArguments().getString("type");

		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");
		onlineTournaments = FirebaseDatabase.getInstance().getReference("tournaments");

		//String name = this.getArguments().getString("name");
		//String comments = this.getArguments().getString("comments");
		//boolean favorite = this.getArguments().getBoolean("favorite");

		if(type != null) {
			if(type.equals("team")) {
				/*int trophies = this.getArguments().getInt("trophies");
				int all_time_wins = this.getArguments().getInt("all_time_wins");
				int all_time_draws = this.getArguments().getInt("all_time_draws");
				int all_time_losses = this.getArguments().getInt("all_time_losses");
				boolean selected = this.getArguments().getBoolean("selected");

				team = new Team(name, comments);
				team.setId(id);
				team.setTrophies(trophies);
				team.setAll_time_wins(all_time_wins);
				team.setAll_time_draws(all_time_draws);
				team.setAll_time_losses(all_time_losses);
				team.setFavorite(favorite);
				team.setSelected(selected);*/

				teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);

				/*teamViewModel.getTeamById(id).observe(this, new Observer<Team>() {
					@Override
					public void onChanged(Team teamById) {
						team = teamById;
					}
				});*/
			}
			else if(type.equals("tournament")) {
				/*String type_2 = this.getArguments().getString("type_2");
				int rounds = this.getArguments().getInt("rounds");
				int teams = this.getArguments().getInt("teams");

				tournament = new Tournament(name, type_2, rounds, teams, comments);
				tournament.setId(id);
				tournament.setFavorite(favorite);*/

				tournamentViewModel = ViewModelProviders.of(getActivity()).get(TournamentViewModel.class);

				/*tournamentViewModel.getTournamentById(id).observe(this, new Observer<Tournament>() {
					@Override
					public void onChanged(Tournament tournamentById) {
						tournament = tournamentById;
					}
				});*/
			}
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.add_comments)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if(type.equals("team")) {
							//final Team team = getTeam();

							onlineTeams.child(id).child("comments").setValue(commentsEditText.getText().toString());

							Toast.makeText(getContext(), "Team comments updated!", Toast.LENGTH_SHORT).show();

							/*new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... voids) {
									teamViewModel.update(team);
									return null;
								}

								@Override
								protected void onPostExecute(Void aVoid) {
									Toast.makeText(getContext(), "Team comments updated!", Toast.LENGTH_SHORT).show();
								}
							}.execute();*/
						}
						else if(type.equals("tournament")) {
							//final Tournament tournament = getTournament();

							onlineTournaments.child(id).child("comments").setValue(commentsEditText.getText().toString());

							Toast.makeText(getContext(), "Tournament comments updated!", Toast.LENGTH_SHORT).show();

							/*new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... voids) {
									tournamentViewModel.update(tournament);
									return null;
								}

								@Override
								protected void onPostExecute(Void aVoid) {
									Toast.makeText(getContext(), "Tournament comments updated!", Toast.LENGTH_SHORT).show();
								}
							}.execute();*/
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.comments_dialog_fragment, null);

		nameTextView = contentView.findViewById(R.id.name_textView);
		commentsEditText = contentView.findViewById(R.id.comments_editText);

		if(type.equals("team")) {
			new AsyncTask<Void, Void, Team>() {
				@Override
				protected Team doInBackground(Void... voids) {
					return teamViewModel.getTeamByIdAsync(id);
				}

				@Override
				protected void onPostExecute(Team team) {
					nameTextView.setText(team.getName());
					commentsEditText.setText(team.getComments());
				}
			}.execute();
		}
		else if(type.equals("tournament")) {
			new AsyncTask<Void, Void, Tournament>() {
				@Override
				protected Tournament doInBackground(Void... voids) {
					return tournamentViewModel.getTournamentByIdAsync(id);
				}

				@Override
				protected void onPostExecute(Tournament tournament) {
					nameTextView.setText(tournament.getName());
					commentsEditText.setText(tournament.getComments());
				}
			}.execute();
		}

		/*nameTextView = contentView.findViewById(R.id.name_textView);
		if(type.equals("team")) {
			nameTextView.setText(team.getName());
		}
		else if(type.equals("tournament")) {
			nameTextView.setText(tournament.getName());
		}

		commentsEditText = contentView.findViewById(R.id.comments_editText);
		if(type.equals("team")) {
			commentsEditText.setText(team.getComments());
		}
		else if(type.equals("tournament")) {
			commentsEditText.setText(tournament.getComments());
		}*/

		return contentView;
	}

	/*private Team getTeam() {
		team.setComments(commentsEditText.getText().toString());

		return team;
	}*/

	/*private Tournament getTournament() {
		tournament.setComments(commentsEditText.getText().toString());

		return tournament;
	}*/
}

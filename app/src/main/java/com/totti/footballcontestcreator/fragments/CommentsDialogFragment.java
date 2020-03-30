package com.totti.footballcontestcreator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.database.Tournament;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class CommentsDialogFragment extends DialogFragment {

	public static final String TAG = "CommentsDialogFragment";

	private TextView nameTextView;
	private EditText commentsEditText;

	private String id;
	private String type;

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

		if(type != null) {
			if(type.equals("team")) {
				teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
			}
			else if(type.equals("tournament")) {
				tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);
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
							onlineTeams.child(id).child("comments").setValue(commentsEditText.getText().toString());

							Toast.makeText(requireContext(), "Team comments updated!", Toast.LENGTH_SHORT).show();
						}
						else if(type.equals("tournament")) {
							onlineTournaments.child(id).child("comments").setValue(commentsEditText.getText().toString());

							Toast.makeText(requireContext(), "Tournament comments updated!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.comments_dialog_fragment, null);

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

		return contentView;
	}
}

package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;

public class NewTeamDialogFragment extends DialogFragment {

	public static final String TAG = "NewTeamDialogFragment";

	private EditText nameEditText;
	private EditText commentsEditText;

	private TeamViewModel teamViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.new_team)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (isValid()) {
							final Team team = getTeam();

							new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... voids) {
									teamViewModel.insert(team);
									return null;
								}

								@Override
								protected void onPostExecute(Void aVoid) {
									Toast.makeText(getContext(), "Team \"" + team.getName() + "\" created!", Toast.LENGTH_SHORT).show();
								}
							}.execute();
						}
						else {
							Toast.makeText(getContext(), "Team not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.new_team_dialog_fragment, null);

		nameEditText = contentView.findViewById(R.id.team_name_editText);

		commentsEditText = contentView.findViewById(R.id.team_comments_editText);

		return contentView;
	}

	private boolean isValid() {
		return nameEditText.getText().length() > 0;
	}

	private Team getTeam() {
		String name = nameEditText.getText().toString();
		String comments = commentsEditText.getText().toString();

		return new Team(name, comments);
	}
}

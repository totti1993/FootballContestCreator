package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.totti.footballcontestcreator.database.Team;
import com.totti.footballcontestcreator.R;

public class NewTeamDialogFragment extends DialogFragment {

	public static final String TAG = "NewTeamDialogFragment";

	private EditText nameEditText;
	private EditText commentsEditText;

	private DatabaseReference onlineTeams;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onlineTeams = FirebaseDatabase.getInstance().getReference("teams");
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
						if(isValid()) {
							Team team = createTeam();

							onlineTeams.child(team.getId()).setValue(team);

							Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" created!", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(requireContext(), "Team not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.new_team_dialog_fragment, null);

		nameEditText = contentView.findViewById(R.id.team_name_editText);

		commentsEditText = contentView.findViewById(R.id.team_comments_editText);

		return contentView;
	}

	private boolean isValid() {
		return nameEditText.getText().length() > 0;
	}

	private Team createTeam() {
		String id = onlineTeams.push().getKey();

		String name = nameEditText.getText().toString();

		String comments = commentsEditText.getText().toString();

		return new Team(id, name, comments);
	}
}

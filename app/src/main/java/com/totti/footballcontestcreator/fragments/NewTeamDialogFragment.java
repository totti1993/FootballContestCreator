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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
					public void onClick(DialogInterface dialog, int which) {
						if(isValid()) {
							// Check if the given team name already exists in the database
							onlineTeams.orderByChild("name").equalTo(nameEditText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
								@Override
								public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
									if(!dataSnapshot.exists()) {
										Team team = createTeam();

										onlineTeams.child(team.getId()).setValue(team);

										Toast.makeText(requireContext(), "Team \"" + team.getName() + "\" created!", Toast.LENGTH_SHORT).show();
									}
									else {
										Toast.makeText(requireContext(), "Team name already in use!", Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public void onCancelled(@NonNull DatabaseError databaseError) {

								}
							});
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

	// Check if all given values are valid
	private boolean isValid() {
		// Check if the name of the team is not an empty string
		return nameEditText.getText().length() > 0;
	}

	// Create a new item under Firebase "teams" node
	private Team createTeam() {
		String id = onlineTeams.push().getKey();

		String creator = FirebaseAuth.getInstance().getCurrentUser().getEmail();

		String name = nameEditText.getText().toString();

		String comments = "Created by: " + creator + "\n\n" + commentsEditText.getText().toString();

		return new Team(id, creator, name, comments);
	}
}

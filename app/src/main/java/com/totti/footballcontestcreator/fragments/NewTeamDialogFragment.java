package com.totti.footballcontestcreator.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Team;

public class NewTeamDialogFragment extends DialogFragment {

	private EditText nameEditText;
	private EditText commentsEditText;

	public static final String TAG = "NewTeamDialogFragment";

	public interface NewTeamDialogListener {
		void onTeamCreated(Team newTeam);
	}

	private NewTeamDialogListener listener;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentActivity activity = getActivity();
		if (activity instanceof NewTeamDialogListener) {
			listener = (NewTeamDialogListener) activity;
		} else {
			throw new RuntimeException("Activity must implement the NewTeamDialogListener interface!");
		}
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
							listener.onTeamCreated(getTeam());
						}
						else {
							Toast.makeText(getActivity(), "Team not created!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_team, null);
		nameEditText = contentView.findViewById(R.id.TeamNameEditText);

		commentsEditText = contentView.findViewById(R.id.TeamCommentsEditText);

		return contentView;
	}

	private boolean isValid() {
		return nameEditText.getText().length() > 0;
	}

	private Team getTeam() {
		String name = nameEditText.getText().toString();
		String comments = commentsEditText.getText().toString();

		return  new Team(name, comments);
	}
}

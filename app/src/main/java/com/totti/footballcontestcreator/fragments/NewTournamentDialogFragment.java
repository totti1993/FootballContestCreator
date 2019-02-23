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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Tournament;

public class NewTournamentDialogFragment extends DialogFragment {

	private EditText nameEditText;
	private RadioGroup typeRadioGroup;
	private RadioButton typeCRadioButton, typeERadioButton;
	private EditText roundsEditText;
	private EditText commentsEditText;

	public static final String TAG = "NewTournamentDialogFragment";

	public interface NewTournamentDialogListener {
		void onTournamentCreated(Tournament newTournament);
	}

	private NewTournamentDialogListener listener;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentActivity activity = getActivity();
		if (activity instanceof NewTournamentDialogListener) {
			listener = (NewTournamentDialogListener) activity;
		} else {
			throw new RuntimeException("Activity must implement the NewTournamentDialogListener interface!");
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.new_tournament)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (isValid()) {
							listener.onTournamentCreated(getTournament());
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_tournament, null);
		nameEditText = contentView.findViewById(R.id.TournamentNameEditText);

		typeRadioGroup = contentView.findViewById(R.id.TournamentTypeRadioGroup);
		typeCRadioButton = contentView.findViewById(R.id.TournamentTypeCRadioButton);
		typeERadioButton = contentView.findViewById(R.id.TournamentTypeERadioButton);

		roundsEditText = contentView.findViewById(R.id.TournamentRoundsEditText);

		commentsEditText = contentView.findViewById(R.id.TournamentCommentsEditText);

		return contentView;
	}

	private boolean isValid() {
		return nameEditText.getText().length() > 0;
	}

	private Tournament getTournament() {

		String name = nameEditText.getText().toString();
		Tournament.Type type = null;
		if(typeRadioGroup.getCheckedRadioButtonId() == typeCRadioButton.getId()) {
			type = Tournament.Type.CHAMPIONSHIP;
		}
		else if(typeRadioGroup.getCheckedRadioButtonId() == typeERadioButton.getId()) {
			type = Tournament.Type.ELIMINATION;
		}
		Integer rounds = Integer.parseInt(roundsEditText.getText().toString());
		String comments = commentsEditText.getText().toString();

		return  new Tournament(name, type, rounds, comments);
	}
}

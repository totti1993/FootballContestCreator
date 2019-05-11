package com.totti.footballcontestcreator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;

public class MatchDetailsDialogFragment extends DialogFragment {

	public static final String TAG = "MatchDetailsDialogFragment";

	private TextView tournamentNameTextView;
	private TextView matchDayNumberTextView;
	private EditText homeTeamScoreEditText;
	private TextView homeTeamNameTextView;
	private EditText visitorTeamScoreEditText;
	private TextView visitorTeamNameTextView;
	private EditText commentsEditText;
	private CheckBox finalScoreCheckBox;

	private Match match;

	private MatchViewModel matchViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		long id = this.getArguments().getLong("id");
		long tournament_id = this.getArguments().getLong("tournament_id");
		String tournament_name = this.getArguments().getString("tournament_name");
		int match_day = this.getArguments().getInt("match_day");
		long home_id = this.getArguments().getLong("home_id");
		String home_name = this.getArguments().getString("home_name");
		int home_score = this.getArguments().getInt("home_score");
		long visitor_id = this.getArguments().getLong("visitor_id");
		String visitor_name = this.getArguments().getString("visitor_name");
		int visitor_score = this.getArguments().getInt("visitor_score");
		String comments = this.getArguments().getString("comments");
		boolean final_score = this.getArguments().getBoolean("final_score");

		match = new Match(tournament_id, tournament_name, match_day, home_id, home_name, visitor_id, visitor_name);
		match.setId(id);
		match.setHome_score(home_score);
		match.setVisitor_score(visitor_score);
		match.setComments(comments);
		match.setFinal_score(final_score);

		matchViewModel = ViewModelProviders.of(getActivity()).get(MatchViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.match_details)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if(isValid()) {
							final Match match = getMatch();

							new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... voids) {
									matchViewModel.update(match);
									return null;
								}

								@Override
								protected void onPostExecute(Void aVoid) {
									Toast.makeText(getContext(), "Match updated!", Toast.LENGTH_SHORT).show();
								}
							}.execute();
						}
						else {
							Toast.makeText(getContext(), "Match not updated!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.match_details_dialog_fragment, null);

		tournamentNameTextView = contentView.findViewById(R.id.match_tournament_name_textView);
		tournamentNameTextView.setText(match.getTournament_name());

		matchDayNumberTextView = contentView.findViewById(R.id.match_day_number_textView);
		matchDayNumberTextView.setText(Integer.toString(match.getMatch_day()));

		homeTeamScoreEditText = contentView.findViewById(R.id.match_home_team_score_editText);
		homeTeamScoreEditText.setText(Integer.toString(match.getHome_score()));

		homeTeamNameTextView = contentView.findViewById(R.id.match_home_team_name_textView);
		homeTeamNameTextView.setText(match.getHome_name());

		visitorTeamScoreEditText = contentView.findViewById(R.id.match_visitor_team_score_editText);
		visitorTeamScoreEditText.setText(Integer.toString(match.getVisitor_score()));

		visitorTeamNameTextView = contentView.findViewById(R.id.match_visitor_team_name_textView);
		visitorTeamNameTextView.setText(match.getVisitor_name());

		commentsEditText = contentView.findViewById(R.id.match_comments_editText);
		commentsEditText.setText(match.getComments());

		finalScoreCheckBox = contentView.findViewById(R.id.match_final_score_checkBox);
		finalScoreCheckBox.setChecked(match.getFinal_score());
		finalScoreCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				match.setFinal_score(isChecked);
			}
		});

		return contentView;
	}

	private boolean isValid() {
		if((Integer.parseInt(homeTeamScoreEditText.getText().toString()) < 0) || (Integer.parseInt(visitorTeamScoreEditText.getText().toString()) < 0)) {
			return false;
		}

		return true;
	}

	private Match getMatch() {
		match.setHome_score(Integer.parseInt(homeTeamScoreEditText.getText().toString()));

		match.setVisitor_score(Integer.parseInt(visitorTeamScoreEditText.getText().toString()));

		match.setComments(commentsEditText.getText().toString());

		return match;
	}
}

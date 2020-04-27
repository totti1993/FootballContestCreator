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

import com.totti.footballcontestcreator.database.Match;
import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.MatchViewModel;

public class ResultDetailsDialogFragment extends DialogFragment {

	public static final String TAG = "ResultDetailsDialogFragment";

	private TextView tournamentNameTextView;
	private TextView matchDayNumberTextView;
	private TextView homeTeamScoreTextView;
	private TextView homeTeamNameTextView;
	private TextView visitorTeamScoreTextView;
	private TextView visitorTeamNameTextView;
	private EditText commentsEditText;

	private String id;

	private DatabaseReference onlineMatches;

	private MatchViewModel matchViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = this.getArguments().getString("id");

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");

		matchViewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.match_details)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onlineMatches.child(id).child("comments").setValue(commentsEditText.getText().toString());

						Toast.makeText(requireContext(), "Match comments updated!", Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.result_details_dialog_fragment, null);

		tournamentNameTextView = contentView.findViewById(R.id.result_tournament_name_textView);
		matchDayNumberTextView = contentView.findViewById(R.id.result_day_number_textView);
		homeTeamScoreTextView = contentView.findViewById(R.id.result_home_team_score_textView);
		homeTeamNameTextView = contentView.findViewById(R.id.result_home_team_name_textView);
		visitorTeamScoreTextView = contentView.findViewById(R.id.result_visitor_team_score_textView);
		visitorTeamNameTextView = contentView.findViewById(R.id.result_visitor_team_name_textView);
		commentsEditText = contentView.findViewById(R.id.result_comments_editText);

		new AsyncTask<Void, Void, Match>() {
			@Override
			protected Match doInBackground(Void... voids) {
				return matchViewModel.getMatchByIdAsync(id);
			}

			@Override
			protected void onPostExecute(Match match) {
				tournamentNameTextView.setText(match.getTournament_name());
				String matchDay = "Matchday #" + match.getMatch_day();
				matchDayNumberTextView.setText(matchDay);
				homeTeamScoreTextView.setText(Integer.toString(match.getHome_score()));
				homeTeamNameTextView.setText(match.getHome_name());
				visitorTeamScoreTextView.setText(Integer.toString(match.getVisitor_score()));
				visitorTeamNameTextView.setText(match.getVisitor_name());
				commentsEditText.setText(match.getComments());
				// Set cursor to the end of the text
				commentsEditText.setSelection(commentsEditText.getText().length());
			}
		}.execute();

		return contentView;
	}
}

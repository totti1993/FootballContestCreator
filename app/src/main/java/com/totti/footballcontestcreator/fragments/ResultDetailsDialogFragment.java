package com.totti.footballcontestcreator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.totti.footballcontestcreator.database.Match;
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

	//private Match match;
	private String id;

	private DatabaseReference onlineMatches;

	private MatchViewModel matchViewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = this.getArguments().getString("id");

		/*long tournament_id = this.getArguments().getLong("tournament_id");
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
		match.setFinal_score(final_score);*/

		onlineMatches = FirebaseDatabase.getInstance().getReference("matches");

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
						//final Match match = getMatch();

						onlineMatches.child(id).child("comments").setValue(commentsEditText.getText().toString());

						Toast.makeText(getContext(), "Match comments updated!", Toast.LENGTH_SHORT).show();

						/*new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... voids) {
								matchViewModel.update(match);
								return null;
							}

							@Override
							protected void onPostExecute(Void aVoid) {
								Toast.makeText(getContext(), "Match comments updated!", Toast.LENGTH_SHORT).show();
							}
						}.execute();*/
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(getContext()).inflate(R.layout.result_details_dialog_fragment, null);

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
			}
		}.execute();

		return contentView;
	}

	/*private Match getMatch() {
		match.setComments(commentsEditText.getText().toString());

		return match;
	}*/
}

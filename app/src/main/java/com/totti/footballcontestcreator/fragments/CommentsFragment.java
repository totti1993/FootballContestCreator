package com.totti.footballcontestcreator.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class CommentsFragment extends Fragment {

	private TeamViewModel teamViewModel;
	private TournamentViewModel tournamentViewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.comments_fragment, container, false);

		final String id = this.getArguments().getString("id");
		final String type = this.getArguments().getString("type");

		final TextView comments = rootView.findViewById(R.id.comments_textView);

		teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
		tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);

		// Keep comments about the team or tournament up to date
		if(type != null) {
			if(type.equals("team")) {
				teamViewModel.getCommentsById(id).observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(String commentsById) {
						comments.setText(commentsById);
					}
				});
			}
			else if(type.equals("tournament")) {
				tournamentViewModel.getCommentsById(id).observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(String commentsById) {
						comments.setText(commentsById);
					}
				});
			}

			// Button to add comments about the team or tournament
			FloatingActionButton fab = rootView.findViewById(R.id.add_comments_fab);
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					new AsyncTask<Void, Void, String>() {
						@Override
						protected String doInBackground(Void... voids) {
							if(type.equals("team")) {
								return teamViewModel.getCreatorByIdAsync(id);
							}
							else if(type.equals("tournament")) {
								return tournamentViewModel.getCreatorByIdAsync(id);
							}
							else {
								// This condition is never reached
								return null;
							}
						}

						@Override
						protected void onPostExecute(String creator) {
							if((FirebaseAuth.getInstance().getCurrentUser() != null) && FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(creator)) {
								Bundle args = new Bundle();
								args.putString("id", id);
								args.putString("type", type);
								CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();
								commentsDialogFragment.setArguments(args);
								commentsDialogFragment.show(requireActivity().getSupportFragmentManager(), CommentsDialogFragment.TAG);
							}
							else {
								Toast.makeText(requireContext(), "Invalid user! Access denied!", Toast.LENGTH_SHORT).show();
							}
						}
					}.execute();
				}
			});
		}

		return rootView;
	}
}

package com.totti.footballcontestcreator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.totti.footballcontestcreator.R;
import com.totti.footballcontestcreator.viewmodels.TeamViewModel;
import com.totti.footballcontestcreator.viewmodels.TournamentViewModel;

public class CommentsFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.comments_fragment, container, false);

		final String id = this.getArguments().getString("id");
		final String type = this.getArguments().getString("type");

		final TextView comments = rootView.findViewById(R.id.comments_textView);

		if(type != null) {
			if(type.equals("team")) {
				TeamViewModel teamViewModel = new ViewModelProvider(requireActivity()).get(TeamViewModel.class);
				teamViewModel.getCommentsById(id).observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(String commentsById) {
						comments.setText(commentsById);
					}
				});
			}
			else if(type.equals("tournament")) {
				TournamentViewModel tournamentViewModel = new ViewModelProvider(requireActivity()).get(TournamentViewModel.class);
				tournamentViewModel.getCommentsById(id).observe(getViewLifecycleOwner(), new Observer<String>() {
					@Override
					public void onChanged(String commentsById) {
						comments.setText(commentsById);
					}
				});
			}

			FloatingActionButton fab = rootView.findViewById(R.id.add_comments_fab);
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle args = new Bundle();
					args.putString("id", id);
					args.putString("type", type);
					CommentsDialogFragment commentsDialogFragment = new CommentsDialogFragment();
					commentsDialogFragment.setArguments(args);
					commentsDialogFragment.show(requireActivity().getSupportFragmentManager(), CommentsDialogFragment.TAG);
				}
			});
		}

		return rootView;
	}
}

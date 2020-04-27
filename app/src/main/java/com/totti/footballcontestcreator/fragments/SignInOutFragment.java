package com.totti.footballcontestcreator.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import com.totti.footballcontestcreator.R;

public class SignInOutFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sign_in_out_fragment, container, false);

		final FirebaseAuth authentication = FirebaseAuth.getInstance();

		Button logInButton = rootView.findViewById(R.id.log_in);
		Button registerButton = rootView.findViewById(R.id.register);
		Button logOutButton = rootView.findViewById(R.id.log_out);

		// Listener: Click on Log In button
		logInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(authentication.getCurrentUser() == null) {
					new LoginDialogFragment().show(requireActivity().getSupportFragmentManager(), LoginDialogFragment.TAG);
				}
				else {
					Toast.makeText(requireContext(), "Sign out first, please!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Listener: Click on Register button
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(authentication.getCurrentUser() == null) {
					new RegisterDialogFragment().show(requireActivity().getSupportFragmentManager(), RegisterDialogFragment.TAG);
				}
				else {
					Toast.makeText(requireContext(), "Sign out first, please!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Listener: Click on Log Out button
		logOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(authentication.getCurrentUser() != null) {
					new AlertDialog.Builder(requireContext()).setMessage("Do you really want to sign out?")
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									authentication.signOut();
									Toast.makeText(requireContext(), "Signed out successfully!", Toast.LENGTH_SHORT).show();
								}
							})
							.setNegativeButton("No", null)
							.show();
				}
				else {
					Toast.makeText(requireContext(), "No user is signed in!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return rootView;
	}
}

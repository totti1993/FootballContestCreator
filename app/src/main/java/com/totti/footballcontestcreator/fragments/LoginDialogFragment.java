package com.totti.footballcontestcreator.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import com.totti.footballcontestcreator.R;

public class LoginDialogFragment extends DialogFragment {

	public static final String TAG = "LoginDialogFragment";

	private EditText loginEmailEditText;
	private EditText loginPasswordEditText;

	private FirebaseAuth authentication;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		authentication = FirebaseAuth.getInstance();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new AlertDialog.Builder(requireContext())
				.setTitle(R.string.log_in)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						final Context context = requireContext();
						authentication.signInWithEmailAndPassword(loginEmailEditText.getText().toString(), loginPasswordEditText.getText().toString())
								.addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
									@Override
									public void onComplete(@NonNull Task<AuthResult> task) {
										if(task.isSuccessful()) {
											Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
										}
										else {
											try{
												throw task.getException();
											}
											catch(FirebaseAuthInvalidUserException exception) {
												Toast.makeText(context, "Login failed: wrong email address!", Toast.LENGTH_SHORT).show();
											}
											catch(FirebaseAuthInvalidCredentialsException exception) {
												Toast.makeText(context, "Login failed: wrong password!", Toast.LENGTH_SHORT).show();
											}
											catch(Exception exception) {
												Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show();
											}
										}
									}
								});
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.login_dialog_fragment, null);

		loginEmailEditText = contentView.findViewById(R.id.login_email_editText);

		loginPasswordEditText = contentView.findViewById(R.id.login_password_editText);

		return contentView;
	}
}

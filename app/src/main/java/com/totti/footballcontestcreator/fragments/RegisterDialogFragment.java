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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.totti.footballcontestcreator.R;

public class RegisterDialogFragment extends DialogFragment {

	public static final String TAG = "RegisterDialogFragment";

	private EditText registerEmailEditText;
	private EditText registerPasswordEditText;
	private EditText registerPasswordAgainEditText;

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
				.setTitle(R.string.register)
				.setView(getContentView())
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Check if passwords match
						if(registerPasswordEditText.getText().toString().equals(registerPasswordAgainEditText.getText().toString())) {
							final Context context = requireContext();
							authentication.createUserWithEmailAndPassword(registerEmailEditText.getText().toString(), registerPasswordEditText.getText().toString())
									.addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
										@Override
										public void onComplete(@NonNull Task<AuthResult> task) {
											if(task.isSuccessful()) {
												Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show();
											}
											else {
												try{
													throw task.getException();
												}
												catch(FirebaseAuthWeakPasswordException exception) {
													Toast.makeText(context, "Registration failed: weak password!", Toast.LENGTH_SHORT).show();
												}
												catch(FirebaseAuthInvalidCredentialsException exception) {
													Toast.makeText(context, "Registration failed: wrong email address!", Toast.LENGTH_SHORT).show();
												}
												catch(FirebaseAuthUserCollisionException exception) {
													Toast.makeText(context, "Registration failed: user already exists!", Toast.LENGTH_SHORT).show();
												}
												catch(Exception exception) {
													Toast.makeText(context, "Registration failed!", Toast.LENGTH_SHORT).show();
												}
											}
										}
									});
						}
						else {
							Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create();
	}

	private View getContentView() {
		View contentView = LayoutInflater.from(requireContext()).inflate(R.layout.register_dialog_fragment, null);

		registerEmailEditText = contentView.findViewById(R.id.register_email_editText);

		registerPasswordEditText = contentView.findViewById(R.id.register_password_editText);

		registerPasswordAgainEditText = contentView.findViewById(R.id.register_password_again_editText);

		return contentView;
	}
}

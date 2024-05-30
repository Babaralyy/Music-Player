package com.example.azikeamusic.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.azikeamusic.databinding.FragmentSignUpBinding;
import com.example.azikeamusic.datamodels.User;
import com.example.azikeamusic.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireStoreDb;
    private FragmentSignUpBinding mBinding;
    private Dialog dialog;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater);

        inIt();

        return mBinding.getRoot();
    }

    private void inIt() {
        //Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();
        fireStoreDb = FirebaseFirestore.getInstance();
        dialog = Constant.getDialog(requireContext());
        mBinding.btnSignUp.setOnClickListener(this::checkCredentials);
    }

    private void checkCredentials(View view) {

        String userName = mBinding.etUsername.getText().toString().trim();
        String userEmail = mBinding.etEmail.getText().toString().trim();
        String userPassword = mBinding.etPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            mBinding.etUsername.setError("Username is required");
            mBinding.etUsername.requestFocus();
            return;
        }
        if (userEmail.isEmpty()) {
            mBinding.etEmail.setError("Email is required");
            mBinding.etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            mBinding.etEmail.setError("Please provide valid email!");
            mBinding.etEmail.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            mBinding.etPassword.setError("Password can't be empty!");
            mBinding.etPassword.requestFocus();
        } else {
            signUpUser(view, userName, userEmail, userPassword);
        }
    }

    private void signUpUser(View view, String userName, String userEmail, String userPassword) {
        dialog.show();
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveUserDataToFireStore(view, userName, userEmail, userPassword);
            } else {
                dialog.dismiss();
                Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserDataToFireStore(View view, String userName, String userEmail, String userPassword) {

        User user = new User(userName, userEmail, userPassword);
        fireStoreDb.collection("Users").document(mAuth.getCurrentUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                dialog.dismiss();
                NavDirections action =
                        SignUpFragmentDirections.
                                actionSignUpFragmentToAudioFragment();
                Navigation.findNavController(view).navigate(action);

            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
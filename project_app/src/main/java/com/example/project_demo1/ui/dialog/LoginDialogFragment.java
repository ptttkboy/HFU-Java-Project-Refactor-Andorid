package com.example.project_demo1.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.project_demo1.R;

public class LoginDialogFragment extends DialogFragment {
    
    private static final String TAG = "LoginDialogFragment";
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + getTheme());
        View view = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("請先登入")
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        navController.navigate(R.id.action_loginDialogFragment_to_nav_loginFragment);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.popBackStack(R.id.nav_myProfileFragment, true);
                        navController.navigateUp();
                    }
                });
        
        return builder.create();
    }
}

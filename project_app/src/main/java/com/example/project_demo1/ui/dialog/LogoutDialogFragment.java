package com.example.project_demo1.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.MemberService;
import com.example.project_demo1.utils.SharedPreferencesUtils;

import java.util.concurrent.Executor;

/**
 * 確認是否登出頁面
 *
 * 當使用者按下確定或取消時，將把該結果以bundle的形式送回前一頁面。
 */
public class LogoutDialogFragment extends DialogFragment {
    
    private static final String TAG = "LoginDialogFragment";
    private NavController navController;
    private Executor executor;
    private MemberService memberService;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = MyApplication.getExecutorService();
        memberService = new MemberServiceImpl();
        sharedPreferencesUtils = SharedPreferencesUtils.getInstance(getActivity().getPreferences(Context.MODE_PRIVATE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        Bundle confirmLogoutResult = new Bundle();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("是否確定登出？")
                .setPositiveButton("登出", (dialog, which) -> {

                    confirmLogoutResult.putBoolean("logout", true);
                    navController.navigate(R.id.action_nav_logoutDialogFragment_to_nav_myProfileFragment, confirmLogoutResult);
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    confirmLogoutResult.putBoolean("logout", false);
                    navController.navigate(R.id.action_nav_logoutDialogFragment_to_nav_myProfileFragment, confirmLogoutResult);
                });

        return builder.create();
    }
}

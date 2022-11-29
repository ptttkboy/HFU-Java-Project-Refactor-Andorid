package com.example.project_demo1.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project_demo1.MyApplication;
import com.example.project_demo1.R;
import com.example.project_demo1.model.Member;
import com.example.project_demo1.network.Result;
import com.example.project_demo1.service.Impl.MemberServiceImpl;
import com.example.project_demo1.service.MemberService;

import java.util.concurrent.Executor;

public class RegistrationInfoFragment extends Fragment {

    private static final String TAG = "RegistrationInfoFragment";

    private Executor executor;
    private MemberService memberService;

    // views
    private NavController navController;

    public RegistrationInfoFragment() {
        this.memberService = new MemberServiceImpl();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = MyApplication.getExecutorService();
        memberService = new MemberServiceImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // navController
        navController = Navigation.findNavController(view);

        // set registration button
        Button reg_submit_btn = view.findViewById(R.id.reg_submit_btn);
        reg_submit_btn.setOnClickListener(v -> {

            // 註冊資料檢查
            String name = ((EditText)view.findViewById(R.id.et_name)).getText().toString();
            String email = ((EditText)view.findViewById(R.id.et_email)).getText().toString();
            String password = ((EditText)view.findViewById(R.id.et_password)).getText().toString();
            String phone = ((EditText)view.findViewById(R.id.et_phone)).getText().toString();

            boolean reject = false;

            if (name.isEmpty()) {
                TextView regInput_name_msg = view.findViewById(R.id.regInput_name_msg);
                regInput_name_msg.setVisibility(View.VISIBLE);
                regInput_name_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
                reject = true;
            }

            if (email.isEmpty() || !email.contains("@")) {
                TextView regInput_email_msg = view.findViewById(R.id.regInput_email_msg);
                regInput_email_msg.setVisibility(View.VISIBLE);
                regInput_email_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
                reject = true;
            }

            if (password.isEmpty() || password.length() < 6) {
                TextView regInput_password_msg = view.findViewById(R.id.regInput_password_msg);
                regInput_password_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
                reject = true;
            }

            if (phone.isEmpty()) {
                TextView regInput_phone_msg = view.findViewById(R.id.regInput_phone_msg);
                regInput_phone_msg.setVisibility(View.VISIBLE);
                regInput_phone_msg.setTextColor(getResources().getColor(R.color.text_warning_red));
                reject = true;
            }

            if (reject) return;

            Member newMember = new Member(name, email, password, phone, null);

            /* ------ 註冊 ------ */
            executor.execute(() -> {
                Result registration = memberService.registration(newMember);

                if(registration.isSuccess) {

                    getActivity().runOnUiThread(() -> {
                        // 註冊完成
                        Log.d(TAG, "onViewCreated: Registration Success. Member name: " + name);
                        navController.navigate(R.id.action_registrationInfoFragment_to_registrationDoneFragment);
                    });

                } else {

                    // 註冊失敗，顯示錯誤訊息
                    getActivity().runOnUiThread(() -> {
                        Result.Error error= (Result.Error)registration;

                        TextView regInput_email_msg = view.findViewById(R.id.regInput_email_msg);
                        regInput_email_msg.setText(error.getMessage());
                        regInput_email_msg.setVisibility(View.VISIBLE);
                        regInput_email_msg.setTextColor(getResources().getColor(R.color.text_warning_red));

                        Log.e(TAG, "onViewCreated: Registration Failed. Member name: "
                                + name
                                +" Error message: "
                                + error);
                    });
                }
            });
        });
    }

    private void inputValidation() {
    }
}
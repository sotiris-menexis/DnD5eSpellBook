package com.example.skyknight.dnd5epocketassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private static View view;



    private static EditText username_S;
    private static EditText password_S;
    private static EditText password_R_S;
    private static EditText email_S;
    private static Button signup_btn;
    private static TextView haveAcc_Txt;
    private static ProgressBar progressBar;

    // Constructor
    public SignUpFragment() {

    }



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//WHAT IS ATTACHtoROOT?
        view = inflater.inflate(R.layout.signup_layout, container, false);

        initializeView();
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_btn.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.log_in_sign_up_anim));
                new insertUser().execute();
            }
        });

        haveAcc_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container,new LogInFragment(),UtilitiesClass.LogInFragName).commit();
            }
        });

        return view;
    }

    private void initializeView() {
        email_S = (EditText) view.findViewById(R.id.signup_EmailId);
        username_S = (EditText) view.findViewById(R.id.username_SU);
        password_S = (EditText) view.findViewById(R.id.password_S);
        password_R_S = (EditText) view.findViewById(R.id.repeatPassword);
        haveAcc_Txt = (TextView) view.findViewById(R.id.already_user);
        signup_btn = (Button) view.findViewById(R.id.signUpBtn);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarSignUp);

        progressBar.setVisibility(View.INVISIBLE);
    }

    private class insertUser extends AsyncTask<Void,Void,Void> {
        private boolean emailFlag=false;
        private boolean usernameFlag = false;
        private boolean toastFlag = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideLayout();

            Pattern p = Pattern.compile(UtilitiesClass.emailValidation);
            Matcher m = p.matcher(email_S.getText().toString());
            if(username_S.getText().toString().trim().equals("")||username_S.length()==0) {
                Toast.makeText(getContext(), "Username can't be empty", Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }else if(email_S.getText().toString().trim().equals("")||email_S.length()==0) {
                Toast.makeText(getContext(), "E-mail can't be empty", Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }else if(!m.find()){
                Toast.makeText(getContext(),"Your e-mail is invalid",Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }else if(password_S.getText().toString().trim().equals("")||password_S.length()==0){
                Toast.makeText(getContext(),"Password can't be empty",Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }else if(password_R_S.getText().toString().trim().equals("")||password_R_S.length()==0){
                Toast.makeText(getContext(),"Please repeat your password",Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }else if(!password_R_S.getText().toString().equals(password_S.getText().toString())){
                Toast.makeText(getContext(),"Please make sure the passwords match",Toast.LENGTH_SHORT).show();
                toastFlag = true;
                showLayout();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(!toastFlag) {
                ArrayList<User> allusers;
                User temp = new User();
                temp.setUsername(username_S.getText().toString().trim());
                temp.setPassword(password_S.getText().toString().trim());
                temp.setEmail(email_S.getText().toString().trim());

                if (!DatabaseHelper.getInstance(getContext()).TableIsEmpty()) {
                    allusers = DatabaseHelper.getInstance(getContext()).getUserLogInInfo();
                    for (int i = 0; i < allusers.size(); i++) {
                        if (allusers.get(i).getEmail().equals(temp.getEmail())) {
                            emailFlag = true;
                        } else if (allusers.get(i).getUsername().equals(temp.getUsername())) {
                            usernameFlag = true;
                        }
                    }
                }

                if (emailFlag == false && usernameFlag == false) {
                    DatabaseHelper.getInstance(getContext()).insertData(temp);
StartScreenActivity.currentUser.setUsername(temp.getUsername());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!toastFlag) {
                if (emailFlag) {
                    Toast.makeText(getContext(), "There is already a user with this email", Toast.LENGTH_SHORT).show();
                    showLayout();
                    email_S.setText("");
                } else if (usernameFlag) {
                    Toast.makeText(getContext(), "There is already a user with this username", Toast.LENGTH_SHORT).show();
                    showLayout();
                    username_S.setText("");
                } else if (emailFlag == false && usernameFlag == false) {
                    Toast.makeText(getContext(), "Successful sign up", Toast.LENGTH_SHORT).show();
//getFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment(), UtilitiesClass.LogInFragName).commit();
startActivity(new Intent(getActivity(),SpellsActivity.class));
//StartScreenActivity.currentUser.setUsername(username_S.getText().toString());
                }
            }else {
                showLayout();
            }
        }
    }

    private void showLayout(){
        email_S.setVisibility(View.VISIBLE);
        password_R_S.setVisibility(View.VISIBLE);
        password_S.setVisibility(View.VISIBLE);
        username_S.setVisibility(View.VISIBLE);
        haveAcc_Txt.setVisibility(View.VISIBLE);
        signup_btn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hideLayout(){
        email_S.setVisibility(View.INVISIBLE);
        password_R_S.setVisibility(View.INVISIBLE);
        password_S.setVisibility(View.INVISIBLE);
        username_S.setVisibility(View.INVISIBLE);
        haveAcc_Txt.setVisibility(View.INVISIBLE);
        signup_btn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

}

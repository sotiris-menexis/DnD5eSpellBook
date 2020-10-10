package com.example.skyknight.dnd5epocketassistant;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogInFragment extends Fragment {
    private static View view;

    private boolean foundUser = false;
    private boolean pwdCorrect = false;

    private static ArrayList<User> users = new ArrayList<>();

    private static Button logInBtn;
    private static EditText email;
    private static EditText password;
    private static CheckBox show_pwd;
    private static TextView sign_up_txt;
    private static ProgressBar progressBar;

    public LogInFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_layout, container,false);
        initializeView();
        setUpListeners();

        return view;

    }

    private void initializeView(){
        email = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        show_pwd = (CheckBox) view.findViewById(R.id.show_hide_password);
        sign_up_txt = (TextView) view.findViewById(R.id.createAccount);
        logInBtn = (Button) view.findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarLogIn);

        progressBar.setVisibility(View.INVISIBLE);

    }

    private void setUpListeners(){
// WHAT ARE HideReturnsTransformationMethod, PasswordTransformationMethod ?????????????????????????????????????????
        show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(show_pwd.isChecked()){
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                }else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInBtn.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.log_in_sign_up_anim));
                //if (AppStatus.getInstance(getContext()).isOnline())
                    new logInTask().execute();
               // else
                    //Toast.makeText(getContext(), "Please ensure that your device has Internet Access.", Toast.LENGTH_LONG).show();
            }
        });

        sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container,new SignUpFragment()
                        ,UtilitiesClass.SignUpFragName).commit();
            }
        });
    }

    private class logInTask extends AsyncTask<Void,Void,Void> {
        Pattern p = Pattern.compile(UtilitiesClass.emailValidation);
        Matcher m = p.matcher(email.getText().toString().trim());
        boolean toastFlag = false;
        boolean emptyDBflag = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideLayout();
            Log.d("Tag","Before pattern");
            Pattern p = Pattern.compile(UtilitiesClass.emailValidation);
            Log.d("Tag","Before email");
            if(email.getText().toString().trim().equals("") || email.getText().toString().trim().length()==0){
                Log.d("Tag","After email_1");
                Toast.makeText(getContext(),"E-mail can't be empty",Toast.LENGTH_SHORT).show();
                toastFlag = true;
                Log.d("Tag","After email");
            }else if(password.getText().toString().trim().equals("") || password.getText().toString().trim().length()==0){
                Toast.makeText(getContext(),"Password can't be empty",Toast.LENGTH_SHORT).show();
                toastFlag = true;
            }else {
                if(!m.find()){
                    Toast.makeText(getContext(),"Your e-mail is invalid",Toast.LENGTH_SHORT).show();
                    email.setText("");
                    toastFlag = true;
                }
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(toastFlag == false) {
                if (!DatabaseHelper.getInstance(getContext()).TableIsEmpty()) {
                    users = DatabaseHelper.getInstance(getContext()).getUserLogInInfo();
                    checkForRegUser();
                } else {
                    emptyDBflag = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(toastFlag == false) {
                if (emptyDBflag == true) {
                    Toast.makeText(getContext(),"No user found please create an account",Toast.LENGTH_SHORT).show();
                    email.setText("");
                    password.setText("");
                    showLayout();
                } else {
                    if (foundUser) {
                        if (pwdCorrect) {
                            Toast.makeText(getContext(), "Succesful log in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), SpellsActivity.class));
                        } else {
                            Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                            password.setText("");
                            showLayout();
                        }
                    } else {
                        Toast.makeText(getContext(), "No user found please create an account", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                        showLayout();
                    }
                }
            }else {
                showLayout();
            }
        }

    }

    private void checkForRegUser(){
        String email_temp = email.getText().toString();
        String password_temp = password.getText().toString();

        for(int i=0; i<users.size(); i++){
            if(users.get(i).getEmail().equals(email_temp)){
                foundUser = true;
                if(users.get(i).getPassword().equals(password_temp)){
                    pwdCorrect = true;
                    StartScreenActivity.currentUser.setUsername(users.get(i).getUsername());
                }else{
                    break;
                }
            }
        }

    }

    private void showLayout(){
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        show_pwd.setVisibility(View.VISIBLE);
        sign_up_txt.setVisibility(View.VISIBLE);
        logInBtn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void hideLayout(){
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        show_pwd.setVisibility(View.INVISIBLE);
        sign_up_txt.setVisibility(View.INVISIBLE);
        logInBtn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

}

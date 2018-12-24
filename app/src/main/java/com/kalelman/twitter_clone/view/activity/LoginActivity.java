package com.kalelman.twitter_clone.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.commons.utils.Tools;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kalelman.twitter_clone.commons.utils.Constants.LOGIN_TAG;
import static com.kalelman.twitter_clone.commons.utils.Constants.SUCCES_MESSAGE;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.til_user)
    TextInputLayout tilUserName;
    @BindView(R.id.tiet_username)
    TextInputEditText tietUsername;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.tiet_password)
    TextInputEditText tietPassword;
    @BindView(R.id.txv_forget_password)
    TextView forgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ButterKnife.bind(this);
        setTextTranslate();

    }

    private void setTextTranslate() {
        /*tietUsername.setText("user");
        tietPassword.setText("password");*/
    }

    @OnClick(R.id.txv_forget_password)
    public void goToRecoverPassword() {
        startActivity(new Intent(this, RecoverPasswordActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void listenerLogIn() {
        Tools.hideKeyboard(this);
        LogIn();
    }

    private void LogIn() {
        if (TextUtils.isEmpty(tietUsername.getText()) && TextUtils.isEmpty(tietPassword.getText())) {
            tilUserName.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(getResources().getText(R.string.text_required_field));
        } else if (TextUtils.isEmpty(tietUsername.getText())) {
            tilUserName.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(null);
        } else if (TextUtils.isEmpty(tietPassword.getText())) {
            tilPassword.setError(getResources().getText(R.string.text_required_field));
            tilUserName.setError(null);
        } else {
            tilUserName.setError(null);
            tilPassword.setError(null);
            signIn(tietUsername.getText().toString(), tietPassword.getText().toString());
            //btnLogin.setEnabled(false);
        }
    }

    private void signIn(String user, String password) {
        ParseUser.logInInBackground(user, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    Log.i(LOGIN_TAG, SUCCES_MESSAGE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_sign_up)
    public void listenerSignUp() {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    private void executeServices(int typeServices) {
        //showProgress(); pendiente de implementar
        startActivity(new Intent(this, MainActivity.class));
    }

    public void showProgress() {
        try {
            progressDialog = ProgressDialog.show(this, "", getString(R.string.text_progress_dialog_loading), true, false);
        } catch (Exception exception) {
            Log.e(getClass().getName().toString(), exception.getMessage());
        }
    }
}

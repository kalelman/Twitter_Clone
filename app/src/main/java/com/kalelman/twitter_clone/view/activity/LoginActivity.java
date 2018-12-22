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

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.commons.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.til_user)
    TextInputLayout tilUserName;
    @BindView(R.id.edt_username)
    TextInputEditText edtUsername;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.txv_forget_password)
    TextView forgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
        if (TextUtils.isEmpty(edtUsername.getText()) && TextUtils.isEmpty(edtPassword.getText())) {
            tilUserName.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(getResources().getText(R.string.text_required_field));
        } else if (TextUtils.isEmpty(edtUsername.getText())) {
            tilUserName.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(null);
        } else if (TextUtils.isEmpty(edtPassword.getText())) {
            tilPassword.setError(getResources().getText(R.string.text_required_field));
            tilUserName.setError(null);
        } else {
            tilUserName.setError(null);
            tilPassword.setError(null);
            btnLogin.setEnabled(false);
            executeServices(1);
        }
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

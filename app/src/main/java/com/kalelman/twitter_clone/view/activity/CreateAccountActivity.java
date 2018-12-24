package com.kalelman.twitter_clone.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.commons.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kalelman.twitter_clone.commons.utils.Constants.SIGN_UP;
import static com.kalelman.twitter_clone.commons.utils.Constants.SUCCES_MESSAGE;

public class CreateAccountActivity extends ToolBar {

    @BindView(R.id.til_username)
    TextInputLayout tilUsername;
    @BindView(R.id.tiet_user_name)
    TextInputEditText edtUserName;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.tiet_email)
    TextInputEditText edtEamil;
    @BindView(R.id.til_password_ca)
    TextInputLayout tilPassword;
    @BindView(R.id.tiet_password_ca)
    TextInputEditText edtPassword;
    @BindView(R.id.btn_sign_up_account)
    Button btnSignUpAc;
    @BindView(R.id.txv_toolbar)
    TextView txvToolBar;
    /*@BindView(R.id.imv_twitter_alert)
    ImageView imvTwitterAlert;
    @BindView(R.id.txv_title_alert_succes)
    TextView txvTitleAlertSucces;*/

    private Button btn_alert_succes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTextTranslate();
        setTitleActionBar("");
    }
    @OnClick(R.id.btn_sign_up_account)
    public void signUp() {
        if (TextUtils.isEmpty(edtUserName.getText()) && TextUtils.isEmpty(edtEamil.getText()) && TextUtils.isEmpty(edtPassword.getText().toString())) {
            tilUsername.setError(getResources().getText(R.string.text_required_field));
            tilEmail.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(getResources().getText(R.string.text_required_field));
        } else if (TextUtils.isEmpty(edtUserName.getText().toString())) {
            tilUsername.setError(getResources().getText(R.string.text_required_field));
            tilEmail.setError(null);
            tilPassword.setError(null);
        } else if (TextUtils.isEmpty(edtEamil.getText().toString())) {
            tilUsername.setError(null);
            tilEmail.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(null);
        } else if (!Tools.isValidEmail(edtEamil.getText().toString())) {
            tilUsername.setError(null);
            tilEmail.setError(getResources().getText(R.string.text_invalid_email));
            tilPassword.setError(null);
        } else if (TextUtils.isEmpty(edtPassword.getText())) {
            tilUsername.setError(getResources().getText(R.string.text_required_field));
            tilEmail.setError(getResources().getText(R.string.text_required_field));
            tilPassword.setError(null);
        } else {
            tilUsername.setError(null);
            tilEmail.setError(null);
            tilPassword.setError(null);
            createAccount();
        }
    }

    private void createAccount() {
        final ParseUser newUser = new ParseUser();
        newUser.setUsername(edtUserName.getText().toString());
        newUser.setPassword(edtPassword.getText().toString());
        newUser.setEmail(edtEamil.getText().toString());

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(SIGN_UP,SUCCES_MESSAGE);
                    Tools.hideKeyboard(CreateAccountActivity.this);
                    redirectLogin();
                }
                else {
                    Toast.makeText(CreateAccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_custom_alert_builder_signup, null);
        btn_alert_succes = dialogView.findViewById(R.id.btn_alert_succes);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        btn_alert_succes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        }); dialog.show();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_create_account;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setTextTranslate() {
        txvToolBar.setText(R.string.text_sign_up);
    }
}

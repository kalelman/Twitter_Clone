package com.kalelman.twitter_clone.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kalelman.twitter_clone.R;
import com.kalelman.twitter_clone.commons.utils.Tools;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kalelman.twitter_clone.commons.utils.Constants.LOGIN_TAG;
import static com.kalelman.twitter_clone.commons.utils.Constants.RECOVER_PASSWORD;
import static com.kalelman.twitter_clone.commons.utils.Constants.RECOVER_PASSWORD_SUCCES;
import static com.kalelman.twitter_clone.commons.utils.Constants.SUCCES_MESSAGE;

public class RecoverPasswordActivity extends ToolBar {

    @BindView(R.id.txv_toolbar)
    TextView txvToolBar;
    @BindView(R.id.edt_enter_email)
    EditText edtEmail;
    @BindView(R.id.btn_resetPassword)
    Button btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleActionBar("");
        setTextTranslate();
    }

    @OnClick(R.id.btn_resetPassword)
    public void listenerResetPassword() {
        Tools.hideKeyboard(this);
        if (TextUtils.isEmpty(edtEmail.getText()))
            edtEmail.setError(getResources().getText(R.string.text_required_field));
        else if (!Tools.isValidEmail(edtEmail.getText().toString()))
            edtEmail.setError(getResources().getText(R.string.text_invalid_email));
        else
            resetPassword(edtEmail.getText().toString());
    }

    private void resetPassword(String email) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(RECOVER_PASSWORD, RECOVER_PASSWORD_SUCCES);
                } else {
                    Toast.makeText(RecoverPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTextTranslate() {
        txvToolBar.setText(R.string.text_change_password);
        txvToolBar.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_recover_password;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

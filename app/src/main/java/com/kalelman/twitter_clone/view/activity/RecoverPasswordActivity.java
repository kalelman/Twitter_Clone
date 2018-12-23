package com.kalelman.twitter_clone.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.kalelman.twitter_clone.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecoverPasswordActivity extends ToolBar {

    @BindView(R.id.txv_toolbar)
    TextView txvToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitleActionBar("");
        setTextTranslate();
    }

    private void setTextTranslate() {
        txvToolBar.setText(R.string.text_change_password);
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

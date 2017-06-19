package com.cloudwalkdigital.verifynegotiator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.cloudwalkdigital.verifynegotiator.events.EventSelectionActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @Inject SharedPreferences sharedPreferences;

    @BindView(R.id.btn_login) Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind butterknife and dagger
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_login)
    public void doLogin(Button button) {
        button.setText(R.string.btn_logging_in);
        button.setEnabled(false);

        Intent intent = new Intent(this, EventSelectionActivity.class);
        startActivity(intent);
    }
}

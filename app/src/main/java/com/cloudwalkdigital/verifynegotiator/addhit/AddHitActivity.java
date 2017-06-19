package com.cloudwalkdigital.verifynegotiator.addhit;

import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cloudwalkdigital.verifynegotiator.R;
import com.cloudwalkdigital.verifynegotiator.events.EventSelectionActivity;
import com.cloudwalkdigital.verifynegotiator.utils.SessionManager;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class AddHitActivity extends AppCompatActivity {
    @Inject SessionManager sessionManager;
    private DrawerLayout mDrawerLayout;

    private final String TAG = "ADDHITACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hit);

        // Bind
        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Creating a new record");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

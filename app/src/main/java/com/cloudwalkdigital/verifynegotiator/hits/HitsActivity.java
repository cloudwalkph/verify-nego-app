package com.cloudwalkdigital.verifynegotiator.hits;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cloudwalkdigital.verifynegotiator.R;
import com.cloudwalkdigital.verifynegotiator.data.models.events.Event;

import java.util.List;

import butterknife.BindView;

public class HitsActivity extends AppCompatActivity {

    @BindView(R.id.rvEvents)
    RecyclerView mRecyclerViewEvents;

    private DrawerLayout mDrawerLayout;
    private List<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hits);
    }
}

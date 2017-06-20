package com.cloudwalkdigital.verifynegotiator.events;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwalkdigital.verifynegotiator.App;
import com.cloudwalkdigital.verifynegotiator.R;
import com.cloudwalkdigital.verifynegotiator.addhit.AddHitActivity;
import com.cloudwalkdigital.verifynegotiator.data.models.Auth;
import com.cloudwalkdigital.verifynegotiator.data.models.events.Event;
import com.cloudwalkdigital.verifynegotiator.data.models.events.remote.EventsService;
import com.cloudwalkdigital.verifynegotiator.utils.SessionManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventSelectionActivity extends AppCompatActivity {
    @Inject SharedPreferences sharedPreferences;
    @Inject SessionManager sessionManager;
    @Inject Retrofit retrofit;

    @BindView(R.id.rvEvents) RecyclerView mRecyclerViewEvents;

    private DrawerLayout mDrawerLayout;
    private List<Event> events;

    private final String TAG = "EVENTSELECTIONACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_selection);

        // Bind
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);

        if (! sessionManager.isLoggedIn()) {
            sessionManager.logout(this);
        }

        setupToolbar();
        setupDrawer();

        getAssignedEvents();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Assigned Events");
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void getAssignedEvents() {
        EventsService service = retrofit.create(EventsService.class);
        Auth auth = sessionManager.getAuthInformation();

        Call<List<Event>> call = service.getAssignedEvents("Bearer " + auth.getAccessToken());
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.isSuccessful()) {
                    events = response.body();

                    mRecyclerViewEvents.setHasFixedSize(true);

                    // Create adapter passing in the sample user data
                    EventsAdapter adapter = new EventsAdapter(EventSelectionActivity.this, events);
                    // Attach the adapter to the recyclerview to populate items
                    mRecyclerViewEvents.setAdapter(adapter);
                    // Set layout manager to position the items
                    LinearLayoutManager layoutManager = new LinearLayoutManager(EventSelectionActivity.this);
                    mRecyclerViewEvents.setLayoutManager(layoutManager);

                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewEvents.getContext(),
                            layoutManager.getOrientation());

                    mRecyclerViewEvents.addItemDecoration(dividerItemDecoration);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_events:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.menu_logout:
                                sessionManager.logout(EventSelectionActivity.this);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * Events Adapter
     */
    public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

        private List<Event> mEvents;

        private Context mContext;

        public EventsAdapter(Context mContext, List<Event> mEvents) {
            this.mEvents = mEvents;
            this.mContext = mContext;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_event, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Get the data model based on position
            Event event = mEvents.get(position);

            // Set item views based on your views and data model
            TextView textView = holder.nameTextView;
            textView.setText(event.getName());

            TextView statusText = holder.statusTextView;
            statusText.setText(event.getStatus());
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView nameTextView;
            public TextView statusTextView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                nameTextView = (TextView) itemView.findViewById(R.id.event_name);
                statusTextView = (TextView) itemView.findViewById(R.id.event_status);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event event = events.get(position);

                    // We can access the data within the views
                    Toast.makeText(getContext(), event.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EventSelectionActivity.this, AddHitActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}

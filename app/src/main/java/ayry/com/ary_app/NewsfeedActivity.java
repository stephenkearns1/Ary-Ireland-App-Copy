package ayry.com.ary_app;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NewsfeedRecylerViewAdapter adapter;

    List<EventsModel> eventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        eventsList = new ArrayList<>();

        //recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NewsfeedRecylerViewAdapter(this);

        //loadData();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NewsfeedRecylerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EventsModel e = eventsList.get(position);
                Toast.makeText(getApplicationContext(), e.getTitle() + " was clicked", Toast.LENGTH_LONG).show();
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void loadData(){
        adapter.clear();
        EventsModel event = new EventsModel("Prayer Meeting ", "Somewhere", "41 adresss street yay ", "01 5985 2156", "8:30", 0);
        EventsModel event1 = new EventsModel("", "", "", "", "", 0);
        EventsModel event2 = new EventsModel("", "", "", "", "", 0);
        EventsModel event3 = new EventsModel("", "", "", "", "", 0);
        EventsModel event4 = new EventsModel("", "", "", "", "", 0);

        eventsList.add(event);
        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);
        eventsList.add(event4);

        adapter.addAll(eventsList);
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }
}

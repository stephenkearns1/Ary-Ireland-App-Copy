package ayry.com.ary_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventsSearchResultsActivity extends AppCompatActivity {
    String catagory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search_results);


        //get the data passeed to this class
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            catagory = extras.getString("events_gatagory");
        }
    }
}

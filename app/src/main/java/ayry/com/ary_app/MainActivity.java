package ayry.com.ary_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FragmentPagerAdapter FragmentPagerAdapter;
    DrawerLayout mDrawerLayot;
    DetailsUserStoreLocal userLocaldetails;
    TextView displayUsernameTV;
    TextView displayUseremailTV;
    private ObjectRequestHolder objWrapper;
    Button mTranlateBtn;
    RecyclerView rvShops;
    ShoplistAdapter adapter;
    TextView userEmail;
    TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sets up the applications toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feature Coming Soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //sets up ViewPage adapter

        //  ViewPager myViewPag = (ViewPager) findViewById(R.id.pager);
        // FragmentPagerAdapter = new TabPageAdapter(getSupportFragmentManager());
        //  myViewPag.setAdapter(FragmentPagerAdapter);

        //pass the tab to the page view
        // TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        // tabLayout.setupWithViewPager(myViewPag);


        // Sets up the navigation drawer and adds an on Navigation drawer
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Creates a new instance of the ObjectRequest holder which is used as a wrapper for passing objects between Async task and the activities class
        objWrapper = new ObjectRequestHolder();


        userLocaldetails = new DetailsUserStoreLocal(this);


        //Inflates the nav_header layout as the header and then access the elements in the nav_header to populate with user details in the drawer
        View navHeader =  navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.usernameTV);
        userEmail = (TextView) navHeader.findViewById(R.id.emailTV);





        rvShops = (RecyclerView) findViewById(R.id.rvshoplist);

        //retrieves the data from the database, and populates the recylerView with the data received from the async task
        getData();


        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();

        for (int i = 0; i < listOfSops.size(); i++) {
            //Error check - data is being retrieved correctly
            Shop_items shop = listOfSops.get(i);
            Log.i("Values", shop.getTitle() + ", " + shop.getDesc());
        }


        //Create the adapter to convert array to view
        adapter = new ShoplistAdapter(listOfSops);


        //LinearLayoutManager is used here, this lays out elements in a similar linear fashion
        // mLayoutManger = new LinearLayoutManager(getActivity());
        rvShops.setLayoutManager(new LinearLayoutManager(this));

        rvShops.setAdapter(adapter);

        //Set and click event on translator button to translate the text
        mTranlateBtn = (Button) findViewById(R.id.translationBtn);

        mTranlateBtn.setOnClickListener(this);

        // mTranlateBtn.performClick();


        // displayUsernameTV = (TextView) findViewById(R.id.usernameTV);
        //  displayUseremailTV = (TextView) findViewById(R.id.emailTV);

        //  displayUseremailTV.setText("working in oncreate");
        // onStart();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (authenticate() == true) {
            //display logged in or start main activity
            displayUserDetails();
        } else {
            //starts loginIn activity
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private boolean authenticate() {
        Log.i("getLoggedIn value", "" + userLocaldetails.getLoggedIn());
        return userLocaldetails.getLoggedIn();
    }


    private void displayUserDetails() {
        User user = userLocaldetails.UserLoggedIn();

        //set text views
        // View header = navigationView.

        //displayUsernameTV.setText(user.getUserName());
        //displayUseremailTV.setText(user.getEmail());
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());


        Log.i("user Loggedin", user.getUserName() + user.getEmail());


    }




/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // calls  the map activity, reason for using seperate activity is due to maps do not work with sliding activities
            Intent intent = new Intent(this, MapsActivityPage.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            userLocaldetails.clearUserData();
            User user = userLocaldetails.UserLoggedIn();
            Log.i("User details clear", user.getEmail());
            Intent intent = new Intent(this, UserLogin.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getData() {

        DB_Sever_Request dbRequest = new DB_Sever_Request(this);
        dbRequest.PullShopData(new GetUserCallBack() {
            @Override
            public void finished(ObjectRequestHolder obj) {
                ArrayList<Shop_items> shops = obj.getShopList();
                if (shops == null) {
                    Log.i("array retuend", "ShopsList reutrned is equal to null");
                } else if (shops.size() == 0) {

                } else {
                    for (int i = 0; i < shops.size(); i++) {
                        Shop_items myshop = shops.get(i);
                        Log.i("result", myshop.getTitle() + myshop.getDesc());

                    }
                    //sends a reference to the object return to be accessible
                    dataSource(obj);

                }

            }

        });


    }


    public void dataSource(ObjectRequestHolder requestObj) {
        //sets a reference to the object in this class for for accessibility between the object in the holder ObjectRequestHolder class
        this.objWrapper = requestObj;

    }

    @Override
    public void onClick(View v) {


         adapter.clear();
         //retrieves the data from the database, and populates the recylerView with the data received from the async task
         getData();


        rvShops = (RecyclerView) findViewById(R.id.rvshoplist);


        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();

        for (int i = 0; i < listOfSops.size(); i++) {
            //Error check - data is being retrived correctly
            Shop_items shop = listOfSops.get(i);
            Log.i("Values", shop.getTitle() + ", " + shop.getDesc());
        }


        //Create the adapter to convert array to view
        adapter.addAll(listOfSops);


        //LinearLayoutManager is used here, this lays out elements in a similar linear fashion
        // mLayoutManger = new LinearLayoutManager(getActivity());
        rvShops.setLayoutManager(new LinearLayoutManager(this));

        rvShops.setAdapter(adapter);
        Toast toast = Toast.makeText(this, "Click working", Toast.LENGTH_SHORT);
        toast.show();

        Log.i("Click event", "Working");
    }


}
package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//imports for microsoft api
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    /* Varaibles */

    private  FragmentPagerAdapter FragmentPagerAdapter;
    private DrawerLayout mDrawerLayot;
    private DetailsUserStoreLocal userLocaldetails;
    private TextView displayUsernameTV;
    private TextView displayUseremailTV;
    private ObjectRequestHolder objWrapper;
    private Button mTranlateBtn;
    private RecyclerView rvShops;
    private ShoplistAdapter adapter;
    private TextView userEmail;
    private TextView userName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Shop_items> listOfShops;
    private ArrayList<Shop_items> tempShopList;
    private ArrayList<Shop_items> listOfShopsArabic;
    private ArrayList<Shop_items> tempShopListArabic;
    private boolean arabicTransBtnClick;
    ProgressDialog progressDialog;



    /* used for parsing json */
    //Tags for parsing json
    private static final String tagId = "id";
    private static final String tagName = "shopname";
    private static final String tagDesc = "shopDesc";
    private static final String tagGeo = "geo";


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


        // Sets up the navigation drawer and adds an event listener so when the drawer toggle
        mDrawerLayot = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayot, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayot.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Inflates the nav_header layout as the header and then access the elements in the nav_header to populate with user details in the drawer
        View navHeader = navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.usernameTV);
        userEmail = (TextView) navHeader.findViewById(R.id.emailTV);


        //Creates a new instance of the ObjectRequest holder which is used as a wrapper for passing objects between Async task and the activities class
        objWrapper = new ObjectRequestHolder();


        userLocaldetails = new DetailsUserStoreLocal(this);



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swpie_refresh_layout);
        rvShops = (RecyclerView) findViewById(R.id.rvshoplist);

        adapter = new ShoplistAdapter();

        adapter.notifyDataSetChanged();
        //LinearLayoutManager is used here, this lays out elements in a similar linear fashion
        rvShops.setLayoutManager(new LinearLayoutManager(this));

        rvShops.setAdapter(adapter);

        // Event listen which listens for refresh event when one happens the UpdateList method will be called,
        // This will update the data with the new data retrieved form the server
        // sets the colors used in the refresh animation

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_purple
        );;

        //retrieves the data from the database, and populates the recylerView with the data received from the async task




        //Set and click event on translator button to translate the text
        mTranlateBtn = (Button) findViewById(R.id.translationBtn);

        mTranlateBtn.setOnClickListener(this);

      //  mTranlateBtn.performClick();

        /* declares the arraylist ot hold the reived text and also the translated */
        listOfShops = new ArrayList<>();
        tempShopList = new ArrayList<>();
        listOfShopsArabic = new ArrayList<>();
        tempShopListArabic = new ArrayList<>();


        /* Instantiates the progress dialog so it can be shown on the translate request to */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Translating");
        progressDialog.setMessage("Please wait....");


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

        if (id == R.id.nav_map) {
            // calls  the map activity, reason for using seperate activity is due to maps do not work with sliding activities
            Intent intent = new Intent(this, MapsActivityPage.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            //this is were a intent will be started for the newsfeed activity
            Intent intent = new Intent(this, NewsfeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            // userLocaldetails.clearUserData();
            // User user = userLocaldetails.UserLoggedIn();
            // Log.i("User details clear", user.getEmail());
            Intent intent = new Intent(this, AccountSettingsActivity.class);
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
                    //sends a reference to the object and returns to be accessible
                    dataSource(obj);

                }

            }

        });


    }


    public void dataSource(ObjectRequestHolder requestObj) {
        //sets a reference to the object in this class for for accessibility between the object in the holder ObjectRequestHolder class
        this.objWrapper = requestObj;

    }

    /*
      * could take the data retrieved from the retriveShopList method and async  and then update the recylerView from there or from the update class by keeping track if a button has been pressed or not and conditions
     */

    @Override
    public void onClick(View v) {

       arabicTransBtnClick = true;
       progressDialog.show();
       retrieveShopList();
       convertToArabic();



    }


    public void updateShopListEnglish(){
        if(arabicTransBtnClick == false) {
            if (!(listOfShops == null)) {
                adapter.clear();
                tempShopList.clear();

                for (int i = 0; i < listOfShops.size(); i++) {
                    tempShopList.add(listOfShops.get(i));
                    Shop_items shop = tempShopList.get(i);
                    Log.d("Data tempLst", "data in temp list after update called");
                    Log.d("data", shop.getTitle() + " " + shop.getDesc());
                }

                adapter.addAll(tempShopList);
            }
        }



    }


    /* the microsoft translator api */

    public void convertToArabic() {

        new MyAsyncTask() {
            protected void onPostExecute(Boolean result) {

                progressDialog.hide();

                if (!(listOfShopsArabic == null)) {
                    adapter.clear();
                    tempShopListArabic.clear();

                    for (int i = 0; i < listOfShopsArabic.size(); i++) {
                        tempShopListArabic.add(listOfShopsArabic.get(i));
                        Shop_items shop = tempShopListArabic.get(i);
                        Log.d("Data tempLst", "data in temp list after update called");
                        Log.d("data", shop.getTitle() + " " + shop.getDesc());
                    }

                    adapter.addAll(tempShopListArabic);
                }
            }

        }.execute();



    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... arg0) {
            Translate.setClientId("Ary-Ireland-app");
            Translate.setClientSecret("OKuiH1Dq2uXViCmt7Mh5GC8nBE1IJA5UE7YAde1H6dQ=");
            try {

                for(int i = 0; i < listOfShops.size(); i++){
                    Shop_items shopsAr = listOfShops.get(i);
                    String shopTitleAr = Translate.execute(shopsAr.getTitle(), Language.ENGLISH, Language.ARABIC);
                    String shopDescAr = Translate.execute(shopsAr.getTitle(), Language.ENGLISH, Language.ARABIC);

                    Shop_items newShopArabic = new Shop_items(shopsAr.id, "", shopTitleAr, shopDescAr);
                    listOfShopsArabic.add(newShopArabic);
                }

            } catch (Exception e) {
                Log.d("Error translating",e.toString());
            }

            return true;
        }
    }

    /*
       * The method starts a Volley JsonArrayRequest, which is used for making network request by use of requestQueues
       * Volley also handle the request on separate worker threads and returns the parsed data back to the main thread
       * The data is recivied form the server as a jsonArray and then parsed, then used to create a shop-list item and added to a array list of shops,
       * which is then passed to the update function to update the recylerView with the recivied data
     */
    public void retrieveShopList() {


        mSwipeRefreshLayout.setRefreshing(true);
        String url = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/App-scripts/PullShopData.php";
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //check the response from the server
                        Log.i("New Response", response.toString());

                        if (!(listOfShops == null)) {
                            listOfShops.clear();
                        }

                        if(!(tempShopList == null)){
                            tempShopList.clear();
                        }

                        if(!(listOfShopsArabic == null)){
                            listOfShopsArabic.clear();
                        }

                        if(!(tempShopListArabic == null)){
                            tempShopListArabic.clear();
                        }


                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject shopObj = (JSONObject) response.get(i);

                                int id = Integer.parseInt(shopObj.getString(tagId));
                                String shopname = shopObj.getString(tagName);
                                String shopdesc = shopObj.getString(tagDesc);
                                String shopgeo = shopObj.getString(tagGeo);

                                Log.i("shopid", "id" + id);//
                                Log.i("shopname", shopname);
                                Log.i("shopdesc", shopdesc);
                                Log.i("shopgeo", shopgeo);

                                Shop_items shop = new Shop_items(id, "", shopname, shopdesc);
                                listOfShops.add(shop);

                            }

                              // convertToArabic();
                               updateShopListEnglish();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("JSONError", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d("Pulling ShopData", "Error" + error.getMessage());
            }
        });

        //adding request to the request queue which is kept in a singleton as to make the request queue last for application lifecycle
        AppController.getInstance().addToRequestQueue(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        retrieveShopList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*
      Will have to keep track of which event it was called on i.e. of which click has been performed,
     */
    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "Swipe refresh working", Toast.LENGTH_SHORT).show();
        retrieveShopList();
    }
}//end of class



                                /* Testing volley old code before the implementation of volley */

/*
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
        // getData();


        // rvShops = (RecyclerView) findViewById(R.id.rvshoplist);


        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();

        for (int i = 0; i < listOfSops.size(); i++) {
            //Error check - data is being retrived correctly
            Shop_items shop = listOfSops.get(i);
            Log.i("Values", shop.getTitle() + ", " + shop.getDesc());
        }
        adapter = new ShoplistAdapter();




        adapter.notifyDataSetChanged();
        //LinearLayoutManager is used here, this lays out elements in a similar linear fashion
        // mLayoutManger = new LinearLayoutManager(getActivity());
        rvShops.setLayoutManager(new LinearLayoutManager(this));

        rvShops.setAdapter(adapter);

        //Set and click event on translator button to translate the text
        mTranlateBtn = (Button) findViewById(R.id.translationBtn);

        mTranlateBtn.setOnClickListener(this);

        mTranlateBtn.performClick();


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
    }

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
            //this is were a intent will be started for the newsfeed activity
            Intent intent = new Intent(this, NewsfeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            // userLocaldetails.clearUserData();
            // User user = userLocaldetails.UserLoggedIn();
            // Log.i("User details clear", user.getEmail());
            Intent intent = new Intent(this, AccountSettingsActivity.class);
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
                    //sends a reference to the object and returns to be accessible
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
     /*
        //retrieves the data from the database, and populates the recylerView with the data received from the async task
        getData();



        adapter.clear();

        // rvShops = (RecyclerView) findViewById(R.id.rvshoplist);


        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();

        for (int i = 0; i < listOfSops.size(); i++) {
            //Error check - data is being retrived correctly
            Shop_items shop = listOfSops.get(i);
            Log.i("Values", shop.getTitle() + ", " + shop.getDesc());
        }


        //Create the adapter to convert array to view
        adapter.addAll(listOfSops);



        Intent intent = getIntent();
        finish();
        startActivity(intent);




      *


        //retrieves the data from the database, and populates the recylerView with the data received from the async task
        getData();


        // rvShops = (RecyclerView) findViewById(R.id.rvshoplist);


        //Create the data source and inflate the populated list view
        ArrayList<Shop_items> listOfSops = objWrapper.getShopList();
        int sizeList = listOfSops.size();
        String size = String.valueOf(sizeList);
        Log.i("Data recivied on click", "MainActivity Translate CLick function");
        Log.i("Dat", "The data being recivied from getData on click");
        Log.i("Size of List", size);
        for(int i = 0; i < listOfSops.size(); i++){
            Shop_items shop = listOfSops.get(i);
            Log.i("data", shop.getTitle() + shop.getDesc());
        }



        //  adapter = new ShoplistAdapter(listOfSops);

        adapter.setShopList(listOfSops);
        //adapter.swapData(listOfSops);

        //Create the adapter to convert array to view
        // adapter = new ShoplistAdapter();
        //adapter.clear();
        // adapter.addAll(listOfSops);

        adapter.notifyDataSetChanged();

        //  rvShops.setAdapter(adapter);





    }


} */
package ayry.com.ary_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountSettingsActivity extends AppCompatActivity {

    DetailsUserStoreLocal userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        /*
          Will get the details of the user logged in by instanciating the SharedPerferance file which holds the slogged in users details localy
         */

      //  (TextView) name = (TextView) findViewById(R.id.t)

    }

}

package ayry.com.ary_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Stephen Kearns on 09/03/2016.
 */
public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    Button LoginBtn;
    EditText userNameET, userPasswordET;
    DetailsUserStoreLocal DetailsUserStoreLocal;

    public void onCreate(Bundle  savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        userNameET = (EditText) findViewById(R.id.userName);
        userPasswordET = (EditText) findViewById(R.id.userPassword);
        LoginBtn = (Button) findViewById(R.id.loginBtn);

        LoginBtn.setOnClickListener(this);
        DetailsUserStoreLocal = new DetailsUserStoreLocal(this);
    }

    public void onClick(View v){
         switch(v.getId()){
             case R.id.loginBtn:
                 String userName = userNameET.getText().toString();
                 String password = userPasswordET.getText().toString();

                 User user = new User(userName,password);

                 authenticate(user);

                 DetailsUserStoreLocal.storeUserDetails(user);
                 DetailsUserStoreLocal.setUserLoggedIn(true);

                 //do on click listner here

                 break;
             case R.id.tvRegister:
                 //starts the register activity
                 Intent intent = new Intent(this,UserRegister.class);
                 break;
         }
    }

    public void  authenticate(User user){
        DB_Sever_Request dbRequest = new DB_Sever_Request(this);
        dbRequest.RequestUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void finished(User returnedUser) {
                if (returnedUser == null){
                    showErrorMsg();
                }
            }
        });
    }

    public void showErrorMsg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserLogin.this);
        builder.setMessage("Incorrect Details");
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    public  void LogUserIn(User userReturned){
        DetailsUserStoreLocal.storeUserDetails(userReturned);
        DetailsUserStoreLocal.setUserLoggedIn(true);

        //start the main activity
        Intent intent = new Intent(this,MainActivity.class);
    }
}

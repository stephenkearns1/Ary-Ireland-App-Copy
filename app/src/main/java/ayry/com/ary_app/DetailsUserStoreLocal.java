package ayry.com.ary_app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ste on 09/03/2016.
 */
public class DetailsUserStoreLocal {

    public static final String SP_NAME = "UserAccountDetails";
    SharedPreferences userLocalDB; //used so data can be stored locally

    public DetailsUserStoreLocal(Context context){
        userLocalDB = context.getSharedPreferences(SP_NAME,0);
    }

    //stores users details locally
    public void storeUserDetails(User user){
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putString("userName", user.userName);
        spEditor.putString("name", user.name);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

    //checks if users is logged in i,e true of if not i,e false
    public User UserLoggedIn(){
        String userName = userLocalDB.getString("username", "");
        String password = userLocalDB.getString("password","");

        User newUser = new User(userName,password);
        return newUser;
    }

    //set user who is logged in
    public boolean getLoggedIn(){
        if(userLocalDB.getBoolean("LoggedIn", false)== true){
            return true;
        }else{
            return false;
        }
    }

    public void setUserLoggedIn(boolean isLogged){
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putBoolean("LoggedIn",isLogged);
        spEditor.commit();
    }


    //clear cached user data when loggin out
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }


}


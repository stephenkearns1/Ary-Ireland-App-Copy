package ayry.com.ary_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephen on 10/03/2016.
 */
public class DB_Sever_Request {

    //creates a loading dialog for server request
    ProgressDialog progressDialog;
    public static final int Con_Timer = 1000*15;
    public static final String server_LoginUrl = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/login.php";
    public  static final String server_Registration = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/Register.php";

    // instaniates the progressDialog by getting the context of the activity which uses it,
    // as Progress dialogs need a class which extends the activity class
    public DB_Sever_Request(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing details");
        progressDialog.setMessage("Please wait....");
    }


    public void SaveUserDataInBackground(User user,GetUserCallBack userCallBack){
      progressDialog.show();

      //initiate the async task
        new storeUserDataAsync(user, userCallBack).execute();
    }

    public void RequestUserDataInBackground(User user,GetUserCallBack userCallBack){
     progressDialog.show();
     new retriveUserDataAsync(user,userCallBack).execute();

    }


    public class storeUserDataAsync extends AsyncTask<Void,Void,Void>{
        User user;
        GetUserCallBack callBackUser;
        public storeUserDataAsync(User user, GetUserCallBack callBackUser){
            this.user = user;
            this.callBackUser = callBackUser;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String,String> DBLoginCred = new HashMap<>();
            DBLoginCred.put("name", user.name);
            DBLoginCred.put("userName", user.userName);
            DBLoginCred.put("email", user.email);
            DBLoginCred.put("password", user.password);



            try {
                URL url = new URL(server_Registration);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(Con_Timer);
                connection.setReadTimeout(Con_Timer);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

              /*  Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", user.name)
                        .appendQueryParameter("userName",user.userName)
                        .appendQueryParameter("email",user.email)
                        .appendQueryParameter("password",user.password);
                String query = builder.build().getEncodedQuery(); */
               // Log.d("Query", query);

                OutputStream outStram = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outStram,"UTF-8")
                );

                writer.write(getPostDataString(DBLoginCred));
                writer.flush();
                writer.close();
                outStram.close();

                int ResponseCode = connection.getResponseCode();
                Log.d("ResponseCode", Integer.toString(ResponseCode));


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
          }


        private String getPostDataString(Map<String, String> dbLoginCred)throws UnsupportedEncodingException{
            StringBuilder resultSB = new StringBuilder();
            boolean first = true;

            for(Map.Entry<String,String> entry : dbLoginCred.entrySet()){
                if (first){
                    first = false;
                }else {
                    resultSB.append("&");
                }

                resultSB.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                resultSB.append("=");
                resultSB.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

            Log.i("Value", resultSB.toString());
            return resultSB.toString();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            callBackUser.finished(null);
            super.onPostExecute(aVoid);
        }
    }

 ///mKE   INTO ONE FUNCTION INSTEAD OF TWO

        public class retriveUserDataAsync extends AsyncTask<Void,Void,User>{
            User user;
            GetUserCallBack callBackUser;
            public retriveUserDataAsync(User user, GetUserCallBack callBackUser){
                this.user = user;
                this.callBackUser = callBackUser;
            }



            @Override
            protected User doInBackground(Void... params) {
                Map<String, String> dbLoginCred = new HashMap<String, String>();
                Log.i("username", user.userName);
                dbLoginCred.put("userName", user.userName);
                dbLoginCred.put("password", user.password);

                URL url;
                User userReturned = null;

                try {
                    url = new URL(server_LoginUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(Con_Timer);
                    connection.setReadTimeout(Con_Timer);
                    connection.setConnectTimeout(Con_Timer);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);


                    OutputStream oStream = connection.getOutputStream();
                    BufferedWriter bWriter = new BufferedWriter(
                            new OutputStreamWriter(oStream, "UTF-8")
                    );

                    bWriter.write(getPostDataString(dbLoginCred));
                    bWriter.flush();
                    bWriter.close();

                    oStream.close();

                    int code = connection.getResponseCode();
                    Log.d("code", Integer.toString(code));

                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = responseReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    responseReader.close();


                    String response = stringBuilder.toString();
                    Log.d("response", response);

                    //crashing here  org.json.JSONException: Value  of type java.lang.String cannot be converted to JSONObject
                    JSONObject jsonResponse = new JSONObject(response);
                    //response from sever is returning null so crash at this point
                    Log.d("length", Integer.toString(jsonResponse.length()));
                    if (jsonResponse.length() == 0) {
                        Log.i("User is null", null);
                        userReturned = null;
                    } else {
                       // String username = jsonResponse.getString("userName");
                       // String password = jsonResponse.getString("password");

                        //if it makes it to this stage, which means the login credintails are correct
                        // the username, and password sent are assigned to a new user and the user will be logged in

                        Log.i("usrnme,passwrd returned", user.userName + user.password);
                        userReturned = new User(user.userName, user.password);
                        //Log.d("UserReturned",userReturned.name);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
              return userReturned;
            }





        private String getPostDataString(Map<String, String> dbLoginCred)throws UnsupportedEncodingException{
            StringBuilder resultSB = new StringBuilder();
            boolean first = true;

            for(Map.Entry<String,String> entry : dbLoginCred.entrySet()){
                if (first){
                    first = false;
                }else {
                    resultSB.append("&");
                }

                resultSB.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                resultSB.append("=");
                resultSB.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

            Log.i("Value", resultSB.toString());
            return resultSB.toString();


        }


            @Override
            protected void onPostExecute(User returnedUser) {
                progressDialog.dismiss();
                callBackUser.finished(returnedUser);
                super.onPostExecute(returnedUser);
            }





 }


}


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
 * Created by ste on 10/03/2016.
 */
public class DB_Sever_Request {
    ProgressDialog progressDialog;
    public static final int Con_Timer = 1000*15;
    public static final String server_LoginUrl = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/login.php";
    public  static final String server_Registration = "https://ary-app-sign-in-script-stephenkearns1.c9users.io/Login/Register.php";


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
            DBLoginCred.put("username", user.userName);
            DBLoginCred.put("password", user.password);

            try {
                URL url = new URL(server_Registration);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(Con_Timer);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username",user.userName)
                        .appendQueryParameter("name",user.name)
                        .appendQueryParameter("email",user.email)
                        .appendQueryParameter("password",user.password);
                String query = builder.build().getEncodedQuery();
                Log.d("Query", query);

                OutputStream outStram = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outStram,"UTF-8")
                );

                writer.write(query);
                writer.flush();
                writer.close();




            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
          }

        }


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
                dbLoginCred.put("username", user.userName);
                dbLoginCred.put("password", user.password);

                URL url;
                User userReturned = null;

                try {
                    url = new URL(server_LoginUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(Con_Timer);
                    connection.setConnectTimeout(Con_Timer);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);


                    OutputStream oStram = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(oStram, "UTF-8")
                    );

                    writer.write(getPostDataString(dbLoginCred));
                    writer.flush();
                    writer.close();

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
                    JSONObject jsonResponse = new JSONObject(response);
                    //log here
                    Log.d("length", Integer.toString(jsonResponse.length()));
                    if (jsonResponse.length() == 0) {
                        userReturned = null;
                    } else {
                        String name = jsonResponse.getString("userName");
                        String password = jsonResponse.getString("password");
                        userReturned = new User(name, password);
                        Log.d("UserReturned",userReturned.name);

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
                resultSB.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            }

            return resultSB.toString();

        }


        protected void onPostExcute(User returnedUser){
             progressDialog.dismiss();
             callBackUser.finished(returnedUser);
             Log.d("post execute", returnedUser.getUserName());
             super.onPostExecute(returnedUser);

           }
        }


}


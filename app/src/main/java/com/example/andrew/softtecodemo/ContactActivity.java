package com.example.andrew.softtecodemo;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    public static String LOG_TAG = "my_log";
    private static String url = "http://jsonplaceholder.typicode.com/users/";
    private static String userId, postId;
    private HashMap<String, String> userInfoMap = new HashMap<>();
    private ProgressDialog pDialog;
    TextView textPostId, textName, textNickName, textEmail, textWebsite, textPhone, textCity;
    Button buttonDB;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        postId = intent.getStringExtra("postId");
        getSupportActionBar().setTitle("contact #"+userId);

        textPostId = (TextView)findViewById(R.id.postId);
        textName = (TextView)findViewById(R.id.name);
        textNickName = (TextView)findViewById(R.id.nickName);
        textEmail = (TextView)findViewById(R.id.email);
        textWebsite = (TextView)findViewById(R.id.site);
        textPhone = (TextView)findViewById(R.id.phone);
        textCity = (TextView)findViewById(R.id.city);

        buttonDB = (Button)findViewById(R.id.buttonDB);

        textEmail.setOnClickListener(this);
        textWebsite.setOnClickListener(this);
        textPhone.setOnClickListener(this);
        textCity.setOnClickListener(this);
        buttonDB.setOnClickListener(this);

        dbHelper = new DBHelper(this);


        //Log.i(LOG_TAG, url+userId);
        new GetUserInfo().execute();
        db = dbHelper.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.email:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:"+userInfoMap.get("email")));
                startActivity(intent);
                break;
            case R.id.site:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://"+userInfoMap.get("website")));
                startActivity(intent);
                break;
            case R.id.phone:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+userInfoMap.get("phone")));
                startActivity(intent);
                break;

            case R.id.city:
                //проверял на 6-ой версии андроида- открывается гугл-картами, не браузером
                String uri = "http://maps.google.com/?q=" + userInfoMap.get("lat") + "," + userInfoMap.get("lng") + "&ll="+ userInfoMap.get("lat") + "," + userInfoMap.get("lng") +"&z=5";
                Log.i(LOG_TAG, userInfoMap.get("lat") + "," + userInfoMap.get("lng"));
                intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));

                startActivity(intent);
                break;
//            case R.id.city:
//                try{
//                    String uri = "geo:" + userInfoMap.get("lat") + "," + userInfoMap.get("lng")+"?q"+userInfoMap.get("lat") + "," + userInfoMap.get("lng");
//                    intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
//                    startActivity(intent);
//                }catch(NoClassDefFoundError e){
//                    (Toast.makeText(this, "Google Map API not found", Toast.LENGTH_LONG)).show();
//                    String uri = "http://maps.google.com/?q=" + userInfoMap.get("lat") + "," + userInfoMap.get("lng") + "&ll="+ userInfoMap.get("lat") + "," + userInfoMap.get("lng") +"&z=5";
//                    intent = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
//                    startActivity(intent);
//                }
//
//                Log.i(LOG_TAG, userInfoMap.get("lat") + "," + userInfoMap.get("lng"));
//                break;
            case R.id.buttonDB:
                saveToDataBase();
                break;

        }
    }

    private class GetUserInfo extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ContactActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler hh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = hh.makeServiceCall(url+userId);
            //Log.e(LOG_TAG, url+userId);
            //Log.e(LOG_TAG, "UserInfo from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject json = new JSONObject(jsonStr);
                    String id = json.getString("id");
                    String name = json.getString("name");
                    String nickName = json.getString("username");
                    String email = json.getString("email");

                    JSONObject address = json.getJSONObject("address");
                    String street = address.getString("street");
                    String suite = address.getString("suite");
                    String city = address.getString("city");
                    String zipcode = address.getString("zipcode");

                    JSONObject geo = address.getJSONObject("geo");
                    String lat = geo.getString("lat");
                    String lng = geo.getString("lng");
                    String latlng = "geo:"+lat + ","+ lng;

                    String phone = json.getString("phone");
                    String website = json.getString("website");

                    JSONObject company = json.getJSONObject("company");

                    String companyName = company.getString("name");
                    String companyCatchPhrase = company.getString("catchPhrase");
                    String companyBs = company.getString("bs");

                    userInfoMap.put("id", id);
                    userInfoMap.put("name", name);
                    userInfoMap.put("nickName", nickName);
                    userInfoMap.put("email", email);
                    userInfoMap.put("website", website);
                    userInfoMap.put("phone",  phone);
                    userInfoMap.put("city", city );
                    userInfoMap.put("lat", lat );
                    userInfoMap.put("lng", lng );
                    userInfoMap.put("latLng", latlng );

                } catch (final JSONException e) {
                    Log.e(LOG_TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
            Log.e(LOG_TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            textPostId.setText(postId);
            textName.setText(userInfoMap.get("name"));
            textNickName.setText(userInfoMap.get("nickName"));
            textEmail.setText(userInfoMap.get("email"));
            textWebsite.setText(userInfoMap.get("website"));
            textPhone.setText(userInfoMap.get("phone"));
            textCity.setText(userInfoMap.get("city"));
        }
    }

    private void saveToDataBase() {
        ContentValues cv = new ContentValues();
        cv.put("name", userInfoMap.get("name"));
        cv.put("nickname",userInfoMap.get("nickName"));
        cv.put("email",userInfoMap.get("email"));
        cv.put("website",userInfoMap.get("website"));
        cv.put("phone",userInfoMap.get("phone"));
        cv.put("city",userInfoMap.get("city"));
        long rowID = db.insert("mytable", null, cv);
        Log.i(LOG_TAG, "row inserted, ID = " + rowID);
        Toast.makeText(this,"User saved to SQL DataBase" , Toast.LENGTH_LONG).show();

        //По ТЗ этого не нужно было делать, но мне самому нужно было убедиться, что база данных работает

//        Cursor c = db.query("mytable", null, null, null, null, null, null);
//        if (c.moveToFirst()) {
//
//            int idColIndex = c.getColumnIndex("id");
//            int nameColIndex = c.getColumnIndex("name");
//            int nickNameColIndex = c.getColumnIndex("nickname");
//            int emailColIndex = c.getColumnIndex("email");
//            int websiteColIndex = c.getColumnIndex("website");
//            int phoneColIndex = c.getColumnIndex("phone");
//            int cityColIndex = c.getColumnIndex("city");
//
//            do {
//                // получаем значения по номерам столбцов и пишем все в лог
//                Log.d(LOG_TAG,
//                        "ID = " + c.getInt(idColIndex) +
//                                ", name = " + c.getString(nameColIndex) +
//                                ", nickname = " + c.getString(nickNameColIndex) +
//                                ", email = " + c.getString(emailColIndex) +
//                                ", website = " + c.getString(websiteColIndex) +
//                                ", phone = " + c.getString(phoneColIndex) +
//                                ", city " + c.getString(cityColIndex));

//            } while (c.moveToNext());
//            c.close();
//        }

    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "nickname text,"
                    + "email text,"
                    + "website text,"
                    + "phone text,"
                    + "city text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}

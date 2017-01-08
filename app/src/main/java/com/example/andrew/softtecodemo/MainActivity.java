package com.example.andrew.softtecodemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static String LOG_TAG = "my_log";

    //private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> userPostsList = new ArrayList<>();
    HashMap<String, String> postDataMap = new HashMap<String, String>();

    public PageIndicator mIndicator;
    private ViewPager awesomePager;
    private PagerAdapter pm;
    private static String url = "http://jsonplaceholder.typicode.com/posts";

    private ImageView imageView;
    private Button button;

    //Permision code that will be checked in the method onRequestPermissionsResult
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);
        imageView.startAnimation(anim);

        new GetPosts().execute();
        awesomePager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (PageIndicator) findViewById(R.id.pagerIndicator);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private List<GridFragment> fragments;

        public PagerAdapter(FragmentManager fm, List<GridFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }


    private class GetPosts extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Thread.sleep(2000); //Это для того, что бы анимация была видна, уж больно быстро грузится json
            } catch (Exception e){
                Log.i(LOG_TAG, "Uups, something wrong", e);
            }

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            //Log.e(LOG_TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray json = new JSONArray(jsonStr);

                    for(int i=0;i<json.length();i++){
                        postDataMap = new HashMap<String, String>();
                        JSONObject e = json.getJSONObject(i);
                        postDataMap.put("userId", e.getString("userId"));
                        postDataMap.put("id",  e.getString("id"));
                        postDataMap.put("title", e.getString("title"));
                        postDataMap.put("body", e.getString("body"));

                        userPostsList.add(postDataMap);
                    }
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

            imageView.setAnimation(null);
            button.setVisibility(View.VISIBLE);

            Iterator<HashMap<String, String >> it;
            List<GridFragment> gridFragments = new ArrayList<GridFragment>();
            it = userPostsList.iterator();

            while (it.hasNext()) {
                ArrayList<GridItems> itmLst = new ArrayList<GridItems>();
                GridItems itm = new GridItems(0, it.next());
                itmLst.add(itm);

                if (it.hasNext()) {
                    GridItems itm1 = new GridItems(1, it.next());
                    itmLst.add(itm1);
                }

                if (it.hasNext()) {
                    GridItems itm2 = new GridItems(2, it.next());
                    itmLst.add(itm2);
                }

                if (it.hasNext()) {
                    GridItems itm3 = new GridItems(3, it.next());
                    itmLst.add(itm3);
                }

                if (it.hasNext()) {
                    GridItems itm4 = new GridItems(4, it.next());
                    itmLst.add(itm4);
                }

                if (it.hasNext()) {
                    GridItems itm5 = new GridItems(5, it.next());
                    itmLst.add(itm5);
                }

                GridItems[] gp = {};
                GridItems[] gridPage = itmLst.toArray(gp);
                gridFragments.add(new GridFragment(gridPage, MainActivity.this));
            }

            pm = new PagerAdapter(getSupportFragmentManager(), gridFragments);
            awesomePager.setAdapter(pm);
            mIndicator.setViewPager(awesomePager);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkAndRequestPermissions()) {
                        appendLog();
                    }
                }
            });


        }
    }

    public void appendLog() {
       if ( isExternalStorageWritable() ) {

            File appDirectory = new File( Environment.getExternalStorageDirectory() + "/SoftTecoDemo" );
            File logDirectory = new File( appDirectory + "/log" );
            File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

            // create app folder
            if ( !appDirectory.exists() ) {
                appDirectory.mkdir();
            }
            // create log folder
            if ( !logDirectory.exists() ) {
                logDirectory.mkdir();
            }
            // write logcat to the file
            try {
                Process process = Runtime.getRuntime().exec("logcat");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
                Log.i(LOG_TAG, "Log is Writing");
                Toast.makeText(this,"LogCat saved to ExternalStorageDirectory\n \"SoftTecoDemo\\log\\\nlogcat(currentTimeMillis).txt \"" , Toast.LENGTH_LONG).show();
            } catch ( IOException e ) {
                e.printStackTrace();
            }

        } else {
            Log.i(LOG_TAG, "ExternalStorage is not Writable");
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    private  boolean checkAndRequestPermissions() {

        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(LOG_TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // process the normal flow
                        Log.i(LOG_TAG, "Write external storage permission granted");
                        Toast.makeText(this,"Now LogCat Button is enabled" , Toast.LENGTH_SHORT).show();
                        //else any one or both the permissions are not granted
                    } else {
                        Log.i(LOG_TAG, "Some permissions are not granted ask again ");
//                        permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
//                        show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                        if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Service Permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();

    }
    private void explain(String msg){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.example.andrew.softtecodemo")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }
}


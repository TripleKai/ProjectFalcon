package com.example.kailashsaravanan.projectfalconofficial;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.gmail.GmailScopes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    private Button mAcknowledgeButton;
    private Button mActivationButton;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY };

    private static String CHANNEL_ID = "ID";
    private int notificationId = 1;
    private boolean mAcknowledge = false;
    private boolean mActive = false;
    private List<String> mAcknowledgedList = new ArrayList<>();

    private Thread t;

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    FragmentTransaction mFragmentTransaction;
    MainActivity main = this;

//    public SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

    private boolean mNotificationSent = false;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Initialize default fragment
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.add(R.id.content_frame, new FragmentHome());
        mFragmentTransaction.commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@Nullable MenuItem menuItem) {
                        // set item as selected to persist highlight

                        // close drawer when item is tapped

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        Fragment fragment = null;
                        Class fragmentClass;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                fragmentClass = FragmentHome.class;
                                break;
                            case R.id.nav_history:
                                fragmentClass = FragmentHistory.class;
                                break;
                            case R.id.nav_videos:
                                fragmentClass = FragmentVideos.class;
                                break;
                            case R.id.nav_gallery:
                                fragmentClass = FragmentGallery.class;
                                break;
                            case R.id.nav_feed:
                                fragmentClass = FragmentLiveFeed.class;
                                break;
                            case R.id.nav_map:
                                fragmentClass = FragmentMap.class;
                                break;
                            case R.id.nav_settings:
                                fragmentClass = FragmentSettings.class;
                                break;
                            default:
                                fragmentClass = FragmentHome.class;
                        }
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                        // Highlight the selected item has been done by NavigationView
                        menuItem.setChecked(true);
                        // Set action bar title
                        setTitle(menuItem.getTitle());
                        // Close the navigation drawer
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(main, "Stopped", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
//        startService( new Intent( this, NotificationService. class )) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar = new ProgressBar(MainActivity.this, null,android.R.attr.progressBarStyleLarge);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int fileLength = 0;
            try{
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();
                File newFolder = new File("sdcard/photoalbum");
                if(!newFolder.exists()){
                    newFolder.mkdir();
                }
                File inputFile = new File(newFolder, "downloadedImage.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(inputFile);

                while((count = inputStream.read(data)) != -1){
                    total += count;
                    outputStream.write(data, 0, count);
                    int progress = (int)total * 100 / fileLength;
                    publishProgress(progress);
                }

                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            return "Download Complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(){
        // -- Notification generation -- //
        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, this.getClass()), PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FALCON ALERT")
                .setContentText("Suspicious motion has been detected")
                .setColor(Color.WHITE)
                .setContentIntent(pendingIntent)
                .setColor(Color.parseColor("#B00020"))
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, "Acknowledge", pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());

        mNotificationSent = !mNotificationSent;
    }

    public void startDownload(String url){
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
        Toast.makeText(main, url, Toast.LENGTH_SHORT).show();
    }

    public boolean getNotificationSent(){
        return mNotificationSent;
    }

}
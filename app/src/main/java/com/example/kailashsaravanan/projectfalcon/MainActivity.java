package com.example.kailashsaravanan.projectfalcon;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.gmail.GmailScopes;

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

    public boolean getNotificationSent(){
        return mNotificationSent;
    }

}
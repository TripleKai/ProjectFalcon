package com.example.kailashsaravanan.projectfalconofficial;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("NotificationService");
    }

    // Example Variables
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;

    // Firebase variables
    private ValueEventListener mFalconListener;
    private boolean mNotificationSent = false;

    private MainActivity mainActivity;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

//    @Override
//    public IBinder onBind(Intent intent) {
//        //// Needs Implementation: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Log. e ( TAG , "onHandleIntent" ) ;
            // Set value event listener for data changes of any kind
            mFalconListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChildren()) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            if ((singleSnapshot.getKey().equals("Captured Faces") && singleSnapshot.hasChildren()) ||
                                    (singleSnapshot.getKey().equals("Captured Motion") && singleSnapshot.hasChildren())) {
                                Log. e ( TAG , "Listener" ) ;
                                mainActivity.sendNotification();
                                mNotificationSent = !mNotificationSent;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return super.getSharedPreferences(name, mode);
    }

//    @Override
//    public int onStartCommand (Intent intent , int flags , int startId) {
//        Log. e ( TAG , "onStartCommand" ) ;
//        super .onStartCommand(intent , flags , startId) ;
////        startTimer() ;
//        return START_STICKY ;
//    }
//    @Override
//    public void onCreate () {
//        Log. e ( TAG , "onCreate" ) ;
//    }
//    @Override
//    public void onDestroy () {
//        Log. e ( TAG , "onDestroy" ) ;
////        stopTimerTask() ;
//        super .onDestroy() ;
//    }
//    //we are going to use a handler to be able to run in our TimerTask
//    final Handler handler = new Handler() ;
//    public void startTimer () {
//        timer = new Timer() ;
//        initializeTimerTask() ;
//        timer .schedule( timerTask , 5000 , Your_X_SECS * 1000 ) ; //
//    }
//    public void stopTimerTask () {
//        if ( timer != null ) {
//            timer .cancel() ;
//            timer = null;
//        }
//    }
//    public void initializeTimerTask () {
//        timerTask = new TimerTask() {
//            public void run () {
//                handler .post( new Runnable() {
//                    public void run () {
//                        createNotification() ;
//                    }
//                }) ;
//            }
//        } ;
//    }
    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "My Notification" ) ;
        mBuilder.setContentText( "Notification Listener Service Example" ) ;
        mBuilder.setTicker( "Notification Listener Service Example" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
}

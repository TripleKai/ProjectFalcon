package com.example.kailashsaravanan.projectfalcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends Fragment implements Animation.AnimationListener{
    private static final String TAG = "FragmentHome";

    private Animation mZoomIn, mZoomOut, mFadeIn, mFadeOut;
    private DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(1.5f);

    private static String CHANNEL_ID = "ID";
    private int notificationId = 1;

    private TextView mOutputFacesText, mOutputMotionText;
    private Button mAcknowledgeButton, mCallApiButton, mActivationButton;
    private boolean mActive = false;

    private boolean mAcknowledge = false;
    private boolean mNotificationSent = false;

    private ValueEventListener mFalconListener;
    private ValueEventListener mPinger;

    private MainActivity mainActivity;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    public FragmentHome() {

    }

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();

        // Set face count output text properties
        mOutputFacesText = view.findViewById(R.id.output_faces);
        mOutputFacesText.setTextColor(getResources().getColor(R.color.colorPrimary));
        mOutputFacesText.setPadding(16, 16, 16, 16);
        mOutputFacesText.setVerticalScrollBarEnabled(true);
        mOutputFacesText.setMovementMethod(new ScrollingMovementMethod());
        mOutputFacesText.setText(R.string.status_sleeping);

        // Set motion count output text properties
        mOutputMotionText = view.findViewById(R.id.output_motion);
        mOutputMotionText.setTextColor(getResources().getColor(R.color.colorPrimary));
        mOutputMotionText.setPadding(16, 16, 16, 16);
        mOutputMotionText.setVerticalScrollBarEnabled(true);
        mOutputMotionText.setMovementMethod(new ScrollingMovementMethod());

        mZoomIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_in );
        mZoomIn.setAnimationListener(this);
        mZoomIn.setInterpolator(mDecelerateInterpolator);

        mZoomOut = AnimationUtils.loadAnimation(getContext(),
                R.anim.zoom_out );
        mZoomOut.setAnimationListener(this);
        mZoomOut.setInterpolator(mDecelerateInterpolator);

        mFadeIn = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_in );
        mFadeIn.setAnimationListener(this);
        mFadeIn.setInterpolator(mDecelerateInterpolator);

        mFadeOut = AnimationUtils.loadAnimation(getContext(),
                R.anim.fade_out );
        mFadeOut.setAnimationListener(this);
        mFadeOut.setInterpolator(mDecelerateInterpolator);

        // Set value event listener for data changes of any kind
        mFalconListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    setmAcknowledge(true);
                    mCallApiButton.setEnabled(false);
                    mActivationButton.setEnabled(false);
                    mAcknowledgeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mAcknowledgeButton.startAnimation(mZoomIn);
                    mAcknowledgeButton.startAnimation(mZoomIn);
                    mCallApiButton.startAnimation(mFadeOut);
                    mCallApiButton.setVisibility(View.GONE);
                    mActivationButton.startAnimation(mFadeOut);
                    mActivationButton.setVisibility(View.GONE);
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (singleSnapshot.getKey().equals("Captured Faces") && singleSnapshot.hasChildren()) {
                            mainActivity.sendNotification();
                            mNotificationSent = !mNotificationSent;
                            for (DataSnapshot snapshot : singleSnapshot.getChildren()) {
                                String[] items = snapshot.getValue(String.class).split(":");
                                String output = "Captured Faces: " + items[0];
                                mOutputFacesText.setText(output);
                            }
                        } else if (singleSnapshot.getKey().equals("Captured Motion") && singleSnapshot.hasChildren()) {
                            mainActivity.sendNotification();
                            mNotificationSent = !mNotificationSent;
                            for (DataSnapshot snapshot : singleSnapshot.getChildren()){
                                String[] items = snapshot.getValue(String.class).split(":");
                                String output = "Captured Motion: " + items[0];
                                mOutputMotionText.setText(output);
                            }
                        }
                    }
                }
                else if(!dataSnapshot.hasChildren()){
                    mAcknowledgeButton.setBackground(getResources().getDrawable(R.drawable.button_bg2));
                    mOutputFacesText.setText(R.string.status_safe);
                    mOutputMotionText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // Set value event listener for one-time snapshot data check
        mPinger = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    setmAcknowledge(true);
                    mCallApiButton.setEnabled(false);
                    mActivationButton.setEnabled(false);
                    mAcknowledgeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mAcknowledgeButton.startAnimation(mZoomIn);
                    mCallApiButton.startAnimation(mFadeOut);
                    mCallApiButton.setVisibility(View.GONE);
                    mActivationButton.startAnimation(mFadeOut);
                    mActivationButton.setVisibility(View.GONE);
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (singleSnapshot.getKey().equals("Captured Faces") && singleSnapshot.hasChildren()) {
                            mainActivity.sendNotification();
                            mNotificationSent = !mNotificationSent;
                            for (DataSnapshot snapshot : singleSnapshot.getChildren()){
                                String[] items = snapshot.getValue(String.class).split(":");
                                String output = "Captured Faces: " + items[0];
                                mOutputFacesText.setText(output);
                            }
                        } else if (singleSnapshot.getKey().equals("Captured Motion") && singleSnapshot.hasChildren()) {
                            mainActivity.sendNotification();
                            mNotificationSent = !mNotificationSent;
                            for (DataSnapshot snapshot : singleSnapshot.getChildren()){
                                String[] items = snapshot.getValue(String.class).split(":");
                                String output = "Captured Motion: " + items[0];
                                mOutputMotionText.setText(output);
                            }
                        }
                    }
                }
                else if(!dataSnapshot.hasChildren()){
                    mAcknowledgeButton.setBackground(getResources().getDrawable(R.drawable.button_bg2));
                    mOutputFacesText.setText(R.string.status_safe);
                    mOutputMotionText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        // Initialize acknowledge button
        mAcknowledgeButton = view.findViewById(R.id.acknowledgeButton);
        mAcknowledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAcknowledge){
                    setmAcknowledge(false);
                    mCallApiButton.setEnabled(true);
                    mActivationButton.setEnabled(true);
                    mAcknowledgeButton.setBackground(getResources().getDrawable(R.drawable.button_bg2));
                    mAcknowledgeButton.startAnimation(mZoomOut);
                    mCallApiButton.startAnimation(mFadeIn);
                    mCallApiButton.setVisibility(View.VISIBLE);
                    mActivationButton.startAnimation(mFadeIn);
                    mActivationButton.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Acknowledged", Toast.LENGTH_SHORT).show();
                    if(mActive){
                        mOutputFacesText.setText(R.string.status_safe);
                        mOutputMotionText.setText("");
                    }
                    else if(!mActive){
                        mOutputFacesText.setText(R.string.status_sleeping);
                        mOutputMotionText.setText("");
                    }
                }
            }
        });


        // Initialize one-time database-calling button
        mCallApiButton = view.findViewById(R.id.callAPIButton);
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingFirebase();
            }
        });

        // Initialize real-time database-calling button
        mActivationButton = view.findViewById(R.id.activationButton);
        mActivationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActive = !mActive;
                activateFirebaseListener();
            }
        });

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null){
//            mActive = savedInstanceState.getBoolean("mActive");
//        }
//        else {
//            mActive = false;
//        }
//    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putBoolean("mActive", mActive);
//    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void pingFirebase(){
        Toast.makeText(getActivity(), "Falcon has been called", Toast.LENGTH_SHORT).show();
        mRef.child("/").addListenerForSingleValueEvent(mPinger);
        mRef.child("/").removeEventListener(mPinger);
    }

    private void activateFirebaseListener(){
        if(mActive) {
            Toast.makeText(getActivity(), "Falcon is now awake", Toast.LENGTH_SHORT).show();
            mRef.child("/").addValueEventListener(mFalconListener);
            mActivationButton.setText(R.string.listening);
            mActivationButton.setBackground(getResources().getDrawable(R.drawable.button_bg6));
    }
        else {
            Toast.makeText(getActivity(), "Falcon is now asleep", Toast.LENGTH_SHORT).show();
            mRef.child("/").removeEventListener(mFalconListener);
            mOutputFacesText.setText(R.string.status_sleeping);
            mOutputMotionText.setText("");
            mActivationButton.setText(R.string.sleeping);
            mActivationButton.setBackground(getResources().getDrawable(R.drawable.button_bg3));}
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//    public void sendNotification(){
//        // -- Notification generation -- //
//        createNotificationChannel();
//
//        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(getActivity(), FragmentHome.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        PendingIntent actionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), getActivity().getClass()), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("FALCON ALERT")
//                .setContentText("Suspicious motion has been detected")
//                .setColor(Color.WHITE)
//                .setContentIntent(actionIntent)
//                .setColor(Color.parseColor("#B00020"))
//                .setOnlyAlertOnce(true)
//                .setAutoCancel(true)
//                .addAction(R.mipmap.ic_launcher, "Acknowledge", actionIntent)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
//
//        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(notificationId, mBuilder.build());
//
//        mNotificationSent = !mNotificationSent;
//    }

    public boolean getmAcknowledge() {
        return mAcknowledge;
    }

    public void setmAcknowledge(boolean mAcknowledge) {
        this.mAcknowledge = mAcknowledge;
    }
}

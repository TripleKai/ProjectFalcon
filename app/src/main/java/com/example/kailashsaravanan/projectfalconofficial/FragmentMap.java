package com.example.kailashsaravanan.projectfalconofficial;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FragmentMap extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "FragmentMap";
    private ValueEventListener mLocationListener;
    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://project-falcon-bucket");
    private StorageReference mStorageRef = mStorage.getReference();
    private double mLatitude=0, mLongitude=0, mAccuracy=0;

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private ArrayList<LatLng> mLocations;
    private ArrayList<Double> mAccuracies;

    public FragmentMap(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        mLocationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocations = new ArrayList<>();
                mAccuracies = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        String[] items = singleSnapshot.getValue(String.class).split(":");
                        mLatitude = Double.parseDouble(items[1]);
                        mLongitude = Double.parseDouble(items[2]);
                        mAccuracy = Double.parseDouble(items[3]);
                        mLocations.add(new LatLng(mLatitude, mLongitude));
                        mAccuracies.add(mAccuracy);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("Captured Motion").addValueEventListener(mLocationListener);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map_view);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        int count = 0;

        for (LatLng location : mLocations) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Incident")
                    .snippet("Accuracy: " + String.valueOf(mAccuracies.get(count)) + " Meters"));
            mGoogleMap.addCircle(new CircleOptions()
                    .center(location)
                    .radius(mAccuracies.get(count))
                    .strokeColor(R.color.colorPrimary)
                    .fillColor(R.color.colorPrimaryDark));
            count++;
        }

        CameraPosition position = CameraPosition.builder().target(new LatLng(mLatitude, mLongitude)).zoom(13).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}

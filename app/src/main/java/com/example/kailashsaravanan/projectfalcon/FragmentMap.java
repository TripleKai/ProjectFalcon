package com.example.kailashsaravanan.projectfalcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class FragmentMap extends Fragment implements OnMapReadyCallback{
    private static final String TAG = "FragmentMap";
    private ValueEventListener mLocationListener;
    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();
    private double mLatitude=0, mLongitude=0, mAccuracy=0;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    public FragmentMap(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        mLocationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    Toast.makeText(getActivity(), dataSnapshot.getChildrenCount() + "", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        if (singleSnapshot.getKey().equals("accuracy")){
                            mAccuracy = (Double) singleSnapshot.getValue();
                        }
                        else if (singleSnapshot.getKey().equals("lat")){
                            mLatitude = (Double) singleSnapshot.getValue();
                        }
                        else if (singleSnapshot.getKey().equals("lng")){
                            mLongitude = (Double) singleSnapshot.getValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("Location").addValueEventListener(mLocationListener);
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
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)).title("Incident").snippet("Accuracy: " + String.valueOf(mAccuracy)));

        mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(mLatitude, mLongitude))
                .radius(mAccuracy)
                .strokeColor(R.color.colorPrimaryDark)
                .fillColor(R.color.colorAccent));

        CameraPosition position = CameraPosition.builder().target(new LatLng(mLatitude, mLongitude)).zoom(13).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}

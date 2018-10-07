package com.miles.foodtruck.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miles.foodtruck.R;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<TrackingService.TrackingInfo> matched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get this trackable id
        String trackableId = getIntent().getStringExtra(Constant.trackableId);

        //get matched tracking infos.
        List<TrackingService.TrackingInfo> matched = Helpers.getTrackingInfoForTrackable(
                trackableId,new Date(),
                getApplicationContext(),false);

        this.matched = matched;


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Make the first point as center and correct zoom level
        if (matched.size() != 0 )
        {
            LatLng center = new LatLng(matched.get(0).latitude,matched.get(0).longitude);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

        }

        //for each tracking info from tracking service.
        //Add marker for them
        for (TrackingService.TrackingInfo trackingInfo: matched) {

            LatLng latLng = new LatLng(trackingInfo.latitude,trackingInfo.longitude);

            if (trackingInfo.stopTime == 0)
            {
                mMap.addMarker(new MarkerOptions().position(latLng).title("No Stop"));

            }
            else
            {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Stop Point"));

            }
        }
    }
}

package com.copsopsapp.fragment.operatorpart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;
import com.copsopsapp.gps.GPSTracker;
import com.copsopsapp.utils.AppSession;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Lenovo on 21-11-2018.
 */

public class Frag_Position_of_Incidents extends Fragment implements View.OnClickListener,OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude, longitude;
    private AppSession mSession;
    private GPSTracker gps;
    private int distance = 500;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_position_of_incidents, container, false);

        gps = new GPSTracker(getActivity());

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);


        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.e("latitude==", "" + latitude);
        Log.e("longitude==", "" + longitude);


        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        // Add a marker in Sydney, Australia, and move the camera.
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));

        LatLng mylocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_map_marker_positionofincidents)).position(mylocation).title("Your Location"));

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

      /*  mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)).radius(distance)
                .fillColor(Color.parseColor("#B2A9F6"))
                .strokeColor(Color.parseColor("#000000"))
                .strokeWidth(2));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

    }


}

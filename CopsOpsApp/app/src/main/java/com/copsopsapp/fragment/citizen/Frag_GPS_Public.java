package com.copsopsapp.fragment.citizen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copsopsapp.R;
import com.copsopsapp.activity.MainActivity;
import com.copsopsapp.gps.GPSTracker;
import com.copsopsapp.utils.AppSession;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Lenovo on 27-11-2018.
 */

public class Frag_GPS_Public extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private EditText ETwherewegoing;
    private GoogleMap mMap;
    private double latitude, longitude;
    private AppSession mSession;
    private GPSTracker gps;
    private int distance = 500;
    private LinearLayout llsearch_layout;
    private ImageView IVimagesgps, IVdrawer, IVdropmenu, IVheaderpolice, IVheadercity, IVheadermedical;
    private TextView Tvheadertitle, Tvheaderpolice, Tvheadercity, Tvheadermedical;
    private int idrop = 0, ipolice = 0, imedical = 0, icity = 0;
    private RelativeLayout RLcontentbackground, RLheader, RLpoliceicons, RLcityicons, RLmedicalicons;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gps, container, false);


        mSession = AppSession.getInstance(getActivity());
        gps = new GPSTracker(getActivity());

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        ((MainActivity) getActivity()).Rltoolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).IVback.setVisibility(View.GONE);
        ((MainActivity) getActivity()).toolbar.setVisibility(View.GONE);
        ((MainActivity) getActivity()).IVlogout.setVisibility(View.GONE);


        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        ETwherewegoing = (EditText) view.findViewById(R.id.ETwherewegoing);
        ETwherewegoing.setOnClickListener(this);

        IVimagesgps = (ImageView) view.findViewById(R.id.IVimagesgps);
        IVdrawer = (ImageView) view.findViewById(R.id.IVdrawer);
        IVdropmenu = (ImageView) view.findViewById(R.id.IVdropmenu);
        IVheaderpolice = (ImageView) view.findViewById(R.id.IVheaderpolice);
        IVheadercity = (ImageView) view.findViewById(R.id.IVheadercity);
        IVheadermedical = (ImageView) view.findViewById(R.id.IVheadermedical);

        Tvheadertitle = (TextView) view.findViewById(R.id.Tvheadertitle);
        Tvheaderpolice = (TextView) view.findViewById(R.id.Tvheaderpolice);
        Tvheadercity = (TextView) view.findViewById(R.id.Tvheadercity);
        Tvheadermedical = (TextView) view.findViewById(R.id.Tvheadermedical);

        RLcontentbackground = (RelativeLayout) view.findViewById(R.id.RLcontentbackground);
        RLheader = (RelativeLayout) view.findViewById(R.id.RLheader);
        RLpoliceicons = (RelativeLayout) view.findViewById(R.id.RLpoliceicons);
        RLcityicons = (RelativeLayout) view.findViewById(R.id.RLcityicons);
        RLmedicalicons = (RelativeLayout) view.findViewById(R.id.RLmedicalicons);
        llsearch_layout = (LinearLayout) view.findViewById(R.id.llsearch_layout);

        IVheadermedical.setOnClickListener(this);
        IVheadercity.setOnClickListener(this);
        IVheaderpolice.setOnClickListener(this);
        IVdropmenu.setOnClickListener(this);
        IVdrawer.setOnClickListener(this);
        IVimagesgps.setOnClickListener(this);
        RLcontentbackground.setOnClickListener(this);

        RLheader.setBackgroundResource(0);
        IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
        IVdrawer.setVisibility(View.VISIBLE);
        Tvheadertitle.setVisibility(View.GONE);
        IVheadermedical.setVisibility(View.GONE);
        IVheadercity.setVisibility(View.GONE);
        IVheaderpolice.setVisibility(View.GONE);
        Tvheadermedical.setVisibility(View.GONE);
        Tvheaderpolice.setVisibility(View.GONE);
        Tvheadercity.setVisibility(View.GONE);
        RLpoliceicons.setVisibility(View.GONE);
        RLmedicalicons.setVisibility(View.GONE);
        RLcityicons.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ETwherewegoing:
                llsearch_layout.setVisibility(View.VISIBLE);
                RLheader.setBackgroundResource(0);
                IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
                IVdrawer.setVisibility(View.VISIBLE);
                Tvheadertitle.setVisibility(View.GONE);
                IVheadermedical.setVisibility(View.GONE);
                IVheadercity.setVisibility(View.GONE);
                IVheaderpolice.setVisibility(View.GONE);
                Tvheadermedical.setVisibility(View.GONE);
                Tvheaderpolice.setVisibility(View.GONE);
                Tvheadercity.setVisibility(View.GONE);
                RLpoliceicons.setVisibility(View.GONE);
                RLmedicalicons.setVisibility(View.GONE);
                RLcityicons.setVisibility(View.GONE);
                IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                IVheadercity.setImageResource(R.mipmap.img_city_header);
                idrop = 0;
                break;

            case R.id.IVdropmenu:
                llsearch_layout.setVisibility(View.GONE);
                if (idrop == 0) {
                    IVdropmenu.setImageResource(R.mipmap.img_drop_up);
                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
                    IVdrawer.setVisibility(View.INVISIBLE);
                    Tvheadertitle.setVisibility(View.VISIBLE);
                    IVheadermedical.setVisibility(View.VISIBLE);
                    IVheadercity.setVisibility(View.VISIBLE);
                    IVheaderpolice.setVisibility(View.VISIBLE);
                    Tvheadermedical.setVisibility(View.VISIBLE);
                    Tvheaderpolice.setVisibility(View.VISIBLE);
                    Tvheadercity.setVisibility(View.VISIBLE);
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                    IVheadercity.setImageResource(R.mipmap.img_city_header);
                    idrop = 1;
                } else {
                    RLheader.setBackgroundResource(0);
                    IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
                    IVdrawer.setVisibility(View.VISIBLE);
                    Tvheadertitle.setVisibility(View.GONE);
                    IVheadermedical.setVisibility(View.GONE);
                    IVheadercity.setVisibility(View.GONE);
                    IVheaderpolice.setVisibility(View.GONE);
                    Tvheadermedical.setVisibility(View.GONE);
                    Tvheaderpolice.setVisibility(View.GONE);
                    Tvheadercity.setVisibility(View.GONE);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.GONE);
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                    IVheadercity.setImageResource(R.mipmap.img_city_header);
                    idrop = 0;
                }
                break;

            case R.id.IVheadercity:
                ipolice = 0;
                imedical = 0;
                if (icity == 0) {
                    IVheadercity.setImageResource(R.mipmap.img_city_header_highlighter);
                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.VISIBLE);
                    icity = 1;
                } else {
                    IVheadercity.setImageResource(R.mipmap.img_city_header);
                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.GONE);
                    icity = 0;
                }
                break;

            case R.id.IVheadermedical:
                ipolice = 0;
                icity = 0;
                if (imedical == 0) {
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header_highlighter);
                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                    IVheadercity.setImageResource(R.mipmap.img_city_header);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.VISIBLE);
                    RLcityicons.setVisibility(View.GONE);
                    imedical = 1;
                } else {
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.GONE);
                    imedical = 0;
                }

                break;

            case R.id.IVheaderpolice:
                icity = 0;
                imedical = 0;
                if (ipolice == 0) {
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header_highlighter);
                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
                    IVheadercity.setImageResource(R.mipmap.img_city_header);
                    RLpoliceicons.setVisibility(View.VISIBLE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.GONE);
                    ipolice = 1;
                } else {
                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
                    RLpoliceicons.setVisibility(View.GONE);
                    RLmedicalicons.setVisibility(View.GONE);
                    RLcityicons.setVisibility(View.GONE);
                    ipolice = 0;
                }

                break;

            case R.id.IVdrawer:
                ((MainActivity)getActivity()).popBackStack();
                break;

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

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_navigation_icon)).position(mylocation).title("Your Location"));

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

      /*  mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude)).radius(distance)
                .fillColor(Color.parseColor("#B2A9F6"))
                .strokeColor(Color.parseColor("#000000"))
                .strokeWidth(2));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

    }
}

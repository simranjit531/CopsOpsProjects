//package copops.com.copopsApp.fragment;
//
//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Criteria;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceSelectionListener;
//import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polyline;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.gson.Gson;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import copops.com.copopsApp.R;
//import copops.com.copopsApp.mapnavigation.AbstractRouting;
//import copops.com.copopsApp.mapnavigation.Route;
//import copops.com.copopsApp.mapnavigation.RouteException;
//import copops.com.copopsApp.mapnavigation.Routing;
//import copops.com.copopsApp.mapnavigation.RoutingListener;
//import copops.com.copopsApp.pojo.IncidentSetPojo;
//import copops.com.copopsApp.pojo.IncidentSubPojo;
//import copops.com.copopsApp.pojo.IncidentTypePojo;
//import copops.com.copopsApp.services.ApiUtils;
//import copops.com.copopsApp.services.Service;
//import copops.com.copopsApp.utils.AppSession;
//import copops.com.copopsApp.utils.EncryptUtils;
//import copops.com.copopsApp.utils.Utils;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
////
//
//public class GPSPublicFragmentbackup extends Fragment implements OnMapReadyCallback,
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener, RoutingListener, View.OnClickListener {
//    IncidentTypePojo incidentTypeResponse;
//    private EditText ETwherewegoing;
//    private GoogleMap mMap;
//    int subPos;
//    int pos;
//    private double latitude, longitude;
//    private LinearLayout llsearch_layout;
//    private ImageView IVdrawer, IVdropmenu, IVheaderpolice, IVheadercity, IVheadermedical, IVpoliceheaderattack, IVpoliceheadertheft, IVpoliceheaderburglary, IVpoliceheadersexualviolence, IVpoliceheaderbodyaccident, IVpoliceheaderother;
//    private TextView Tvheadertitle, Tvheaderpolice, Tvheadercity, Tvheadermedical;
//    private int idrop = 0, ipolice = 0, imedical = 0, icity = 0;
//    private RelativeLayout RLcontentbackground, RLheader, RLpoliceicons, RLcityicons, RLmedicalicons;
//    private Button BTstart;
//    private List<LatLng> gpslstLatLngRoute = null;
//    ImageView IVmedicalheartattack, IVmedicalheaderfire, IVmedicalheaderhemorrhage, IVmedicalheadersexualviolence, IVmedicalheaderbodyaccident, IVmedicalheaderother, IVcitytag, IVcityheaderdirtyplace, IVcityheaderhole, IVcityheaderlighting, IVcityheaderbodyaccident, IVcityheaderother;
//    /// Manish Map
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    Marker mCurrLocationMarker, toLocationMarker;
//    LocationRequest mLocationRequest;
//    ArrayList<LatLng> points = new ArrayList<LatLng>();
//    LatLng fromLatLng, toLatLng;
//    public SupportPlaceAutocompleteFragment places = null;
//    private ProgressDialog progressDialog;
//    private List<Polyline> polylines;
//    View mapView;
//    ArrayList<Integer> distance = new ArrayList<Integer>();
//    IncidentSubPojo incidentSubPojo;
//    AppSession mAppSession;
//    View view = null;
//    TextView timeId;
//    private Marker marker;
//
//    TextView kmId;
//    //end
//
//    public GPSPublicFragmentbackup() {
//
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        if (view == null) {
//            view = inflater.inflate(R.layout.frag_gps, container, false);
//        }
//        gpslstLatLngRoute = new ArrayList<LatLng>();
//        gpslstLatLngRoute.clear();
//
//        ETwherewegoing = (EditText) view.findViewById(R.id.ETwherewegoing);
//        ETwherewegoing.setOnClickListener(this);
//
//        IVdrawer = (ImageView) view.findViewById(R.id.IVdrawer);
//        IVdropmenu = (ImageView) view.findViewById(R.id.IVdropmenu);
//        IVheaderpolice = (ImageView) view.findViewById(R.id.IVheaderpolice);
//        IVheadercity = (ImageView) view.findViewById(R.id.IVheadercity);
//        IVheadermedical = (ImageView) view.findViewById(R.id.IVheadermedical);
//        IVpoliceheaderattack = (ImageView) view.findViewById(R.id.IVpoliceheaderattack);
//        IVpoliceheadertheft = (ImageView) view.findViewById(R.id.IVpoliceheadertheft);
//        IVpoliceheaderburglary = (ImageView) view.findViewById(R.id.IVpoliceheaderburglary);
//        IVpoliceheadersexualviolence = (ImageView) view.findViewById(R.id.IVpoliceheadersexualviolence);
//        IVpoliceheaderbodyaccident = (ImageView) view.findViewById(R.id.IVpoliceheaderbodyaccident);
//        IVpoliceheaderother = (ImageView) view.findViewById(R.id.IVpoliceheaderother);
//
//
//        IVmedicalheartattack = (ImageView) view.findViewById(R.id.IVmedicalheartattack);
//        IVmedicalheaderfire = (ImageView) view.findViewById(R.id.IVmedicalheaderfire);
//        IVmedicalheaderhemorrhage = (ImageView) view.findViewById(R.id.IVmedicalheaderhemorrhage);
//        IVmedicalheadersexualviolence = (ImageView) view.findViewById(R.id.IVmedicalheadersexualviolence);
//        IVmedicalheaderbodyaccident = (ImageView) view.findViewById(R.id.IVmedicalheaderbodyaccident);
//        IVmedicalheaderother = (ImageView) view.findViewById(R.id.IVmedicalheaderother);
//
//        IVcitytag = (ImageView) view.findViewById(R.id.IVcitytag);
//        IVcityheaderdirtyplace = (ImageView) view.findViewById(R.id.IVcityheaderdirtyplace);
//        IVcityheaderhole = (ImageView) view.findViewById(R.id.IVcityheaderhole);
//        IVcityheaderlighting = (ImageView) view.findViewById(R.id.IVcityheaderlighting);
//        IVcityheaderbodyaccident = (ImageView) view.findViewById(R.id.IVcityheaderbodyaccident);
//        IVcityheaderother = (ImageView) view.findViewById(R.id.IVcityheaderother);
//
//        timeId = (TextView) view.findViewById(R.id.timeId);
//        kmId = (TextView) view.findViewById(R.id.kmId);
//        Tvheadertitle = (TextView) view.findViewById(R.id.Tvheadertitle);
//        Tvheaderpolice = (TextView) view.findViewById(R.id.Tvheaderpolice);
//        Tvheadercity = (TextView) view.findViewById(R.id.Tvheadercity);
//        Tvheadermedical = (TextView) view.findViewById(R.id.Tvheadermedical);
//
//        BTstart = (Button) view.findViewById(R.id.BTstart);
//
//        RLcontentbackground = (RelativeLayout) view.findViewById(R.id.RLcontentbackground);
//        RLheader = (RelativeLayout) view.findViewById(R.id.RLheader);
//        RLpoliceicons = (RelativeLayout) view.findViewById(R.id.RLpoliceicons);
//        RLcityicons = (RelativeLayout) view.findViewById(R.id.RLcityicons);
//        RLmedicalicons = (RelativeLayout) view.findViewById(R.id.RLmedicalicons);
//        llsearch_layout = (LinearLayout) view.findViewById(R.id.llsearch_layout);
//
//        llsearch_layout.setVisibility(View.GONE);
//        IVpoliceheaderattack.setOnClickListener(this);
//        IVpoliceheadertheft.setOnClickListener(this);
//        IVpoliceheaderburglary.setOnClickListener(this);
//        IVpoliceheadersexualviolence.setOnClickListener(this);
//        IVpoliceheaderbodyaccident.setOnClickListener(this);
//        IVpoliceheaderother.setOnClickListener(this);
//        IVheadermedical.setOnClickListener(this);
//        IVheadercity.setOnClickListener(this);
//        IVheaderpolice.setOnClickListener(this);
//        IVdropmenu.setOnClickListener(this);
//        IVdrawer.setOnClickListener(this);
//        RLcontentbackground.setOnClickListener(this);
//        IVcitytag = (ImageView) view.findViewById(R.id.IVcitytag);
//        IVcityheaderdirtyplace = (ImageView) view.findViewById(R.id.IVcityheaderdirtyplace);
//        IVcityheaderhole = (ImageView) view.findViewById(R.id.IVcityheaderhole);
//        IVcityheaderlighting = (ImageView) view.findViewById(R.id.IVcityheaderlighting);
//        IVcityheaderbodyaccident = (ImageView) view.findViewById(R.id.IVcityheaderbodyaccident);
//        IVcityheaderother = (ImageView) view.findViewById(R.id.IVcityheaderother);
//
//
//        IVcitytag.setOnClickListener(this);
//        IVcityheaderdirtyplace.setOnClickListener(this);
//        IVcityheaderhole.setOnClickListener(this);
//        IVcityheaderlighting.setOnClickListener(this);
//        IVcityheaderbodyaccident.setOnClickListener(this);
//        IVcityheaderother.setOnClickListener(this);
//
//        IVmedicalheartattack.setOnClickListener(this);
//        IVmedicalheaderfire.setOnClickListener(this);
//        IVmedicalheaderhemorrhage.setOnClickListener(this);
//        IVmedicalheadersexualviolence.setOnClickListener(this);
//        IVmedicalheaderbodyaccident.setOnClickListener(this);
//        IVmedicalheaderother.setOnClickListener(this);
//        BTstart.setOnClickListener(this);
//        BTstart.setVisibility(View.GONE);
//
//        RLheader.setBackgroundResource(0);
//        IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
//        IVdrawer.setVisibility(View.VISIBLE);
//        Tvheadertitle.setVisibility(View.GONE);
//        IVheadermedical.setVisibility(View.GONE);
//        IVheadercity.setVisibility(View.GONE);
//        IVheaderpolice.setVisibility(View.GONE);
//        Tvheadermedical.setVisibility(View.GONE);
//        Tvheaderpolice.setVisibility(View.GONE);
//        Tvheadercity.setVisibility(View.GONE);
//        RLpoliceicons.setVisibility(View.GONE);
//        RLmedicalicons.setVisibility(View.GONE);
//        RLcityicons.setVisibility(View.GONE);
//        mAppSession = mAppSession.getInstance(getActivity());
//        getIncidentType();
/////manish
//        polylines = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            checkLocationPermission();
//        }
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapView = mapFragment.getView();
//        mapFragment.getMapAsync(this);
//        places = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        ImageView searchIcon = (ImageView) ((LinearLayout) places.getView()).getChildAt(0);
//        searchIcon.setVisibility(View.GONE);
//        places.setHint(getString(R.string.whereareyougoing));
//
//        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                toLatLng = place.getLatLng();
//                //   Toast.makeText(getActivity(), place.getName() + "Lat Long" + place.getLatLng(), Toast.LENGTH_SHORT).show();
//                if (toLocationMarker != null) {
//                    toLocationMarker.remove();
//                }
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(toLatLng);
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                toLocationMarker = mMap.addMarker(markerOptions);
////                drawPolyline();
//                route();
//            }
//
//            @Override
//            public void onError(Status status) {
//
//                Toast.makeText(getActivity(), status.toString(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//
//            case R.id.IVpoliceheaderattack:
//                subPos = 0;
//
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVpoliceheadertheft:
//                subPos = 1;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVpoliceheaderburglary:
//                subPos = 2;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//                break;
//            case R.id.IVpoliceheadersexualviolence:
//                subPos = 3;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVpoliceheaderbodyaccident:
//                subPos = 4;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVpoliceheaderother:
//                subPos = 5;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//
//            case R.id.IVmedicalheartattack:
//                subPos = 0;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVmedicalheaderfire:
//                subPos = 1;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVmedicalheaderhemorrhage:
//                subPos = 2;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//                break;
//            case R.id.IVmedicalheadersexualviolence:
//                subPos = 3;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVmedicalheaderbodyaccident:
//                subPos = 4;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVmedicalheaderother:
//                subPos = 5;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//
//
//            case R.id.IVcitytag:
//                subPos = 0;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVcityheaderdirtyplace:
//                subPos = 1;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVcityheaderhole:
//                subPos = 2;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//                break;
//            case R.id.IVcityheaderlighting:
//                subPos = 3;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVcityheaderbodyaccident:
//                subPos = 4;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//            case R.id.IVcityheaderother:
//                subPos = 5;
//                mAppSession.saveData("screen","1");
//                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
//
//                break;
//
//            case R.id.ETwherewegoing:
////                llsearch_layout.setVisibility(View.VISIBLE);
//                IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
//                IVdrawer.setVisibility(View.VISIBLE);
//                Tvheadertitle.setVisibility(View.GONE);
//                IVheadermedical.setVisibility(View.GONE);
//                IVheadercity.setVisibility(View.GONE);
//                IVheaderpolice.setVisibility(View.GONE);
//                Tvheadermedical.setVisibility(View.GONE);
//                Tvheaderpolice.setVisibility(View.GONE);
//                Tvheadercity.setVisibility(View.GONE);
//                RLpoliceicons.setVisibility(View.GONE);
//                RLmedicalicons.setVisibility(View.GONE);
//                RLcityicons.setVisibility(View.GONE);
//                IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                IVheadercity.setImageResource(R.mipmap.img_city_header);
//                idrop = 0;
//                break;
//
//            case R.id.IVdropmenu:
////                llsearch_layout.setVisibility(View.GONE);
//                if (idrop == 0) {
//                    IVdropmenu.setImageResource(R.mipmap.img_drop_up);
//                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
//                    IVdrawer.setVisibility(View.INVISIBLE);
//                    Tvheadertitle.setVisibility(View.VISIBLE);
//                    IVheadermedical.setVisibility(View.VISIBLE);
//                    IVheadercity.setVisibility(View.VISIBLE);
//                    IVheaderpolice.setVisibility(View.VISIBLE);
//                    Tvheadermedical.setVisibility(View.VISIBLE);
//                    Tvheaderpolice.setVisibility(View.VISIBLE);
//                    Tvheadercity.setVisibility(View.VISIBLE);
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                    IVheadercity.setImageResource(R.mipmap.img_city_header);
//                    idrop = 1;
//                } else {
//                    RLheader.setBackgroundResource(0);
//                    IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
//                    IVdrawer.setVisibility(View.VISIBLE);
//                    Tvheadertitle.setVisibility(View.GONE);
//                    IVheadermedical.setVisibility(View.GONE);
//                    IVheadercity.setVisibility(View.GONE);
//                    IVheaderpolice.setVisibility(View.GONE);
//                    Tvheadermedical.setVisibility(View.GONE);
//                    Tvheaderpolice.setVisibility(View.GONE);
//                    Tvheadercity.setVisibility(View.GONE);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.GONE);
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                    IVheadercity.setImageResource(R.mipmap.img_city_header);
//                    idrop = 0;
//                }
//                break;
//
//            case R.id.IVheadercity:
//                ipolice = 0;
//                imedical = 0;
//                pos = 2;
//
//                if (Utils.checkConnection(getActivity()))
//
//                    getIncedentSubType(pos);
//
//                else
//                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//                if (icity == 0) {
//                    IVheadercity.setImageResource(R.mipmap.img_city_header_highlighter);
//                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.VISIBLE);
//                    icity = 1;
//                } else {
//                    IVheadercity.setImageResource(R.mipmap.img_city_header);
//                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.GONE);
//                    icity = 0;
//                }
//                break;
//
//            case R.id.IVheadermedical:
//                ipolice = 0;
//                icity = 0;
//                pos = 1;
//                if (Utils.checkConnection(getActivity()))
//                    getIncedentSubType(pos);
//                else
//                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//                if (imedical == 0) {
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header_highlighter);
//                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                    IVheadercity.setImageResource(R.mipmap.img_city_header);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.VISIBLE);
//                    RLcityicons.setVisibility(View.GONE);
//                    imedical = 1;
//                } else {
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.GONE);
//                    imedical = 0;
//                }
//
//                break;
//
//            case R.id.IVheaderpolice:
//                icity = 0;
//                imedical = 0;
//                pos = 0;
//                if (Utils.checkConnection(getActivity()))
//                    getIncedentSubType(pos);
//                else
//                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
//                if (ipolice == 0) {
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header_highlighter);
//                    RLheader.setBackgroundResource(R.mipmap.img_full_header);
//                    IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//                    IVheadercity.setImageResource(R.mipmap.img_city_header);
//                    RLpoliceicons.setVisibility(View.VISIBLE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.GONE);
//                    ipolice = 1;
//                } else {
//                    IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//                    RLheader.setBackgroundResource(R.mipmap.img_header_single);
//                    RLpoliceicons.setVisibility(View.GONE);
//                    RLmedicalicons.setVisibility(View.GONE);
//                    RLcityicons.setVisibility(View.GONE);
//                    ipolice = 0;
//                }
//
//                break;
//
//            case R.id.IVdrawer:
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//                }
//                break;
//
//            case R.id.BTstart:
//
//                //   zoomRoute(mMap,gpslstLatLngRoute);
//
//                if (mCurrLocationMarker != null) {
//                    mCurrLocationMarker.remove();
//                }
//
//                LatLng startpostionlatlong = new LatLng(gpslstLatLngRoute.get(0).latitude, gpslstLatLngRoute.get(0).longitude);
//
//
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(startpostionlatlong);
//                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_direction));
//                mCurrLocationMarker = mMap.addMarker(markerOptions);
//             /*   CircleOptions circleOptions = new CircleOptions()
//                        .center(new LatLng(gpslstLatLngRoute.get(0).latitude, gpslstLatLngRoute.get(0).longitude))
//                        .radius(80)
//                        .fillColor(Color.TRANSPARENT).strokeColor(Color.parseColor("#2984ff"))
//                        .strokeWidth(1);
//                mMap.addCircle(circleOptions);*/
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(startpostionlatlong));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(22.0f));
//
//                break;
//
//        }
//
//    }
//
//    ////manish
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
////Initialize Google Play Services
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (ContextCompat.checkSelfPermission(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
//                mMap.setMyLocationEnabled(true);
//
//                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//                // position on right bottom
//                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                rlp.setMargins(0, 0, 30, 300);
//            }
//        } else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, this);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
////Showing Current Location Marker on Map
//        fromLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(fromLatLng);
//        LocationManager locationManager = (LocationManager)
//                getActivity().getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getBestProvider(new Criteria(), true);
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        Location locations = locationManager.getLastKnownLocation(provider);
//        List<String> providerList = locationManager.getAllProviders();
//        if (null != locations && null != providerList && providerList.size() > 0) {
//            double longitude = locations.getLongitude();
//            double latitude = locations.getLatitude();
//            Geocoder geocoder = new Geocoder(getActivity(),
//                    Locale.getDefault());
//            try {
//                List<Address> listAddresses = geocoder.getFromLocation(latitude,
//                        longitude, 1);
//                if (null != listAddresses && listAddresses.size() > 0) {
//// Here we are finding , whatever we want our marker to show when clicked
//                    String state = listAddresses.get(0).getAdminArea();
//                    String country = listAddresses.get(0).getCountryName();
//                    String subLocality = listAddresses.get(0).getSubLocality();
//                    markerOptions.title("" + fromLatLng + "," + subLocality + "," +
//                            "," + state
//                            + "," + country);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//      //  markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.img_direction));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
////move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromLatLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
////        marker.setRotation(location.getBearing());
////this code stops location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
//                    this);
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//// Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            } else {
//// No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted. Do the
//                    // contacts-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(getActivity(),
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }
//
//    public void route() {
//        progressDialog = ProgressDialog.show(getActivity(), "Please wait...",
//                "Fetching route information.", true);
//        Routing routing = new Routing.Builder()
//                .travelMode(AbstractRouting.TravelMode.DRIVING)
//                .withListener(this)
//                .alternativeRoutes(true)
//                .waypoints(fromLatLng, toLatLng)
//                .build();
//        routing.execute();
//
//    }
//
//    @Override
//    public void onRoutingFailure(RouteException e) {
//        // The Routing request failed
//        progressDialog.dismiss();
//        if (e != null) {
//            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onRoutingStart() {
//        // The Routing Request starts
//    }
//
//    @Override
//    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
//        progressDialog.dismiss();
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromLatLng, 15));
//
//
//        if (polylines.size() > 0) {
//            for (Polyline poly : polylines) {
//                poly.remove();
//            }
//        }
//
//        //  polylines = new ArrayList<>();
//        //add route(s) to the map.
//        distance.clear();
//        for (int i = 0; i < route.size(); i++) {
//            distance.add(route.get(i).getDistanceValue());
//            gpslstLatLngRoute.addAll(route.get(i).getPoints());
//        }
//        Log.d("DistanceList", distance.toString());
//        Log.d("gpslstLatLngRoutesize", "==" + gpslstLatLngRoute.size());
//        Log.d("gpslstLatLngRoute", "==" + gpslstLatLngRoute);
//
//
//        //In case of more than 5 alternative routes
//        PolylineOptions polyOptions = new PolylineOptions();
//        polyOptions.color(getActivity().getResources().getColor(R.color.blue_shade));
//        polyOptions.width(15);
//        polyOptions.addAll(route.get(distance.indexOf(Collections.min(distance))).getPoints());
//        Polyline polyline = mMap.addPolyline(polyOptions);
//        polylines.add(polyline);
//
//        kmId.setText(route.get(distance.indexOf(Collections.min(distance))).getDistanceText());
//        timeId.setText(route.get(distance.indexOf(Collections.min(distance))).getDurationText());
//
//        BTstart.setVisibility(View.VISIBLE);
//
//
//        IVdropmenu.setImageResource(R.mipmap.img_menudropdown);
//        IVdrawer.setVisibility(View.VISIBLE);
//        Tvheadertitle.setVisibility(View.GONE);
//        IVheadermedical.setVisibility(View.GONE);
//        IVheadercity.setVisibility(View.GONE);
//        IVheaderpolice.setVisibility(View.GONE);
//        Tvheadermedical.setVisibility(View.GONE);
//        Tvheaderpolice.setVisibility(View.GONE);
//        Tvheadercity.setVisibility(View.GONE);
//        RLpoliceicons.setVisibility(View.GONE);
//        RLmedicalicons.setVisibility(View.GONE);
//        RLcityicons.setVisibility(View.GONE);
//        IVheaderpolice.setImageResource(R.mipmap.img_police_header);
//        IVheadermedical.setImageResource(R.mipmap.img_medical_header);
//        IVheadercity.setImageResource(R.mipmap.img_city_header);
//        idrop = 0;
//
//        //  Toast.makeText(getActivity(),"Route "+ ( distance.indexOf(Collections.min(distance))) +": distance - "+ route.get( distance.indexOf(Collections.min(distance))).getDistanceText()+": duration - "+ route.get( distance.indexOf(Collections.min(distance))).getDurationText(),Toast.LENGTH_LONG).show();
////        }
//
//    }
//
//    @Override
//    public void onRoutingCancelled() {
//        Log.i("Failed", "Routing was cancelled.");
//    }
//
//
//    public void getIncidentType() {
//        try {
//
//
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
//            progressDialog.show();
//
//            Service incidentType = ApiUtils.getAPIService();
//
//            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(Utils.getDeviceId(getActivity()))));
//            Call<IncidentTypePojo> fileUpload = incidentType.incidentType(mFile);
//            fileUpload.enqueue(new Callback<IncidentTypePojo>() {
//                @Override
//                public void onResponse(Call<IncidentTypePojo> call, Response<IncidentTypePojo> response)
//
//                {
//                    try {
//                        if (response.body() != null) {
//                            incidentTypeResponse = response.body();
//
//                            //   IncidentTypeAdapter mAdapter = new IncidentTypeAdapter(getActivity(), incidentTypeResponse.getData(), mIncedentInterface);
//                            //  mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            //   mRecyclerview.setItemAnimator(new DefaultItemAnimator());
//                            //  mRecyclerview.setAdapter(mAdapter);
//                            //  mAdapter.notifyDataSetChanged();
//                            progressDialog.dismiss();
//
//                        }
//
//                    } catch (Exception e) {
//                        progressDialog.dismiss();
//                        e.getMessage();
//                        Utils.showAlert(e.getMessage(), getActivity());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<IncidentTypePojo> call, Throwable t) {
//                    Log.d("TAG", "Error " + t.getMessage());
//                    progressDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getMessage();
//            Utils.showAlert(e.getMessage(), getActivity());
//        }
//    }
//
//
//    public void getIncedentSubType(int pos) {
//        try {
//
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
//            progressDialog.show();
//            IncidentSetPojo incidentSetPojo = new IncidentSetPojo();
//
//            incidentSetPojo.setIncident_id(incidentTypeResponse.getData().get(pos).getIncident_id());
//            incidentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));
//
//
//            //   incedentTypeId=incidentTypeResponse.getData().get(pos).getIncident_id();
//            Service incedentTypeData = ApiUtils.getAPIService();
//            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incidentSetPojo)));
//            //  RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"),  "6ofmYMOSHsnj+NTBDXo6107Iqzaw+Rx0X2brlpHmVYk=:ZmVkY2JhOTg3NjU0MzIxMA==");
//
//            Call<IncidentSubPojo> fileUpload = incedentTypeData.getIncedentSubTypeData(mFile);
//
//            fileUpload.enqueue(new Callback<IncidentSubPojo>() {
//                @Override
//                public void onResponse(Call<IncidentSubPojo> call, Response<IncidentSubPojo> response) {
//                    try {
//                        if (response.body() != null) {
//                            incidentSubPojo = response.body();
//                            progressDialog.dismiss();
//                            if (incidentSubPojo.getStatus().equals("false")) {
//                                progressDialog.dismiss();
//                            } else {
//                                progressDialog.dismiss();
//                                //   IncidentSubTypeAdapter adapter = new IncidentSubTypeAdapter(getActivity(), incidentSubPojo,mIncedentInterface);
//                                //   recyclerView.setAdapter(adapter);
//                                //   GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
//                                //   recyclerView.setLayoutManager(manager);
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        progressDialog.dismiss();
//                        e.getMessage();
//                        Utils.showAlert(e.getMessage(), getActivity());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<IncidentSubPojo> call, Throwable t) {
//                    Log.d("TAG", "Error " + t.getMessage());
//                    progressDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            Utils.showAlert(e.getMessage(), getActivity());
//        }
//
//    }
//
//    public void drawPolyline() {
//        points.clear();
//        PolylineOptions polyLineOptions = new PolylineOptions();
//        points.add(fromLatLng);
//        points.add(toLatLng);
//        polyLineOptions.width(7 * 1);
//        polyLineOptions.geodesic(true);
//        polyLineOptions.color(getActivity().getResources().getColor(R.color.colorAccent));
//        polyLineOptions.addAll(points);
//        Polyline polyline = mMap.addPolyline(polyLineOptions);
//        polyline.setGeodesic(true);
//    }
//
//    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {
//
//        Log.e("googlMap", "==" + googleMap);
//        Log.e("lstLatLngRoute", "==" + lstLatLngRoute);
//
//        /*CircleOptions circleOptions = new CircleOptions()
//                .center(new LatLng(fromLatLng.latitude, fromLatLng.longitude))
//                .radius(100)
//                .fillColor(Color.TRANSPARENT).strokeColor(Color.parseColor("#2984ff"))
//                .strokeWidth(1);
//
//                 mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));*/
//
//        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
//
//        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
//        for (LatLng latLngPoint : lstLatLngRoute)
//            boundsBuilder.include(latLngPoint);
//
//        int routePadding = 10;
//        LatLngBounds latLngBounds = boundsBuilder.build();
//        Log.e("latLngBounds", "==" + latLngBounds);
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
//
//    }
//
//
//}

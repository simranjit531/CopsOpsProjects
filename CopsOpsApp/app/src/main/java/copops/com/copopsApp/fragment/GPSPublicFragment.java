package copops.com.copopsApp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.guidance.NavigationManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import copops.com.copopsApp.R;
import copops.com.copopsApp.pojo.IncidentSetPojo;
import copops.com.copopsApp.pojo.IncidentSubPojo;
import copops.com.copopsApp.pojo.IncidentTypePojo;
import copops.com.copopsApp.services.ApiUtils;
import copops.com.copopsApp.services.ForegroundService;
import copops.com.copopsApp.services.Service;
import copops.com.copopsApp.utils.AppSession;
import copops.com.copopsApp.utils.EncryptUtils;
import copops.com.copopsApp.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//

public class GPSPublicFragment extends Fragment implements  View.OnClickListener {
    IncidentTypePojo incidentTypeResponse;
    private EditText ETwherewegoing;
    int subPos;
    int pos;
    private LinearLayout llsearch_layout;
    private ImageView IVdrawer, IVdropmenu, IVheaderpolice, IVheadercity, IVheadermedical, IVpoliceheaderattack, IVpoliceheadertheft, IVpoliceheaderburglary, IVpoliceheadersexualviolence, IVpoliceheaderbodyaccident, IVpoliceheaderother;
    private TextView Tvheadertitle, Tvheaderpolice, Tvheadercity, Tvheadermedical;
    private int idrop = 0, ipolice = 0, imedical = 0, icity = 0;
    private RelativeLayout RLcontentbackground, RLheader, RLpoliceicons, RLcityicons, RLmedicalicons;
    private Button BTstart;
    ImageView IVmedicalheartattack, IVmedicalheaderfire, IVmedicalheaderhemorrhage, IVmedicalheadersexualviolence, IVmedicalheaderbodyaccident, IVmedicalheaderother, IVcitytag, IVcityheaderdirtyplace, IVcityheaderhole, IVcityheaderlighting, IVcityheaderbodyaccident, IVcityheaderother;
    /// Manish Map
    private Map m_map;
    private NavigationManager m_navigationManager;
    private GeoBoundingBox m_geoBoundingBox;
    private Route m_route;
    private boolean m_foregroundServiceStarted;
    private MapFragment m_mapFragment;
    private Activity m_activity;
    Location mCurrentLocation;
    private LocationManager mLocationManager;
    private boolean isGpsEnabled;
    private boolean isNetworkEnabled;
    PlaceAutocompleteFragment places;
    LatLng toLatLng;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private ProgressDialog progressDialog;


    IncidentSubPojo incidentSubPojo;
    AppSession mAppSession;
    private static View view;
    TextView timeId;
    TextView kmId;
    //end

    public GPSPublicFragment() {

    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.frag_gps, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        m_activity = getActivity();

        ETwherewegoing = (EditText) view.findViewById(R.id.ETwherewegoing);
        ETwherewegoing.setOnClickListener(this);

        IVdrawer = (ImageView) view.findViewById(R.id.IVdrawer);
        IVdropmenu = (ImageView) view.findViewById(R.id.IVdropmenu);
        IVheaderpolice = (ImageView) view.findViewById(R.id.IVheaderpolice);
        IVheadercity = (ImageView) view.findViewById(R.id.IVheadercity);
        IVheadermedical = (ImageView) view.findViewById(R.id.IVheadermedical);
        IVpoliceheaderattack = (ImageView) view.findViewById(R.id.IVpoliceheaderattack);
        IVpoliceheadertheft = (ImageView) view.findViewById(R.id.IVpoliceheadertheft);
        IVpoliceheaderburglary = (ImageView) view.findViewById(R.id.IVpoliceheaderburglary);
        IVpoliceheadersexualviolence = (ImageView) view.findViewById(R.id.IVpoliceheadersexualviolence);
        IVpoliceheaderbodyaccident = (ImageView) view.findViewById(R.id.IVpoliceheaderbodyaccident);
        IVpoliceheaderother = (ImageView) view.findViewById(R.id.IVpoliceheaderother);


        IVmedicalheartattack = (ImageView) view.findViewById(R.id.IVmedicalheartattack);
        IVmedicalheaderfire = (ImageView) view.findViewById(R.id.IVmedicalheaderfire);
        IVmedicalheaderhemorrhage = (ImageView) view.findViewById(R.id.IVmedicalheaderhemorrhage);
        IVmedicalheadersexualviolence = (ImageView) view.findViewById(R.id.IVmedicalheadersexualviolence);
        IVmedicalheaderbodyaccident = (ImageView) view.findViewById(R.id.IVmedicalheaderbodyaccident);
        IVmedicalheaderother = (ImageView) view.findViewById(R.id.IVmedicalheaderother);

        IVcitytag = (ImageView) view.findViewById(R.id.IVcitytag);
        IVcityheaderdirtyplace = (ImageView) view.findViewById(R.id.IVcityheaderdirtyplace);
        IVcityheaderhole = (ImageView) view.findViewById(R.id.IVcityheaderhole);
        IVcityheaderlighting = (ImageView) view.findViewById(R.id.IVcityheaderlighting);
        IVcityheaderbodyaccident = (ImageView) view.findViewById(R.id.IVcityheaderbodyaccident);
        IVcityheaderother = (ImageView) view.findViewById(R.id.IVcityheaderother);

        timeId = (TextView) view.findViewById(R.id.timeId);
        kmId = (TextView) view.findViewById(R.id.kmId);
        Tvheadertitle = (TextView) view.findViewById(R.id.Tvheadertitle);
        Tvheaderpolice = (TextView) view.findViewById(R.id.Tvheaderpolice);
        Tvheadercity = (TextView) view.findViewById(R.id.Tvheadercity);
        Tvheadermedical = (TextView) view.findViewById(R.id.Tvheadermedical);

        BTstart = (Button) view.findViewById(R.id.BTstart);

        RLcontentbackground = (RelativeLayout) view.findViewById(R.id.RLcontentbackground);
        RLheader = (RelativeLayout) view.findViewById(R.id.RLheader);
        RLpoliceicons = (RelativeLayout) view.findViewById(R.id.RLpoliceicons);
        RLcityicons = (RelativeLayout) view.findViewById(R.id.RLcityicons);
        RLmedicalicons = (RelativeLayout) view.findViewById(R.id.RLmedicalicons);
        llsearch_layout = (LinearLayout) view.findViewById(R.id.llsearch_layout);

        llsearch_layout.setVisibility(View.GONE);
        IVpoliceheaderattack.setOnClickListener(this);
        IVpoliceheadertheft.setOnClickListener(this);
        IVpoliceheaderburglary.setOnClickListener(this);
        IVpoliceheadersexualviolence.setOnClickListener(this);
        IVpoliceheaderbodyaccident.setOnClickListener(this);
        IVpoliceheaderother.setOnClickListener(this);
        IVheadermedical.setOnClickListener(this);
        IVheadercity.setOnClickListener(this);
        IVheaderpolice.setOnClickListener(this);
        IVdropmenu.setOnClickListener(this);
        IVdrawer.setOnClickListener(this);
        RLcontentbackground.setOnClickListener(this);
        IVcitytag = (ImageView) view.findViewById(R.id.IVcitytag);
        IVcityheaderdirtyplace = (ImageView) view.findViewById(R.id.IVcityheaderdirtyplace);
        IVcityheaderhole = (ImageView) view.findViewById(R.id.IVcityheaderhole);
        IVcityheaderlighting = (ImageView) view.findViewById(R.id.IVcityheaderlighting);
        IVcityheaderbodyaccident = (ImageView) view.findViewById(R.id.IVcityheaderbodyaccident);
        IVcityheaderother = (ImageView) view.findViewById(R.id.IVcityheaderother);


        IVcitytag.setOnClickListener(this);
        IVcityheaderdirtyplace.setOnClickListener(this);
        IVcityheaderhole.setOnClickListener(this);
        IVcityheaderlighting.setOnClickListener(this);
        IVcityheaderbodyaccident.setOnClickListener(this);
        IVcityheaderother.setOnClickListener(this);

        IVmedicalheartattack.setOnClickListener(this);
        IVmedicalheaderfire.setOnClickListener(this);
        IVmedicalheaderhemorrhage.setOnClickListener(this);
        IVmedicalheadersexualviolence.setOnClickListener(this);
        IVmedicalheaderbodyaccident.setOnClickListener(this);
        IVmedicalheaderother.setOnClickListener(this);
        BTstart.setOnClickListener(this);
//        BTstart.setVisibility(View.GONE);

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
        mAppSession = mAppSession.getInstance(getActivity());
        getIncidentType();
///manish
        m_mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        places = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        ImageView searchIcon = (ImageView) ((LinearLayout) places.getView()).getChildAt(0);
        searchIcon.setVisibility(View.GONE);
        places.setHint(getString(R.string.whereareyougoing));

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission() && gpsEnabled()) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        10, mLocationListener);
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                        10, mLocationListener);
            }
        }
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                toLatLng = place.getLatLng();

                if(mCurrentLocation == null || toLatLng == null)
                {
                    Utils.showAlert(getActivity().getString(R.string.destination_selection),getActivity());
                }else {
                    createRoute();
                }

            }

            @Override
            public void onError(Status status) {

                Toast.makeText(m_activity, status.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        initMapFragment();
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.IVpoliceheaderattack:
                subPos = 0;

                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVpoliceheadertheft:
                subPos = 1;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVpoliceheaderburglary:
                subPos = 2;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
                break;
            case R.id.IVpoliceheadersexualviolence:
                subPos = 3;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVpoliceheaderbodyaccident:
                subPos = 4;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVpoliceheaderother:
                subPos = 5;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;

            case R.id.IVmedicalheartattack:
                subPos = 0;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVmedicalheaderfire:
                subPos = 1;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVmedicalheaderhemorrhage:
                subPos = 2;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
                break;
            case R.id.IVmedicalheadersexualviolence:
                subPos = 3;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVmedicalheaderbodyaccident:
                subPos = 4;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVmedicalheaderother:
                subPos = 5;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;


            case R.id.IVcitytag:
                subPos = 0;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVcityheaderdirtyplace:
                subPos = 1;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVcityheaderhole:
                subPos = 2;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());
                break;
            case R.id.IVcityheaderlighting:
                subPos = 3;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVcityheaderbodyaccident:
                subPos = 4;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;
            case R.id.IVcityheaderother:
                subPos = 5;
                mAppSession.saveData("screen","1");
                Utils.fragmentCall(new IncedentGenerateFragment(incidentTypeResponse.getData().get(pos).getIncident_id(), incidentSubPojo, subPos, mAppSession.getData("id"), mAppSession.getData("screen")), getFragmentManager());

                break;

            case R.id.ETwherewegoing:
//                llsearch_layout.setVisibility(View.VISIBLE);
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
//                llsearch_layout.setVisibility(View.GONE);
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
                pos = 2;
                mAppSession.saveData("city","city");
                if (Utils.checkConnection(getActivity()))

                    getIncedentSubType(pos);

                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
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
                pos = 1;
                mAppSession.saveData("city","medical");
                if (Utils.checkConnection(getActivity()))
                    getIncedentSubType(pos);
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
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
                pos = 0;
                mAppSession.saveData("city","police");
                if (Utils.checkConnection(getActivity()))
                    getIncedentSubType(pos);
                else
                    Utils.showAlert(getActivity().getString(R.string.internet_conection), getActivity());
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
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }

                break;

            case R.id.BTstart:
                if(mCurrentLocation == null || toLatLng == null)
                {
                    Utils.showAlert(getActivity().getString(R.string.destination_selection),getActivity());
                }else {
                    if (m_route == null) {
                    //   createRoute();
                    } else {

                        if(BTstart.getText().toString().equalsIgnoreCase("Start")) {
                            startNavigation();
                            BTstart.setText(R.string.stop);
                        }else {
                            m_navigationManager.stop();


                            /*
                             * Restore the map orientation to show entire route on screen
                             */
                            m_map.zoomTo(m_geoBoundingBox, Map.Animation.NONE, 16);
                            BTstart.setText(R.string.start);
                            m_map.setZoomLevel(17.5);
                            m_route = null;
                        }
                    }
                }
                break;

        }

    }

    ////manish



    public void getIncidentType() {
        try {


            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
          //  progressDialog.show();

            Service incidentType = ApiUtils.getAPIService();

            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(Utils.getDeviceId(getActivity()))));
            Call<IncidentTypePojo> fileUpload = incidentType.incidentType(mFile);
            fileUpload.enqueue(new Callback<IncidentTypePojo>() {
                @Override
                public void onResponse(Call<IncidentTypePojo> call, Response<IncidentTypePojo> response)

                {
                    try {
                        if (response.body() != null) {
                            incidentTypeResponse = response.body();

                            //   IncidentTypeAdapter mAdapter = new IncidentTypeAdapter(getActivity(), incidentTypeResponse.getData(), mIncedentInterface);
                            //  mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            //   mRecyclerview.setItemAnimator(new DefaultItemAnimator());
                            //  mRecyclerview.setAdapter(mAdapter);
                            //  mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();

                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailure(Call<IncidentTypePojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            Utils.showAlert(e.getMessage(), getActivity());
        }
    }


    public void getIncedentSubType(int pos) {
        try {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getString(R.string.loading_msg));
         //   progressDialog.show();
            IncidentSetPojo incidentSetPojo = new IncidentSetPojo();

            incidentSetPojo.setIncident_id(incidentTypeResponse.getData().get(pos).getIncident_id());
            incidentSetPojo.setDevice_id(Utils.getDeviceId(getActivity()));


            //   incedentTypeId=incidentTypeResponse.getData().get(pos).getIncident_id();
            Service incedentTypeData = ApiUtils.getAPIService();
            RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"), EncryptUtils.encrypt(Utils.key, Utils.iv, new Gson().toJson(incidentSetPojo)));
            //  RequestBody mFile = RequestBody.create(MediaType.parse("text/plain"),  "6ofmYMOSHsnj+NTBDXo6107Iqzaw+Rx0X2brlpHmVYk=:ZmVkY2JhOTg3NjU0MzIxMA==");

            Call<IncidentSubPojo> fileUpload = incedentTypeData.getIncedentSubTypeData(mFile);

            fileUpload.enqueue(new Callback<IncidentSubPojo>() {
                @Override
                public void onResponse(Call<IncidentSubPojo> call, Response<IncidentSubPojo> response) {
                    try {
                        if (response.body() != null) {
                            incidentSubPojo = response.body();
                            progressDialog.dismiss();
                            if (incidentSubPojo.getStatus().equals("false")) {
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                //   IncidentSubTypeAdapter adapter = new IncidentSubTypeAdapter(getActivity(), incidentSubPojo,mIncedentInterface);
                                //   recyclerView.setAdapter(adapter);
                                //   GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
                                //   recyclerView.setLayoutManager(manager);
                            }
                        }

                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.getMessage();
                        Utils.showAlert(e.getMessage(), getActivity());
                    }
                }

                @Override
                public void onFailure(Call<IncidentSubPojo> call, Throwable t) {
                    Log.d("TAG", "Error " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(e.getMessage(), getActivity());
        }

    }
//manish map
private void initMapFragment() {
    // Set path of isolated disk cache
    String diskCacheRoot = Environment.getExternalStorageDirectory().getPath()
            + File.separator + ".isolated-here-maps";
    // Retrieve intent name from manifest
    String intentName = "";
    try {
        ApplicationInfo ai = m_activity.getPackageManager().getApplicationInfo(m_activity.getPackageName(), PackageManager.GET_META_DATA);
        Bundle bundle = ai.metaData;
        intentName = bundle.getString("INTENT_NAME");
    } catch (PackageManager.NameNotFoundException e) {
        Log.d(this.getClass().toString(), "Failed to find intent name, NameNotFound: " + e.getMessage());
    }

    boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot, intentName);
    if (!success) {
        // Setting the isolated disk cache was not successful, please check if the path is valid and
        // ensure that it does not match the default location
        // (getExternalStorageDirectory()/.here-maps).
        // Also, ensure the provided intent name does not match the default intent name.
    } else {
        if (m_mapFragment != null) {
            /* Initialize the MapFragment, results will be given via the called back. */
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                    if (error == Error.NONE) {
                        m_map = m_mapFragment.getMap();
                        if(mCurrentLocation != null)
                            m_map.setCenter(new GeoCoordinate(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                                    Map.Animation.NONE);
                        //Put this call in Map.onTransformListener if the animation(Linear/Bow)
                        //is used in setCenter()
                        m_map.setZoomLevel(17.5);
                        /*
                         * Get the NavigationManager instance.It is responsible for providing voice
                         * and visual instructions while driving and walking
                         */
                        // Create the MapMarker
                        try {
                            Image mMarker = new Image();
                            mMarker.setImageResource(R.mipmap.img_navigation_icon);
                            MapMarker myMapMarker = new MapMarker(new GeoCoordinate(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), mMarker);
                            m_map.addMapObject(myMapMarker);
                            m_navigationManager = NavigationManager.getInstance();
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    } else {
                        Toast.makeText(m_activity,
                                "ERROR: Cannot initialize Map with error " + error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

    private void createRoute() {
        /* Initialize a CoreRouter */
        CoreRouter coreRouter = new CoreRouter();

        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();

        /*
         * Initialize a RouteOption.HERE SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        RouteOptions routeOptions = new RouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);

        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

        /* Define waypoints for the route */
        /* START: 4350 Still Creek Dr */
        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        /* END: Langley BC */
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(toLatLng.latitude, toLatLng.longitude));

        /* Add both waypoints to the route plan */

        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);

        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(routePlan,
                new Router.Listener<List<RouteResult>, RoutingError>() {

                    @Override
                    public void onProgress(int i) {
                        /* The calculation progress can be retrieved in this callback. */
                    }

                    @Override
                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                         RoutingError routingError) {
                        /* Calculation is done.Let's handle the result */
                        if (routingError == RoutingError.NONE) {
                            if (routeResults.get(0).getRoute() != null) {

                                m_route = routeResults.get(0).getRoute();
                                /* Create a MapRoute so that it can be placed on the map */
                                MapRoute mapRoute = new MapRoute(routeResults.get(0).getRoute());

                                /* Show the maneuver number on top of the route */
                                mapRoute.setManeuverNumberVisible(true);

                                /* Add the MapRoute to the map */
                                m_map.addMapObject(mapRoute);

                                /*
                                 * We may also want to make sure the map view is orientated properly
                                 * so the entire route can be easily seen.
                                 */
                                m_geoBoundingBox = routeResults.get(0).getRoute().getBoundingBox();
                                m_map.zoomTo(m_geoBoundingBox, Map.Animation.NONE,
                                        Map.MOVE_PRESERVE_TILT);
                                m_map.setZoomLevel(10.5);
                                String aaa=""+(routeResults.get(0).getRoute().getLength()/1000);
                                kmId.setText(""+(routeResults.get(0).getRoute().getLength()/1000)+" KM ");

                                if(routeResults.get(0).getRoute().getLength()/360>60){

                                    String time=Utils.formatHoursAndMinutes(routeResults.get(0).getRoute().getLength() / 360 );
                                    timeId.setText("" + time+ " Hr ");
                                }else {
                                    timeId.setText("" + routeResults.get(0).getRoute().getLength() / 360 + " Min ");
                                }
                               // startNavigation();
                            } else {
                                Toast.makeText(m_activity,
                                        "Error:route results returned is not valid",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(m_activity,
                                    "Error:route calculation returned error code: " + routingError,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void startForegroundService() {
        if (!m_foregroundServiceStarted) {
            m_foregroundServiceStarted = true;
            Intent startIntent = new Intent(m_activity, ForegroundService.class);
            startIntent.setAction(ForegroundService.START_ACTION);
            m_activity.getApplicationContext().startService(startIntent);
        }
    }

    private void stopForegroundService() {
        if (m_foregroundServiceStarted) {
            m_foregroundServiceStarted = false;
            Intent stopIntent = new Intent(m_activity, ForegroundService.class);
            stopIntent.setAction(ForegroundService.STOP_ACTION);
            m_activity.getApplicationContext().startService(stopIntent);
        }
    }

    private void addNavigationListeners() {

        /*
         * Register a NavigationManagerEventListener to monitor the status change on
         * NavigationManager
         */
        m_navigationManager.addNavigationManagerEventListener(
                new WeakReference<NavigationManager.NavigationManagerEventListener>(
                        m_navigationManagerEventListener));

        /* Register a PositionListener to monitor the position updates */
        m_navigationManager.addPositionListener(
                new WeakReference<NavigationManager.PositionListener>(m_positionListener));
    }

    private NavigationManager.PositionListener m_positionListener = new NavigationManager.PositionListener() {
        @Override
        public void onPositionUpdated(GeoPosition geoPosition) {
            /* Current position information can be retrieved in this callback */
        }
    };

    private NavigationManager.NavigationManagerEventListener m_navigationManagerEventListener = new NavigationManager.NavigationManagerEventListener() {
        @Override
        public void onRunningStateChanged() {
//            Toast.makeText(m_activity, "Running state changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNavigationModeChanged() {
//            Toast.makeText(m_activity, "Navigation mode changed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnded(NavigationManager.NavigationMode navigationMode) {
//            Toast.makeText(m_activity, navigationMode + " was ended", Toast.LENGTH_SHORT).show();
            stopForegroundService();
        }

        @Override
        public void onMapUpdateModeChanged(NavigationManager.MapUpdateMode mapUpdateMode) {
//            Toast.makeText(m_activity, "Map update mode is changed to " + mapUpdateMode,
//                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRouteUpdated(Route route) {
            Toast.makeText(m_activity, "Route updated", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCountryInfo(String s, String s1) {
//            Toast.makeText(m_activity, "Country info updated from " + s + " to " + s1,
//                    Toast.LENGTH_SHORT).show();
        }
    };
@Override
    public void onDestroy() {
    super.onDestroy();
        /* Stop the navigation when app is destroyed */
        if (m_navigationManager != null) {
            stopForegroundService();
            m_navigationManager.stop();
        }
    }
    ////Manish
    private final android.location.LocationListener mLocationListener = new android.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                mCurrentLocation = location;
                initMapFragment();
            } else {
                Toast.makeText(m_activity, "Location is not available now", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private boolean checkPermission() {
        boolean check = ContextCompat.checkSelfPermission(m_activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(m_activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!check) {
            ActivityCompat.requestPermissions(m_activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        return true;
//    }
    }

    private boolean gpsEnabled() {
        isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            displayLocationSettingsRequest(m_activity);
//    if (!isGpsEnabled) {
//            Toast.makeText(m_activity, "GPS is not enabled", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(10);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        initMapFragment();
//                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(m_activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
//                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void startNavigation() {
      //  BTstart.setText(R.string.stop);
        BTstart.setText(R.string.start);
        /* Configure Navigation manager to launch navigation on current map */
        m_navigationManager.setMap(m_map);

        /*
         * Start the turn-by-turn navigation.Please note if the transport mode of the passed-in
         * route is pedestrian, the NavigationManager automatically triggers the guidance which is
         * suitable for walking. Simulation and tracking modes can also be launched at this moment
         * by calling either simulate() or startTracking()
         */

        /* Choose navigation modes between real time navigation and simulation */
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_activity);
//        alertDialogBuilder.setTitle("Navigation");
//        alertDialogBuilder.setMessage("Choose Mode");
//        alertDialogBuilder.setNegativeButton("Navigation",new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialoginterface, int i) {
                m_navigationManager.startNavigation(m_route);
                m_map.setTilt(60);
                startForegroundService();
//            };
//        });
//        alertDialogBuilder.setPositiveButton("Simulation",new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialoginterface, int i) {
//                m_navigationManager.simulate(m_route,60);//Simualtion speed is set to 60 m/s
//                m_map.setTilt(60);
//                startForegroundService();
//            };
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
        /*
         * Set the map update mode to ROADVIEW.This will enable the automatic map movement based on
         * the current location.If user gestures are expected during the navigation, it's
         * recommended to set the map update mode to NONE first. Other supported update mode can be
         * found in HERE Android SDK API doc
         */
        m_navigationManager.setMapUpdateMode(NavigationManager.MapUpdateMode.NONE);

        /*
         * NavigationManager contains a number of listeners which we can use to monitor the
         * navigation status and getting relevant instructions.In this example, we will add 2
         * listeners for demo purpose,please refer to HERE Android SDK API documentation for details
         */
        addNavigationListeners();
    }
}

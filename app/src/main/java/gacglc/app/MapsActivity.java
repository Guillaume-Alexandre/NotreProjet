package gacglc.app;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Gare> gares;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fragmentManager = getSupportFragmentManager();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        gares = getIntent().getParcelableArrayListExtra("GARES_LIST");
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

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                setResult((int) arg0.getTag(), new Intent());
                finish();
                /*gacglc.app.GareInfoFragment gareInfoFragment = gacglc.app.GareInfoFragment.newInstance(gares.get((int) arg0.getTag()));
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.a_main_lyt_fragment_container, gareInfoFragment);
                transaction.commit();*/
            }
        });
        setUpClusterer();
    }

    public void addMarkers() {
        int i=0;
        for(Gare truc:gares) {
            truc.pos=i;
            if (truc.longitude_wgs84 != null && truc.latitude_wgs84 != null)
                addItems(Double.parseDouble(truc.latitude_wgs84), Double.parseDouble(truc.longitude_wgs84), truc.intitule_gare, truc.pos);
            i++;
            //googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(truc.latitude_wgs84),Double.parseDouble(truc.longitude_wgs84))).title(truc.intitule_gare));
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.5,3),5));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(new OwnIconRendered(this.getApplicationContext(), getMap(), mClusterManager));
        // Add cluster items (markers) to the cluster manager.
        addMarkers();
    }

    private void addItems(double lat, double lng, String title, int pos) {
        MyItem offsetItem = new MyItem(lat, lng, title, pos);
        mClusterManager.addItem(offsetItem);
    }
}

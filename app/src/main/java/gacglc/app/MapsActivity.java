package gacglc.app;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Gare> gares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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



        // Add a marker in Sydney and move the camera
        LatLng gardanne = new LatLng(43.45, 5.4667);
        googleMap.addMarker(new MarkerOptions().position(gardanne).title("Mines St Etienne"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gardanne));
        setUpClusterer();
    }

    public void addMarkers() {
        for(Gare truc:gares)
            if(truc.longitude_wgs84!=null && truc.latitude_wgs84!=null)
                addItems(Double.parseDouble(truc.latitude_wgs84),Double.parseDouble(truc.longitude_wgs84), truc.intitule_gare);
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(truc.latitude_wgs84),Double.parseDouble(truc.longitude_wgs84))).title(truc.intitule_gare));
    }

    public GoogleMap getMap() {
        return mMap;
    }

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer() {
        // Position the map.
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.45, 5.4667), 10));

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

    private void addItems(double lat, double lng, String title) {
        MyItem offsetItem = new MyItem(lat, lng, title);
        mClusterManager.addItem(offsetItem);
    }
}

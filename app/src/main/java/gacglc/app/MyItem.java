package gacglc.app;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by guigo on 21/11/2017.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;

    public MyItem(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle=title;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {return mTitle; }
}
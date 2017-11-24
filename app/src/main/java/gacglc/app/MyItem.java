package gacglc.app;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by guigo on 21/11/2017.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    public int mPos;

    public MyItem(double lat, double lng, String title, int pos) {
        mPosition = new LatLng(lat, lng);
        mTitle=title;
        mPos=pos;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public int getTag() { return mPos; }
    public String getTitle() {return mTitle; }
}
package gacglc.app;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class OwnIconRendered extends DefaultClusterRenderer<MyItem> implements ClusterManager.OnClusterItemClickListener<MyItem> {

    OIRInterface machin;

    public interface OIRInterface {
        void displayGareList();
        void toaster();
    }

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        // Does nothing, but you could go into the user's profile page, for example.
        machin.displayGareList();
        return false;
    }

    @Override
    protected void onClusterItemRendered(MyItem item, Marker marker) {
        super.onClusterItemRendered(item, marker);
        marker.setTag(item.mPos);
    }
}
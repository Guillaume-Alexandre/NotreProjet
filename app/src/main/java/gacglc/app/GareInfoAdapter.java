package gacglc.app;

/**
 * Created by guigo on 21/11/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.*;

public class GareInfoAdapter extends ArrayAdapter<Gare> {

    public GareInfoAdapter(@NonNull Context context, @NonNull java.util.List<Gare> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gare_info, parent, false);

        Gare gare = getItem(position);

        TextView txvCommune= rootView.findViewById(R.id.textViewCommune);
        TextView txvIntitule= rootView.findViewById(R.id.textViewIntitule);
        TextView txvLat= rootView.findViewById(R.id.textViewLat);
        TextView txvLong= rootView.findViewById(R.id.textViewLong);
        txvCommune.setText(gare.commune);
        txvIntitule.setText(gare.intitule_gare);
        txvLat.setText(gare.latitude_wgs84);
        txvLong.setText(gare.longitude_wgs84);

        return rootView;
    }
}

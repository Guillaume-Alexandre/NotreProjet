package gacglc.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guigo on 30/10/2017.
 */

public class GareAdapter extends ArrayAdapter<Gare> {

    public GareAdapter(@NonNull Context context, @NonNull List<Gare> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_gare, parent, false);

        TextView txvIntitule= rootView.findViewById(R.id.i_gare_txv_intitule_gare);
        TextView txvCommune = rootView.findViewById(R.id.i_gare_txv_commune);

        Gare gareToDisplay = getItem(position);

        txvIntitule.setText(gareToDisplay.intitule_gare);
        txvCommune.setText(gareToDisplay.commune);

        return rootView;
    }
}


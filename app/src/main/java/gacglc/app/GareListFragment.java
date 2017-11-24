package gacglc.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import gacglc.app.Gare;
import java.util.ArrayList;

/**
 * Created by guigo on 30/10/2017.
 */
/**
 * Fragment that display the list of bottles send as argument with {@link #BUNDLE_PARAM_GARES} key
 */
public class GareListFragment extends Fragment {

    public static final String BUNDLE_PARAM_GARES = "BUNDLE_PARAM_GARES";
    private String mParam1;

    private GareListListener activity;
    GareListListener mCallback;

    private ListView ltvGares;
    private ArrayList<Gare> gares;
    private GareAdapter gareAdapter;

    public interface GareListListener {
        public void displayInfo(Gare gare);
    }

    public GareListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param gares List of gares to display
     * @return A new instance of fragment GareListFragment.
     */
    public static GareListFragment newInstance(ArrayList<Gare> gares) {
        GareListFragment fragment = new GareListFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_PARAM_GARES, gares);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Get list of gares to display from fragment arguments
            gares = (ArrayList<Gare>) getArguments().getSerializable(BUNDLE_PARAM_GARES);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (GareListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GareListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gare_list, container, false);

        ltvGares = (ListView) rootView.findViewById(R.id.f_gare_lis_ltv_gares);
        gareAdapter = new GareAdapter(getContext(), gares);
        ltvGares.setAdapter(gareAdapter);
        ltvGares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                mCallback.displayInfo(gares.get(pos));
            }
        });

        return rootView;
    }


}


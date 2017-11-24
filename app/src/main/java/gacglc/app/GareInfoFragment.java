package gacglc.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GareInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GareInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GareInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BUNDLE_PARAM_GARES = "BUNDLE_PARAM_GARES";


    // TODO: Rename and change types of parameters
    private Gare gare ;

    private OnFragmentInteractionListener mListener;

    public GareInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gare Gare.
     * @return A new instance of fragment GareInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GareInfoFragment newInstance(Gare gare) {
        GareInfoFragment fragment = new GareInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_PARAM_GARES, gare);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gare = (Gare) getArguments().getSerializable(BUNDLE_PARAM_GARES);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gare_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setText() {
        View rootView = getView();
        TextView txvCommune= rootView.findViewById(R.id.textViewCommune);
        TextView txvIntitule= rootView.findViewById(R.id.textViewIntitule);
        TextView txvLat= rootView.findViewById(R.id.textViewLat);
        TextView txvLong= rootView.findViewById(R.id.textViewLong);
        txvCommune.setText(gare.commune);
        txvIntitule.setText(gare.intitule_gare);
        txvLat.setText(gare.latitude_wgs84);
        txvLong.setText(gare.longitude_wgs84);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

package ch.glucalc.about;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.glucalc.R;

/**
 * Created by jmottet on 28/10/2015.
 */
public class ProjectInformationFragment extends Fragment {

    private static String TAG = "GluCalc";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("ProjectInformationFragment.onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.project_information, container, false);
        return layout;
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

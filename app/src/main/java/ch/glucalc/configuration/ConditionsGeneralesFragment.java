package ch.glucalc.configuration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.glucalc.EnumBloodGlucose;
import ch.glucalc.GestureHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.KeyboardHelper;
import ch.glucalc.R;


@SuppressLint("DefaultLocale")
public class ConditionsGeneralesFragment extends Fragment {

    private static String TAG = "GluCalc";

    private boolean isInitialProcess = false;

    private OnConditionsAccepted mCallback;

    public interface OnConditionsAccepted {

        void openConfigurationFirstStepFragment();

    }

    public void setIsInitialProcess(boolean isInitialProcess) {
        this.isInitialProcess = isInitialProcess;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnConditionsAccepted) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnConditionsAccepted");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("ConditionsGeneralesFragment.onCreate");
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.conditions_generales_view, container, false);
        Button accepterButton = (Button) layout.findViewById(R.id.accepter_button);
        Button refuserButton = (Button) layout.findViewById(R.id.refuser_button);

        if (isInitialProcess) {
            accepterButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallback.openConfigurationFirstStepFragment();
                }
            });

            refuserButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    System.exit(0);
                }
            });
        } else {
            LinearLayout titleContainer = (LinearLayout) layout.findViewById(R.id.conditions_generales_title_container);

            titleContainer.setVisibility(View.GONE);
            accepterButton.setVisibility(View.GONE);
            refuserButton.setVisibility(View.GONE);
        }
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("ConditionsGeneralesFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("ConditionsGeneralesFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("ConditionsGeneralesFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("ConditionsGeneralesFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("ConditionsGeneralesFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("ConditionsGeneralesFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("ConditionsGeneralesFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

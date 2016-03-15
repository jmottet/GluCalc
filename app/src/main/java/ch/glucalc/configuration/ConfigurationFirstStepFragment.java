package ch.glucalc.configuration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.DialogHelper;
import ch.glucalc.GestureHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.KeyboardHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;
import ch.glucalc.meal.NewMealFoodListFragment;
import ch.glucalc.meal.type.MealType;


@SuppressLint("DefaultLocale")
public class ConfigurationFirstStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private OnConfigurationSecondStep mCallback;

    private BloodGlucoseUnitListFragment bloodGlucoseUnitListFragment;

    // Container Activity must implement this interface
    public interface OnConfigurationSecondStep {

        void openConfigurationSecondStepFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("ConfigurationFirstStepFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("ConfigurationFirstStepFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.next_page_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("ConfigurationFirstStepFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.next:
                goToNextPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToNextPage() {
        KeyboardHelper.hideKeyboard(getActivity());
        mCallback.openConfigurationSecondStepFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bloodGlucoseUnitListFragment = new BloodGlucoseUnitListFragment();

        View layout = inflater.inflate(R.layout.configuration_first_step, container, false);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.configuration_blood_glucose_unit_container, bloodGlucoseUnitListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        initializeGestureDetector(layout);

        return layout;
    }

    private void initializeGestureDetector(View layout) {
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        log("ConfigurationFirstStepFragment.onFling");
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > GestureHelper.SWIPE_MAX_OFF_PATH)
                                return false;
                            if (GestureHelper.isGestureRightToLeft(e1, e2, velocityX)) {
                                goToNextPage();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("ConfigurationFirstStepFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnConfigurationSecondStep) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnConfigurationSecondStep");
        }
    }

    @Override
    public void onDestroy() {
        log("ConfigurationFirstStepFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("ConfigurationFirstStepFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("ConfigurationFirstStepFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("ConfigurationFirstStepFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("ConfigurationFirstStepFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("ConfigurationFirstStepFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

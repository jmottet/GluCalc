package ch.glucalc.configuration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.glucalc.EnumBloodGlucose;
import ch.glucalc.GestureHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.NavigationBackAndNext;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.meal.NewMealConstants;
import ch.glucalc.meal.NewMealFoodAdapter;
import ch.glucalc.meal.diary.FoodDiary;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

@SuppressLint("DefaultLocale")
public class BloodGlucoseUnitListFragment extends ListFragment {

    private static String TAG = "GluCalc";

    private  BloodGlucoseUnitAdapter bloodGlucoseUnitAdapter;

    private List<EnumBloodGlucose> bloodGlucoseUnits = Arrays.asList(EnumBloodGlucose.values());

    private NavigationBackAndNext navigationBackAndNext;

    private EnumBloodGlucose currentBloodGlucoseUnit = MainActivity.GLOBAL_BLOOD_GLUCOSE;

    public void setNavigationBackAndNext(NavigationBackAndNext navigationBackAndNext) {
        this.navigationBackAndNext = navigationBackAndNext;
    }

    public EnumBloodGlucose getCurrentBloodGlucoseUnit() {
        return currentBloodGlucoseUnit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("BloodGlucoseUnitListFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Set the list adapter for this ListFragment
        bloodGlucoseUnitAdapter = new BloodGlucoseUnitAdapter(getActivity(), bloodGlucoseUnits, bloodGlucoseUnits.indexOf(MainActivity.GLOBAL_BLOOD_GLUCOSE));
        setListAdapter(bloodGlucoseUnitAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (navigationBackAndNext != null) {
            initializeGestureDetector(getListView());
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("BloodGlucoseUnitListFragment.onListItemClick");

        super.onListItemClick(l, v, position, id);
        for (int i= 0; i < l.getChildCount(); i++) {
            if (i != position) {
                l.getChildAt(i).setBackground(null);
            }
        }
        v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
        bloodGlucoseUnitAdapter.setSelectedPosition(position);
        currentBloodGlucoseUnit = (EnumBloodGlucose) getListView().getItemAtPosition(position);
    }

    @Override
    public void onDetach() {
        log("BloodGlucoseUnitListFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        log("BloodGlucoseUnitListFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        log("BloodGlucoseUnitListFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("BloodGlucoseUnitListFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("BloodGlucoseUnitListFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("BloodGlucoseUnitListFragment.onStop");
        super.onStart();
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
                        log("NewMealFragment.onFling");
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > GestureHelper.SWIPE_MAX_OFF_PATH)
                                return false;
                            if (GestureHelper.isGestureRightToLeft(e1, e2, velocityX)) {
                                navigationBackAndNext.goToNextPage();

                            } else if (GestureHelper.isGestureLeftToRight(e1, e2, velocityX)) {
                                // nothing
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

    private void log(String msg) {
        Log.i(TAG, msg);
    }


}

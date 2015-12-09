package ch.glucalc.meal.display;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.meal.diary.MealDiary;

/**
 * Created by jmottet on 28/10/2015.
 */
public class MealDiaryFirstStepFragment extends ListFragment {

    private OnMealDiaryDate mCallback;

    private static String TAG = "GluCalc";

    private static DateFormat formatter = DateFormat.getDateInstance();
    private static SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd");

    // Container Activity must implement this interface
    public interface OnMealDiaryDate {

        void openMealDiarySecondStep(String date);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealDiaryDate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealDiaryDate");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("MealDiaryFirstStepFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Load the mealDiaryDates from the Database
        List<MealDiary> mealDiaries = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
                .loadMealDiaries();

        List<String> dateOfDiaries = new ArrayList<>(mealDiaries.size());
        for (MealDiary mealDiary : mealDiaries) {
            try {
                Date dateParsed = inputSdf.parse(mealDiary.getMealDate());
                String dateFormatted = formatter.format(dateParsed);
                if (!dateOfDiaries.contains(dateFormatted)) {
                    dateOfDiaries.add(dateFormatted);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,
                dateOfDiaries);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("MealDiaryFirstStepFragment.onListItemClick");
        super.onListItemClick(l, v, position, id);
        final String currentDate = (String) getListView().getItemAtPosition(position);
        try {
            Date parsedDate = formatter.parse(currentDate);
            mCallback.openMealDiarySecondStep(outputSdf.format(parsedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("MealDiaryFirstStepFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("MealDiaryFirstStepFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("MealDiaryFirstStepFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("MealDiaryFirstStepFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("MealDiaryFirstStepFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("MealDiaryFirstStepFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("MealDiaryFirstStepFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

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
import ch.glucalc.meal.NewMealConstants;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;

/**
 * Created by jmottet on 28/10/2015.
 */
public class MealDiarySecondStepFragment extends ListFragment {

    private static String TAG = "GluCalc";

    private OnMealDiary mCallback;

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH'h'mm");
    private static SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<MealDiary> mealDiaries = null;

    // Container Activity must implement this interface
    public interface OnMealDiary {

        public void openMealDiaryThirdStep(long mealDiaryId);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealDiary) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealDiary");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("MealDiarySecondStepFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Load the mealDiaryDates from the Database
        mealDiaries = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
                .loadMealDiariesStartingWith(getMealDiaryDate());

        List<String> typeOfDiaries = new ArrayList<>(mealDiaries.size());
        for (MealDiary mealDiary : mealDiaries) {
            try {
                Date dateParsed = inputSdf.parse(mealDiary.getMealDate());
                String dateFormatted = formatter.format(dateParsed);
                MealType mealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealType(mealDiary.getMealTypeId());

                if (!typeOfDiaries.contains(dateFormatted)) {
                    typeOfDiaries.add(mealType.getName() + " (" + dateFormatted + ")");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1,
                typeOfDiaries);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("MealDiarySecondStepFragment.onListItemClick");
        super.onListItemClick(l, v, position, id);
        MealDiary mealDiary = mealDiaries.get(position);
        MealType mealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealType(mealDiary.getMealTypeId());
        mCallback.openMealDiaryThirdStep(mealDiary.getId());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("MealDiarySecondStepFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("MealDiarySecondStepFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("MealDiarySecondStepFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("MealDiarySecondStepFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("MealDiarySecondStepFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("MealDiarySecondStepFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("MealDiarySecondStepFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private String getMealDiaryDate() {
        return getArguments().getString(MealDiaryConstants.MEAL_DIARY_DATE_PARAMETER);
    }
}

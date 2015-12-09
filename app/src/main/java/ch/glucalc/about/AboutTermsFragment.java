package ch.glucalc.about;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.meal.diary.MealDiary;

/**
 * Created by jmottet on 28/10/2015.
 */
public class AboutTermsFragment extends Fragment {

    private static String TAG = "GluCalc";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("AboutTermsFragment.onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.about_terms, container, false);
        return layout;
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

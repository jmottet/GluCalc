package ch.glucalc.meal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;

import ch.glucalc.R;


@SuppressLint("DefaultLocale")
public class NewMealFragment extends Fragment {

  private static String TAG = "GluCalc";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("NewMealFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("EditMealTypeFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      // Inflate the layout for this fragment
      return inflater.inflate(R.layout.new_meal_view, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    log("NewMealFragment.onActivityCreated");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    log("NewMealFragment.onAttach");
    super.onAttach(activity);
  }

  @Override
  public void onDestroy() {
    log("NewMealFragment.onDestroy");
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    log("NewMealFragment.onDetach");
    super.onDetach();
  }

  @Override
  public void onPause() {
    log("NewMealFragment.onPause");
    super.onPause();
  }

  @Override
  public void onResume() {
    log("NewMealFragment.onResume");
    super.onResume();
  }

  @Override
  public void onStart() {
    log("NewMealFragment.onStart");
    super.onStart();
  }

  @Override
  public void onStop() {
    log("NewMealFragment.onStop");
    super.onStart();
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }

}

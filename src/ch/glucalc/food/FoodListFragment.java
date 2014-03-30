package ch.glucalc.food;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import ch.glucalc.GluCalcSQLiteHelper;

public class FoodListFragment extends ListFragment {

	private static String TAG = "GluCalc";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log("FoodListFragment.onCreate");
		super.onCreate(savedInstanceState);

		List<Food> foods = new GluCalcSQLiteHelper(getActivity()
				.getApplicationContext()).loadFoods();

		// Set the list adapter for this ListFragment
		setListAdapter(new FoodAdapter(getActivity(), foods));

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		log("FoodListFragment.onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		log("FoodListFragment.onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		log("FoodListFragment.onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		log("FoodListFragment.onDetach");
		super.onDetach();
	}

	@Override
	public void onPause() {
		log("FoodListFragment.onPause");
		super.onPause();
	}

	@Override
	public void onResume() {
		log("FoodListFragment.onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		log("FoodListFragment.onStart");
		super.onStart();
	}

	@Override
	public void onStop() {
		log("FoodListFragment.onStop");
		super.onStart();
	}

	private void log(String msg) {
		Log.i(TAG, msg);
	}

}

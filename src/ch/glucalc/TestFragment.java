package ch.glucalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ch.glucalc.food.Food;

public class TestFragment extends Fragment {

	private static String TAG = "GluCalc";

	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = container.getContext();

		View rootView = inflater.inflate(R.layout.fragment_test, container,
				false);

		Button generateFoodsBtn = (Button) rootView
				.findViewById(R.id.generate_foods);
		generateFoodsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				generateFoods();
			}
		});

		Button deleteFoodsBtn = (Button) rootView
				.findViewById(R.id.delete_foods);
		deleteFoodsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteFoods();
			}
		});

		return rootView;
	}

	private void generateFoods() {

		int size = 1000;

		Log.i(TAG, "Generating " + size + " food");

		List<Food> foods = new ArrayList<Food>(size);
		Random random = new Random();

		for (int i = 0; i < size; i++) {
			Food food = new Food();
			food.setName(generateName(random));
			food.setQuantity((float) Math.round(random.nextFloat() * 100 * 1000) / 1000);
			food.setCarbonhydrate((float) Math.round(random.nextFloat() * 10 * 1000) / 1000);
			food.setUnit(generateUnit(random));
			foods.add(food);
		}
		Log.i(TAG, "" + size + " foods generated");

		new GluCalcSQLiteHelper(context).store(foods);

		Log.i(TAG, "" + size + " foods stored");
	}

	private String generateName(Random random) {
		StringBuilder name = new StringBuilder();
		int size = random.nextInt(5) + 2;
		for (int i = 0; i < size; i++) {
			name.append((char) (random.nextInt(26) + 'a'));
		}
		name.append(" blabla food ");
		return name.toString();
	}

	private String generateUnit(Random random) {
		switch (random.nextInt(5)) {
		case 0:
			return "g";
		case 1:
			return "kg";
		case 2:
			return "l";
		case 3:
			return "ml";
		case 4:
			return "pce";
		default:
			return "xxx";
		}

	}

	private void deleteFoods() {
		new GluCalcSQLiteHelper(context).deleteFoods();
	}

}

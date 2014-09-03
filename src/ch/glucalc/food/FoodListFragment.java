package ch.glucalc.food;

import static ch.glucalc.food.FoodConstants.REQUEST_EDIT_CODE;
import static ch.glucalc.food.FoodConstants.RESULT_CODE_EDITED;
import static ch.glucalc.food.category.CategoryFoodConstants.REQUEST_CREATE_CODE;
import static ch.glucalc.food.category.CategoryFoodConstants.RESULT_CODE_CREATED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.FoodAdapter.Section;

public class FoodListFragment extends ListFragment {

  private static String TAG = "GluCalc";

  private final FoodAdapter adapter = new FoodAdapter();
  private GestureDetector mGestureDetector;
  private List<Object> rows;
  private final List<Object[]> alphabet = new ArrayList<Object[]>();
  private final HashMap<String, Integer> sections = new HashMap<String, Integer>();
  private int sideIndexHeight;
  private static float sideIndexX;
  private static float sideIndexY;
  private int indexListSize;

  class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      // we know already coordinates of first touch
      // we know as well a scroll distance
      sideIndexX = sideIndexX - distanceX;
      sideIndexY = sideIndexY - distanceY;
      // when the user scrolls within our side index
      // we can show for every position in it a proper
      // item in the country list
      if (sideIndexX >= 0 && sideIndexY >= 0) {
        displayListItem();
      }
      return super.onScroll(e1, e2, distanceX, distanceY);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("FoodListFragment.onCreate");
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.options_menu, menu);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.list_alphabet, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initList();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.add:
      final Intent createFoodIntent = new Intent(getActivity().getApplicationContext(), EditFoodActivity.class);
      // Un résultat est attendu pour savoir si la catégorie a été crée
      startActivityForResult(createFoodIntent, FoodConstants.REQUEST_CREATE_CODE);
      return true;
    case R.id.search:
      Toast.makeText(getActivity(), "You have clicked on Search Button", Toast.LENGTH_SHORT).show();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    log("FoodListFragment.onListItemClick");
    final Object currentRow = getListView().getItemAtPosition(position);
    if (currentRow instanceof Food) {
      final Food currentFood = (Food) currentRow;
      final Intent editFoodIntent = new Intent(getActivity().getApplicationContext(), EditFoodActivity.class);
      editFoodIntent.putExtra(FoodConstants.FOOD_ID_PARAMETER, currentFood.getId());
      editFoodIntent.putExtra(FoodConstants.FOOD_NAME_PARAMETER, currentFood.getName());
      editFoodIntent.putExtra(FoodConstants.FOOD_CARBONHYDRATE_PARAMETER, currentFood.getCarbonhydrate());
      editFoodIntent.putExtra(FoodConstants.FOOD_QUANTITY_PARAMETER, currentFood.getQuantity());
      editFoodIntent.putExtra(FoodConstants.FOOD_UNIT_PARAMETER, currentFood.getUnit());
      editFoodIntent.putExtra(FoodConstants.FOOD_CATEGORY_ID_PARAMETER, currentFood.getCategoryId());

      // Un résultat est attendu pour savoir si l'aliment a été modifiée
      startActivityForResult(editFoodIntent, FoodConstants.REQUEST_EDIT_CODE);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == REQUEST_EDIT_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_EDITED) {
        initList();
      }
    } else if (requestCode == REQUEST_CREATE_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_CREATED) {
        initList();
      }
    }
  }

  @Override
  public void onAttach(Activity activity) {
    log("FoodListFragment.onAttach");
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    log("FoodListFragment.onDetach");
    super.onDetach();
  }

  @Override
  public void onDestroy() {
    log("FoodListFragment.onDestroy");
    super.onDestroy();
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

  private void displayListItem() {

    log("FoodListFragment.displayListItem");

    final LinearLayout sideIndex = (LinearLayout) getActivity().findViewById(R.id.sideIndex);
    sideIndexHeight = sideIndex.getHeight();
    // compute number of pixels for every side index item
    final double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;
    // compute the item index for given event position belongs to
    final int itemPosition = (int) (sideIndexY / pixelPerIndexItem);
    // get the item (we can do it since we know item index)

    if (itemPosition < alphabet.size()) {
      final Object[] indexItem = alphabet.get(itemPosition);
      if (indexItem.length > 0) {
        final Integer subitemPosition = sections.get(indexItem[0]);
        if (subitemPosition != null) {
          getListView().setSelection(subitemPosition);
        }
      }
    }
  }

  private void initList() {
    mGestureDetector = new GestureDetector(getActivity(), new SideIndexGestureListener());
    final List<Food> foods = populateFoods();// populateCountries();
    Collections.sort(foods, new Comparator<Food>() {

      @Override
      public int compare(Food lhs, Food rhs) {
        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
      }
    });

    alphabet.clear();
    sections.clear();

    rows = new ArrayList<Object>();
    int start = 0;
    int end = 0;
    String previousLetter = null;
    Object[] tmpIndexItem = null;
    final Pattern numberPattern = Pattern.compile("[0-9]");
    for (final Food food : foods) {
      String firstLetter = food.getName().substring(0, 1).toUpperCase();
      // Group numbers together in the scroller
      if (numberPattern.matcher(firstLetter).matches()) {
        firstLetter = "#";
      }
      // If we've changed to a new letter, add the previous letter to the
      // alphabet scroller
      if (previousLetter != null && !firstLetter.equals(previousLetter)) {
        end = rows.size() - 1;
        tmpIndexItem = new Object[3];
        tmpIndexItem[0] = previousLetter;
        tmpIndexItem[1] = start;
        tmpIndexItem[2] = end;
        alphabet.add(tmpIndexItem);
        start = end + 1;
      }
      // Check if we need to add a header row
      if (!firstLetter.equals(previousLetter)) {
        rows.add(new Section(firstLetter));
        sections.put(firstLetter, start);
      }
      // Add the item to the list
      rows.add(food);
      previousLetter = firstLetter;
    }
    if (previousLetter != null) {
      // Save the last letter
      tmpIndexItem = new Object[3];
      tmpIndexItem[0] = previousLetter;
      tmpIndexItem[1] = start;
      tmpIndexItem[2] = rows.size() - 1;
      alphabet.add(tmpIndexItem);
    }
    adapter.setRows(rows);
    setListAdapter(adapter);

    getView().setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
          return true;
        } else {
          return false;
        }
      }
    });

    updateList();
  }

  private List<Food> populateFoods() {
    final List<Food> foods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
        .loadFoods();
    return foods;
  }

  private void updateList() {

    log("FoodListFragment.updateList");

    final LinearLayout sideIndex = (LinearLayout) getActivity().findViewById(R.id.sideIndex);
    sideIndex.removeAllViews();
    indexListSize = alphabet.size();
    if (indexListSize < 1) {
      return;
    }
    final int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
    int tmpIndexListSize = indexListSize;
    while (tmpIndexListSize > indexMaxSize) {
      tmpIndexListSize = tmpIndexListSize / 2;
    }
    double delta;
    if (tmpIndexListSize > 0) {
      delta = indexListSize / tmpIndexListSize;
    } else {
      delta = 1;
    }
    TextView tmpTV;
    for (double i = 1; i <= indexListSize; i = i + delta) {
      final Object[] tmpIndexItem = alphabet.get((int) i - 1);
      final String tmpLetter = tmpIndexItem[0].toString();
      tmpTV = new TextView(getActivity());
      tmpTV.setText(tmpLetter);
      tmpTV.setGravity(Gravity.CENTER);
      tmpTV.setTextSize(15);
      final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT, 1);
      tmpTV.setLayoutParams(params);
      sideIndex.addView(tmpTV);
    }
    sideIndexHeight = sideIndex.getHeight();
    sideIndex.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        // now you know coordinates of touch
        sideIndexX = event.getX();
        sideIndexY = event.getY();
        // and can display a proper item it country list
        displayListItem();
        return false;
      }
    });
  }

  /**
   * Modify the corresponding food of the list if it already exists
   * 
   * @param aFood
   *          - the food to be added to the list
   */
  private void updateFoodList(Food aFood) {
    boolean itemHasBeenReplace = false;
    final ListIterator<Object> listIterator = rows.listIterator();
    while (listIterator.hasNext() && !itemHasBeenReplace) {
      final Object currentObject = listIterator.next();
      if (currentObject instanceof Food) {
        final Food currentFood = (Food) currentObject;
        if (currentFood.getId() == aFood.getId()) {
          listIterator.set(aFood);
          itemHasBeenReplace = true;
        }
      }
    }
    adapter.notifyDataSetChanged();
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }

}

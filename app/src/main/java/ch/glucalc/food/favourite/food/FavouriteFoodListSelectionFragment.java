package ch.glucalc.food.favourite.food;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.food.Food;
import ch.glucalc.food.FoodAdapter;
import ch.glucalc.food.FoodAdapter.Section;
import ch.glucalc.meal.type.MealTypeConstants;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

@SuppressLint("DefaultLocale")
public class FavouriteFoodListSelectionFragment extends ListFragment implements OnQueryTextListener, OnCloseListener {

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
    private SearchView mSearchView;

    private final SelectionBean selectionBean = new SelectionBean();

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


    private OnFavouriteFoodAddition mCallback;

    // Container Activity must implement this interface
    public interface OnFavouriteFoodAddition {

        public void openFavouriteFoodListFragment(long mealTypeId);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFavouriteFoodAddition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFavouriteFoodAddition");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("FavouriteFoodListSelectionFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectionBean.setNumberItemSelected(0);
        selectionBean.setModeMultiSelection(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("FavouriteFoodListSelectionFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        selectionBean.setmMenu(menu);
        inflater.inflate(R.menu.search_food_menu, menu);
        inflater.inflate(R.menu.add_menu, menu);
        MenuItem menuItem =  menu.findItem(R.id.search_food);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        // Place an action bar item for searching.
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);

        initMenuVisibility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log("FavouriteFoodListSelectionFragment.onCreateView");
        return inflater.inflate(R.layout.list_alphabet, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("FavouriteFoodListSelectionFragment.onActivityCreated");
        super.onCreate(savedInstanceState);
        initList(false, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("FavouriteFoodListSelectionFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.add:
                addAction();
                mCallback.openFavouriteFoodListFragment(getMealTypeId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        log("FavouriteFoodListSelectionFragment.onListItemClick");
        final Object currentRow = getListView().getItemAtPosition(position);
        if (currentRow instanceof Food) {
            final Food currentFood = (Food) currentRow;

            if (!currentFood.isSelected()) {
                v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
                currentFood.setSelected(true);
                selectionBean.addOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 1) {
                    selectionBean.getmMenu().findItem(R.id.add).setVisible(true);
                }
            } else {
                v.setBackground(null);
                currentFood.setSelected(false);
                selectionBean.substractOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 0) {
                    selectionBean.getmMenu().findItem(R.id.add).setVisible(false);
                }
            }
        }
    }

    @Override
    public void onDetach() {
        log("FavouriteFoodListSelectionFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        log("FavouriteFoodListSelectionFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        log("FavouriteFoodListSelectionFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("FavouriteFoodListSelectionFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("FavouriteFoodListSelectionFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("FavouriteFoodListSelectionFragment.onStop");
        super.onStart();
    }

    private void displayListItem() {

        log("FavouriteFoodListSelectionFragment.displayListItem");

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

    private void initList(boolean filtered, String nameCriteria) {
        selectionBean.resetIdsToDelete();
        selectionBean.setNumberItemSelected(0);

        mGestureDetector = new GestureDetector(getActivity(), new SideIndexGestureListener());
        final List<Food> foods = populateFoods(filtered, nameCriteria);
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

    private List<Food> populateFoods(boolean filtered, String nameCriteria) {
        final List<Food> result;
        if (!filtered || TextUtils.isEmpty(nameCriteria)) {
            result = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFoods();
        } else {
            result = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
                    .loadFoodsFilteredByName(nameCriteria);
        }
        return result;
    }

    private void updateList() {

        log("FavouriteFoodListSelectionFragment.updateList");

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
     * @param aFood - the food to be added to the list
     */
    @SuppressWarnings("unused")
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

    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        log("FavouriteFoodListSelectionFragment.onQueryTextSubmit");
        // Don't care about this.
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    private void initMenuVisibility() {
        selectionBean.getmMenu().findItem(R.id.search_food).setVisible(true);
        if (selectionBean.getNumberItemSelected() == 0) {
            selectionBean.getmMenu().findItem(R.id.add).setVisible(false);
        } else {
            selectionBean.getmMenu().findItem(R.id.add).setVisible(true);
        }
    }

    private void addAction() {

        for (final Object currentObject : rows) {
            if (currentObject instanceof Food) {
                final Food currentFood = (Food) currentObject;
                if (currentFood.isSelected()) {
                    FavouriteFood newFavouriteFood = new FavouriteFood();
                    newFavouriteFood.setMealTypeId(getMealTypeId());
                    newFavouriteFood.setFoodId(currentFood.getId());
                    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).storeFavouriteFood(newFavouriteFood);
                }
            }
        }
    }

    private long getMealTypeId() {
        return getArguments().getLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);
    }
}

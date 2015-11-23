package ch.glucalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.food.EditFoodFragment;
import ch.glucalc.food.Food;
import ch.glucalc.food.FoodConstants;
import ch.glucalc.food.FoodListFragment;
import ch.glucalc.food.category.CategoryFood;
import ch.glucalc.food.category.CategoryFoodConstants;
import ch.glucalc.food.category.CategoryFoodListFragment;
import ch.glucalc.food.category.EditCategoryFoodFragment;
import ch.glucalc.food.favourite.food.EditFavouriteFoodFragment;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.food.favourite.food.FavouriteFoodConstants;
import ch.glucalc.food.favourite.food.FavouriteFoodListFragment;
import ch.glucalc.food.favourite.food.FavouriteFoodListSelectionFragment;
import ch.glucalc.food.favourite.food.FavouriteFoodMealTypeListSelectionFragment;
import ch.glucalc.insulin.InsulinOverviewMealTypeFragment;
import ch.glucalc.insulin.InsulinOverviewMealTypeListSelectionFragment;
import ch.glucalc.meal.EditNewMealFoodFragment;
import ch.glucalc.meal.NewMealConstants;
import ch.glucalc.meal.NewMealFoodDiaryListSelectionFragment;
import ch.glucalc.meal.NewMealFoodListFragment;
import ch.glucalc.meal.NewMealFragment;
import ch.glucalc.meal.NewMealSecondStepFragment;
import ch.glucalc.meal.NewMealThirdStepFragment;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.display.MealDiaryConstants;
import ch.glucalc.meal.display.MealDiaryFirstStepFragment;
import ch.glucalc.meal.display.MealDiarySecondStepFragment;
import ch.glucalc.meal.display.MealDiaryThirdStepFragment;
import ch.glucalc.meal.type.EditMealTypeFragment;
import ch.glucalc.meal.type.MealType;
import ch.glucalc.meal.type.MealTypeConstants;
import ch.glucalc.meal.type.MealTypeListFragment;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;
import static ch.glucalc.food.category.CategoryFoodConstants.REQUEST_EDIT_CODE;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.OnNavigationItemSelected, CategoryFoodListFragment.OnCategoryFoodEdition,
        EditCategoryFoodFragment.OnCategoryFoodSaved, EditFoodFragment.OnFoodSaved, FoodListFragment.OnFoodEdition, EditMealTypeFragment.OnMealTypeSaved, MealTypeListFragment.OnMealTypeEdition,
        FavouriteFoodMealTypeListSelectionFragment.OnMealTypeFavouriteFood, FavouriteFoodListFragment.OnFavouriteFoodAddition, FavouriteFoodListSelectionFragment.OnFavouriteFoodAddition,
        EditFavouriteFoodFragment.OnFavouriteFoodSaved, InsulinOverviewMealTypeListSelectionFragment.OnMealTypeInsulinOverview, NewMealFragment.OnMealTypeInsulinSecondStep,
        NewMealFoodListFragment.OnNewMealFoodEdition, EditNewMealFoodFragment.OnNewMealFoodSaved, NewMealSecondStepFragment.OnNewMealFoodDiaryAddition,
        NewMealFoodDiaryListSelectionFragment.OnNewMealFoodAddition, NewMealSecondStepFragment.OnNewMealThirdStep, NewMealThirdStepFragment.OnNewMealSaved, MealDiaryFirstStepFragment.OnMealDiaryDate, MealDiarySecondStepFragment.OnMealDiary {

    private Toolbar toolbar;
    NavigationDrawerFragment drawerFragment;

    public static EnumBloodGlucose GLOBAL_BLOOD_GLUCOSE = EnumBloodGlucose.MMOL_L;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public ActionBar getSupportedActionBar() {
        return getSupportActionBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "onCreate");
        int count = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(this).loadFavouriteFoods().size();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /* Set up the Toolbar which was previously an actionBar */
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* Set up the navigation bar which is the left menu */
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private List<FavouriteFood> getNewFavouriteFoods(int mealTypeId, List<Integer> foodIds) {
        List<FavouriteFood> result = new ArrayList<>();

        for (Integer foodId : foodIds) {
            FavouriteFood favouriteFood = new FavouriteFood();
            favouriteFood.setMealTypeId(mealTypeId);
            favouriteFood.setFoodId(foodId);
            result.add(favouriteFood);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("MainActivity", "onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToolbarTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public CharSequence getToolbarTitle() {
        return getSupportActionBar().getTitle();
    }

    @Override
    public void startExportActivity() {
        final Intent startIntent = new Intent(this, ExportActivity.class);
        startActivity(startIntent);
    }

    @Override
    public void openNewMealFragment() {
        openFragment(new NewMealFragment(), false);
    }

    @Override
    public void openFoodListFragment() {
        openFragment(new FoodListFragment(), false);
    }

    @Override
    public void openMealTypeListFragment() {
        openFragment(new MealTypeListFragment(), false);
    }

    @Override
    public void openCategoryFoodListFragment() {
        openFragment(new CategoryFoodListFragment(), false);
    }

    @Override
    public void openFavouriteFoodMealTypeListSelectionFragment() {
        openFragment(new FavouriteFoodMealTypeListSelectionFragment(), false);
    }

    @Override
    public void openInsulinOverviewMealTypeListSelectionFragment() {
        openFragment(new InsulinOverviewMealTypeListSelectionFragment(), false);
    }

    @Override
    public void openMealDiaryFirstStepFragment() {
        openFragment(new MealDiaryFirstStepFragment(), false);
    }

    @Override
    public void openEditCategoryFoodFragment(CategoryFood categoryFood) {
        Bundle arguments = new Bundle();

        arguments.putLong(CategoryFoodConstants.REQUEST_CODE, REQUEST_EDIT_CODE);
        arguments.putLong(CategoryFoodConstants.CATEGORY_ID_PARAMETER, categoryFood.getId());
        arguments.putString(CategoryFoodConstants.CATEGORY_NAME_PARAMETER, categoryFood.getName());

        EditCategoryFoodFragment editCategoryFoodFragment = new EditCategoryFoodFragment();
        editCategoryFoodFragment.setArguments(arguments);

        openFragment(editCategoryFoodFragment, true);
    }

    @Override
    public void openEditCategoryFoodFragment() {
        Bundle arguments = new Bundle();
        arguments.putLong(CategoryFoodConstants.CATEGORY_ID_PARAMETER, FAKE_DEFAULT_ID);

        EditCategoryFoodFragment editCategoryFoodFragment = new EditCategoryFoodFragment();
        editCategoryFoodFragment.setArguments(arguments);
        openFragment(editCategoryFoodFragment, true);
    }

    @Override
    public void openEditFoodFragment(Food food) {
        Bundle arguments = new Bundle();

        arguments.putLong(FoodConstants.FOOD_ID_PARAMETER, food.getId());
        arguments.putString(FoodConstants.FOOD_NAME_PARAMETER, food.getName());
        arguments.putFloat(FoodConstants.FOOD_CARBONHYDRATE_PARAMETER, food.getCarbonhydrate());
        arguments.putFloat(FoodConstants.FOOD_QUANTITY_PARAMETER, food.getQuantity());
        arguments.putString(FoodConstants.FOOD_UNIT_PARAMETER, food.getUnit());
        arguments.putLong(FoodConstants.FOOD_CATEGORY_ID_PARAMETER, food.getCategoryId());

        EditFoodFragment editFoodFragment = new EditFoodFragment();
        editFoodFragment.setArguments(arguments);
        openFragment(editFoodFragment, true);
    }

    @Override
    public void openEditFoodFragment() {
        Bundle arguments = new Bundle();
        arguments.putLong(FoodConstants.FOOD_ID_PARAMETER, FAKE_DEFAULT_ID);

        EditFoodFragment editFoodFragment = new EditFoodFragment();
        editFoodFragment.setArguments(arguments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        openFragment(editFoodFragment, true);
    }

    @Override
    public void openEditMealTypeFragment(MealType mealType) {
        Bundle arguments = new Bundle();

        arguments.putLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, mealType.getId());
        arguments.putString(MealTypeConstants.MEAL_TYPE_NAME_PARAMETER, mealType.getName());
        arguments.putFloat(MealTypeConstants.MEAL_TYPE_FOOD_TARGET_PARAMETER, mealType.getFoodTarget());
        arguments.putFloat(MealTypeConstants.MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER,
                mealType.getGlycemiaTarget());
        arguments.putFloat(MealTypeConstants.MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER,
                mealType.getInsulinSensitivity());
        arguments.putFloat(MealTypeConstants.MEAL_TYPE_INSULIN_PARAMETER, mealType.getInsulin());

        EditMealTypeFragment editMealTypeFragment = new EditMealTypeFragment();
        editMealTypeFragment.setArguments(arguments);
        openFragment(editMealTypeFragment, true);
    }

    @Override
    public void openEditMealTypeFragment() {
        Bundle arguments = new Bundle();
        arguments.putLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);

        EditMealTypeFragment editMealTypeFragment = new EditMealTypeFragment();
        editMealTypeFragment.setArguments(arguments);
        openFragment(editMealTypeFragment, true);
    }

    @Override
    public void openFavouriteFoodListFragment(long mealTypeId) {
        Bundle arguments = new Bundle();
        arguments.putLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, mealTypeId);

        FavouriteFoodListFragment favouriteFoodListFragment = new FavouriteFoodListFragment();
        favouriteFoodListFragment.setArguments(arguments);
        openFragment(favouriteFoodListFragment, true);

    }

    @Override
    public void openEditFavouriteFoodFragment(FavouriteFood favouriteFood) {
        Bundle arguments = new Bundle();
        arguments.putLong(FavouriteFoodConstants.FAVOURITE_FOOD_ID_PARAMETER, favouriteFood.getId());

        EditFavouriteFoodFragment editFavouriteFoodFragment = new EditFavouriteFoodFragment();
        editFavouriteFoodFragment.setArguments(arguments);
        openFragment(editFavouriteFoodFragment, true);
    }

    @Override
    public void openFavouriteFoodListSelectionFragment(long mealTypeId) {
        Bundle arguments = new Bundle();
        arguments.putLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, mealTypeId);

        FavouriteFoodListSelectionFragment favouriteFoodListSelectionFragment = new FavouriteFoodListSelectionFragment();
        favouriteFoodListSelectionFragment.setArguments(arguments);
        openFragment(favouriteFoodListSelectionFragment, true);
    }

    @Override
    public void openNewMealFoodDiaryListSelectionFragment(long mealDiaryId) {
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, mealDiaryId);

        NewMealFoodDiaryListSelectionFragment newMealFoodDiaryListSelectionFragment = new NewMealFoodDiaryListSelectionFragment();
        newMealFoodDiaryListSelectionFragment.setArguments(arguments);
        openFragment(newMealFoodDiaryListSelectionFragment, true);
    }

    @Override
    public void openInsulinOverviewMealTypeFragment(long mealTypeId) {
        Bundle arguments = new Bundle();
        arguments.putLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, mealTypeId);

        InsulinOverviewMealTypeFragment insulinOverviewMealTypeFragment = new InsulinOverviewMealTypeFragment();
        insulinOverviewMealTypeFragment.setArguments(arguments);
        openFragment(insulinOverviewMealTypeFragment, true);
    }

    @Override
    public void openNewMealSecondStepFragment(long mealTypeId, float newMealBloodGlucose, boolean favouriteFood) {
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_TYPE_ID_PARAMETER, mealTypeId);
        arguments.putFloat(NewMealConstants.NEW_MEAL_BLOOD_GLUCOSE_PARAMETER, newMealBloodGlucose);
        arguments.putBoolean(NewMealConstants.NEW_MEAL_WITH_FAVOURITE_FOOD_PARAMETER, favouriteFood);

        NewMealSecondStepFragment newMealSecondStepFragment = new NewMealSecondStepFragment();
        newMealSecondStepFragment.setArguments(arguments);
        openFragment(newMealSecondStepFragment, true);
    }

    @Override
    public void openEditNewMealFoodFragment(FoodDiary newMealFood) {
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_FOOD_ID_PARAMETER, newMealFood.getId());

        EditNewMealFoodFragment editNewMealFoodFragment = new EditNewMealFoodFragment();
        editNewMealFoodFragment.setArguments(arguments);
        openFragment(editNewMealFoodFragment, true);
    }

    @Override
    public void openNewMealSecondStepFragment(long mealDiaryId) {
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, mealDiaryId);

        NewMealSecondStepFragment newMealSecondStepFragment = new NewMealSecondStepFragment();
        newMealSecondStepFragment.setArguments(arguments);
        openFragment(newMealSecondStepFragment, true);
    }

    @Override
    public void openNewMealThirdStepFragment(long mealDiaryId) {
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, mealDiaryId);

        NewMealThirdStepFragment newMealThirdStepFragment = new NewMealThirdStepFragment();
        newMealThirdStepFragment.setArguments(arguments);
        openFragment(newMealThirdStepFragment, true);
    }

    @Override
    public void openMealDiarySecondStep(String date) {
        Bundle arguments = new Bundle();
        arguments.putString(MealDiaryConstants.MEAL_DIARY_DATE_PARAMETER, date);
        MealDiarySecondStepFragment mealDiarySecondStepFragment = new MealDiarySecondStepFragment();
        mealDiarySecondStepFragment.setArguments(arguments);
        openFragment(mealDiarySecondStepFragment, true);
    }

    @Override
    public void openMealDiaryThirdStep(long mealDiaryId) {
        Bundle arguments = new Bundle();
        arguments.putLong(MealDiaryConstants.MEAL_DIARY_ID_PARAMETER, mealDiaryId);
        MealDiaryThirdStepFragment mealDiaryThirdStepFragment = new MealDiaryThirdStepFragment();
        mealDiaryThirdStepFragment.setArguments(arguments);
        openFragment(mealDiaryThirdStepFragment, true);
    }

    private void openFragment(Fragment fragment, boolean backAvailable) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, fragment);
        if (backAvailable) {
            transaction.addToBackStack(null);
        }

        // Commit the transaction
        transaction.commit();
    }
}

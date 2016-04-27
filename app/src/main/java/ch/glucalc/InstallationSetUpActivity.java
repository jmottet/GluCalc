package ch.glucalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.about.AboutTermsFragment;
import ch.glucalc.configuration.ConditionsGeneralesFragment;
import ch.glucalc.configuration.ConfigurationFirstStepFragment;
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

public class InstallationSetUpActivity extends FragmentActivity implements ConditionsGeneralesFragment.OnConditionsAccepted, ConfigurationFirstStepFragment.OnConfigurationFirstStep {

    private static String TAG = "GluCalc";

    public static String CONDITIONS_GENERALES_ACCEPTED = "CONDITIONS_GENERALES_ACCEPTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SetUpActivity", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.installation_setup_view);
        ConditionsGeneralesFragment conditionsGeneralesFragment = new ConditionsGeneralesFragment();
        conditionsGeneralesFragment.setIsInitialProcess(true);
        openFragment(conditionsGeneralesFragment, true);
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

    @Override
    public void openConfigurationFirstStepFragment() {
        ConfigurationFirstStepFragment configurationFirstStepFragment = new ConfigurationFirstStepFragment();
        configurationFirstStepFragment.setInstallationProcess(true);
        openFragment(configurationFirstStepFragment, true);
    }

    @Override
    public void saveBloodGlucoseUnit(EnumBloodGlucose bloodGlucoseUnit, boolean withRedirectionToNewMeal) {
        GluCalcSQLiteHelper.getGluCalcSQLiteHelper(this).storeParameter(MainActivity.GLOBAL_BLOOD_GLUCOSE_UNIT_KEY, bloodGlucoseUnit.name());
        MainActivity.GLOBAL_BLOOD_GLUCOSE = bloodGlucoseUnit;
    }
}

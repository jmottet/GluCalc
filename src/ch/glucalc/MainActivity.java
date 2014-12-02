package ch.glucalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import ch.glucalc.food.FoodListFragment;
import ch.glucalc.food.category.CategoryFoodListFragment;
import ch.glucalc.meal.type.MealTypeListFragment;

public class MainActivity extends FragmentActivity {

  /* Main categories of left menu */
  private static final int FOOD_MENU_IDX = 0;
  private static final int REPORT_MENU_IDX = 1;
  private static final int SETTINGS_MENU_IDX = 2;
  private static final int EXPORT_MENU_IDX = 3;
  private static final int ABOUT_MENU_IDX = 4;

  /* Sub categories of Report left menu */
  private static final int REPORT_MEAL_DIARY_IDX = 0;
  private static final int REPORT_INSULIN_OVERVIEW_IDX = 1;
  private static final int REPORT_INSULIN_RATIO_IDX = 2;

  /* Sub categories of Settings left menu */
  private static final int SETTINGS_MEALS_AND_INSULIN_IDX = 0;
  private static final int SETTINGS_FAVOURITE_FOODS_IDX = 1;
  private static final int SETTINGS_FOOD_CATEGORIES_IDX = 2;
  private static final int SETTINGS_PASSWORD_LOCK_IDX = 3;
  private static final int SETTINGS_ALERTS_IDX = 4;
  private static final int SETTINGS_INITIAL_SETUP_IDX = 5;
  private static final int SETTINGS_RESET_IDX = 6;

  /* Sub categories of About menu */
  private static final int ABOUT_TERMS_OF_USE_IDX = 0;

  private static final List<Integer> CATEGORIES_WITH_SUBMENUS = Arrays.asList(REPORT_MENU_IDX, SETTINGS_MENU_IDX,
      ABOUT_MENU_IDX);

  private ActionBarDrawerToggle mDrawerToggle;
  private DrawerLayout mDrawerLayout;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;

  ExpandableListAdapter listAdapter;
  ExpandableListView mDrawerList;
  HashMap<String, List<String>> menuListDataChild;
  List<String> menuListDataHeader;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i("MainActivity", "onCreate");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTitle = mDrawerTitle = getTitle();

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ExpandableListView) findViewById(R.id.nav_left_drawer);
    prepareListData();

    listAdapter = new LeftMenuExpandableListAdapter(this, menuListDataHeader, menuListDataChild);
    mDrawerList.setAdapter(listAdapter);
    mDrawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    // ActionBarDrawerToggle ties together the the proper interactions
    // between the sliding drawer and the action bar app icon
    mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
    mDrawerLayout, /* DrawerLayout object */
    R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
    R.string.drawer_open, /* "open drawer" description for accessibility */
    R.string.drawer_close /* "close drawer" description for accessibility */
    ) {
      @Override
      public void onDrawerClosed(View view) {
        getActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        getActionBar().setTitle(mDrawerTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };
    mDrawerLayout.setDrawerListener(mDrawerToggle);

    // enable ActionBar app icon to behave as action to toggle nav drawer
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // hide the application icon in the action bar
    getActionBar().setDisplayShowHomeEnabled(false);

  }

  @Override
  protected void onResume() {
    super.onResume();
    mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        final int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
        parent.setItemChecked(index, true);
        if (!CATEGORIES_WITH_SUBMENUS.contains(groupPosition)) {
          selectItem(groupPosition, -1);
        }

        return false;
      }
    });

    mDrawerList.setOnChildClickListener(new OnChildClickListener() {

      @Override
      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        final int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,
            childPosition));
        parent.setItemChecked(index, true);

        selectItem(groupPosition, childPosition);

        return false;
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_list, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.i("MainActivity", "onOptionsItemSelected");

    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    Log.i("MainActivity", "onPostCreate");
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    Log.i("MainActivity", "onConfigurationChanged");
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public void setTitle(CharSequence title) {
    Log.i("MainActivity", "setTitle");

    mTitle = title;
    getActionBar().setTitle(mTitle);
  }

  private void selectItem(int groupPosition, int childPosition) {
    Log.i("MainActivity", "selectItem");

    if (groupPosition == FOOD_MENU_IDX) {
      openFragment(new FoodListFragment());
    } else if (groupPosition == REPORT_MENU_IDX) {
      switch (childPosition) {
      case REPORT_MEAL_DIARY_IDX:
        break;
      case REPORT_INSULIN_OVERVIEW_IDX:
        break;
      case REPORT_INSULIN_RATIO_IDX:
        break;
      default:
        break;
      }
    } else if (groupPosition == SETTINGS_MENU_IDX) {
      switch (childPosition) {
      case SETTINGS_MEALS_AND_INSULIN_IDX:
        openFragment(new MealTypeListFragment());
        break;
      case SETTINGS_FAVOURITE_FOODS_IDX:
        break;
      case SETTINGS_FOOD_CATEGORIES_IDX:
        openFragment(new CategoryFoodListFragment());
      case SETTINGS_PASSWORD_LOCK_IDX:
        break;
      case SETTINGS_ALERTS_IDX:
        break;
      case SETTINGS_INITIAL_SETUP_IDX:
        break;
      case SETTINGS_RESET_IDX:
        break;
      default:
        break;
      }
    } else if (groupPosition == EXPORT_MENU_IDX) {
      startExportActivity();
      setTitle(menuListDataHeader.get(groupPosition));
    } else if (groupPosition == ABOUT_MENU_IDX) {
      switch (childPosition) {
      case ABOUT_TERMS_OF_USE_IDX:
        break;
      default:
        break;
      }
    }

    String newTitle = null;
    if (CATEGORIES_WITH_SUBMENUS.contains(groupPosition)) {
      newTitle = menuListDataChild.get(menuListDataHeader.get(groupPosition)).get(childPosition);
    } else {
      newTitle = menuListDataHeader.get(groupPosition);
    }
    setTitle(newTitle);

    // update selected item and title, then close the drawer
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  private void startExportActivity() {
    Log.i("MainActivity", "startExportActivity");
    final Intent startIntent = new Intent(this, ExportActivity.class);
    startActivity(startIntent);
  }

  private void prepareListData() {
    Log.i("MainActivity", "prepareListData");

    menuListDataHeader = Arrays.asList(getResources().getStringArray(R.array.left_menu_items_array));
    menuListDataChild = new HashMap<String, List<String>>();

    final List<String> subSettings = Arrays.asList(getResources().getStringArray(
        R.array.left_submenu_settings_items_array));
    final List<String> subReport = Arrays
        .asList(getResources().getStringArray(R.array.left_submenu_report_items_array));
    final List<String> subAbout = Arrays.asList(getResources().getStringArray(R.array.left_submenu_about_items_array));

    menuListDataChild.put(menuListDataHeader.get(FOOD_MENU_IDX), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(REPORT_MENU_IDX), subReport);
    menuListDataChild.put(menuListDataHeader.get(SETTINGS_MENU_IDX), subSettings);
    menuListDataChild.put(menuListDataHeader.get(EXPORT_MENU_IDX), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(ABOUT_MENU_IDX), subAbout);
  }

  private void openFragment(Fragment fragment) {
    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
  }
}

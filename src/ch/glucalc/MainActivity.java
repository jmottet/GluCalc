package ch.glucalc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  private static final int NB_FRAGMENTS = 3;

  private final Fragment[] fFragments = new Fragment[NB_FRAGMENTS];

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

    // Set up the action bar.
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    actionBar.addTab(actionBar.newTab().setText(R.string.tab_new_meal).setTabListener(this)
        .setIcon(R.drawable.ic_tab_new_meal_light));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_food).setTabListener(this)
        .setIcon(R.drawable.ic_tab_food_light));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_menu).setTabListener(this)
        .setIcon(R.drawable.ic_tab_settings_light));

  }

  @Override
  protected void onResume() {
    super.onResume();
    mDrawerList.setOnGroupClickListener(new OnGroupClickListener() {

      @Override
      public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        final int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
        parent.setItemChecked(index, true);
        if (groupPosition != 3) {
          selectItem(groupPosition, -1);
        }

        return false;
      }
    });

    mDrawerList.setOnChildClickListener(new OnChildClickListener() {

      @Override
      public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        Log.d("CHECK", "child clicked");

        final int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,
            childPosition));
        parent.setItemChecked(index, true);

        selectItem(groupPosition, childPosition);

        return false;
      }
    });
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Restore the previously serialized current tab position.
    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    // Serialize the current tab position.
    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main_list, menu);
    return true;
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // When the given tab is selected, show the tab contents in the
    // container view.

    final EnumTab enumTab = EnumTab.getInstanceOf(tab.getPosition());
    switch (enumTab) {
    case TAB_NEW_MEAL:
      tab.setIcon(R.drawable.ic_tab_new_meal_light_selected);
      if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
        mDrawerLayout.closeDrawer(mDrawerList);
      }
      getActionBar().setTitle(mDrawerTitle);
      getFragmentManager().beginTransaction().replace(R.id.container, getFragement(enumTab.getIndex())).commit();
      break;
    case TAB_FOOD:
      tab.setIcon(R.drawable.ic_tab_food_light_selected);
      if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
        mDrawerLayout.closeDrawer(mDrawerList);
      }
      getActionBar().setTitle(mDrawerTitle);
      getFragmentManager().beginTransaction().replace(R.id.container, getFragement(enumTab.getIndex())).commit();
      break;
    default:
      tab.setIcon(R.drawable.ic_tab_settings_light_selected);
      if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
        mDrawerLayout.closeDrawer(mDrawerList);
      } else {
        mDrawerLayout.openDrawer(mDrawerList);
      }
      break;
    }
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    final EnumTab enumTab = EnumTab.getInstanceOf(tab.getPosition());
    switch (enumTab) {
    case TAB_NEW_MEAL:
      tab.setIcon(R.drawable.ic_tab_new_meal_light);
      break;
    case TAB_FOOD:
      tab.setIcon(R.drawable.ic_tab_food_light);
      break;
    default:
      tab.setIcon(R.drawable.ic_tab_settings_light);
      break;
    }
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    final EnumTab enumTab = EnumTab.getInstanceOf(tab.getPosition());
    switch (enumTab) {
    case TAB_MENU:
      if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
        mDrawerLayout.closeDrawer(mDrawerList);
      } else {
        mDrawerLayout.openDrawer(mDrawerList);
      }
      break;
    default:
      break;
    }
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

  /**
   * Retourne un fragment selon sa position. Si celui-ci n'existe pas, il est
   * créé dynamiquement.
   * 
   * @param position
   * @return un fragment
   */
  private Fragment getFragement(int position) {
    final EnumTab enumTab = EnumTab.getInstanceOf(position);
    Fragment fragment = fFragments[position];
    if (fragment == null) {

      switch (enumTab) {
      case TAB_NEW_MEAL:
        fragment = new TestFragment();
        break;
      case TAB_FOOD:
        fragment = new FoodListFragment();
        break;
      case TAB_MENU:
        fragment = new TestFragment();
        break;
      }

      fFragments[position] = fragment;
    }
    return fragment;
  }

  private void selectItem(int groupPosition, int childPosition) {
    Log.i("MainActivity", "selectItem");

    if (groupPosition == 3) {
      switch (childPosition) {
      case 0:
        // position 3/0 => Meals & Insulin
        getFragmentManager().beginTransaction().replace(R.id.container, new MealTypeListFragment()).commit();
        break;
      case 2:
        // position 3/2 => Food categories
        getFragmentManager().beginTransaction().replace(R.id.container, new CategoryFoodListFragment()).commit();
      default:
        break;
      }
      setTitle(menuListDataChild.get(menuListDataHeader.get(groupPosition)).get(childPosition));
    } else {
      switch (groupPosition) {
      case 4:
        // position 4 => Export
        startExportActivity();
        break;
      default:
        break;
      }
      setTitle(menuListDataHeader.get(groupPosition));
    }

    // update selected item and title, then close the drawer

    mDrawerLayout.closeDrawer(mDrawerList);
    getActionBar().selectTab(getActionBar().getTabAt(2));
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

    final List<String> settings = Arrays.asList(getResources()
        .getStringArray(R.array.left_submenu_settings_items_array));
    menuListDataChild.put(menuListDataHeader.get(0), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(1), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(2), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(3), settings);
    menuListDataChild.put(menuListDataHeader.get(4), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(5), new ArrayList<String>());
    menuListDataChild.put(menuListDataHeader.get(6), new ArrayList<String>());
  }
}

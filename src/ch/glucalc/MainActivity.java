package ch.glucalc;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ch.glucalc.food.FoodListFragment;
import ch.glucalc.food.category.CategoryFoodListFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  private static final int NB_FRAGMENTS = 3;

  private final Fragment[] fFragments = new Fragment[NB_FRAGMENTS];

  private String[] mLeftMenuItems;
  private ActionBarDrawerToggle mDrawerToggle;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;

  // private CharSequence mTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mLeftMenuItems = getResources().getStringArray(R.array.left_menu_items_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    // set a custom shadow that overlays the main content when the drawer opens
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    // Set the adapter for the list view
    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mLeftMenuItems));
    // Set the list's click listener
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
        // getActionBar().setTitle(mTitle);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      @Override
      public void onDrawerOpened(View drawerView) {
        // getActionBar().setTitle(mDrawerTitle);
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

    actionBar.addTab(actionBar.newTab().setText(R.string.tab_food).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_categories).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_database).setTabListener(this));

    // final Intent intent = getIntent();
    // if (intent != null &&
    // intent.getAction().equals("android.intent.action.VIEW")) {
    // Toast.makeText(getApplicationContext(), R.string.import_csv,
    // Toast.LENGTH_LONG).show();
    // }
  }

  // @Override
  // public void setTitle(CharSequence title) {
  // mTitle = title;
  // getActionBar().setTitle(mTitle);
  // }

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
    final int newPosition = tab.getPosition();
    final Fragment fragment = getFragement(newPosition);
    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // The action bar home/up action should open or close the drawer.
    // ActionBarDrawerToggle will take care of this.
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    // Pass any configuration change to the drawer toggls
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  /**
   * Retourne un fragment selon sa position. Si celui-ci n'existe pas, il est
   * créé dynamiquement.
   * 
   * @param position
   * @return un fragment
   */
  private Fragment getFragement(int position) {
    Fragment fragment = fFragments[position];
    if (fragment == null) {

      switch (position) {
      case 0:
        fragment = new FoodListFragment();
        break;
      case 1:
        fragment = new CategoryFoodListFragment();
        break;
      case 2:
        fragment = new TestFragment();
        break;
      }
      fFragments[position] = fragment;
    }
    return fragment;
  }

  /* The click listner for ListView in the navigation drawer */
  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      selectItem(position);
    }
  }

  private void selectItem(int position) {
    // update the main content by replacing fragments
    // final Fragment fragment = new PlanetFragment();
    // final Bundle args = new Bundle();
    // args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
    // fragment.setArguments(args);
    //
    // final FragmentManager fragmentManager = getFragmentManager();
    // fragmentManager.beginTransaction().replace(R.id.content_frame,
    // fragment).commit();

    // position 6 => Food Export
    if (position == 6) {
      startExportActivity();
    }
    // update selected item and title, then close the drawer
    mDrawerList.setItemChecked(position, true);
    mDrawerLayout.closeDrawer(mDrawerList);
  }

  private void startExportActivity() {
    final Intent startIntent = new Intent(this, ExportActivity.class);
    startActivity(startIntent);
  }
}

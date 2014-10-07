package ch.glucalc;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;
import ch.glucalc.food.FoodListFragment;
import ch.glucalc.food.category.CategoryFoodListFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  private static final int NB_FRAGMENTS = 3;

  private final Fragment[] fFragments = new Fragment[NB_FRAGMENTS];

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Set up the action bar.
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    actionBar.addTab(actionBar.newTab().setText(R.string.tab_food).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_categories).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_database).setTabListener(this));

    final Intent intent = getIntent();
    if (intent != null && intent.getAction().equals("android.intent.action.VIEW")) {
      Toast.makeText(getApplicationContext(), R.string.import_csv, Toast.LENGTH_LONG).show();
    }
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

}

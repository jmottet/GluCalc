package ch.glucalc;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ch.glucalc.food.FoodListFragment;
import ch.glucalc.food.category.CategoryFoodListFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

  private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

  private static final int NB_FRAGMENTS = 4;

  private final Fragment[] fFragments = new Fragment[NB_FRAGMENTS];

  private boolean showAddAndSearchMenu = true;

  private int oldPosition = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Set up the action bar.
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    actionBar.addTab(actionBar.newTab().setText(R.string.tab_food).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_categories).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_dummy).setTabListener(this));
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_database).setTabListener(this));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
    case R.id.add:
      Toast.makeText(MainActivity.this, "You have clicked on Add Button", Toast.LENGTH_SHORT).show();

      return true;
    case R.id.search:
      Toast.makeText(MainActivity.this, "You have clicked on Search Button", Toast.LENGTH_SHORT).show();
      return true;
    default:
      return super.onOptionsItemSelected(item);
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
    getMenuInflater().inflate(R.menu.options_menu, menu);
    getMenuInflater().inflate(R.menu.main_list, menu);

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    final MenuItem itemAdd = menu.findItem(R.id.add);
    final MenuItem itemSearch = menu.findItem(R.id.search);

    if (showAddAndSearchMenu) {
      itemAdd.setVisible(true);
      itemSearch.setVisible(true);
    } else {
      itemAdd.setVisible(false);
      itemSearch.setVisible(false);
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // When the given tab is selected, show the tab contents in the
    // container view.
    final int newPosition = tab.getPosition();
    final Fragment fragment = getFragement(newPosition);
    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    if (oldPosition != newPosition) {
      showAddAndSearchMenu = mustAddAndSearchMenuItemDisplayed(newPosition);
      invalidateOptionsMenu();
      oldPosition = newPosition;
    }
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  /**
   * A dummy fragment representing a section of the app, but that simply
   * displays dummy text.
   */
  public static class DummySectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    public DummySectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      final View rootView = inflater.inflate(R.layout.fragment_dummy, container, false);
      final TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
      dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
      return rootView;
    }
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
      case 2: {
        fragment = new DummySectionFragment();
        final Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
      }
        break;
      case 3:
        fragment = new TestFragment();
        break;
      }
      fFragments[position] = fragment;
    }
    return fragment;
  }

  private boolean mustAddAndSearchMenuItemDisplayed(int position) {
    return position == 0 || position == 1 ? true : false;
  }

}

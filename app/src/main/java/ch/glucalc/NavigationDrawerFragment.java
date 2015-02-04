package ch.glucalc;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {


    /* Main categories of left menu */
    private static final int NEW_MEAL_MENU_IDX = 0;
    private static final int FOOD_MENU_IDX = 1;
    private static final int CHECK_MENU_IDX = 2;
    private static final int SETTINGS_MENU_IDX = 3;
    private static final int EXPORT_MENU_IDX = 4;
    private static final int ABOUT_MENU_IDX = 5;

    /* Sub categories of Check left menu */
    private static final int CHECK_MEAL_DIARY_IDX = 0;
    private static final int CHECK_INSULIN_OVERVIEW_IDX = 1;
    private static final int CHECK_INSULIN_RATIO_IDX = 2;

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

    private static final List<Integer> CATEGORIES_WITH_SUBMENUS = Arrays.asList(CHECK_MENU_IDX, SETTINGS_MENU_IDX,
            ABOUT_MENU_IDX);

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView mDrawerList;
    private HashMap<String, List<String>> menuListDataChild;
    private List<String> menuListDataHeader;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private OnNavigationItemSelected mCallback;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    // Container Activity must implement this interface
    public interface OnNavigationItemSelected {

        public void setToolbarTitle(CharSequence title);

        public CharSequence getToolbarTitle();

        public void startExportActivity();

        public void openNewMealFragment();

        public void openFoodListFragment();

        public void openMealTypeListFragment();

        public void openCategoryFoodListFragment();
    }

    private static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }


    private static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNavigationItemSelected) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationItemSelected");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mTitle = mDrawerTitle = mCallback.getToolbarTitle();
        mDrawerList = (ExpandableListView) getActivity().findViewById(R.id.nav_left_drawer);
        prepareListData();

        listAdapter = new LeftMenuExpandableListAdapter(getActivity(), menuListDataHeader, menuListDataChild);
        mDrawerList.setAdapter(listAdapter);
        mDrawerList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

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

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                final int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,
                        childPosition));
                parent.setItemChecked(index, true);
                selectItem(groupPosition, childPosition);

                return false;
            }
        });


//        set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mCallback.setToolbarTitle(mDrawerTitle);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, String.valueOf(mUserLearnedDrawer));
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mCallback.setToolbarTitle(mTitle);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                if (slideOffset < 0.6) {
//                    toolbar.setAlpha(1-slideOffset);
//                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mCallback.openNewMealFragment();
    }

    private void prepareListData() {

        menuListDataHeader = Arrays.asList(getResources().getStringArray(R.array.left_menu_items_array));
        menuListDataChild = new HashMap<String, List<String>>();

        final List<String> subSettings = Arrays.asList(getResources().getStringArray(
                R.array.left_submenu_settings_items_array));
        final List<String> subReport = Arrays
                .asList(getResources().getStringArray(R.array.left_submenu_report_items_array));
        final List<String> subAbout = Arrays.asList(getResources().getStringArray(R.array.left_submenu_about_items_array));

        menuListDataChild.put(menuListDataHeader.get(NEW_MEAL_MENU_IDX), new ArrayList<String>());
        menuListDataChild.put(menuListDataHeader.get(FOOD_MENU_IDX), new ArrayList<String>());
        menuListDataChild.put(menuListDataHeader.get(CHECK_MENU_IDX), subReport);
        menuListDataChild.put(menuListDataHeader.get(SETTINGS_MENU_IDX), subSettings);
        menuListDataChild.put(menuListDataHeader.get(EXPORT_MENU_IDX), new ArrayList<String>());
        menuListDataChild.put(menuListDataHeader.get(ABOUT_MENU_IDX), subAbout);
    }

    private void selectItem(int groupPosition, int childPosition) {

        if (groupPosition == NEW_MEAL_MENU_IDX) {
            mCallback.openNewMealFragment();
        } else if (groupPosition == FOOD_MENU_IDX) {
            mCallback.openFoodListFragment();
        } else if (groupPosition == CHECK_MENU_IDX) {
            switch (childPosition) {
                case CHECK_MEAL_DIARY_IDX:
                    break;
                case CHECK_INSULIN_OVERVIEW_IDX:
                    break;
                case CHECK_INSULIN_RATIO_IDX:
                    break;
                default:
                    break;
            }
        } else if (groupPosition == SETTINGS_MENU_IDX) {
            switch (childPosition) {
                case SETTINGS_MEALS_AND_INSULIN_IDX:
                    mCallback.openMealTypeListFragment();
                    break;
                case SETTINGS_FAVOURITE_FOODS_IDX:
                    break;
                case SETTINGS_FOOD_CATEGORIES_IDX:
                    mCallback.openCategoryFoodListFragment();
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
            mCallback.startExportActivity();
            // setTitle(menuListDataHeader.get(groupPosition));
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
        mDrawerLayout.closeDrawer(containerView);
    }

    private void setTitle(CharSequence title) {
        Log.i("NavigationDrawerFragment", "setTitle");

        mTitle = title;
        mCallback.setToolbarTitle(mTitle);
    }
}
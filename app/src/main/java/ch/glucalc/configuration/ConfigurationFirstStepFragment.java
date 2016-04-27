package ch.glucalc.configuration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.glucalc.EnumBloodGlucose;
import ch.glucalc.GestureHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.InstallationSetUpActivity;
import ch.glucalc.KeyboardHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;


@SuppressLint("DefaultLocale")
public class ConfigurationFirstStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private OnConfigurationFirstStep mCallback;

    private BloodGlucoseUnitListFragment bloodGlucoseUnitListFragment;

    private boolean isInstallationProcess = false;


    // Container Activity must implement this interface
    public interface OnConfigurationFirstStep {

        void saveBloodGlucoseUnit(EnumBloodGlucose bloodGlucoseUnit, boolean withRedirectionToNewMeal);
    }

    public void setInstallationProcess(boolean isInstallationProcess) {
        this.isInstallationProcess = isInstallationProcess;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("ConfigurationFirstStepFragment.onCreate");
        super.onCreate(savedInstanceState);
        if (!isInstallationProcess) {
            // On désactive le menu car on affiche un titre avec bouton à la place
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("ConfigurationFirstStepFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.accept_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("ConfigurationFirstStepFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                mCallback.saveBloodGlucoseUnit(bloodGlucoseUnitListFragment.getCurrentBloodGlucoseUnit(), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bloodGlucoseUnitListFragment = new BloodGlucoseUnitListFragment();

        View layout = inflater.inflate(R.layout.configuration_first_step, container, false);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.configuration_blood_glucose_unit_container, bloodGlucoseUnitListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        ImageButton saveImageButton = (ImageButton) layout.findViewById(R.id.saveImageButton);

        if (isInstallationProcess) {
            saveImageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCallback.saveBloodGlucoseUnit(bloodGlucoseUnitListFragment.getCurrentBloodGlucoseUnit(), false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.dialog_configuration_meal_type_title);
                            builder.setMessage(R.string.dialog_configuration_meal_type_message)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.dialog_configuration_meal_create, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).storeParameter(InstallationSetUpActivity.CONDITIONS_GENERALES_ACCEPTED, "true");

                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra(MainActivity.REDIRECT_TO_NEW_MEAL_TYPE, true);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(R.string.dialog_configuration_meal_cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).storeParameter(InstallationSetUpActivity.CONDITIONS_GENERALES_ACCEPTED, "true");

                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            intent.putExtra(MainActivity.REDIRECT_TO_NEW_MEAL_TYPE, false);
                                            startActivity(intent);
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        } else {
            TextView warningTextView = (TextView) layout.findViewById(R.id.configuration_first_step_warning_textview);
            warningTextView.setVisibility(View.GONE);

            RelativeLayout titleContainer = (RelativeLayout) layout.findViewById(R.id.configuration_first_step_title_container);
            titleContainer.setVisibility(View.GONE);
        }

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("ConfigurationFirstStepFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnConfigurationFirstStep) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnConfigurationFirstStep");
        }
    }

    @Override
    public void onDestroy() {
        log("ConfigurationFirstStepFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("ConfigurationFirstStepFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("ConfigurationFirstStepFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("ConfigurationFirstStepFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("ConfigurationFirstStepFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("ConfigurationFirstStepFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

package ch.glucalc.configuration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import ch.glucalc.DialogHelper;
import ch.glucalc.ImportActivity;
import ch.glucalc.R;

@SuppressLint("DefaultLocale")
public class ResetFragment extends Fragment {

    private static String TAG = "GluCalc";

    private boolean resetDiaries = false;

    private boolean resetMeals = false;

    private boolean resetFoods= false;

    private OnResetRequest mCallback;

    public interface OnResetRequest {

        void reset(boolean resetFoods, boolean resetMeals, boolean resetDiaries, boolean loadDefaultFoods);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnResetRequest) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnResetRequest");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("ResetFragment.onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.reset_view, container, false);

        final CheckBox checkBoxFoods = (CheckBox) layout.findViewById(R.id.checkbox_foods);
        checkBoxFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFoods = checkBoxFoods.isChecked();
            }
        });

        final CheckBox checkBoxMeals = (CheckBox) layout.findViewById(R.id.checkbox_meals);
        checkBoxMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMeals = checkBoxMeals.isChecked();
            }
        });


        final CheckBox checkBoxDiaries = (CheckBox) layout.findViewById(R.id.checkbox_diaries);
        checkBoxDiaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDiaries = checkBoxDiaries.isChecked();
            }
        });

        Button resetButton = (Button) layout.findViewById(R.id.reset_button);

            resetButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (resetFoods) {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                        // set title
                        alertDialogBuilder.setTitle(R.string.dialog_confirm_title);

                        // set dialog message
                        alertDialogBuilder.setMessage(R.string.dialog_reload_default_food).setCancelable(false)
                                .setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        mCallback.reset(resetFoods, resetMeals, resetDiaries, true);
                                    }
                                }).setNegativeButton(R.string.dialog_button_no, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCallback.reset(resetFoods, resetMeals, resetDiaries, false);
                                DialogHelper.getDialogInfo(getActivity(), getString(R.string.reset_performed_title), getString(R.string.reset_performed)).show();
                                reinitCheckboxes(checkBoxFoods, checkBoxMeals, checkBoxDiaries);

                            }
                        });

                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    } else if (!resetFoods && !resetMeals && !resetDiaries) {
                        DialogHelper.getDialogInfo(getActivity(), getString(R.string.reset_at_least_one_criteria_title), getString(R.string.reset_at_least_one_criteria_message)).show();
                    } else {
                        mCallback.reset(resetFoods, resetMeals, resetDiaries, false);
                        DialogHelper.getDialogInfo(getActivity(), getString(R.string.reset_performed_title), getString(R.string.reset_performed)).show();
                        reinitCheckboxes(checkBoxFoods, checkBoxMeals, checkBoxDiaries);
                    }
                }
            });
        return layout;
    }

    private void reinitCheckboxes(CheckBox checkBoxFoods, CheckBox checkBoxMeals, CheckBox checkBoxDiaries) {
        checkBoxFoods.setChecked(false);
        resetFoods = false;

        checkBoxMeals.setChecked(false);
        resetMeals = false;

        checkBoxDiaries.setChecked(false);
        resetDiaries = false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("ResetFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("ResetFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("ResetFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("ResetFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("ResetFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("ResetFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("ResetFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}

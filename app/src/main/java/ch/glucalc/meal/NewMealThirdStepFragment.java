package ch.glucalc.meal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import ch.glucalc.EnumColor;
import ch.glucalc.ExportNewMealActivity;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;

public class NewMealThirdStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private MealType mealType = null;
    private MealDiary mealDiary = null;
    private boolean alreadyExists = true;

    private TextView carbohydrateTextView;
    private TextView insulinTextView;
    private TextView sendMealStatus;
    private Switch sendMealSwitch;
    private TextView percentageOfDiffenrenceTextView;
    private TextView bolusGivenTextView ;
    private int percentageOfDiffenrence = 0;
    private OnNewMealSaved mCallback;

    private static int ORANGE_COLOR = Color.parseColor("#FF6600");
    private static int GREEN_COLOR = Color.parseColor("#669900");
    private static int RED_COLOR = Color.parseColor("#FF0000");

    // Container Activity must implement this interface
    public interface OnNewMealSaved {

        void openNewMealFragment();

    }

    // Container Activity must implement this interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNewMealSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewMealSaved");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealDiary = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealDiary(getNewMealDiaryId());
        mealType =  GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealType(mealDiary.getMealTypeId());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("NewMealThirdStepFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("NewMealThirdStepFragment.onCreate");
        View layout = inflater.inflate(R.layout.new_meal_third_step_view, container, false);
        initFields(layout);

        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("NewMealSecondStepFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                // On sauve les données et on passe à l'écran 3 de la prise de repas

                mealDiary.setBolusGiven(Float.valueOf(bolusGivenTextView.getText().toString()));
                mealDiary.setTemporary(false);
                GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateMealDiary(mealDiary);

                if (sendMealSwitch.isChecked()) {
                    final Intent startIntent = new Intent(getActivity(), ExportNewMealActivity.class);
                    startIntent.putExtra(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, mealDiary.getId());
                    startActivity(startIntent);
                }


                mCallback.openNewMealFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        log("NewMealThirdStepFragment.onDestroy");
        super.onDestroy();
    }

    private void initFields(View layout) {
        log("NewMealThirdStepFragment.initFieldsAndButtonForEdition");

        carbohydrateTextView = (TextView) layout.findViewById(R.id.new_meal_third_step_carbohydrate_value_textview);
        if (mealDiary.getCarbohydrateTotal() > mealType.getFoodTarget() + 5) {
            carbohydrateTextView.setBackgroundColor(ORANGE_COLOR);
        } else {
            carbohydrateTextView.setBackgroundColor(GREEN_COLOR);
        }

        carbohydrateTextView.setText(format(mealDiary.getCarbohydrateTotal()) + " " + getCarbohydrateUnit());

        insulinTextView = (TextView) layout.findViewById(R.id.new_meal_third_step_insulin_value_textview);
        insulinTextView.setText(format(mealDiary.getBolusCalculated()) + " " + getBolusUnit());
        insulinTextView.setTypeface(null, Typeface.BOLD);

        TextView bloodGlucoseTextView = (TextView) layout.findViewById(R.id.new_meal_third_step_blood_glucose_value_textview);
        bloodGlucoseTextView.setText(format(mealDiary.getGlycemiaMeasured()) + " " + getBloodGlucoseUnit());

        EnumColor color = MainActivity.GLOBAL_BLOOD_GLUCOSE.getColor(mealDiary.getGlycemiaMeasured());
        switch (color) {
            case GREEN:
                bloodGlucoseTextView.setBackgroundColor(GREEN_COLOR);
                break;
            case RED:
                bloodGlucoseTextView.setBackgroundColor(RED_COLOR);
                break;
            case ORANGE:
                bloodGlucoseTextView.setBackgroundColor(ORANGE_COLOR);
                break;
        }

        bolusGivenTextView = (TextView) layout.findViewById(R.id.new_meal_third_step_bolus_given_textview);
        bolusGivenTextView .setText(format(mealDiary.getBolusCalculated()));

        percentageOfDiffenrenceTextView = (TextView) layout.findViewById(R.id.new_meal_third_step_percentage_difference_textview);

        SeekBar seekBar = (SeekBar) layout.findViewById((R.id.seek_bolus_adjust));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int stepValue = 5;
                int modulo = progress % stepValue;
                if (modulo == 0) {
                    percentageOfDiffenrence = progress - (seekBar.getMax() / 2);
                } else if (modulo <= 2) {
                    seekBar.setProgress(progress - modulo);
                } else {
                    seekBar.setProgress(progress + (stepValue - modulo));
                }
                progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                float newBolusGiven = mealDiary.getBolusCalculated() + (mealDiary.getBolusCalculated() * percentageOfDiffenrence / 100);
                percentageOfDiffenrenceTextView.setText(format(percentageOfDiffenrence) + " %");
                bolusGivenTextView .setText(format(newBolusGiven));
            }
        });

        sendMealStatus = (TextView) layout.findViewById(R.id.send_meal_status);
        sendMealSwitch = (Switch) layout.findViewById(R.id.send_meal_switch);

        //set the switch to OFF by default
        sendMealSwitch.setChecked(false);
        //attach a listener to check for changes in state
        sendMealSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    sendMealStatus.setText("ON");
                } else {
                    sendMealStatus.setText("OFF");
                }
            }
        });

        if (sendMealSwitch.isChecked()) {
            sendMealStatus.setText("ON");
        } else {
            sendMealStatus.setText("OFF");
        }
    }

    public String getCarbohydrateUnit() {
        return "[g]";
    }

    public String getBolusUnit() {
        return getResources().getString(R.string.new_meal_third_step_bolus_unit);
    }

    public String getBloodGlucoseUnit() {
        return MainActivity.GLOBAL_BLOOD_GLUCOSE.getLabel();
    }

    private Long getNewMealDiaryId() {
        return getArguments().getLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, NewMealConstants.NEW_MEAL_DIARY_ID_DEFAULT);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",", ".");
    }
}

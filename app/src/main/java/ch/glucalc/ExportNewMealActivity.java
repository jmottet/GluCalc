package ch.glucalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.glucalc.meal.NewMealConstants;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;

import static ch.glucalc.GluCalcSQLiteHelper.*;

public class ExportNewMealActivity extends Activity {

  private static final String EMAIL_MESSAGE_TYPE = "message/rfc822";
  private static final int REQUEST_SHARE_DATA = 1;

  private static SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");
  private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_SHARE_DATA) {
      final String errorMessage = getString(R.string.exported_new_meal_successfully);
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      Intent intent = getIntent();
      Long mealDiaryId = intent.getLongExtra(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, NewMealConstants.NEW_MEAL_DIARY_ID_DEFAULT);

      MealDiary mealDiary = getGluCalcSQLiteHelper(this).loadMealDiary(mealDiaryId);
      MealType mealType = getGluCalcSQLiteHelper(this).loadMealType(mealDiary.getMealTypeId());
      List<FoodDiary> foodDiaries = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(this).loadFoodDiaries(mealDiary.getId());

      exportNewMealByEmail(mealDiary, mealType, foodDiaries);
    } catch (final IOException | ParseException e) {
      final String errorMessage = getString(R.string.exported_food_failed);
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
  }

  private void exportNewMealByEmail(MealDiary mealDiary, MealType mealType, List<FoodDiary> foodDiaries) throws IOException, ParseException {


    final String emailTitle = getString(R.string.export_new_meal_title);
    final String recipient = "", subject = emailTitle;
    final StringBuilder message = new StringBuilder();
    Date dateParsed = inputSdf.parse(mealDiary.getMealDate());

    message.append("######\n");

    message.append(getString(R.string.export_new_meal_field1_date));
    message.append("\t");
    message.append(dateSdf.format(dateParsed));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field2_time));
    message.append("\t");
    message.append(timeSdf.format(dateParsed));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field3_meal));
    message.append("\t");
    message.append(mealType.getName());
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field4_insulin));
    message.append("\t");
    message.append(format(mealDiary.getBolusCalculated()));
    message.append(" ");
    message.append(getString(R.string.export_new_meal_inulin_unit));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field5_insulin_given));
    message.append("\t");
    message.append(format(mealDiary.getBolusGiven()));
    message.append(" ");
    message.append(getString(R.string.export_new_meal_inulin_unit));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field6_carbohydrate));
    message.append("\t");
    message.append(format(mealDiary.getCarbohydrateTotal()));
    message.append(" ");
    message.append(getString(R.string.export_new_meal_carbohydrate_unit));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field7_food_target));
    message.append("\t");
    message.append(format(mealType.getFoodTarget()));
    message.append(" ");
    message.append(getString(R.string.export_new_meal_carbohydrate_unit));
    message.append("\n");

    message.append(getString(R.string.export_new_meal_field8_blood_glucose));
    message.append("\t");
    message.append(format(mealDiary.getGlycemiaMeasured()));
    message.append(" ");
    message.append(getString(R.string.export_new_meal_blood_glucose_unit));
    message.append("\n");

    message.append("\n");
    message.append(getString(R.string.export_new_meal_field9a_food));
    message.append("\t");
    message.append(getString(R.string.export_new_meal_field9b_food_carbohydrate));
    message.append("\n");


    for (FoodDiary foodDiary : foodDiaries) {
      message.append(foodDiary.getFoodName());
      message.append("\t");
      message.append(format(foodDiary.getCarbohydrate()));
      message.append("\n");
    }

    final Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setType(EMAIL_MESSAGE_TYPE);

    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

    final String emailChooserTitle = getString(R.string.export_email_chooser_title);
    startActivityForResult(Intent.createChooser(emailIntent, emailChooserTitle), REQUEST_SHARE_DATA);

  }


  private String format(float number) {
    return String.format("%.2f", number).replaceAll(",", ".");
  }

}

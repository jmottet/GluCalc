package ch.glucalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import ch.glucalc.food.Food;
import ch.glucalc.food.category.CategoryFood;

public class ExportActivity extends Activity {

  private static final String CSV_SEPARATOR = ";";
  private static final String EMAIL_MESSAGE_TYPE = "message/rfc822";
  private static final String EXPORT_FILENAME = "Glucalc_Food.csv.glucalc";
  private static final int REQUEST_SHARE_DATA = 1;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_SHARE_DATA) {
      final String errorMessage = getString(R.string.exported_food_successfully);
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      exportFoodByEmail();
    } catch (final IOException e) {
      final String errorMessage = getString(R.string.exported_food_failed);
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
  }

  private void exportFoodByEmail() throws IOException {
    final Context context = getApplicationContext();
    final String emailTitle = getString(R.string.export_food_title);
    final String recipient = "", subject = emailTitle, message = "";

    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    emailIntent.setType(EMAIL_MESSAGE_TYPE);

    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { recipient });
    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

    // create attachment
    final String filename = EXPORT_FILENAME;

    final File exportGlucalcFile = new File(getExternalCacheDir(), filename);
    exportFoodToCsvFile(context, exportGlucalcFile);

    if (!exportGlucalcFile.exists() || !exportGlucalcFile.canRead()) {
      final String errorMessage = getString(R.string.exported_food_failed);
      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      return;
    }

    final Uri uri = Uri.parse("file://" + exportGlucalcFile.getAbsolutePath());
    emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
    final String emailChooserTitle = getString(R.string.export_email_chooser_title);
    startActivityForResult(Intent.createChooser(emailIntent, emailChooserTitle), REQUEST_SHARE_DATA);

  }

  private void exportFoodToCsvFile(final Context context, final File exportGlucalcFile) throws FileNotFoundException,
      IOException {
    final String firstLine = getFirstLine();
    final String separator = CSV_SEPARATOR;

    final FileOutputStream fOut = new FileOutputStream(exportGlucalcFile);
    fOut.write(firstLine.getBytes());

    final List<Food> foods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).loadFoods();
    for (final Food food : foods) {
      final CategoryFood category = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).loadCategoryOfFood(
          food.getCategoryId());
      final StringBuilder line = new StringBuilder();
      line.append(category.getName()).append(separator);
      line.append(food.getName()).append(separator);
      line.append(food.getCarbonhydrate()).append(separator);
      line.append(food.getQuantity()).append(separator);
      line.append(food.getUnit()).append("\n");
      fOut.write(line.toString().getBytes());
    }
    fOut.close();
  }

  private String getFirstLine() {
    final StringBuilder sb = new StringBuilder();
    sb.append(getString(R.string.export_category_column));
    sb.append(CSV_SEPARATOR);
    sb.append(getString(R.string.export_food_column));
    sb.append(CSV_SEPARATOR);
    sb.append(getString(R.string.export_carbonhydrate_column));
    sb.append(CSV_SEPARATOR);
    sb.append(getString(R.string.export_quantity_column));
    sb.append(CSV_SEPARATOR);
    sb.append(getString(R.string.export_unit_column));
    sb.append("\n");
    return sb.toString();
  }

}

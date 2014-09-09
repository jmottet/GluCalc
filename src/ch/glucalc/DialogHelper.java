package ch.glucalc;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

  public static void displayErrorMessageAllFieldsMissing(Context context) {
    final Builder builder = new AlertDialog.Builder(context);

    // Setting Dialog Title
    builder.setTitle(R.string.dialog_error_title);

    // Setting Dialog Message
    builder.setMessage(R.string.dialog_error_fields_missing);

    // Setting Icon to Dialog if needed
    // alertDialog.setIcon(R.drawable.)

    // Setting OK Button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });

    final AlertDialog dialog = builder.create();
    dialog.show();
  }

  public static void displayErrorMessageCategoriesMissing(Context context) {
    final Dialog dialog = getDialogErrorMessageCategoriesMissing(context);
    dialog.show();
  }

  public static Dialog getDialogErrorMessageCategoriesMissing(Context context) {
    final Builder builder = new AlertDialog.Builder(context);

    // Setting Dialog Title
    builder.setTitle(R.string.dialog_error_title);

    // Setting Dialog Message
    builder.setMessage(R.string.dialog_error_categories_missing);

    // Setting Icon to Dialog if needed
    // alertDialog.setIcon(R.drawable.)

    // Setting OK Button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
      }
    });

    final AlertDialog dialog = builder.create();
    return dialog;
  }
}

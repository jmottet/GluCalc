package ch.glucalc.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * Created by jmottet on 01/12/2015.
 */
public class ColorHelper {

    public static void showBackgroundColor(Activity activity) {
        TypedValue a = new TypedValue();
        activity.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            int mycolor = a.data;
            StringBuilder sb = new StringBuilder();
            sb.append("Alpha : " + Color.alpha(mycolor)); //255 - 48 - 48 - 255
            sb.append("/ Red : " + Color.red(mycolor)); // FF3030FF
            sb.append("/ Green : " + Color.green(mycolor));
            sb.append("/ Blue : " + Color.blue(mycolor));
            Toast.makeText(activity.getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
        } else {
            // windowBackground is not a color, probably a drawable
            Drawable d = activity.getResources().getDrawable(a.resourceId);
            Toast.makeText(activity.getApplicationContext(), "windowBackground is not a color, probably a drawable", Toast.LENGTH_LONG).show();
        }
    }
}

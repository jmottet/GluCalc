package ch.glucalc;

import android.content.Context;
import android.content.res.Resources;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class ImportDefaultHelper {

    public static void importDefaultFoods(Context context, Resources resources) {
        Integer resourceId = null;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase("fr")) {
            resourceId = R.raw.default_foods_fr;
        } else {
            resourceId = R.raw.default_foods_en;
        }
        InputStream is = resources.openRawResource(resourceId);
        try {
            ImportActivity.ImportDatas(context.getApplicationContext(), is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

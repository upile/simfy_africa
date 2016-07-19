package upile.simfy;

import android.app.Activity;

/**
 * Created by upile.milanzi on 7/19/2016.
 */
public class AppData {
    static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        AppData.activity = activity;
    }
}

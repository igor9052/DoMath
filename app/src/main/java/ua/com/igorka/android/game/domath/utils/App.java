package ua.com.igorka.android.game.domath.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import ua.com.igorka.android.game.domath.R;

/**
 * Created by Igor Kuzmenko on 26.01.16.
 *
 */
public class App extends Application {

    private static Context context;
    private static Util util;
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        util = new Util(context);

        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(App.getContext());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public static Context getContext() {
        return context;
    }

    public static Util getUtil() {
        return util;
    }

    public static class Settings {

        private static final String pref_key_game_time = context.getString(R.string.pref_key_name_game_time);
        private static final String pref_key_multiple_numbers = context.getString(R.string.pref_key_mul_numbers);

        private static int gameTime = Integer.parseInt(sharedPreferences.getString(pref_key_game_time,
                context.getResources().getString(R.string.game_time_default_value)));
        public static int getGameTime() {
            return gameTime;
        }

        private static List<Integer> mulNumbers = new ArrayList<>();

        static {
            updateSettings(pref_key_multiple_numbers);
        }

        public static List<Integer> getMultipliers() {
            return mulNumbers;
        }

        public static void updateSettings(String key) {
            Log.i("SETTINGS", "TIME UPDATED");
            gameTime = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_key_name_game_time),
                    context.getResources().getString(R.string.game_time_default_value)));
            if (key != null && key.equals(pref_key_multiple_numbers)) {
                TreeSet<String> values = new TreeSet<>(sharedPreferences.getStringSet(pref_key_multiple_numbers, new TreeSet<String>()));
                mulNumbers.clear();
                for (String value : values) {
                    mulNumbers.add(Integer.valueOf(value));
                }
                Log.i("SETTINGS", "MUL UPDATED");
            }
        }
    }

}

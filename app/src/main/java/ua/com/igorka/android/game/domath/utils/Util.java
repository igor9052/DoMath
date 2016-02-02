package ua.com.igorka.android.game.domath.utils;

import android.content.Context;

import ua.com.igorka.android.game.domath.R;
import ua.com.igorka.android.game.domath.statistics.IGameResult;

/**
 * Created by Igor Kuzmenko on 26.01.16.
 *
 */
public class Util {

    public static final String COMMA_SPACE = ", ";

    private Context context;

    public Util(Context context) {
        this.context = context;
    }

    public String composeStatisticsMessage(IGameResult result) {
        StringBuilder str = new StringBuilder();
        str.append(context.getString(R.string.statistics_message_title)).append("\n\n");
        str.append("Game time: ").append(String.valueOf(result.getTime() / 1000)).append(" seconds").append("\n");
        str.append("Number of correct answers: ").append(String.valueOf(result.numberOfCorrectAnswers())).append("\n");
        str.append("Number of incorrect answers: ").append(String.valueOf(result.numberOfIncorrectAnswers())).append("\n");
        str.append("Multipliers: ").append(result.getDescription());
        return str.toString();
    }

    public String composeStatisticsMessage(IGameResult result, String playerName) {
        StringBuilder str = new StringBuilder(composeStatisticsMessage(result));
        str.insert(context.getString(R.string.statistics_message_title).length(), " " + playerName);
        return str.toString();
    }

}

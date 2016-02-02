package ua.com.igorka.android.game.domath.statistics;

import ua.com.igorka.android.game.domath.utils.App;
import ua.com.igorka.android.game.domath.utils.Util;

/**
 * Created by Igor Kuzmenko on 25.01.16.
 *
 */
public class GameStat {

    private IGameResult mulGameResult;
    private long startTime;
    private int correctAnswers;
    private int inCorrectAnswers;
    private float gameLevel;
    private String multipliers;

    public void runStatistics(float gameLevel) {
        correctAnswers = 0;
        inCorrectAnswers = 0;
        this.gameLevel = gameLevel;
        multipliers = getMultipliers();
        startTime = System.currentTimeMillis();
    }

    public void stopStatistics() {
        long time = System.currentTimeMillis() - startTime;
        mulGameResult = new MulGameResult(gameLevel, multipliers, time, correctAnswers, inCorrectAnswers);
    }

    public void addCorrectAnswer() {
        correctAnswers++;
    }

    public void addIncorrectAnswer() {
        inCorrectAnswers++;
    }

    public IGameResult getMulGameResult() {
        return mulGameResult;
    }

    private String getMultipliers() {
        StringBuilder str = new StringBuilder();
        for (Integer item : App.Settings.getMultipliers()) {
            str.append(item).append(Util.COMMA_SPACE);
        }
        return str.toString().substring(0, str.length() - Util.COMMA_SPACE.length());
    }

}

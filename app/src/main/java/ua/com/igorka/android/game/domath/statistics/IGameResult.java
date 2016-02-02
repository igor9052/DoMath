package ua.com.igorka.android.game.domath.statistics;

import android.os.Parcelable;

/**
 * Created by Igor Kuzmenko on 25.01.16.
 */
public interface IGameResult extends Parcelable {
    /**
     *
     * @return time in milliseconds
     */
    long getTime();

    /**
     *
     * @return number of correct answers
     */
    int numberOfCorrectAnswers();

    /**
     *
     * @return number of incorrect answers
     */
    int numberOfIncorrectAnswers();

    /**
     *
     * @return score
     */
    int getScore();

    /**
     *
     * @return game level
     */
    float getGameLevel();

    /**
     *
     * @return Additional information about results
     */
    String getDescription();

}

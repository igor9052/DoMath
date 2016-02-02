package ua.com.igorka.android.game.domath.common;

import java.util.List;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 */
public interface IQuestion {

    String getQuestion();
    List<String> getAnswers();
    boolean isCorrect(String answer);
    void shuffleAnswers();

}

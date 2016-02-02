package ua.com.igorka.android.game.domath.presenter;

import ua.com.igorka.android.game.domath.common.IQuestion;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 */
public interface IGamePresenter {

    IQuestion nextQuestion();
    void checkAnswer(String answer);
    boolean isCorrectAnswer(String answer);
    void startGame();
    boolean isGameStarted();

}

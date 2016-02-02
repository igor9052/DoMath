package ua.com.igorka.android.game.domath;

import ua.com.igorka.android.game.domath.common.IQuestion;
import ua.com.igorka.android.game.domath.statistics.IGameResult;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 */
public interface IGameView {

    void updateView(IQuestion question);
    void updateTimeCounter(int time);
    void startGame();
    void stopGame(IGameResult result);

    interface ViewStateListener {
        void onResume();
        void onBackPressed();
        void onPause();
    }
}

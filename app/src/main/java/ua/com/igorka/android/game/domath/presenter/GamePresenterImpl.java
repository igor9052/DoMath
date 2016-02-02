package ua.com.igorka.android.game.domath.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import ua.com.igorka.android.game.domath.IGameView;
import ua.com.igorka.android.game.domath.R;
import ua.com.igorka.android.game.domath.activity.ScoreActivity;
import ua.com.igorka.android.game.domath.activity.SettingsActivity;
import ua.com.igorka.android.game.domath.common.CountDownCounter;
import ua.com.igorka.android.game.domath.common.IQuestion;
import ua.com.igorka.android.game.domath.model.GameModel;
import ua.com.igorka.android.game.domath.statistics.GameStat;
import ua.com.igorka.android.game.domath.statistics.ScoreTable;
import ua.com.igorka.android.game.domath.utils.App;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 *
 */
public class GamePresenterImpl implements IGamePresenter, CountDownCounter.OnTimeCounterChangedListener, IGameView.ViewStateListener {

    private static IGamePresenter instance = null;

    private static IGameView view;
    private GameModel gameModel;
    private IQuestion question;
    private CountDownCounter counter;
    private boolean isGameStared = false;
    private boolean isViewResumed = false;
    private int wrongAnswersCounter = 0;
    private GameStat statistics;

    public static synchronized IGamePresenter getInstance(IGameView view) {
        if (instance == null) {
            instance = new GamePresenterImpl();
        }
        GamePresenterImpl.view = view;
        return instance;
    }

    private GamePresenterImpl() {
        gameModel = GameModel.getInstance();
        counter = new CountDownCounter(App.Settings.getGameTime());
        counter.setTimeCounterChangedListener(this);
        statistics = new GameStat();
    }

    @Override
    public IQuestion nextQuestion() {
        return question;
    }

    @Override
    public void checkAnswer(String answer) {
        if (question.isCorrect(answer)) {
            generateNewQuestion();
            counter.addTime(1);
            wrongAnswersCounter = 0;
            statistics.addCorrectAnswer();
            view.updateTimeCounter(counter.getCounterValue());
        }
        else {
            wrongAnswersCounter++;
            statistics.addIncorrectAnswer();
            counter.subTime(2 * wrongAnswersCounter);
            view.updateTimeCounter(counter.getCounterValue());
        }
        question.shuffleAnswers();
        view.updateView(question);
    }

    @Override
    public boolean isCorrectAnswer(String answer) {
        return question.isCorrect(answer);
    }

    @Override
    public void startGame() {
        if (App.Settings.getMultipliers().size() == 0 ) {
            showSettingsWarningDialog();
            return;
        }
        view.updateTimeCounter(counter.getCounterValue());
        if (!isGameStared) {
            isGameStared = true;
            counter.clear();
            counter.startCounter();
            generateNewQuestion();
            statistics.runStatistics(gameModel.getGameLevel());
            view.startGame();
        }
    }

    @Override
    public boolean isGameStarted() {
        return isGameStared;
    }

    private void generateNewQuestion() {
        question = gameModel.getMulQuestion();
    }

    @Override
    public void update(int newTime) {
        view.updateTimeCounter(newTime);
        if (newTime <= 0) {
            stopGame();
        }
    }

    /*Stop game and update view that game has been stopped*/
    private void stopGame() {
        silentStopGame();
        view.stopGame(statistics.getMulGameResult());
        if (!isViewResumed) {
            return;
        }
        if (statistics.getMulGameResult().getScore() > ScoreTable.getInstance().getMinScore()) {
            ((Activity)view).startActivity(ScoreActivity.makeIntent((Activity) view, statistics.getMulGameResult()));
        }
    }

    /*Stop game and don't update view that game has been stopped*/
    private void silentStopGame() {
        counter.stopCounter();
        wrongAnswersCounter = 0;
        isGameStared = false;
        statistics.stopStatistics();
    }

    @Override
    public void onResume() {
        isViewResumed = true;
        if (!isGameStarted()) {
            counter.setInitValue(App.Settings.getGameTime());
            counter.clear();
            view.updateView(null);
        }
        view.updateTimeCounter(counter.getCounterValue());
    }

    @Override
    public void onBackPressed() {
        silentStopGame();
    }

    @Override
    public void onPause() {
        isViewResumed = false;
    }

    private void showSettingsWarningDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder((Activity)view);
        b.setMessage(R.string.please_go_to_settings);
        b.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent((Activity) view, SettingsActivity.class);
                ((Activity) view).startActivity(intent);
            }
        });

        AlertDialog dialog = b.create();

        dialog.show();
    }
}

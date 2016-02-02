package ua.com.igorka.android.game.domath.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ua.com.igorka.android.game.domath.presenter.IGamePresenter;
import ua.com.igorka.android.game.domath.IGameView;
import ua.com.igorka.android.game.domath.R;
import ua.com.igorka.android.game.domath.presenter.GamePresenterImpl;
import ua.com.igorka.android.game.domath.common.IQuestion;
import ua.com.igorka.android.game.domath.statistics.IGameResult;
import ua.com.igorka.android.game.domath.utils.App;

public class GameActivity extends Activity implements IGameView {

    private IGamePresenter presenter;
    private TextView questionView;
    private ProgressBar progressBar;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        questionView = (TextView) findViewById(R.id.question_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(App.Settings.getGameTime());

        answer1Button = (Button) findViewById(R.id.btn_answer1);
        answer2Button = (Button) findViewById(R.id.btn_answer2);
        answer3Button = (Button) findViewById(R.id.btn_answer3);
        answer4Button = (Button) findViewById(R.id.btn_answer4);

        presenter = GamePresenterImpl.getInstance(this);
        questionView.setOnClickListener(onQuestionViewClickListener);
        setButtonsListeners();
        if (presenter.isGameStarted()) {
            questionView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_font_size_big));
            updateView(presenter.nextQuestion());
        }
        else {
            disableButtons();
            progressBar.setProgress(App.Settings.getGameTime());
            //progressBar.setMax(App.Settings.getGameTime());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter instanceof IGameView.ViewStateListener) {
            ((IGameView.ViewStateListener)presenter).onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter instanceof IGameView.ViewStateListener) {
            ((IGameView.ViewStateListener)presenter).onPause();
        }
    }

    private View.OnClickListener onQuestionViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.startGame();
        }
    };

    private View.OnTouchListener onAnswerTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    disableButtonsExceptOne(v);
                    return false;
                case MotionEvent.ACTION_UP:
                    Log.i("ON_TOUCH", "ACTION_UP");
                    enableButtons();
                    return false;
            }
            return false;
        }
    };

    View.OnClickListener onAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("ON_CLICK", "CLICKED");
            presenter.checkAnswer(((Button) v).getText().toString());
        }
    };

    private void disableButtonsExceptOne(View v) {
        disableButtons();
        v.setEnabled(true);
        v.setOnTouchListener(onAnswerTouchListener);
        v.setOnClickListener(onAnswerClickListener);
    }

    private void disableButtons() {
        answer1Button.setEnabled(false);
        answer2Button.setEnabled(false);
        answer3Button.setEnabled(false);
        answer4Button.setEnabled(false);
        answer1Button.setOnTouchListener(null);
        answer2Button.setOnTouchListener(null);
        answer3Button.setOnTouchListener(null);
        answer4Button.setOnTouchListener(null);

    }

    private void enableButtons() {
        answer1Button.setEnabled(true);
        answer2Button.setEnabled(true);
        answer3Button.setEnabled(true);
        answer4Button.setEnabled(true);
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        answer1Button.setOnClickListener(onAnswerClickListener);
        answer2Button.setOnClickListener(onAnswerClickListener);
        answer3Button.setOnClickListener(onAnswerClickListener);
        answer4Button.setOnClickListener(onAnswerClickListener);

        answer1Button.setOnTouchListener(onAnswerTouchListener);
        answer2Button.setOnTouchListener(onAnswerTouchListener);
        answer3Button.setOnTouchListener(onAnswerTouchListener);
        answer4Button.setOnTouchListener(onAnswerTouchListener);
    }

    @Override
    public void updateView(IQuestion question) {
        if (question != null) {
            questionView.setText(question.getQuestion());
            assignAnswerToButton(answer1Button, question.getAnswers().get(0), question.isCorrect(question.getAnswers().get(0)));
            assignAnswerToButton(answer2Button, question.getAnswers().get(1), question.isCorrect(question.getAnswers().get(1)));
            assignAnswerToButton(answer3Button, question.getAnswers().get(2), question.isCorrect(question.getAnswers().get(2)));
            assignAnswerToButton(answer4Button, question.getAnswers().get(3), question.isCorrect(question.getAnswers().get(3)));
        }
    }

    @Override
    public void updateTimeCounter(int time) {
        progressBar.setProgress(time);
    }

    @Override
    public void startGame() {
        enableButtons();
        questionView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.question_font_size_big));
        questionView.setTextColor(getResources().getColor(R.color.colorAccent));
        updateView(presenter.nextQuestion());
    }

    @Override
    public void stopGame(IGameResult result) {
        disableButtons();
        if (result != null) {
            displayStatistics(result);
        }
    }

    private void displayStatistics(IGameResult result) {
        questionView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.statistics_font_size));
        questionView.setTextColor(getResources().getColor(R.color.colorAmber50));
        questionView.setText(App.getUtil().composeStatisticsMessage(result) + "\n\n" +
                        getString(R.string.tap_to_start_new_game));
    }

    private void assignAnswerToButton(Button button, String answer, boolean isCorrect) {
        button.setText(answer);
        if (isCorrect) {
            button.setBackgroundResource(R.drawable.answer_button_green);
        }
        else {
            button.setBackgroundResource(R.drawable.answer_button_red);
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter instanceof IGameView.ViewStateListener) {
            ((IGameView.ViewStateListener)presenter).onBackPressed();
        }
        super.onBackPressed();
    }
}

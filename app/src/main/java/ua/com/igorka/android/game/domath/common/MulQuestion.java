package ua.com.igorka.android.game.domath.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 *
 */
public class MulQuestion implements IQuestion {

    private static final String MUL = "x";
    private int x;
    private int y;
    private List<String> answers;

    public MulQuestion(int x, int y) {
        this.x = x;
        this.y = y;
        answers = new ArrayList<>();
        generateQuestion();
    }

    private void generateQuestion() {
        int result = x * y;
        Random random = new Random();
        answers.add(String.valueOf(result));
        while (answers.size() < 4) {
            int temp = random.nextInt(99) + 1;
            if (!answers.contains(String.valueOf(temp))) {
                answers.add(String.valueOf(temp));
            }
        }
        Collections.shuffle(answers);
    }

    @Override
    public String getQuestion() {
        return String.valueOf(x) + " " + MUL + " " + String.valueOf(y);
    }

    @Override
    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public boolean isCorrect(String answer) {
        return String.valueOf(x*y).equals(answer);
    }

    @Override
    public void shuffleAnswers() {
        Collections.shuffle(answers);
    }


}

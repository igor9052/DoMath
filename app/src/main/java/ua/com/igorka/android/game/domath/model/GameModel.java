package ua.com.igorka.android.game.domath.model;

import java.util.List;
import java.util.Random;

import ua.com.igorka.android.game.domath.common.IQuestion;
import ua.com.igorka.android.game.domath.common.MulQuestion;
import ua.com.igorka.android.game.domath.utils.App;

/**
 * Created by Igor Kuzmenko on 21.01.16.
 */
public class GameModel {

    private List<Integer> xNums;
    private int yMax = 9;
    private Random random;

    private static GameModel instance = null;

    public synchronized static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    private GameModel() {
        random = new Random();
    }

    public IQuestion getMulQuestion() {
        xNums = App.Settings.getMultipliers();
        if (xNums.size() == 0) {
            return new MulQuestion(0, 0);
        }
        int i = random.nextInt(xNums.size());
        int y = random.nextInt(yMax) + 2;
        return new MulQuestion(xNums.get(i), y);
    }

    public float getGameLevel() {
        switch (App.Settings.getMultipliers().size()) {
            case 1: return 1.1f;
            case 2: return 1.2f;
            case 3:
            case 4: return 2.0f;
            case 5:
            case 6: return 3.0f;
            case 7:
            case 8: return 5.0f;
            case 9: return 6.0f;
            default: return 1.0f;
        }
    }
}

package ua.com.igorka.android.game.domath.statistics;

import java.io.Serializable;

/**
 * Created by Igor Kuzmenko on 25.01.16.
 *
 */
public class ScoreTableItem implements Comparable<ScoreTableItem>, Serializable {

    private String name;
    private IGameResult gameResult;

    public ScoreTableItem(String name, IGameResult gameResult) {
        this.name = name;
        this.gameResult = gameResult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IGameResult getGameResult() {
        return gameResult;
    }

    @Override
    public int compareTo(ScoreTableItem another) {
        if (another != null) {
            return this.getGameResult().getScore() - another.getGameResult().getScore();
        }
        else {
            throw new IllegalArgumentException();
        }

    }

}

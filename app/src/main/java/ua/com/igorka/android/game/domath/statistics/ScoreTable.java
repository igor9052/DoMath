package ua.com.igorka.android.game.domath.statistics;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ua.com.igorka.android.game.domath.utils.App;

/**
 * Created by Igor Kuzmenko on 25.01.16.
 *
 */
public class ScoreTable implements Serializable {


    private static final String FILE_NAME = "score_table.txt";
    private static final int MAX_ITEMS = 10;
    private static ScoreTable instance = new ScoreTable();
    private List<ScoreTableItem> scoreItems;
    private Comparator<ScoreTableItem> comparator;

    public static ScoreTable getInstance() {
        return instance;
    }

    private ScoreTable() {
        loadScoreItems();
        comparator = new ScoreItemComparator();
        Collections.sort(scoreItems, comparator);
    }

    private void loadScoreItems() {
        scoreItems = readFromInternalStorage();
    }

    public void addScoreItem(ScoreTableItem item) {
        if (item.getGameResult().getScore() > getMinScore()) {
            addItem(item);
        }
    }

    private void addItem(ScoreTableItem item) {
        if (scoreItems.size() == 10) {
            scoreItems.remove(9);
        }
        scoreItems.add(item);
        Collections.sort(scoreItems, comparator);
        writeToInternalStorage(scoreItems);
    }

    public int getMinScore() {
        if (scoreItems.size() < 10) {
            return -1;
        }
        return scoreItems.get(scoreItems.size() - 1).getGameResult().getScore();
    }

    public List<ScoreTableItem> getScoreItems() {
        return scoreItems;
    }

    private List<ScoreTableItem> readFromInternalStorage() {
        FileInputStream fis = null;
        List<ScoreTableItem> result = new ArrayList<>(MAX_ITEMS);
        try {
            fis = App.getContext().openFileInput(FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fis);
            result = (List<ScoreTableItem>) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            return result;
        } catch (StreamCorruptedException e) {
            return result;
        } catch (IOException e) {
            return result;
        } catch (ClassNotFoundException e) {
            return result;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private boolean writeToInternalStorage(List<ScoreTableItem> tableItems) {
        FileOutputStream fos = null;
        try {
            fos = App.getContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(scoreItems);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private class ScoreItemComparator implements Comparator<ScoreTableItem> {

        @Override
        public int compare(ScoreTableItem lhs, ScoreTableItem rhs) {
            return rhs.getGameResult().getScore() - lhs.getGameResult().getScore();
        }
    }


}

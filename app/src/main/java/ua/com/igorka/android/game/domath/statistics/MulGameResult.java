package ua.com.igorka.android.game.domath.statistics;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Igor Kuzmenko on 25.01.16.
 *
 */
public class MulGameResult implements IGameResult, Comparable<MulGameResult>, Serializable {

    private float gameLevel;
    private int initTime;
    private long time;
    private int numberOfCorrectAnswers;
    private int numberOfIncorrectAnswers;
    private String description;

    public MulGameResult(float gameLevel, String description, long time, int numberOfCorrectAnswers, int numberOfIncorrectAnswers) {
        this.gameLevel = gameLevel;
        this.description = description;
        this.initTime = 0;
        this.time = time;
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
        this.numberOfIncorrectAnswers = numberOfIncorrectAnswers;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public int numberOfCorrectAnswers() {
        return numberOfCorrectAnswers;
    }

    @Override
    public int numberOfIncorrectAnswers() {
        return numberOfIncorrectAnswers;
    }

    @Override
    public int getScore() {
        return (int)(numberOfCorrectAnswers / (numberOfIncorrectAnswers + 1) * (numberOfCorrectAnswers * 100_000 / this.getTime()) * gameLevel);
    }

    @Override
    public float getGameLevel() {
        return gameLevel;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(MulGameResult another) {
        return this.getScore() - another.getScore();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.gameLevel);
        dest.writeInt(this.initTime);
        dest.writeLong(this.time);
        dest.writeInt(this.numberOfCorrectAnswers);
        dest.writeInt(this.numberOfIncorrectAnswers);
        dest.writeString(this.description);
    }

    protected MulGameResult(Parcel in) {
        this.gameLevel = in.readFloat();
        this.initTime = in.readInt();
        this.time = in.readLong();
        this.numberOfCorrectAnswers = in.readInt();
        this.numberOfIncorrectAnswers = in.readInt();
        this.description = in.readString();
    }

    public static final Creator<MulGameResult> CREATOR = new Creator<MulGameResult>() {
        public MulGameResult createFromParcel(Parcel source) {
            return new MulGameResult(source);
        }

        public MulGameResult[] newArray(int size) {
            return new MulGameResult[size];
        }
    };
}

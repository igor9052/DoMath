package ua.com.igorka.android.game.domath.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ua.com.igorka.android.game.domath.R;
import ua.com.igorka.android.game.domath.statistics.IGameResult;
import ua.com.igorka.android.game.domath.statistics.ScoreTable;
import ua.com.igorka.android.game.domath.statistics.ScoreTableItem;
import ua.com.igorka.android.game.domath.utils.App;

public class ScoreActivity extends Activity {

    private static final String EXTRA_SCORE_ITEM = "ua.com.igorka.android.game.domath.activity.ScoreActivity.score_item";
    private ScoreTable scoreTable;
    private IGameResult gameResult;
    private ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ListView listView = (ListView) findViewById(R.id.list);
        scoreTable = ScoreTable.getInstance();
        scoreAdapter = new ScoreAdapter(this, scoreTable.getScoreItems());
        listView.setAdapter(scoreAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_SCORE_ITEM)) {
            gameResult = intent.getParcelableExtra(EXTRA_SCORE_ITEM);
            showTypeNameDialog();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScoreTableItem scoreTableItem = (ScoreTableItem) parent.getItemAtPosition(position);
                showStatisticsDialog(App.getUtil().composeStatisticsMessage(scoreTableItem.getGameResult(), scoreTableItem.getName()));
            }
        });
    }

    public static Intent makeIntent(Context context, IGameResult gameResult) {
        Intent intent = new Intent(context, ScoreActivity.class);
        intent.putExtra(EXTRA_SCORE_ITEM, gameResult);
        return intent;
    }

    private void showStatisticsDialog(String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.score_statistics_view, null);
        TextView textView = (TextView) dialogLayout.findViewById(R.id.score_statistics);
        textView.setText(message);
        b.setView(dialogLayout);
        final AlertDialog dialog = b.create();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**/
    private void showTypeNameDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("New High Score: " + gameResult.getScore());
        b.setMessage("Please, type your name here:");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        float padding = getResources().getDimension(R.dimen.activity_horizontal_margin);
        layout.setPadding((int) padding, 0, (int) padding, 0);
        final EditText input = new EditText(this);
        layout.addView(input);
        b.setView(layout);
        b.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                ScoreTableItem scoreTableItem = new ScoreTableItem(
                        input.getText().toString(),
                        gameResult
                );
                scoreTable.addScoreItem(scoreTableItem);
                scoreAdapter.notifyDataSetChanged();
            }
        });
        b.setNegativeButton("CANCEL", null);
        AlertDialog dialog = b.create();
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        dialog.show();
    }


    private static class ScoreAdapter extends ArrayAdapter<ScoreTableItem> {

        public ScoreAdapter(Context context, List<ScoreTableItem> scoreTableItemList) {
            super(context, 0, scoreTableItemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScoreTableItem scoreTableItem = getItem(position);
            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                view = layoutInflater.inflate(R.layout.score_table_item_view, parent, false);
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView score = (TextView) view.findViewById(R.id.score);
                viewHolder = new ViewHolder(name, score, position);
            }
            else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
                viewHolder.setPosition(position);
            }

            Log.i("ADAPTER", scoreTableItem.getName());
            Log.i("ADAPTER", scoreTableItem.getGameResult().toString());
            Log.i("ADAPTER", "" + scoreTableItem.getGameResult().getScore());

            viewHolder.getName().setText(scoreTableItem.getName());
            viewHolder.getScore().setText("" + scoreTableItem.getGameResult().getScore());
            switch (position) {
                case 0:
                    viewHolder.getName().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_first_font_size));
                    viewHolder.getScore().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_first_font_size));
                    break;
                case 1:
                    viewHolder.getName().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_second_font_size));
                    viewHolder.getScore().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_second_font_size));
                    break;
                case 2:
                    viewHolder.getName().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_third_font_size));
                    viewHolder.getScore().setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.score_item_third_font_size));
                    break;
            }
            view.setTag(viewHolder);
            return view;
        }
    }

    private static class ViewHolder {
        private TextView name;
        private TextView score;
        private int position;

        public ViewHolder(TextView name, TextView score, int position) {
            this.name = name;
            this.score = score;
            this.position = position;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getScore() {
            return score;
        }

        public void setScore(TextView score) {
            this.score = score;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}

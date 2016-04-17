package com.group.msci.puzzlegenerator.maze.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by Filipt on 05/03/2016.
 */
public class SolvedDialog extends Dialog implements View.OnClickListener {

    private Activity parentActivity;
    private TextView notice;
    private int score;

    public SolvedDialog(Activity parentActivity) {
        super(parentActivity);
        score = 0;
        this.parentActivity = parentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_solved_alert);
        notice = (TextView) findViewById(R.id.dialog_info);
        Button solvedBtn = (Button) findViewById(R.id.dialog_ok);
        solvedBtn.setOnClickListener(this);
        updateTextWithScore();
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        parentActivity.finish();
    }

    public void setScore(int score) {
        this.score = score;
    }

    private void updateTextWithScore() {
        String text = String.format("%s\nYour score was: %d.", notice.getText(), score);
        notice.setText(text.toCharArray(), 0, text.length());
    }

}

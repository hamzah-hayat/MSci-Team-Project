package com.group.msci.puzzlegenerator.maze.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.group.msci.puzzlegenerator.R;

/**
 * Created by Filipt on 05/03/2016.
 */
public class SolvedDialog extends Dialog implements View.OnClickListener{

    private Activity parentActivity;

    public SolvedDialog(Activity parentActivity) {
        super(parentActivity);
        this.parentActivity = parentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.maze_solved_alert);
        Button solvedBtn = (Button) findViewById(R.id.dialog_ok);
        setTitle("Solved !");
        solvedBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        parentActivity.finish();
    }

}

package com.group.msci.puzzlegenerator.BallSwitch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Ball;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.BallSwitchObject;
import com.group.msci.puzzlegenerator.BallSwitch.PuzzleObjects.Switch;
import com.group.msci.puzzlegenerator.R;

import java.util.Scanner;

/**
 * This is a class that creates everything necessary for the BallSwitchPuzzleModule
 * Created by Hamzah on 21/01/2016.
 */
public class BallSwitchPuzzleGame extends AppCompatActivity {

    BallSwitchPuzzleModel model;
    BallSwitchPuzzleController controller;
    BallSwitchPuzzleView view;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        model = new BallSwitchPuzzleModel();
        controller = new BallSwitchPuzzleController();
        view = new BallSwitchPuzzleView(this);
        //Now link them together

        model.setController(controller);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        //Fully set up

        view.showMainMenu();
    }
}

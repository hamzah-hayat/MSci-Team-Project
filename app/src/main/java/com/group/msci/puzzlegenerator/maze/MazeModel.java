package com.group.msci.puzzlegenerator.maze;

import java.util.List;

/**
 * Created by filipt on 11/28/15.
 */
public class MazeModel {

    private List<User> users;
    private List<Maze> previouslyCreated;
    private User currentUser;
    private Maze currentMaze;

    public MazeModel() {

    }

    public void addUser(User user) {

    }

    public Maze currentMaze() {

        return new Maze();
    }
}

package com.example.packagingjavaappexample.controller;

import com.example.packagingjavaappexample.entity.Tile;
import com.example.packagingjavaappexample.model.Model;
import com.example.packagingjavaappexample.view.View;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private final Model model;
    private final View view;
    private static final int WINNING_TILE = 2048;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.getScore();
    }

    public void resetGame() {
        model.setMaxTile(2);
        model.setScore(0);
        view.setGameLost(false);
        view.setGameWon(false);
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) resetGame();

        if (!model.canMove()) view.setGameLost(true);

        if (!view.isGameLost() && !view.isGameWon()) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT -> model.left();
                case KeyEvent.VK_RIGHT -> model.right();
                case KeyEvent.VK_UP -> model.up();
                case KeyEvent.VK_DOWN -> model.down();
                case KeyEvent.VK_Z -> model.rollback();
                case KeyEvent.VK_R -> model.randomMove();
                case KeyEvent.VK_A -> model.autoMove();
            }
        }

        if(model.getMaxTile() == WINNING_TILE)
            view.setGameWon(true);

        view.repaint();
    }

    public View getView() {
        return this.view;
    }
}

package com.example.packagingjavaappexample.model;

import com.example.packagingjavaappexample.entity.Tile;
import java.util.*;

public class Model {
    private Tile[][] gameTiles;
    private static final int FIELD_WIDTH = 4;
    private int maxTile = 2;
    private int score = 0;
    private final Stack<Tile[][]> previousStates = new Stack<>();
    private final Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    private void saveState(Tile[][] gameTiles) {
        Tile[][] tempTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempTiles[i][j] = new Tile(gameTiles[i][j].getValue());
            }
        }
        previousStates.push(tempTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.isEmpty() && !previousStates.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    private void addTile() {
        List<Tile> emptyList = getEmptyTiles();
        if (!emptyList.isEmpty()) {
            int index = (int) (emptyList.size() * Math.random());
            Tile tile = emptyList.get(index);
            tile.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyList = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    emptyList.add(gameTiles[i][j]);
                }
            }
        }
        return emptyList;
    }

    public void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChange = false;
        int insertPosition = 0;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (!tiles[i].isEmpty()) {
                if (i != insertPosition) {
                    tiles[insertPosition] = tiles[i];
                    tiles[i] = new Tile();
                    isChange = true;
                }
                insertPosition++;
            }
        }
        return isChange;
    }

    private boolean mergeTiles(Tile[] tiles) {
        LinkedList<Tile> tilesList = new LinkedList<>();
        boolean isChange = false;

        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (tiles[i].isEmpty()) {
                continue;
            }

            if (i < FIELD_WIDTH - 1 && tiles[i].getValue() == tiles[i + 1].getValue()) {
                int updatedValue = tiles[i].getValue() * 2;
                if (updatedValue > maxTile) {
                    maxTile = updatedValue;
                }
                score += updatedValue;
                tilesList.addLast(new Tile(updatedValue));
                tiles[i + 1].setValue(0);
                isChange = true;
            } else {
                tilesList.addLast(new Tile(tiles[i].getValue()));
            }
            tiles[i].setValue(0);
        }

        for (int i = 0; i < tilesList.size(); i++) {
            tiles[i] = tilesList.get(i);
        }

        return isChange;
    }

    public void left() {
        if(isSaveNeeded) {
            saveState(gameTiles);
        }

        boolean moveFlag = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                moveFlag = true;
            }
        }
        if (moveFlag) {
            addTile();
        }

        isSaveNeeded = true;
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public void up() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    public void down() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0 -> left();
            case 1 -> right();
            case 2 -> up();
            case 3 -> down();
            default -> {
            }
        }
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));

        priorityQueue.peek().getMove().move();
    }

    private boolean hasBoardChanged() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].getValue() != previousStates.peek()[i][j].getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        move.move();
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTilesCount(), score, move);
        }
        rollback();
        return moveEfficiency;
    }

    private int getEmptyTilesCount() {
        return getEmptyTiles().size();
    }

    public boolean canMove() {
        if (getEmptyTiles().size() != 0) {
            return true;
        }

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                Tile t = gameTiles[x][y];
                if ((x < FIELD_WIDTH - 1 && t.getValue() == gameTiles[x + 1][y].getValue())
                        || ((y < FIELD_WIDTH - 1) && t.getValue() == gameTiles[x][y + 1].getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void rotate() {
        Tile[][] newGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        int x = 3;
        int y = 0;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                newGameTiles[y][x] = gameTiles[i][j];
                y++;
            }
            x--;
            y = 0;
        }
        gameTiles = newGameTiles;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void setGameTiles(Tile[][] gameTiles) {
        this.gameTiles = gameTiles;
    }

    public int getMaxTile() {
        return maxTile;
    }

    public void setMaxTile(int maxTile) {
        this.maxTile = maxTile;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Stack<Tile[][]> getPreviousStates() {
        return previousStates;
    }

    public Stack<Integer> getPreviousScores() {
        return previousScores;
    }

    public boolean isSaveNeeded() {
        return isSaveNeeded;
    }

    public void setSaveNeeded(boolean saveNeeded) {
        isSaveNeeded = saveNeeded;
    }
}

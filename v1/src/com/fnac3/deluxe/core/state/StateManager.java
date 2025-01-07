package com.fnac3.deluxe.core.state;

public class StateManager {

    private State state;
    private int gameState;

    public enum State {
        MENU,
        LOADING,
        GAME
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
}
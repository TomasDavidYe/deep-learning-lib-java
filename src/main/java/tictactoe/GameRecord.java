package tictactoe;

import java.util.ArrayList;
import java.util.List;

public class GameRecord {
  private char winningSymbol;
  private List<GameState> gameStates;

  public GameRecord(){
    this.winningSymbol = 'N';
    this.gameStates = new ArrayList<>();
  }

  public char getWinningSymbol() {
    return winningSymbol;
  }

  public List<GameState> getGameStates() {
    return gameStates;
  }

  public void setWinningSymbol(char winningSymbol) {
    this.winningSymbol = winningSymbol;
  }

  public void addGameState(GameState gameState){
    gameStates.add(gameState);
  }
}

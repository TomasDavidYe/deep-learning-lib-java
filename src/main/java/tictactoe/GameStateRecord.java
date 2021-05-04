package tictactoe;

public class GameStateRecord {
  private GameState gameState;
  private char winningSymbol;

  public GameStateRecord(GameState gameState, char winningSymbol){
    this.gameState = gameState;
    this.winningSymbol = winningSymbol;
  }

  public char getWinningSymbol() {
    return winningSymbol;
  }

  public GameState getGameState() {
    return gameState;
  }
}

package tictactoe;

public abstract class Player {
   abstract GameState play(GameState gameState);
   protected char playerSymbol;

  public void setPlayerSymbol(char playerSymbol) {
    this.playerSymbol = playerSymbol;
  }
}

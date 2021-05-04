package tictactoe;

import tictactoe.AIPlayer;

import java.util.List;

public class RandomPlayer extends AIPlayer {

  @Override
  public GameState play(GameState gameState){
    List<GameState> possibleMoves = getPossibleMoves(gameState);
    return getRandomMove(possibleMoves);
  }
}

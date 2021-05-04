package tictactoe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import machinelearning.NeuralNetwork;
import org.la4j.Matrix;
import tictactoe.GameState;
import tictactoe.Player;

public class EnhancedAIPlayer extends Player {

  NeuralNetwork net;

  @Override
  public GameState play(GameState gameState){
    List<GameState> possibleMoves = getPossibleMoves(gameState);
    Function<GameState, Double> f = state -> {
      Matrix temp = state.extractFeature();
      return net.applyNetToMatrix(temp).get(0,0);
    };
    return possibleMoves.stream().max(Comparator.comparing(s -> f.apply(s))).get();
  }

  public EnhancedAIPlayer(NeuralNetwork net){
    this.net = net;
  }

  public List<GameState> getPossibleMoves(GameState gameState){
    List<GameState> result = new ArrayList<>();
    for (int i = 0; i<= 2; i++){
      for(int j = 0; j <= 2; j++){
        if(gameState.moveIsValid(i,j)){
          int[][] possibleMap = cloneGameMap(gameState.getGameMap());
          if(this.playerSymbol == 'X') possibleMap[i][j] = 1;
          else possibleMap[i][j] = -1;
          GameState possibleGameState = new GameState(possibleMap);
          result.add(possibleGameState);
        }
      }
    }
    return result;
  }

  public int[][] cloneGameMap(int[][] originalMap){
    int[][] result = new int[3][3];
    for (int i = 0; i<= 2; i++){
      for(int j = 0; j <= 2; j++){
        result[i][j] = originalMap[i][j];
      }
    }
    return result;
  }
}

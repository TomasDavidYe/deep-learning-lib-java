package tictactoe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import machinelearning.NeuralNet;
import org.la4j.Vector;
import utils.DataManager;
import utils.ProjectMath;

public class AIPlayer extends Player {


    double[] weights;
    NeuralNet neuralNet;

    public AIPlayer(){
      weights = new double[19];
    }

    public AIPlayer(double[] weights){
      super();
      this.weights = weights;
    }

    public AIPlayer(NeuralNet net){
      this.neuralNet = net;
    }

    public double hypothesis(int[] features){
      return sigmoid(dotProduct(features,weights));
    }


    public double dotProduct(int[] features, double[] weights){
      int length = features.length;
      double result = 0;
      for(int i = 0; i < length; i++) result += features[i]*weights[i];

      return result;
    }

    public double sigmoid(double x){
      return 1 / (1 + Math.exp(-x));
    }

    public double evaluateWithLinearHypothesis(GameState gameState){
      int[] features = new int[19];
      features[0] = 1;
      int index = 0;
      for(int i = 0; i<3;i++){
        for(int j = 0; j<3; j++){
          index++;
          features[index] = gameState.getValueAt(i,j);
        }
      }


      for(int i = 0; i<3;i++){
        for(int j = 0; j<3; j++){
          index++;
          features[index] = gameState.getValueAt(i,j)*gameState.getValueAt(i,j);
        }
      }
      return hypothesis(features);
    }



    @Override
    public GameState play(GameState gameState){
      List<GameState> possibleMoves = getPossibleMoves(gameState);
      return getBestMoveWithNeuralNet(possibleMoves);
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


    public double evaluateWithNeuralNet(GameState gameState){
      double[] array = DataManager.getFeaturesFromId(gameState.getStateInString());
      Vector feature = Vector.fromArray(array);
      return ProjectMath.applyNetToFeatures(neuralNet, feature);

    }

    public GameState getTheBestMove(List<GameState> possibleMoves){
      return possibleMoves.stream().max(Comparator.comparing(this::evaluateWithLinearHypothesis)).get();
    }

    public GameState getBestMoveWithNeuralNet(List<GameState> possibleMoves){
      return  possibleMoves.stream().max(Comparator.comparing(this::evaluateWithNeuralNet)).get();
    }

    public GameState getRandomMove(List<GameState> possibleMoves){
      int length = possibleMoves.size();
      Random numGen = new Random();
      int randomIndex = numGen.nextInt(length);
      return possibleMoves.get(randomIndex);
    }

}

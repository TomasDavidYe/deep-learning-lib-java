import machinelearning.LabeledResult;
import machinelearning.NetTrainer;
import machinelearning.NeuralNetwork;
import org.la4j.Matrix;
import tictactoe.EnhancedAIPlayer;
import tictactoe.GameHost;
import tictactoe.HumanPlayer;
import utils.DataManager;
import utils.ProjectMath;
import utils.ResourceReader;

import java.util.List;
import java.util.Map;

public class PlayTicTacToe {

  public static final String TABLE_NAME = "GameState3";
  public static final String URL = "jdbc:sqlite:testDb.db";



  public static void main(String[] argv) throws Exception {

      System.out.println("Initializing...");
      int numOfLayers = 5;
      DataManager manager = new DataManager(URL, TABLE_NAME);
      List<LabeledResult> results = manager.getLabeledResultsFromDb('O');
      NetTrainer trainer = new NetTrainer(results);
      Matrix features = trainer.getFeatures();
      Matrix labels = trainer.getLabels();
      int numOfExamples = features.rows();
      int numOfFeatures = features.columns();
      Map<Integer, Matrix> weights = ResourceReader.getWeightsFromFiles(numOfLayers - 1);
      Map<Integer, Matrix> biases = ResourceReader.getBiasesFromFiles(numOfLayers - 1);
      NeuralNetwork net = new NeuralNetwork(5, weights, biases);
      System.out.println("Features shape = [" + features.rows() + " : " + features.columns() + "]");
      System.out.println("Labels shape = [" + labels.rows() + " : " + labels.columns() + "]");
      System.out.println();
      System.out.println();
      Matrix temp = net.applyNetToMatrix(features);
      int rows = temp.rows();
      Matrix halves = Matrix.constant(rows, 1, 0.5);
      Matrix predictions = ProjectMath.applyPredicateElementWise((a, b) -> a >= b, temp, halves);
      Matrix accuracy = ProjectMath.applyPredicateElementWise((a, b) -> a == b, predictions, labels);
      System.out.println("Accuracy =  " + accuracy.sum() / rows + "%");

      HumanPlayer playerX = new HumanPlayer();
      EnhancedAIPlayer playerO = new EnhancedAIPlayer(net);
      GameHost host = new GameHost(playerX, playerO);
      host.playASingleGame();


  }
}

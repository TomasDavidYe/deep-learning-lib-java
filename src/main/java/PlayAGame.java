import machinelearning.LabeledResult;
import machinelearning.NetTrainer;
import machinelearning.NeuralNet;
import org.la4j.Matrix;
import org.la4j.Vector;
import tictactoe.*;
import utils.DataManager;
import utils.ProjectMath;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayAGame {

  public static final String TABLE_NAME = "GameState3";
  public static final String URL = "jdbc:sqlite:testDb.db";
  public static final int NUM_OF_GAMES = 30;

  public static void letPlayerXPractise(int numOfGames) throws SQLException {
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    for(int i = 1; i<= numOfGames; i++) {
      System.out.println("Starting game "+ i + " for player X");
      double[] weightsForX = trainer.getWeightsFromDb('X');
      Player playerX = new AIPlayer(weightsForX);
      Player playerO = new RandomPlayer();
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();
      trainer.storeGameRecord(gameRecord);
      double[] betterWeightsForX = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('X'), weightsForX);
      System.out.println("Learning weights for X");
      trainer.storeWeightsIntoDb('X', betterWeightsForX);
    }
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();
  }

  public static void playAgainstPlayerX() throws SQLException{
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    double[] weightsForX = trainer.getWeightsFromDb('X');
    Player playerX = new AIPlayer(weightsForX);
    Player playerO = new HumanPlayer();
    GameHost gameHost = new GameHost(playerX, playerO);
    GameRecord gameRecord = gameHost.playASingleGame();
    trainer.storeGameRecord(gameRecord);
    System.out.println("Learning weights for X");
    double[] betterWeightsForX = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('X'), weightsForX);
    trainer.storeWeightsIntoDb('X', betterWeightsForX);
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();

  }

  public static void playAgainstPlayerO() throws SQLException{
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    double[] weightsForO = trainer.getWeightsFromDb('O');
    Player playerX = new HumanPlayer();
    Player playerO = new AIPlayer(weightsForO);
    GameHost gameHost = new GameHost(playerX, playerO);
    GameRecord gameRecord = gameHost.playASingleGame();
    trainer.storeGameRecord(gameRecord);
    System.out.println("Learning weights for O");
    double[] betterWeightsForO = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('O'), weightsForO);
    trainer.storeWeightsIntoDb('O', betterWeightsForO);
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();
  }

  public static void letPlayersPractiseTogether(int numOfGames) throws SQLException{
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    for(int i = 1; i<= numOfGames; i++) {
      double[] weightsForX = trainer.getWeightsFromDb('X');
      double[] weightsForO = trainer.getWeightsFromDb('O');
      Player playerX = new AIPlayer(weightsForX);
      Player playerO = new AIPlayer(weightsForO);
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();
      trainer.storeGameRecord(gameRecord);
      System.out.println("Learning weights for X");
      double[] betterWeightsForX = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('X'), weightsForX);
      System.out.println("Learning weights for O");
      double[] betterWeightsForO = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('O'), weightsForO);

      trainer.storeWeightsIntoDb('X', betterWeightsForX);
      trainer.storeWeightsIntoDb('O', betterWeightsForO);
    }
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();
  }

  public static void letPlayerOPractise(int numOfGames) throws  SQLException{
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    for(int i = 1; i<= numOfGames; i++) {
      System.out.println("Starting game "+ i + " for player O");
      double[] weightsForO = trainer.getWeightsFromDb('O');
      Player playerX = new RandomPlayer();
      Player playerO = new AIPlayer(weightsForO);
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();
      trainer.storeGameRecord(gameRecord);
      double[] betterWeightsForO = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('O'), weightsForO);
      System.out.println("Learning weights for O");
      trainer.storeWeightsIntoDb('O', betterWeightsForO);
    }
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();
  }

  public static int totalStatesVisited() throws SQLException {
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    return trainer.totalStates();
  }

  public static int statesWonByX() throws  SQLException{
    DataManager trainer = new DataManager(URL, TABLE_NAME);
    return trainer.totalStatesWonByX();

  }

  public static int statesWonByO() throws  SQLException{
    DataManager trainer = new DataManager(URL, TABLE_NAME);
    return trainer.totalStatesWonByO();
  }

  public static void eachPlayerPracticesOnHisOwn() throws SQLException {
    letPlayerOPractise(NUM_OF_GAMES);
    letPlayerXPractise(NUM_OF_GAMES);
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());

  }

  public static void letRandomPlayersPlayAgainstEachOther() throws SQLException {
    DataManager trainer = new DataManager(URL,TABLE_NAME);
    int victoriesOfX = 0;
    int victoriesOfO = 0;
    int ties = 0;
    for(int i = 1; i<= NUM_OF_GAMES; i++) {
      System.out.println("Playing game number:  " + i);
      Player playerX = new RandomPlayer();
      Player playerO = new RandomPlayer();
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();
      char winningSymbol = gameRecord.getWinningSymbol();
      switch (winningSymbol){
        case 'X': victoriesOfX++;
          break;
        case 'O': victoriesOfO++;
          break;
        case 'N': ties++;
          break;
      }
      trainer.storeGameRecord(gameRecord);
    }
    System.out.println("Total games played in the session =   " + NUM_OF_GAMES);
    System.out.println("Total games won by X =                " + victoriesOfX);
    System.out.println("Total states won by O =               " + victoriesOfO);
    System.out.println("Total ties =                          " + ties);
    System.out.println("Total states visited =  " + totalStatesVisited());
    System.out.println("Total states won by X =  " + statesWonByX());
    System.out.println("Total states won by O =  " + statesWonByO());
    trainer.closeConnection();
  }

  public static void letPlayersLearn() throws SQLException{
    DataManager trainer = new DataManager(URL,TABLE_NAME);

    double[] weightsForX = trainer.getWeightsFromDb('X');
    double[] weightsForO = trainer.getWeightsFromDb('O');

    System.out.println("Learning weights for X");
    double[] betterWeightsForX = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('X'), weightsForX);
    System.out.println("Learning weights for O");
    double[] betterWeightsForO = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('O'), weightsForO);

    trainer.storeWeightsIntoDb('X', betterWeightsForX);
    trainer.storeWeightsIntoDb('O', betterWeightsForO);
  }

  public static void neuralNetPractiseWithRandom() throws Exception{
    double learningRate = 1;
    int numOfIter = 1000;
    DataManager manager = new DataManager(URL,TABLE_NAME);
    List<LabeledResult> results = manager.getLabeledResultsFromDb('O');

    int numberOfLayers = 4;

    //hidden layer sizes
    int layer1Size = 2;
    int layer2Size = 2;
    int layer3Size = 2;
    int layer4Size = 2;


    Map<Integer,Matrix> weights = new HashMap<>();
    Map<Integer, Vector> bias = new HashMap<>();


    bias.put(1, Vector.random(layer1Size,new Random()));
    bias.put(2, Vector.random(layer2Size,new Random()));
    bias.put(3, Vector.random(layer3Size,new Random()));
    bias.put(4, Vector.random(layer4Size,new Random()));



    weights.put(1,Matrix.random(layer1Size,9,new Random()));
    weights.put(2,Matrix.random(layer2Size,layer1Size,new Random() ));
    weights.put(3,Matrix.random(layer3Size,layer2Size, new Random()));
    weights.put(4,Matrix.random(layer4Size,layer3Size, new Random()));


    NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);

    NetTrainer trainer = new NetTrainer(results);
    Matrix features = trainer.getFeatures();
    Matrix labels = trainer.getLabels();
    NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);
    int numOfExamples = results.size();
    int correctPredictionCounter = 0;
    for(int i = 0; i < numOfExamples; i++){
      Vector x = features.getRow(i);
      int y = (int) labels.get(i,0);
      double hThetaX = ProjectMath.applyNetToFeatures(optimized,x);
      if(Math.abs(y-hThetaX) <= 0.5) correctPredictionCounter++;
      System.out.println("On the example  " + i+ "  with label  " + y + "  the model predicts  "+ hThetaX);
    }
    System.out.println("The model predicted  " + correctPredictionCounter +"  out of  " + numOfExamples);
    System.out.println("Overall accuracy of the model is:  "+ ((double)correctPredictionCounter/ (double) numOfExamples) + " %");
    return;
  }


  public static AIPlayer neuralNetPractise() throws Exception{
    double const1 = -1;
    double const2 = -1;
    double const3 = 0.1;
    double const4 = -0.8;
    double const5 = -0.3;
    double const6 = -0.3;
    double const7 = -0.2;
    double const8 = -0.2;

    double learningRate = 100;
    int numOfIter = 100;

    DataManager manager = new DataManager(URL,TABLE_NAME);
    List<LabeledResult> results = manager.getLabeledResultsFromDb('O');

    double[] array10 = new double[10];
    double[] array1 = new double[1];
    int numberOfLayers = 2;

    //hidden layer sizes
    int layer1Size = 10;
    int layer2Size = 1;
    int layer3Size = 2;
    int layer4Size = 2;

    Map<Integer,Matrix> weights = new HashMap<>();
    Map<Integer, Vector> bias = new HashMap<>();


    bias.put(1, Vector.random(layer1Size, new Random()));
    bias.put(2, Vector.random(layer2Size, new Random()));
   // bias.put(3, org.la4j.Vector.constant(layer3Size,const2));
//    bias.put(3, Vector.constant(1,const3));

    weights.put(1,Matrix.random(layer1Size,9, new Random()));
    weights.put(2,Matrix.random(1,layer1Size, new Random()));
  //  weights.put(3,Matrix.constant(layer3Size,layer2Size,const8));
//    weights.put(3,Matrix.constant(1,layer2Size,const6));

    NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);
    NetTrainer trainer = new NetTrainer(results);
    Matrix features = trainer.getFeatures();
    Matrix labels = trainer.getLabels();
    NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);
    int numOfExamples = results.size();
    int correctPredictionCounter = 0;

    for(int i = 0; i < numOfExamples; i++){
      Vector x = features.getRow(i);
      int y = (int) labels.get(i,0);
      double hThetaX = ProjectMath.applyNetToFeatures(optimized,x);
      if(Math.abs(y-hThetaX) <= 0.5) correctPredictionCounter++;
      System.out.println("On the example  " + i+ "  with label  " + y + "  the model predicts  "+ hThetaX);
    }

    System.out.println("The model predicted  " + correctPredictionCounter +"  out of  " + numOfExamples);
    System.out.println("Overall accuracy of the model is:  "+ ((double)correctPredictionCounter/ (double) numOfExamples) + " %");
    return new AIPlayer(optimized);

  }


  public static void main(String[] argv) throws Exception {

      neuralNetPractise();

  }

}

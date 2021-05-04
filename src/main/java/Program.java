import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import machinelearning.LabeledResult;
import machinelearning.NetTrainer;
import machinelearning.NeuralNet;
import machinelearning.NeuralNetwork;
import org.la4j.Matrix;
import org.la4j.Vector;
import tictactoe.*;
import utils.DataManager;
import utils.ProjectMath;
import utils.ResourceReader;

public class Program {

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
    double constant = 1;
    double learningRate = 1;
    int numOfIter = 1000;
    DataManager manager = new DataManager(URL,TABLE_NAME);
    List<LabeledResult> results = manager.getLabeledResultsFromDb('O');
    double[] array10 = new double[10];
    double[] array1 = new double[1];
    int numberOfLayers = 6;

    //hidden layer sizes
    int layer1Size = 2;
    int layer2Size = 2;
    int layer3Size = 2;
    int layer4Size = 2;


    Map<Integer,Matrix> weights = new HashMap<>();
    Map<Integer, org.la4j.Vector> bias = new HashMap<>();


  /*  bias.put(1, org.la4j.Vector.constant(layer1Size,constant));
    bias.put(2, org.la4j.Vector.constant(layer2Size,constant));
    bias.put(3, org.la4j.Vector.constant(1,constant));


    weights.put(1,Matrix.constant(layer1Size,9,constant));
    weights.put(2,Matrix.constant(layer2Size,layer1Size,constant ));
    weights.put(3,Matrix.constant(1,layer2Size,constant));*/

    bias.put(1, org.la4j.Vector.random(layer1Size,new Random()));
    bias.put(2, org.la4j.Vector.random(layer2Size,new Random()));
    bias.put(3, Vector.random(layer3Size,new Random()));
    bias.put(4, Vector.random(layer4Size,new Random()));

    bias.put(5, org.la4j.Vector.random(1, new Random()));


    weights.put(1,Matrix.random(layer1Size,9,new Random()));
    weights.put(2,Matrix.random(layer2Size,layer1Size,new Random() ));
    weights.put(3,Matrix.random(layer3Size,layer2Size, new Random()));
    weights.put(4,Matrix.random(layer4Size,layer3Size, new Random()));
    weights.put(5,Matrix.random(1,layer4Size,new Random()));



    NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);
    NetTrainer trainer = new NetTrainer(results);
    Matrix features = trainer.getFeatures();
    Matrix labels = trainer.getLabels();
    NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);
    int numOfExamples = results.size();
    int correctPredictionCounter = 0;
    for(int i = 0; i < numOfExamples; i++){
      org.la4j.Vector x = features.getRow(i);
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

    double learningRate = 0.3;
    int numOfIter = 100;
    DataManager manager = new DataManager(URL,TABLE_NAME);
    List<LabeledResult> results = manager.getLabeledResultsFromDb('O');
    double[] array10 = new double[10];
    double[] array1 = new double[1];
    int numberOfLayers = 4;

    //hidden layer sizes
    int layer1Size = 30;
    int layer2Size = 2;
    int layer3Size = 2;
    int layer4Size = 2;


    Map<Integer,Matrix> weights = new HashMap<>();
    Map<Integer, org.la4j.Vector> bias = new HashMap<>();


    bias.put(1, org.la4j.Vector.constant(layer1Size,const1));
    bias.put(2, org.la4j.Vector.constant(layer2Size,const2));
   // bias.put(3, org.la4j.Vector.constant(layer3Size,const2));
    bias.put(3, org.la4j.Vector.constant(1,const3));


    weights.put(1,Matrix.constant(layer1Size,9,const4));
    weights.put(2,Matrix.constant(layer2Size,layer1Size,const5));
  //  weights.put(3,Matrix.constant(layer3Size,layer2Size,const8));
    weights.put(3,Matrix.constant(1,layer2Size,const6));





    NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);
    NetTrainer trainer = new NetTrainer(results);
    Matrix features = trainer.getFeatures();
    Matrix labels = trainer.getLabels();
    NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);
    int numOfExamples = results.size();
    int correctPredictionCounter = 0;
    for(int i = 0; i < numOfExamples; i++){
      org.la4j.Vector x = features.getRow(i);
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

    System.out.println("Initializing...");
    int numOfLayers = 5;
    DataManager manager = new DataManager(URL,TABLE_NAME);
    List<LabeledResult> results = manager.getLabeledResultsFromDb('O');
    NetTrainer trainer = new NetTrainer(results);
    Matrix features = trainer.getFeatures();
    Matrix labels = trainer.getLabels();
    int numOfExamples = features.rows();
    int numOfFeatures = features.columns();
    Map<Integer, Matrix> weights = ResourceReader.getWeightsFromFiles(numOfLayers - 1);
    Map<Integer, Matrix> biases = ResourceReader.getBiasesFromFiles(numOfLayers - 1);
    NeuralNetwork net = new NeuralNetwork(5, weights,biases);
    System.out.println("Features shape = [" + features.rows() + " : "+ features.columns()+"]");
    System.out.println("Labels shape = [" + labels.rows() + " : " + labels.columns()+"]");
    System.out.println();
    System.out.println();
    Matrix temp = net.applyNetToMatrix(features);
    int rows = temp.rows();
    Matrix halves = Matrix.constant(rows, 1, 0.5);
    Matrix predictions = ProjectMath.applyPredicateElementWise((a,b) -> a>=b ,temp,halves);
    Matrix accuracy = ProjectMath.applyPredicateElementWise((a,b) -> a == b, predictions, labels);
    System.out.println("Accuracy =  " + accuracy.sum() / rows + "%");

    HumanPlayer playerX = new HumanPlayer();
    EnhancedAIPlayer playerO = new EnhancedAIPlayer(net);
    GameHost host = new GameHost(playerX,playerO);
    host.playASingleGame();


//    System.out.println("Calculating layer 2...");
//    Matrix layer1Input = utils.ProjectMath.appendColumnOfOnes(features);
//    Matrix transformation1 = utils.ProjectMath.appendRow(weights.get(1), biases.get(1).getColumn(0));
//    temp = layer1Input.multiply(transformation1);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    System.out.println("Calculating layer 3...");
//    Matrix layer2input = utils.ProjectMath.appendColumnOfOnes(temp);
//    Matrix transformation2 = utils.ProjectMath.appendRow(weights.get(2), biases.get(2).getColumn(0));
//    temp = layer2input.multiply(transformation2);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    System.out.println("Calculating layer 4...");
//    Matrix layer3input = utils.ProjectMath.appendColumnOfOnes(temp);
//    Matrix transformation3 = utils.ProjectMath.appendRow(weights.get(3), biases.get(3).getColumn(0));
//    temp = layer3input.multiply(transformation3);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    System.out.println("Calculating output layer...");
//    Matrix layer4Input = utils.ProjectMath.appendColumnOfOnes(temp);
//    Matrix transformation4 = utils.ProjectMath.appendRow(weights.get(4), biases.get(4).getColumn(0));
//    temp = layer4Input.multiply(transformation4);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    System.out.println("Output =  ");
//    System.out.println(temp);



//    PrintWriter out = new PrintWriter("./src/main/resources/labels.txt");
//    out.println(labels.toCSV());
//    out.close();
//
//    out = new PrintWriter("./src/main/resources/features.txt");
//    out.println(features.toCSV());
//    out.close();

//here is the calculation

//    Matrix layer1Input = features;
//    Matrix transformation1 = weights.get(1);
//    temp = layer1Input.multiply(transformation1);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    Matrix layer2input = temp;
//    Matrix transformation2 = weights.get(2);
//    temp = layer2input.multiply(transformation2);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    Matrix layer3input = temp;
//    Matrix transformation3 = weights.get(3);
//    temp = layer3input.multiply(transformation3);
//    temp = utils.ProjectMath.sigmoid(temp);
//
//    Matrix layer4Input = temp;
//    Matrix transformation4 = weights.get(4);
//    temp = layer4Input.multiply(transformation4);

//    temp = utils.ProjectMath.sigmoid(temp);








    //Matrix weights = utils.ResourceReader.readMatrixFromCSV("./src/main/resources/weightsAfter.txt");

   /* utils.DataManager manager = new utils.DataManager(URL,TABLE_NAME);
    List<machinelearning.LabeledResult> results = manager.getLabeledResultsFromDb('O');
    machinelearning.NetTrainer trainer = new machinelearning.NetTrainer(results);
    Matrix features = trainer.getFeatures();
    PrintWriter out = new PrintWriter("features.txt");
    out.println(features.toCSV());
    out.close();*/
/*
    Matrix onesMat = Matrix.constant(3,1,1);
    System.out.println(onesMat);*/




 /*   double[] array1 =
        {1,2,3,9};

    org.la4j.Vector vec = org.la4j.Vector.fromArray(array1);
    System.out.println(vec);
    System.out.println(vec.blank());*/




    /*double[] array = {1,2,3};
    org.la4j.Vector vect = org.la4j.Vector.fromArray(array);
    System.out.println(vect);
    System.out.println(vect.toColumnMatrix().transpose());
    System.out.println(vect.toColumnMatrix().transpose().sum());


    */



    /*utils.DataManager manager = new utils.DataManager(URL,TABLE_NAME);
    List<machinelearning.LabeledResult> results = manager.getLabeledResultsFromDb('X');
    machinelearning.NetTrainer trainer = new machinelearning.NetTrainer(results);
    Matrix data = trainer.data;
    System.out.println(data);
    System.out.println();
    System.out.println();
    System.out.println(data.removeLastColumn());
    Matrix label = data.copy();
    for(int i = 0; i < 9; i++) label = label.removeFirstColumn();
    System.out.println(label);
    System.out.println(data);*/



/*

    utils.DataManager trainer = new utils.DataManager(URL,TABLE_NAME);
    List<machinelearning.LabeledResult> labeledResultsForX= trainer.getLabeledResultsFromDb('X');
*/

   /* double[][] array1 = {
        {1,2,3},
        {4,5,6},
        {7,8,9},
    };


    Map<Integer,Matrix> map = new HashMap<>();
    map.put(1,Matrix.from2DArray(array1));
    System.out.println(map.get(1));
    map.get(1).set(1,1,115);
    System.out.println(map.get(1));*/






    /*
    double[][] array2 = {
        {1,1,1}
    };

    double[] array3 = { 1,1,1 };
    Matrix matrix = Matrix.from2DArray(array1);
    Matrix vector1 = Matrix.from2DArray(array2);
    Matrix vector2 = Matrix.from1DArray(3,1,array3);
    System.out.println(vector1);
    System.out.println(vector2);
    Matrix matrix2 = matrix.copy();
    MatrixIterator iterator = matrix2.iterator();
    while (iterator.hasNext()) {
      double x = iterator.next();
      double newX = 3*x;
      iterator.set(newX);
    }
    System.out.println(matrix);
    System.out.println(matrix2);

*/





    /*
    double[] initialWeights = {0,0,0,0,0,0,0,0,0,0};
    trainer.storeWeightsIntoDb('O',initialWeights);
*/







   /* for (int i = 0; i < 10; i++) {
      System.out.println("Weight number "+ i +" has a value of " + betterWeights[i]);
    }
    System.out.println("Cost of new weights =   " + trainer.logisticRegressionCost(results,betterWeights));
*/

/*
    double[] randomWeigths = {1,2,2,5, -5,6,5,8,40,15};
    trainer.storeWeightsIntoDb('X',randomWeigths);
    List<machinelearning.LabeledResult> labeledResultsForX = trainer.getLabeledResultsFromDb('X');*/
/*
    double learningRate = 5;
    double[] initialWeights = new double[10];
    int numberOfIterations = 100;
    double[] optimalWeights = trainer.getOptimalWeights(labeledResultsForX,initialWeights,learningRate,numberOfIterations);
*/





/*

    labeledResultsForO.stream().forEach(result -> {
      String feature = "";
      for(int i = 0; i <= 9; i ++) feature += result.features[i];
      System.out.println("This is the game state:  "+result.id+"  which corresponds to the feature  " +feature+ "  and is labeled with:  "+ result.label);
    });
    System.out.println("We have  "+labeledResultsForO.size() +" labeled examples");
*/



/*
    double[] weights = {1, 2, 3, 4};
    int[] features = {1, 1, -2, 0};
    tictactoe.AIPlayer player = new tictactoe.AIPlayer(weights);
    double dotProduct = player.dotProduct(features,weights);
    System.out.println(dotProduct);*/

/*    String url = "jdbc:sqlite:testDb.db";
    String tableName = "GameStateTest";
    utils.DataManager trainer = new utils.DataManager(url, tableName);
    int[][] someMap = {
        {0,0, 0},
        {0, 1, 0},
        {0,0, -1 }
    };

    tictactoe.GameState state = new tictactoe.GameState(someMap);
    state.printOutState();
    tictactoe.GameStateRecord record = new tictactoe.GameStateRecord(state, 'O');
    trainer.storeSingleGameStateRecord(record);*/


/*
    tictactoe.Player playerX = new tictactoe.AIPlayer(new double[10]);
    Function<Double,Double> sigmoid = ((tictactoe.AIPlayer) playerX)::sigmoid;
    System.out.println("sigmoid(0) = " +sigmoid.apply((double)0));
    System.out.println("sigmoid(1) = " + sigmoid.apply((double)1));
    System.out.println("sigmoid(-1) = "+ sigmoid.apply((double)-1));
    System.out.println("sigmoid(10) = "+ sigmoid.apply((double)10));
    System.out.println("sigmoid(-10) = "+ sigmoid.apply((double)-10));*/


/*

    double[] weightsX = {0,-1,0,1,0,3,0,1,0,-1};
    double[] weightsY = {1,0,-1,0,1,0,-1,0,1,0};
    tictactoe.Player playerX = new tictactoe.AIPlayer(weightsX);
    tictactoe.Player playerY = new tictactoe.AIPlayer(weightsY);
    tictactoe.GameHost gameHost = new tictactoe.GameHost(playerX,playerY);
    tictactoe.GameRecord gameRecord = gameHost.playASingleGame();
    System.out.println("Game Over!");
*/

    /*
    String url = "jdbc:sqlite:testDb.db";
    String tableName = "GameStateTest";
    utils.DataManager trainer = new utils.DataManager(url, tableName);
    System.out.println("Storing the results of the game!");
    trainer.storeGameRecord(gameRecord);
*/
    /*
    String url = "jdbc:sqlite:testDb.db";
    Connection conn = DriverManager.getConnection(url);
    int id = 5;
    String firstName = "Katerina";
    String lastName = "Sirova";
    String position = "Biologist";
    String query = "INSERT INTO tictactoe.GameState VALUES (?,?,?,?)";
    PreparedStatement statement = conn.prepareStatement(query);
    statement.setInt(1,id);
    statement.setString(2,firstName);
    statement.setString(3,lastName);
    statement.setString(4,position);
    statement.execute();
    int id = 8;
    String firstName = "Tommy";
    String lastName = "Gambic";
    String position = "Gymnast";

    String query = "BEGIN "
                     + "IF NOT EXISTS (SELECT * FROM tictactoe.GameState WHERE firstName = ? ) "
                        + "BEGIN "
                            + "INSERT INTO tictactoe.GameState VALUES (?, ?, ?, ?)"
                        + "END "
                + "END";
    PreparedStatement statement = conn.prepareStatement(query);
    statement.setString(1,firstName);
    statement.setInt(2,id);
    statement.setString(3, firstName);
    statement.setString(4,lastName);
    statement.setString(5,position);
    statement.executeUpdate();
*/
  }

}

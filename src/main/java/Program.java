import java.sql.SQLException;

public class Program {

  public static void letPlayerXPractise(int numOfGames, String url, String tableName) throws SQLException {
    ModelTrainer trainer = new ModelTrainer(url,tableName);
    for(int i = 1; i<= numOfGames; i++) {
      double[] weightsForX = trainer.getWeightsFromDb('X');
      Player playerX = new AIPlayer(weightsForX);
      Player playerO = new AIPlayer(new double[10]);
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();
      trainer.storeGameRecord(gameRecord);
      double[] betterWeightsForX = trainer.getOptimalWeights(trainer.getLabeledResultsFromDb('X'), weightsForX);
      trainer.storeWeightsIntoDb('X', betterWeightsForX);
    }
  }

  public static void playAgainstPlayerX(String url, String tableName) throws SQLException{
    ModelTrainer trainer = new ModelTrainer(url,tableName);
    double[] weightsForX = trainer.getWeightsFromDb('X');
    Player playerX = new AIPlayer(weightsForX);
    Player playerO = new HumanPlayer();
    GameHost gameHost = new GameHost(playerX, playerO);
    gameHost.playASingleGame();

  }

  public static void main(String[] argv) throws Exception {

    String url = "jdbc:sqlite:testDb.db";
    String tableName = "GameState2";
    playAgainstPlayerX(url,tableName);
//Unit test material




    /*
    double[] initialWeights = {0,0,0,0,0,0,0,0,0,0};
    trainer.storeWeightsIntoDb('O',initialWeights);
*/


    int numberOfGames = 500;





   /* for (int i = 0; i < 10; i++) {
      System.out.println("Weight number "+ i +" has a value of " + betterWeights[i]);
    }
    System.out.println("Cost of new weights =   " + trainer.logisticRegressionCost(results,betterWeights));
*/

/*
    double[] randomWeigths = {1,2,2,5, -5,6,5,8,40,15};
    trainer.storeWeightsIntoDb('X',randomWeigths);
    List<LabeledResult> labeledResultsForX = trainer.getLabeledResultsFromDb('X');*/
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
    AIPlayer player = new AIPlayer(weights);
    double dotProduct = player.dotProduct(features,weights);
    System.out.println(dotProduct);*/

/*    String url = "jdbc:sqlite:testDb.db";
    String tableName = "GameStateTest";
    ModelTrainer trainer = new ModelTrainer(url, tableName);
    int[][] someMap = {
        {0,0, 0},
        {0, 1, 0},
        {0,0, -1 }
    };

    GameState state = new GameState(someMap);
    state.printOutState();
    GameStateRecord record = new GameStateRecord(state, 'O');
    trainer.storeSingleGameStateRecord(record);*/


/*
    Player playerX = new AIPlayer(new double[10]);
    Function<Double,Double> sigmoid = ((AIPlayer) playerX)::sigmoid;
    System.out.println("sigmoid(0) = " +sigmoid.apply((double)0));
    System.out.println("sigmoid(1) = " + sigmoid.apply((double)1));
    System.out.println("sigmoid(-1) = "+ sigmoid.apply((double)-1));
    System.out.println("sigmoid(10) = "+ sigmoid.apply((double)10));
    System.out.println("sigmoid(-10) = "+ sigmoid.apply((double)-10));*/


/*

    double[] weightsX = {0,-1,0,1,0,3,0,1,0,-1};
    double[] weightsY = {1,0,-1,0,1,0,-1,0,1,0};
    Player playerX = new AIPlayer(weightsX);
    Player playerY = new AIPlayer(weightsY);
    GameHost gameHost = new GameHost(playerX,playerY);
    GameRecord gameRecord = gameHost.playASingleGame();
    System.out.println("Game Over!");
*/

    /*
    String url = "jdbc:sqlite:testDb.db";
    String tableName = "GameStateTest";
    ModelTrainer trainer = new ModelTrainer(url, tableName);
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
    String query = "INSERT INTO GameState VALUES (?,?,?,?)";
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
                     + "IF NOT EXISTS (SELECT * FROM GameState WHERE firstName = ? ) "
                        + "BEGIN "
                            + "INSERT INTO GameState VALUES (?, ?, ?, ?)"
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

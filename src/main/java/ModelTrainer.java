import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModelTrainer {

  Connection conn;
  String tableName;
  generalHypothesis hypothesis;

  public ModelTrainer(String url, String tableName) throws SQLException {
    conn = DriverManager.getConnection(url);
    this.tableName = tableName;
    hypothesis = (features,weights) ->{
      Function<Double,Double> sigmoid = x -> 1 / (1 + Math.exp(-x));
      double dotProduct = 0;
      for(int i = 0; i<features.length; i++) dotProduct += features[i]*weights[i];
      return sigmoid.apply(dotProduct);
    };
  }

  public List<GameStateRecord> produceGameStateRecordsFromGameRecord(GameRecord record){
    List<GameStateRecord> result = new ArrayList<>();
    char winningSymbol = record.getWinningSymbol();
    record.getGameStates().forEach(gameState -> {
      result.add(new GameStateRecord(gameState, winningSymbol));
    });

    return result;
  }

  public void storeSingleGameStateRecord(GameStateRecord gameStateRecord) throws SQLException{
    String id = gameStateRecord.getGameState().getStateInString();
    char winningSymbol = gameStateRecord.getWinningSymbol();



    if(dbContainsEntry(id)){
      System.out.println("Updating existing state");
      String selectQuery = "SELECT * FROM " + tableName +" WHERE (id = ?)";
      PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
      selectStatement.setString(1,id);
      ResultSet selection = selectStatement.executeQuery();

      int totalOccurrence = selection.getInt("totalOccurrences") + 1;
      int occurrencesWhereXWon = selection.getInt("occurrencesWhereXWon");
      int occurrencesWhereOWon = selection.getInt("occurrencesWhereOWon");
      if(winningSymbol == 'X'){
        occurrencesWhereXWon++;
      }else if(winningSymbol == 'O'
          ){
        occurrencesWhereOWon++;
      }
      String updateQuery = "UPDATE "+ tableName+ " SET totalOccurrences = ?, occurrencesWhereXWon = ?, occurrencesWhereOWon = ? WHERE (id = ?)";
      PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
      updateStatement.setInt(1,totalOccurrence);
      updateStatement.setInt(2,occurrencesWhereXWon);
      updateStatement.setInt(3,occurrencesWhereOWon);
      updateStatement.setString(4, id);
      updateStatement.executeUpdate();


    } else{
      String insertQuery = "INSERT INTO " + tableName + "(id, totalOccurrences, occurrencesWhereXWon, occurrencesWhereOWon) VALUES (?,1,?,?)";
      PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
      insertStatement.setString(1, id);
      if(winningSymbol == 'N'){
        insertStatement.setInt(2,0);
        insertStatement.setInt(3,0);
      } else{
        if(winningSymbol == 'X'){
          insertStatement.setInt(2,1);
          insertStatement.setInt(3,0);
        }else {
          insertStatement.setInt(2,0);
          insertStatement.setInt(3,1);
        }
      }
      insertStatement.executeUpdate();

    }


  }

  public boolean dbContainsEntry(String id) throws SQLException {
    String query = "SELECT COUNT(*) FROM " +tableName+" WHERE (id = ?)";
    PreparedStatement statement= conn.prepareStatement(query);
    statement.setString(1,id);
    ResultSet selection = statement.executeQuery();
    return  selection.getInt(1) > 0;
  }




  public List<LabeledResult> getLabeledResultsFromDb(char playerSymbol) throws SQLException{
    List<LabeledResult> results = new ArrayList<>();
    String selectQuery = "SELECT * FROM "+tableName;
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(selectQuery);
    while(resultSet.next()){
      String id = resultSet.getString("id");
      int totalPlayed = resultSet.getInt("totalOccurrences");
      int xWon = resultSet.getInt("occurrencesWhereXWon");
      int oWon = resultSet.getInt("occurrencesWhereOWon");
      int[] features = getFeaturesFromId(id);
      int label;
      if(playerSymbol == 'X'){
        if (xWon/totalPlayed >= 0.5) label = 1;
        else label = 0;
      }else{
        if(oWon/totalPlayed >= 0.5) label = 1;
        else label = 0;

      }
      results.add(new LabeledResult(features,label, id));

    }
    return  results;

  }

  public int[] getFeaturesFromId(String id){
    int[] features = new int[10];
    features[0] = 1;
    for(int i = 0; i < 9; i++ ){
      if(id.charAt(i) == 'X'){
        features[i + 1] = 1;
      } else if(id.charAt(i) == 'O'){
        features[i+1] = -1;
      }else{
        features[i+1] = 0;
      }
    }
    return features;
  }

  public double[] getOptimalWeights(List<LabeledResult> results, double[] initialWeights, double learningRate, int maxNumOfIterations){
    double[] optimalWeights = initialWeights;
    System.out.println("Initial cost =  " + logisticRegressionCost(results,initialWeights));
    for(int iteration = 1; iteration <= maxNumOfIterations; iteration++ ){
      double[] temp = new double[10];
      double[] gradient = logisticRegressionGradient(results, optimalWeights);
      for(int k = 0; k < 10; k++){
        optimalWeights[k] = optimalWeights[k] - (learningRate * gradient[k]);
      }
      System.out.println("Cost in iteration "+iteration+" =  "+ logisticRegressionCost(results,optimalWeights) );

    }
    return optimalWeights;
  }

  public double[] getOptimalWeights(List<LabeledResult> results, double[] initialWeights){
    double customLearningRate = 1;
    int customNumberOfIterations = 100;
    return getOptimalWeights(results,initialWeights,customLearningRate,customNumberOfIterations);
  }


  public double logisticRegressionCost(List<LabeledResult> results, double[] weights){
    double cost = 0;
    for (LabeledResult result: results) {
      int y = result.label;
      double hThetaX = hypothesis.apply(result.features,weights);
      Function<Double,Double> log = Math::log;
      double resultCost = -(y*log.apply(hThetaX) + (1-y)*log.apply(1-hThetaX));
      cost += resultCost;
    }
    return (cost / results.size());
  }

  public double[] logisticRegressionGradient(List<LabeledResult> results, double[] weights){
    double[] gradient = new double[10];
    for(int k = 0; k<10; k++){
      gradient[k] = 0;
      for(LabeledResult result : results){
        int y = result.label;
        int[] X = result.features;
        double hThetaX = hypothesis.apply(X,weights);
        double temp = (hThetaX - y) * X[k];
        gradient[k] += temp;
      }
      gradient[k] /= results.size();
    }
    return gradient;
  }

  public void storeGameRecord(GameRecord gameRecord) {
    char winningSymbol = gameRecord.getWinningSymbol();
    gameRecord.getGameStates().forEach(gameState -> {
      GameStateRecord gameStateRecord = new GameStateRecord(gameState, winningSymbol);
      try {
        storeSingleGameStateRecord(gameStateRecord);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }


  @FunctionalInterface
  interface generalHypothesis{
    double apply(int[] features, double[] weights);
  }

  public double[] getWeightsFromDb(char playerSymbol) throws SQLException{
    double[] weights = new double[10];
    String weightTableName = "ModelWeightsFor" + playerSymbol;
    String selectQuery = "SELECT * FROM "+weightTableName;
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(selectQuery);
    while(resultSet.next()){
      int index = resultSet.getInt("id");
      double weight = resultSet.getDouble("weight");
      weights[index] = weight;
    }
    return  weights;
  }

  public void storeWeightsIntoDb(char playerSymbol, double[] weights) throws SQLException{
    String weightTableName = "ModelWeightsFor" + playerSymbol;
    for(int i = 0; i < 10; i++){
      String updateQuery = "UPDATE "+ weightTableName +" SET weight = ? WHERE (id = ?)";
      PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
      updateStatement.setDouble(1, weights[i]);
      updateStatement.setInt(2,i);
      updateStatement.executeUpdate();
    }

  }
}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelTrainer {

  Connection conn;
  String tableName;

  public ModelTrainer(String url, String tableName) throws SQLException {
    conn = DriverManager.getConnection(url);
    this.tableName = tableName;
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
      System.out.println("Storing a new state");
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

  public void trainModel(){

  }
}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class Program {

  public static void main(String[] argv) throws Exception {

    Player playerX = new HumanPlayer();
    Player playerY = new HumanPlayer();
    GameHost gameHost = new GameHost(playerX,playerY);
    GameRecord gameRecord = gameHost.playASingleGame();
    List<GameStateRecord> gameStateRecords = gameHost.produceGameStateRecordsFromGameRecord(gameRecord);
    gameHost.storeGameRecord(gameStateRecords);

  }

}

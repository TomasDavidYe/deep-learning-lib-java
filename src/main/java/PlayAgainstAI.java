import machinelearning.NeuralNet;
import tictactoe.*;
import utils.DataManager;


public class PlayAgainstAI {

  public static final String TABLE_NAME = "GameState3";
  public static final String URL = "jdbc:sqlite:testDb.db";


  public static void main(String[] argv) throws Exception {

      // Initialise the AI player with NN coming from the local file DB
      // You can train your own NN on the game record dataset and
      // then pass that as the brain to the AI Player
      DataManager dataManager = new DataManager(URL,TABLE_NAME);
      NeuralNet weightsForX = dataManager.initNeuralNetFromDB();
      Player playerX = new AIPlayer(weightsForX);
      Player playerO = new HumanPlayer();

      // Play against NN powered AI Player
      GameHost gameHost = new GameHost(playerX, playerO);
      GameRecord gameRecord = gameHost.playASingleGame();

      // Store game result
      dataManager.storeGameRecord(gameRecord);
      dataManager.closeConnection();

  }

}

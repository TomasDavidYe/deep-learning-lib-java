import java.util.ArrayList;
import java.util.List;

public class GameHost {
  private Player playerX;
  private Player playerO;
  public static final GameState initialState = new GameState(new int[][]{
      {0,0,0},
      {0,0,0},
      {0,0,0},
  });

  public GameHost(Player playerX, Player playerO){
    this.playerX = playerX;
    this.playerO = playerO;
    playerX.setPlayerSymbol('X');
    playerO.setPlayerSymbol('O');
  }

  public GameRecord playASingleGame(){
    GameRecord gameRecord = new GameRecord();
    int turnsPlayed = 0;
    GameState lastStatePlayed = initialState;
    lastStatePlayed.printOutState();
    GameState temp;
    boolean playerXIsPlaying = true;

    while(!lastStatePlayed.hasWinner() && turnsPlayed < 9){
      turnsPlayed++;
      System.out.println("Starting turn number " + turnsPlayed);
      playerXIsPlaying = turnsPlayed % 2 == 1;
      if(playerXIsPlaying){
        System.out.println("X is playing");
        temp = playerX.play(lastStatePlayed);
        lastStatePlayed = temp;
      } else
      {
        System.out.println("O is playing");
        temp = playerO.play(lastStatePlayed);
        lastStatePlayed = temp;
      }
      gameRecord.addGameState(lastStatePlayed);
      lastStatePlayed.printOutState();
    }
    String resultMessage;
    if(lastStatePlayed.hasWinner()){
      if(playerXIsPlaying) {
        gameRecord.setWinningSymbol('X');
        resultMessage = "Player with X has won";
      }
      else {
        gameRecord.setWinningSymbol('O');
        resultMessage = "Player with O has won";
      }
    }else{
      gameRecord.setWinningSymbol('N');
      resultMessage = "Neither player has won";
    }

    System.out.println("Game finished");
    System.out.println(resultMessage);
    return gameRecord;
  }

  public List<GameStateRecord> produceGameStateRecordsFromGameRecord(GameRecord record){
    List<GameStateRecord> result = new ArrayList<>();
    char winningSymbol = record.getWinningSymbol();
    record.getGameStates().forEach(gameState -> {
      result.add(new GameStateRecord(gameState, winningSymbol));
    });

    return result;
  }

  public void storeGameRecord(List<GameStateRecord> gameStateRecords){
    gameStateRecords.forEach(gameStateRecord -> {
      System.out.println("Storing gamestate: ");
      gameStateRecord.getGameState().printOutState();
      System.out.println("With the winning player:  " + gameStateRecord.getWinningSymbol());
    });
  }

}

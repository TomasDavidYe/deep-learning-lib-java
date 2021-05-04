package tictactoe;

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
    boolean humanIsPlaying = (playerX instanceof HumanPlayer)||(playerO instanceof HumanPlayer);
    GameRecord gameRecord = new GameRecord();
    int turnsPlayed = 0;
    GameState lastStatePlayed = initialState;
    if(humanIsPlaying) lastStatePlayed.printOutState();
    GameState temp;
    boolean playerXIsPlaying = true;

    while(!lastStatePlayed.hasWinner() && turnsPlayed < 9){
      if(humanIsPlaying){
        System.out.println();
        System.out.println();
      }
      turnsPlayed++;
      playerXIsPlaying = turnsPlayed % 2 == 1;
      if(playerXIsPlaying){
        temp = playerX.play(lastStatePlayed);
        lastStatePlayed = temp;
      } else
      {
        temp = playerO.play(lastStatePlayed);
        lastStatePlayed = temp;
      }
      gameRecord.addGameState(lastStatePlayed);
      if(humanIsPlaying)lastStatePlayed.printOutState();
    }
    String resultMessage;
    if(lastStatePlayed.hasWinner()){
      if(playerXIsPlaying) {
        gameRecord.setWinningSymbol('X');
        resultMessage = "tictactoe.Player with X has won";
      }
      else {
        gameRecord.setWinningSymbol('O');
        resultMessage = "tictactoe.Player with O has won";
      }
    }else{
      gameRecord.setWinningSymbol('N');
      resultMessage = "Neither player has won";
    }

    System.out.println("Game finished");
    System.out.println(resultMessage);
    return gameRecord;
  }



}

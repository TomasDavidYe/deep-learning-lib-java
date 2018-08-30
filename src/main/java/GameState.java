import java.util.Arrays;

public class GameState {
  private int[][] gameMap;
  private String stateInString;

  public int[][] getGameMap() {
    return gameMap;
  }

  public GameState (int[][] gameMap){
    this.gameMap = gameMap;
    this.stateInString = mapStateToString(gameMap);
  }


  public String mapStateToString(int[][] state){
    String result = "";
    for(int i = 0; i<=2; i++){
      for(int j = 0; j<=2; j++){
        switch (state[i][j]){
          case 1: result += "X";
            break;
          case 0: result += ".";
            break;
          case -1:result += "O";
            break;
          default:
            break;
        }
      }
      if(i <= 1) result += " ";
    }
    return result;
  }

  public void printOutState(){
    String[] rows = stateInString.split(" ");
    Arrays.stream(rows).forEach(row -> System.out.println(row));
  }

  public boolean hasWinner(){
    return false;
  }


}

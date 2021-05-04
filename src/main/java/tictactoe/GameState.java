package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.la4j.Matrix;

public class GameState {
  private int[][] gameMap;
  private String stateInString;

  public int[][] getGameMap() {
    return gameMap;
  }

  public String getStateInString() {
    return stateInString;
  }

  public GameState (int[][] gameMap){
    this.gameMap = gameMap;
    this.stateInString = mapStateToString(gameMap);
  }

  public boolean moveIsValid(int rowIndex, int columnIndex){
    return gameMap[rowIndex][columnIndex] == 0;
  }

  public int getValueAt(int rowIndex, int columnIndex){
    return gameMap[rowIndex][columnIndex];
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
    }
    return result;
  }

  public void printOutState(){
    String[] rows = new String[3];
    rows[0] = stateInString.substring(0,3);
    rows[1] = stateInString.substring(3,6);
    rows[2] = stateInString.substring(6,9);
    Arrays.stream(rows).forEach(row -> System.out.println(row));
  }

  public boolean hasWinner(){
    List<Integer> sums = new ArrayList<>();
    for(int i = 0; i < 3; i++) {
      sums.add(gameMap[i][0] + gameMap[i][1] + gameMap[i][2]);
      sums.add(gameMap[0][i] + gameMap[1][i] + gameMap[2][i]);
    }
    sums.add(gameMap[0][0] + gameMap[1][1] + gameMap[2][2]);
    sums.add(gameMap[0][2] + gameMap[1][1] + gameMap[2][0]);
    return sums.stream().anyMatch(num -> Math.abs(num) == 3);
  }

  public Matrix extractFeature(){
    double[][] array = {new double[9]};
    Map<Character, Double> charToNumber = new HashMap<Character, Double>(){
      {
        put('X',1.0);
        put('.', 0.0);
        put('O', -1.0);
      }
    };
    String temp = this.getStateInString();
    for(int i = 0; i<9; i++) array[0][i] = charToNumber.get(temp.charAt(i));
    return Matrix.from2DArray(array);
  }


}

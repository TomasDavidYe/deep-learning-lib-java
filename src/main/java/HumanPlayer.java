import java.util.Scanner;

public class HumanPlayer extends Player{

    @Override
    public GameState play(GameState gameState){
      Scanner scanner = new Scanner(System.in);
      boolean validInput = false;
      int[][] newGameMap = gameState.getGameMap().clone();
      int i = -1;
      int j = -1;
      while(!validInput){
        System.out.println("Enter coordinates: ");
        i = scanner.nextInt();
        j = scanner.nextInt();
        validInput = newGameMap[i][j] == 0;
      }
      if(this.playerSymbol == 'X') newGameMap[i][j] = 1;
      else newGameMap[i][j] = -1;
      return new GameState(newGameMap);
    }
}
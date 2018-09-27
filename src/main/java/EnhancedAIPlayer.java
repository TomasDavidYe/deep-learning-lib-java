public class EnhancedAIPlayer extends Player {

  NeuralNetwork net;

  @Override
  public GameState play(GameState state){
    return null;
  }

  public EnhancedAIPlayer(NeuralNetwork net){
    this.net = net;
  }
}

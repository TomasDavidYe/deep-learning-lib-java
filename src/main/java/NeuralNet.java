import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.la4j.Matrix;
import org.la4j.Vector;


public class NeuralNet {
  Map<Integer, Matrix> weightMap;
  Map<Integer, Vector> biasMap;
  int numberOfLayers; //the number of matrix transformations, technically there is one more layer

  public NeuralNet(Map<Integer,Matrix> weightMap, Map<Integer,Vector> biasMap, int numberOfLayers){
    this.numberOfLayers = numberOfLayers;
    this.weightMap = weightMap;
    this.biasMap = biasMap;
  }

  public NeuralNet add(NeuralNet secondNet){
    Map<Integer,Matrix> newWeights = new HashMap<>();
    Map<Integer, Vector> newBiases = new HashMap<>();
    for(int k = 1; k <= numberOfLayers; k++){
        newWeights.put(k,weightMap.get(k).add(secondNet.weightMap.get(k)));
        newBiases.put(k,biasMap.get(k).add(secondNet.biasMap.get(k)));
    }
    return new NeuralNet(newWeights, newBiases, numberOfLayers);
  }

  public NeuralNet multuplyByScalar(double scalar){

    Map<Integer,Matrix> newWeights = new HashMap<>();
    Map<Integer, Vector> newBiases = new HashMap<>();
    for(int k = 1; k <= numberOfLayers; k++){
      newWeights.put(k,weightMap.get(k).multiply(scalar));
      newBiases.put(k,biasMap.get(k).multiply(scalar));
    }
    return new NeuralNet(newWeights, newBiases, numberOfLayers);
  }




}


import java.util.HashMap;
import java.util.Map;
import org.la4j.Matrix;
import org.la4j.Vector;


public class NeuralNet {
  Map<Integer, Matrix> weightMap;
  Map<Integer, Vector> biasMap;
  Map<Integer, Integer> layerSizeMap;
  int numberOfLayers;

  public NeuralNet(Map<Integer,Matrix> weightMap, Map<Integer,Vector> biasMap, int numberOfLayers) throws NeuralNetException {
    this.layerSizeMap = getNetworkStructure(weightMap,biasMap,numberOfLayers);
    this.numberOfLayers = numberOfLayers;
    this.weightMap = weightMap;
    this.biasMap = biasMap;
  }

  public Map<Integer,Integer> getNetworkStructure(Map<Integer,Matrix> weightMap, Map<Integer,Vector> biasMap, int numberOfLayers) throws NeuralNetException {
    Map<Integer,Integer> result = new HashMap<>();
    int inputLayerSize = weightMap.get(1).columns();
    result.put(1,inputLayerSize);
    for(int i = 2; i < numberOfLayers; i++){
      if(weightMap.get(i-1).rows() == weightMap.get(i).columns()) result.put(i,weightMap.get(i).columns());
      else throw new NeuralNetException();
    }
    result.put(numberOfLayers, weightMap.get(numberOfLayers-1).rows());
    return result;
  }

  public NeuralNet add(NeuralNet secondNet) throws NeuralNetException{
    Map<Integer,Matrix> newWeights = new HashMap<>();
    Map<Integer, Vector> newBiases = new HashMap<>();
    for(int k = 1; k < numberOfLayers; k++){
        newWeights.put(k,weightMap.get(k).add(secondNet.weightMap.get(k)));
        newBiases.put(k,biasMap.get(k).add(secondNet.biasMap.get(k)));
    }
    return new NeuralNet(newWeights, newBiases, numberOfLayers);
  }

  public NeuralNet multiplyByScalar(double scalar) throws NeuralNetException{

    Map<Integer,Matrix> newWeights = new HashMap<>();
    Map<Integer, Vector> newBiases = new HashMap<>();
    for(int k = 1; k < numberOfLayers; k++){
      newWeights.put(k,weightMap.get(k).multiply(scalar));
      newBiases.put(k,biasMap.get(k).multiply(scalar));
    }
    return new NeuralNet(newWeights, newBiases, numberOfLayers);
  }




}


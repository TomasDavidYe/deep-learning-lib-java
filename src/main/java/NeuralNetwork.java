//better designed version of the class NeuralNet

import java.util.Map;
import org.la4j.Matrix;
import org.la4j.Vector;

public class NeuralNetwork {
  Map<Integer, Matrix> weightMap;
  Map<Integer, Matrix> biasMap;
  int numberOfLayers;

   public NeuralNetwork(int numberOfLayers, Map<Integer, Matrix> weightMap, Map<Integer,Matrix> biasMap){
     this.numberOfLayers = numberOfLayers;
     this.weightMap = weightMap;
     this.biasMap = biasMap;
   }


   public double applyNetToVector(Matrix input){
     return 0;
   }
}

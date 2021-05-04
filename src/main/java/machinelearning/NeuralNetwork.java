package machinelearning;//better designed version of the class machinelearning.NeuralNet

import java.util.Map;
import org.la4j.Matrix;
import utils.ProjectMath;

public class NeuralNetwork {
  Map<Integer, Matrix> weightMap;
  Map<Integer, Matrix> biasMap;
  int numberOfLayers;

   public NeuralNetwork(int numberOfLayers, Map<Integer, Matrix> weightMap, Map<Integer,Matrix> biasMap){
     this.numberOfLayers = numberOfLayers;
     this.weightMap = weightMap;
     this.biasMap = biasMap;
   }



   public Matrix applyNetToMatrix(Matrix X){
     Map<Integer,Matrix> weights = this.weightMap;
     Matrix temp;

     Matrix layer1Input = X;
     Matrix transformation1 = weights.get(1);
     temp = layer1Input.multiply(transformation1);
     temp = ProjectMath.sigmoid(temp);

     Matrix layer2input = temp;
     Matrix transformation2 = weights.get(2);
     temp = layer2input.multiply(transformation2);
     temp = ProjectMath.sigmoid(temp);

     Matrix layer3input = temp;
     Matrix transformation3 = weights.get(3);
     temp = layer3input.multiply(transformation3);
     temp = ProjectMath.sigmoid(temp);

     Matrix layer4Input = temp;
     Matrix transformation4 = weights.get(4);
     temp = layer4Input.multiply(transformation4);
     temp = ProjectMath.sigmoid(temp);
     return temp;
   }
}

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.la4j.Matrix;
import org.la4j.iterator.MatrixIterator;

public class ProjectMath {

  public static double sigmoid(double x){
    return 1 / (1 + Math.exp(-x));
  }

  public static double sigmoidPrime(double x){
    return sigmoid(x) * (1- sigmoid(x));
  }

  public static Matrix sigmoid(Matrix A){
    Matrix result = A.copy();
    MatrixIterator iterator = result.iterator();
    while (iterator.hasNext()){
      double x = iterator.next();
      iterator.set(sigmoid(x));
    }
    return result;
  }

  public static  Matrix sigmoidPrime(Matrix A){
    Matrix result = A.copy();
    MatrixIterator iterator = result.iterator();
    while (iterator.hasNext()){
      double x = iterator.next();
      iterator.set(sigmoidPrime(x));
    }
    return result;
  }


  public static double cost(NeuralNet net, Matrix features, Matrix labels){
    Matrix input = features.transpose();
    int numOfTransformations = net.numberOfLayers - 1;
    int numOfExamples = features.rows();
    Matrix result = input;
    for(int l = 1; l<=numOfTransformations; l++){
      Matrix temp = net.weightMap.get(l).multiply(result);
      for(int k = 0; k < result.columns(); k++){
        org.la4j.Vector tempVector = temp.getColumn(k);
        org.la4j.Vector bias = net.biasMap.get(l);
        temp.setColumn(k,tempVector.add(bias));
      }
      result = sigmoid(temp);
    }
    for(int i = 0; i<numOfExamples; i++){
      double y = labels.get(i,0);
      double hThetaX = result.get(0,i);
      double localCost = -(y*Math.log(hThetaX) + (1-y)*Math.log(1-hThetaX));
      result.set(0,i, localCost);
    }


    return (result.sum()) / numOfExamples;
  }

  public static NeuralNet getGradient(NeuralNet net, Matrix features, Matrix labels) throws NeuralNetException{
    int numberOfLayers = net.numberOfLayers;
    int numOfExamples = features.rows();
    NeuralNet result = getBlankNetOfSameShape(net);

    for(int k = 0; k < numOfExamples; k++) {
      NeuralNet localGradient = getBlankNetOfSameShape(net);
      Matrix x = features.getRow(k).toColumnMatrix();
      double y = labels.get(k, 0);
      Map<Integer, Matrix> A = new HashMap<>();
      A.put(1, x);
      for (int i = 2; i <= numberOfLayers; i++) {
        Matrix prevLayer = A.get(i - 1);
        Matrix temp = net.weightMap.get(i - 1).multiply(prevLayer).add(net.biasMap.get(i - 1).toColumnMatrix());
        A.put(i, sigmoid(temp));
      }
      Map<Integer, Matrix> deltas = new HashMap<>();
      double[][] tempArray = {{A.get(numberOfLayers).get(0, 0) - y}};
      deltas.put(numberOfLayers, Matrix.from2DArray(tempArray));
      for(int l = numberOfLayers -1; l >=2; l--){
        //recursively compute the rest of the deltas going down...
        Matrix prevDelta = deltas.get(l+1);
        Matrix currentA = A.get(l);
        Matrix sigmoidFactor = currentA.hadamardProduct(ones(currentA.rows()).subtract(currentA));
        Matrix weightsTrans = net.weightMap.get(l).transpose();
        Matrix temp = weightsTrans.multiply(prevDelta).hadamardProduct(sigmoidFactor);
        deltas.put(l,temp);
      }
      for(int l = 1; l< numberOfLayers -1; l++){
        org.la4j.Vector bias = localGradient.biasMap.get(l);
        Matrix weights = localGradient.weightMap.get(l);
        int rows = localGradient.weightMap.get(l).rows();
        int columns = localGradient.weightMap.get(l).columns();
        for(int i = 0; i<rows; i++){
          bias.set(i,deltas.get(l+1).get(i,0));
          for(int j = 0; j<columns; j++){
            double deltaZ = deltas.get(l+1).get(i,0);
            double a = A.get(l).get(j,0);
            weights.set(i,j, deltaZ*a);
          }
        }
      }
      NeuralNet tempNet = result.add(localGradient);
      result = tempNet;
    }
    return result;
  }



  public static NeuralNet optimize(NeuralNet initialNet, Matrix features, Matrix labels, double learningRate, int numOfIterations)
  throws NeuralNetException
  {

    System.out.println("Initial cost =  " + cost(initialNet,features,labels));
    NeuralNet result = initialNet;
    for(int iter = 1; iter < numOfIterations; iter++){
      NeuralNet temp = getGradient(result,features,labels).multiplyByScalar(-learningRate);
      NeuralNet temp2 = result.add(temp);
      result = temp2;
      System.out.println("Cost int iteration  " + iter + "  =  " + cost(result,features,labels));
    }
    return result;
  }

  public static NeuralNet getBlankNetOfSameShape(NeuralNet net) throws  NeuralNetException{
    int numberOfLayers = net.numberOfLayers;
    Map<Integer, Matrix> emptyWeightMap  = new HashMap<>();
    Map<Integer, org.la4j.Vector> emptyBiasMap = new HashMap<>();
    for(int i = 1; i < numberOfLayers; i++){
      emptyWeightMap.put(i,net.weightMap.get(i).blank());
      emptyBiasMap.put(i,net.biasMap.get(i).blank());
    }
    return new NeuralNet(emptyWeightMap,emptyBiasMap, numberOfLayers);

  }

  public static double applyNetToFeatures(NeuralNet net, org.la4j.Vector features){
    Matrix result = features.toColumnMatrix();
    int numOfTransformations = net.numberOfLayers - 1;
    for(int i = 1; i <= numOfTransformations; i++){
      Matrix temp = net.weightMap.get(i).multiply(result).add(net.biasMap.get(i).toColumnMatrix());
      result = sigmoid(temp);
    }
    return result.get(0,0);
  }

  public static Matrix ones(int rows, int columns){
    return Matrix.constant(rows,columns,1);
  }

  public static Matrix ones(int rows){
    return ones(rows,1);
  }


}

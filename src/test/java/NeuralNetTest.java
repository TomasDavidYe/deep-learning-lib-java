import java.util.HashMap;
import java.util.Map;

import machinelearning.NeuralNet;
import machinelearning.NeuralNetException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Matrix;
import org.la4j.Vector;

public class NeuralNetTest {

  NeuralNet net1;
  NeuralNet net2;
  double scalar1;
  double scalar2;
  static final double delta = 0.0001;


  @Before
  public void init() throws NeuralNetException {
    double[][] weights1ForNet1 = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9},
    };
    double[][] weights2ForNet1 = {
        {1, 1, 1},
        {1, 2, 3},
        {3, 1, 2},
    };

    double[][] weights1ForNet2 = {
        {1, 0, 0},
        {0, 1, 0},
        {0, 0, 1},
    };

    double[][] weights2ForNet2 = {
        {1, 3, 5},
        {2, 4, 6},
        {3, 6, 9},
    };

    double[] bias1ForNet1 = {1, 1, 1};
    double[] bias2ForNet1 = {1, 0, 0};
    double[] bias1ForNet2 = {0, 0, 1};
    double[] bias2ForNet2 = {1, 2, 3};

    Map<Integer, Matrix> weightsForNet1 = new HashMap<>();
    weightsForNet1.put(1, Matrix.from2DArray(weights1ForNet1));
    weightsForNet1.put(2, Matrix.from2DArray(weights2ForNet1));
    Map<Integer, Vector> biasForNet1 = new HashMap<>();
    biasForNet1.put(1, Vector.fromArray(bias1ForNet1));
    biasForNet1.put(2, Vector.fromArray(bias2ForNet1));
    net1 = new NeuralNet(weightsForNet1, biasForNet1, 3);

    Map<Integer, Matrix> weightsForNet2 = new HashMap<>();
    weightsForNet2.put(1, Matrix.from2DArray(weights1ForNet2));
    weightsForNet2.put(2, Matrix.from2DArray(weights2ForNet2));
    Map<Integer, Vector> biasForNet2 = new HashMap<>();
    biasForNet2.put(1, Vector.fromArray(bias1ForNet2));
    biasForNet2.put(2, Vector.fromArray(bias2ForNet2));
    net2 = new NeuralNet(weightsForNet2, biasForNet2, 3);

    scalar1 = 2;
    scalar2 = 0.5;
  }

  @Test
  public void addTest() throws NeuralNetException {
    NeuralNet result = net1.add(net2);
    for (int l = 1; l <= 2; l++) {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          Assert.assertEquals(net1.weightMap.get(l).get(i, j) + net2.weightMap.get(l).get(i, j), result.weightMap.get(l).get(i, j), delta);
        }
        Assert.assertEquals(net1.biasMap.get(l).get(i) + net2.biasMap.get(l).get(i), result.biasMap.get(l).get(i), delta);
      }
    }
  }

  @Test
  public void multiplyByScalarTest() throws NeuralNetException {
    NeuralNet result1 = net1.multiplyByScalar(scalar1);
    NeuralNet result2 = net2.multiplyByScalar(scalar2);

    for (int l = 1; l <= 2; l++) {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          Assert.assertEquals(net1.weightMap.get(l).get(i, j) * scalar1, result1.weightMap.get(l).get(i, j), delta);
          Assert.assertEquals(net2.weightMap.get(l).get(i, j) * scalar2, result2.weightMap.get(l).get(i, j), delta);
        }
        Assert.assertEquals(net1.biasMap.get(l).get(i) * scalar1, result1.biasMap.get(l).get(i), delta);
        Assert.assertEquals(net2.biasMap.get(l).get(i) * scalar2, result2.biasMap.get(l).get(i), delta);

      }
    }

  }
}
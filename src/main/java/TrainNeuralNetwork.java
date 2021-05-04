import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import machinelearning.LabeledResult;
import machinelearning.NetTrainer;
import machinelearning.NeuralNet;
import org.la4j.Matrix;
import org.la4j.Vector;
import utils.DataManager;
import utils.ProjectMath;

public class TrainNeuralNetwork {

  public static final String TABLE_NAME = "GameState3";
  public static final String URL = "jdbc:sqlite:testDb.db";


    public static void main(String[] argv) throws Exception {
        double learningRate = 1;
        int numOfIter = 1000;
        DataManager dataManager = new DataManager(URL,TABLE_NAME);
        List<LabeledResult> results = dataManager.getLabeledResultsFromDb('O');

        //hidden layer sizes
        int numberOfLayers = 4;
        int layer1Size = 2;
        int layer2Size = 2;
        int layer3Size = 2;


        Map<Integer,Matrix> weights = new HashMap<>();
        Map<Integer, org.la4j.Vector> bias = new HashMap<>();

        // Init Biases
        bias.put(1, org.la4j.Vector.random(layer1Size,new Random()));
        bias.put(2, org.la4j.Vector.random(layer2Size,new Random()));
        bias.put(3, Vector.random(layer3Size,new Random()));
        bias.put(4, Vector.random(1,new Random()));


        // Init Weights
        weights.put(1,Matrix.random(layer1Size,9,new Random()));
        weights.put(2,Matrix.random(layer2Size,layer1Size,new Random() ));
        weights.put(3,Matrix.random(layer3Size,layer2Size, new Random()));
        weights.put(4,Matrix.random(1,layer3Size, new Random()));


        // Init Training Environment
        NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);
        NetTrainer trainer = new NetTrainer(results);
        Matrix features = trainer.getFeatures();
        Matrix labels = trainer.getLabels();

        // Training
        NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);

        // Store the NN
        dataManager.storeNN(optimized, "Example NN with 4 Hidden Layers");
    }

}

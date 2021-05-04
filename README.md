# About the Project
I have written this package back in 2018 when I was a learning about Deep Learning in school and I wanted to test my understand.

To satisfy my passion for games, I developed a game engine in which it is possible to play standard 3x3 TicTacToe against Neural Network powered AI players.

This package contains both the code for the game engine and for the Neural Network training. I also included in the repository the training data so you can experiment and build your own AI players.


# How to use this package

## 1) Train a Neural Network
You can use my prepared training data for this problem and my high level API (almost as sexy as TensorFlow :D ) to train your own Multi Layer Perceptron network and store it back to the local DB.

```java
// Init Training Environment
NeuralNet initialNet = new NeuralNet(weights,bias,numberOfLayers);
NetTrainer trainer = new NetTrainer(results);
Matrix features = trainer.getFeatures();
Matrix labels = trainer.getLabels();

// Training
NeuralNet optimized = ProjectMath.optimize(initialNet,features,labels,learningRate,numOfIter);
```

Full Example can be found in the [**TrainNeuralNetwork**](src/main/java/TrainNeuralNetwork.java) class.

## 2) Play TicTacToe against NN based
You can use the network you have trained in the previous step to power your own AI player and play against it.

```java
// Init AI Player with your network you trained in the previous step
NeuralNet weightsForX = dataManager.initNeuralNetFromDB(networkId); // Your Neural Net from previous step
Player playerX = new AIPlayer(weightsForX);
Player playerO = new HumanPlayer();

// Play against NN powered AI Player
GameHost gameHost = new GameHost(playerX, playerO);
GameRecord gameRecord = gameHost.playASingleGame();
```

Full Example can be found in the [**PlayAgainstAI**](src/main/java/PlayAgainstAI.java) class.  

## 3) Check my Back Propagation Logic
In the [**ProjectMath**](./src/main/java/utils/ProjectMath.java), you will find the **getGradient()** method which implements (very inefficiently to be honest) the backward propagation algorithm described at https://en.wikipedia.org/wiki/Backpropagation

Let me know if you find a mistake :)  




# ML-tic-tac-toe-AI
In this project we apply logistic regression and train a Model to effectively play standard Tic Tac Toe. So far plain logistic regression
seems not to be getting the job done so the next step is to implement the training via Neural Networks.
Using TensorFlow API from Python we have now available good weights for the second player such that accuracy on the training set
is nearly 94% (training set size = 5442). Though the tictactoe.Player with neural net plays significantly better than the player with
simple logistic regression on, it is still not really satisfactory. I believe statistical methods alone cannot solve even a simple game like
this. Some game analysis combined with MachineLearning is probably needed.
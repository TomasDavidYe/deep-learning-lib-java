package machinelearning;

import java.util.List;

import machinelearning.LabeledResult;
import org.la4j.Matrix;

public class NetTrainer {

  Matrix data;

  public NetTrainer(List<LabeledResult> labeledResults){
    int numRows = labeledResults.size();
    double[][] matrixArray = new double[numRows][10];
    int rowIndex = 0;
    for (LabeledResult result: labeledResults) {
      for(int i = 0; i<9; i++) matrixArray[rowIndex][i] = result.features[i];
      matrixArray[rowIndex][9] = result.label;
      rowIndex++;
    }
    data = Matrix.from2DArray(matrixArray);
  }

  public Matrix getFeatures(){
    return data.copyOfColumns(9);
  }

  public Matrix getLabels(){
    Matrix label = data.copy();
    for(int i = 0; i < 9; i++) label = label.removeFirstColumn();
    return label;
  }






}

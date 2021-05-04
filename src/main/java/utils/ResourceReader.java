package utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import org.la4j.Matrix;

public class ResourceReader {

  public static Matrix readMatrixFromCSV(String path) throws Exception{
    Scanner scanner = new Scanner(new FileReader(path));
    List<double[]> rows = new ArrayList<>();
    while(scanner.hasNext()){
      String line = scanner.nextLine();
      String[] elements = line.split(",");
      double[] convertedElements = new double[elements.length];
      for(int i = 0; i < elements.length; i++) convertedElements[i] = Double.parseDouble(elements[i]);
      rows.add(convertedElements);
    }

    int numRows = rows.size();
    int numColumns = rows.get(0).length;

    double[][] matrixArray = new double[numRows][numColumns];
    for(int i = 0; i < numRows; i++) matrixArray[i] = rows.get(i);
    return Matrix.from2DArray(matrixArray);
  }

  public static Map<Integer, Matrix> getWeightsFromFiles(int numOfFiles) throws Exception{
    String resourcePath = "./src/main/resources/weights";
    Map<Integer,Matrix> result = new HashMap<>();
    for(int i = 1; i <= numOfFiles; i++){
      String filePath = resourcePath + i + ".txt";
      result.put(i,readMatrixFromCSV(filePath));
    }
    return result;
  }

  public static Map<Integer, Matrix> getBiasesFromFiles(int numOfFiles) throws Exception{
    String resourcePath = "./src/main/resources/bias";
    Map<Integer,Matrix> result = new HashMap<>();
    for(int i = 1; i <= numOfFiles; i++){
      String filePath = resourcePath + i + ".txt";
      result.put(i,readMatrixFromCSV(filePath));
    }
    return result;
  }



}

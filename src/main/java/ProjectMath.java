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
}

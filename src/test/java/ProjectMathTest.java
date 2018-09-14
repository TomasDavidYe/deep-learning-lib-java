import static org.junit.Assert.*;

import javax.swing.Popup;
import org.junit.Assert;
import org.la4j.Matrix;

public class ProjectMathTest {

  public static final double DELTA = 0.0001;

  @org.junit.Test
  public void sigmoidTest() {
    Assert.assertEquals(0.5, ProjectMath.sigmoid(0),DELTA);
    Assert.assertEquals(0.26894, ProjectMath.sigmoid(-1), DELTA);
    Assert.assertEquals(0.73105, ProjectMath.sigmoid(1), DELTA);
    Assert.assertEquals(0.99331,ProjectMath.sigmoid(5),DELTA);
    Assert.assertEquals(0.00669, ProjectMath.sigmoid(-5),DELTA);
    Assert.assertEquals(0.99995, ProjectMath.sigmoid(10),DELTA);
    Assert.assertEquals(0.00005, ProjectMath.sigmoid(-10),DELTA);
  }

  @org.junit.Test
  public void sigmoidPrimeTest() {
    Assert.assertEquals(0.25, ProjectMath.sigmoidPrime(0),DELTA);
    Assert.assertEquals(0.19661, ProjectMath.sigmoidPrime(-1), DELTA);
    Assert.assertEquals(0.19661, ProjectMath.sigmoidPrime(1), DELTA);
    Assert.assertEquals(0.00665,ProjectMath.sigmoidPrime(5),DELTA);
    Assert.assertEquals(0.00665,ProjectMath.sigmoidPrime(-5),DELTA);
    Assert.assertEquals(0.00005, ProjectMath.sigmoidPrime(10),DELTA);
    Assert.assertEquals(0.00005, ProjectMath.sigmoidPrime(-10),DELTA);
  }

  @org.junit.Test
  public void sigmoidMatrixTest() {
    double[][] array = {
        {1,2,3},
        {4,5,6},
        {7,8,9}
    };
    Matrix inputMatrix = Matrix.from2DArray(array);
    Matrix resultMatrix = ProjectMath.sigmoid(inputMatrix);
    for(int i =0; i< 3; i++){
      for(int j = 0; j < 3; j++)Assert.assertEquals(ProjectMath.sigmoid(inputMatrix.get(i,j)),resultMatrix.get(i,j),DELTA);

    }
  }

  @org.junit.Test
  public void sigmoidPrimeMatrixTest() {
    double[][] array = {
        {1,2,3},
        {4,5,6},
        {7,8,9}
    };
    Matrix inputMatrix = Matrix.from2DArray(array);
    Matrix resultMatrix = ProjectMath.sigmoidPrime(inputMatrix);
    for(int i =0; i< 3; i++){
      for(int j = 0; j < 3; j++)Assert.assertEquals(ProjectMath.sigmoidPrime(inputMatrix.get(i,j)),resultMatrix.get(i,j),DELTA);

    }
  }
}
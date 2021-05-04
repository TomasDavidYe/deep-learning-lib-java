import javax.naming.OperationNotSupportedException;
import org.junit.Assert;
import org.junit.Before;
import org.la4j.Matrix;
import org.la4j.Vector;
import utils.MatPredicate;
import utils.ProjectMath;

public class ProjectMathTest {

  public static final double DELTA = 0.0001;
  private Matrix inputMatrix1;
  private Matrix inputMatrix2;

  @Before
  public void init(){
    double[][] array1 = {
        {1,2,3},
        {4,5,6},
        {7,8,9}
    };

    double[][] array2 = {
        {0,2,4},
        {1,3,5},
        {3,9,10}
    };
    inputMatrix1 = Matrix.from2DArray(array1);
    inputMatrix2 = Matrix.from2DArray(array2);
  }

  @org.junit.Test
  public void sigmoidTest() {
    Assert.assertEquals(0.5, ProjectMath.sigmoid(0),DELTA);
    Assert.assertEquals(0.26894, ProjectMath.sigmoid(-1), DELTA);
    Assert.assertEquals(0.73105, ProjectMath.sigmoid(1), DELTA);
    Assert.assertEquals(0.99331, ProjectMath.sigmoid(5),DELTA);
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

    Matrix resultMatrix = ProjectMath.sigmoid(inputMatrix1);
    for(int i =0; i< 3; i++){
      for(int j = 0; j < 3; j++)Assert.assertEquals(ProjectMath.sigmoid(inputMatrix1.get(i,j)),resultMatrix.get(i,j),DELTA);

    }
  }

  @org.junit.Test
  public void sigmoidPrimeMatrixTest() {
    Matrix resultMatrix = ProjectMath.sigmoidPrime(inputMatrix1);
    for(int i =0; i< 3; i++){
      for(int j = 0; j < 3; j++)Assert.assertEquals(ProjectMath.sigmoidPrime(inputMatrix1.get(i,j)),resultMatrix.get(i,j),DELTA);

    }
  }

  @org.junit.Test
  public void appendRowTest(){
    double[][] twoDimArray = {
        {2, 4, 6},
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };
    double[] oneDimArray = {2,4,6};
    Matrix expectedMatrix = Matrix.from2DArray(twoDimArray);
    Vector appendedRow = Vector.fromArray(oneDimArray);
    Assert.assertEquals(expectedMatrix, ProjectMath.appendRow(inputMatrix1, appendedRow));
  }

  @org.junit.Test
  public void appendRowOfOnesTest(){
    double[][] twoDimArray = {
        {1, 1, 1},
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    };
    Matrix expectedMatrix = Matrix.from2DArray(twoDimArray);
    Assert.assertEquals(expectedMatrix, ProjectMath.appendRowOfOnes(inputMatrix1));

  }

  @org.junit.Test
  public void appendColumnTest(){
    double[][] twoDimArray = {
        {2, 1, 2, 3},
        {4, 4, 5, 6},
        {6, 7, 8, 9}
    };
    double[] oneDimArray = {2,4,6};
    Matrix expectedMatrix = Matrix.from2DArray(twoDimArray);
    Vector appendedColumn = Vector.fromArray(oneDimArray);
    Assert.assertEquals(expectedMatrix, ProjectMath.appendColumn(inputMatrix1, appendedColumn));
  }

  @org.junit.Test
  public void appendColumnOfOnesTest(){
    double[][] twoDimArray = {
        {1,1,2,3},
        {1,4,5,6},
        {1,7,8,9}
    };
    Matrix expectedMatrix = Matrix.from2DArray(twoDimArray);
    Assert.assertEquals(expectedMatrix, ProjectMath.appendColumnOfOnes(inputMatrix1));
  }

  @org.junit.Test
  public void applyPredicateTest() throws OperationNotSupportedException {

    MatPredicate predicate1 = (a, b) -> a >= b;
    MatPredicate predicate2 = (a,b) -> a -1 == b;

    double[][] array1 = {
        {1,1,0},
        {1,1,1},
        {1,0,0},
    };

    double[][] array2 = {
        {1, 0, 0},
        {0, 0, 1},
        {0, 0, 0},
    };

    Matrix expectedMatrix1 = Matrix.from2DArray(array1);
    Matrix expectedMatrix2 = Matrix.from2DArray(array2);
    Assert.assertEquals(expectedMatrix1, ProjectMath.applyPredicateElementWise(predicate1, inputMatrix1, inputMatrix2));
    Assert.assertEquals(expectedMatrix2, ProjectMath.applyPredicateElementWise(predicate2, inputMatrix1, inputMatrix2));
  }
}
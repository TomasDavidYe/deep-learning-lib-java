import java.sql.SQLException;
import java.util.List;

import machinelearning.LabeledResult;
import machinelearning.NetTrainer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.la4j.Matrix;
import utils.DataManager;

public class NetTrainerTest {


  NetTrainer testedTrainer;
  List<LabeledResult> fetchedResults;
  double delta = 0.0001;


  @Before
  public void init() throws SQLException {
    String url = Program.URL;
    String tableName = "GameStateTest";
    DataManager manager = new DataManager(url,tableName);
    fetchedResults = manager.getLabeledResultsFromDb('X');
    testedTrainer = new NetTrainer(fetchedResults);

  }

  @Test
  public void getFeatures() {
    Matrix feautres = testedTrainer.getFeatures();
    int numOfResults = fetchedResults.size();
    Assert.assertEquals(numOfResults, feautres.rows());
    Assert.assertEquals(9, feautres.columns());
    for(int i = 0; i < numOfResults; i++){
      for(int j = 0; j < 9; j++){
        Assert.assertEquals(fetchedResults.get(i).features[j],(int)feautres.get(i,j),delta);
      }
    }

  }

  @Test
  public void getLabels() {
    Matrix labels = testedTrainer.getLabels();
    int numOfResults = fetchedResults.size();
    Assert.assertEquals(numOfResults, labels.rows());
    Assert.assertEquals(1,labels.columns());
    for(int i = 0; i< numOfResults; i++) Assert.assertEquals(fetchedResults.get(i).label, (int) labels.get(i,0));
  }
}
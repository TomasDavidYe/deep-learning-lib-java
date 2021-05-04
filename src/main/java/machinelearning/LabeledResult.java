package machinelearning;

public class LabeledResult {
  public double[] features;
  public int label;
  public String id;

  public LabeledResult(double[] features, int label, String id){
    this.features = features;
    this.label = label;
    this.id = id;
  }

}

package ranger.data.sets;

import java.util.Random;

import ranger.data.LabeledDatapoint;
import ranger.math.Vector;
import ranger.nn.plot.Window;

public class XOrDataset {

  public static Dataset generateDefaultXOrDataset(int numExamples) {
    return generateXOrDataset(10_000, 0.2, 0.2, new Random());
  }

  public static Dataset generateXOrDataset(int numExamples, double inputNoise, double outputNoise, Random random) {
    Dataset ret = new Dataset();
    for (int i = 0; i < numExamples; i++) {
      ret.add(generateLabeledDataPoint(inputNoise, outputNoise, random));
    }
    return ret;
  }

  public static LabeledDatapoint generateLabeledDataPoint(double inputNoise, double outputNoise, Random random) {
    // We let the picture be the decider.
    // int i1 = random.nextDouble() > 0.5 ? 1 : 0;
    // int i2 = random.nextDouble() > 0.5 ? 1 : 0;
    double x1 = random.nextDouble();
    double x2 = random.nextDouble();
    Vector x = new Vector(x1, x2);
    double y = getOptimalValue(x);

    return new LabeledDatapoint(x, new Vector(y));
  }

  public static double getOptimalValue(Vector in) {
    double distA = in.distance(new Vector(0.0, 0.0));
    double distB = in.distance(new Vector(1.0, 0.0));
    double distC = in.distance(new Vector(0.0, 1.0));
    double distD = in.distance(new Vector(1.0, 1.0));
    double eps = 0.01;
    
    double wA = 1.0 / Math.pow(distA + eps, 5);
    double wB = 1.0 / Math.pow(distB + eps, 5);
    double wC = 1.0 / Math.pow(distC + eps, 5);
    double wD = 1.0 / Math.pow(distD + eps, 5);
    
    return (1.0 * wB + 1.0 * wC) / (wA + wB + wC + wD);
  }

  public static Window getWindow() {
    return new Window(-0.2, 1.2, -0.2, 1.2);
  }

}

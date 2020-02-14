package ranger.nn.ranger;

import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Maps;

import ox.Json;

public class Dendrites {

  protected Map<UUID, Double> dendrites = Maps.newHashMap();

  public static Dendrites randomDendrites(Layer prev, double dendriteSparsityConstant, Random random) {
    Dendrites ret = new Dendrites();
    double proba = dendriteSparsityConstant / (prev.neurons.size() + 1);
    double stdDev = 1.0; // TODO: This needs to be changed to sqrt( 2 / # of neurons).
    for (Neuron neuron : prev.neurons.values()) {
      if (random.nextDouble() < proba) {
        ret.add(neuron.uuid, random.nextGaussian() * stdDev);
      }
    }
    return ret;
  }

  public Dendrites add(UUID neuronUUID, double weight) {
    checkState(neuronUUID != null);
    dendrites.put(neuronUUID, weight);
    return this;
  }

  public double computeAxonPreactivation(SignalVector signalVector, double receiverSignalStrength) {
    double ret = 0.0;
    for (Entry<UUID, Double> dendrite : dendrites.entrySet()) {
      Signal signal = signalVector.signals.get(dendrite.getKey());
      ret += signal.value * (1 - Math.max(receiverSignalStrength - signal.strength, 0));
    }
    return ret;
  }

  public Map<UUID, Double> computeDendriteSignal(double preAxonSignal, double signalStrength) {
    Map<UUID, Double> ret = Maps.newHashMap();
    for (Entry<UUID, Double> dendrite : dendrites.entrySet()) {
      ret.put(dendrite.getKey(), dendrite.getValue() * preAxonSignal * signalStrength);
    }
    return ret;
  }

  public void update(Map<UUID, Double> dendriteSignal, double learningRate) {
    for (Entry<UUID, Double> singleDendriteSignal : dendriteSignal.entrySet()) {
      checkState(dendrites.containsKey(singleDendriteSignal.getKey()));
      dendrites.computeIfPresent(singleDendriteSignal.getKey(),
          (wutev, oldVal) -> oldVal - learningRate * singleDendriteSignal.getValue());
    }
  }

  public void remove(UUID uuid) {
    this.dendrites.remove(uuid);
  }

  public int size() {
    return dendrites.size();
  }

  public static Dendrites fromJson(Json json) {
    if (json == null) {
      return null;
    }
    Dendrites ret = new Dendrites();
    for (String uuid : json.asJsonObject().keySet()) {
      ret.add(UUID.fromString(uuid), json.getDouble(uuid));
    }
    return ret;
  }

  public Json toJson() {
    Json ret = Json.object();
    for (Entry<UUID, Double> entry : dendrites.entrySet()) {
      ret.with(entry.getKey().toString(), entry.getValue());
    }
    return ret;
  }
}

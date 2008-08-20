/**
 *
 */
package rhein;


import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.graph.Network;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;


/**
 * @author mlunzena
 *
 */
public class RheinHelper {


  private static RandomEngine generator;


  private static Normal normal;


  public final static String RIVER_NETWORK_NAME = "Rhein/Rivers";


  public final static String STEWARD_RIVER_NETWORK_NAME = "Rhein/StewardsRivers";


  private static Uniform uniform;


  @SuppressWarnings("unchecked")
  public static Network<Object> getRiverNetwork() {
    return (Network<Object>) RepastEssentials
        .FindProjection(RIVER_NETWORK_NAME);
  }


  /**
   * @return
   */
  @SuppressWarnings("unchecked")
  public static Network<Object> getStewardRiverNetwork() {
    return (Network<Object>) RepastEssentials
        .FindProjection(STEWARD_RIVER_NETWORK_NAME);
  }


  public static void init() {
    RheinHelper.generator = new MersenneTwister(0);
    RheinHelper.normal = new Normal(0, 1, RheinHelper.generator);
    RheinHelper.uniform = new Uniform(0, 1, RheinHelper.generator);
  }


  /**
   * @return
   */
  public static double nextNormalDouble() {
    return nextNormalDouble(0, 1);
  }


  public static double nextNormalDouble(double mean, double stdDev) {
    double raw = normal.nextDouble();
    return raw * stdDev + mean;
  }


  public static int nextIntFromTo(int from, int to) {
    return uniform.nextIntFromTo(from, to);
  }


  public static double nextUniform() {
    return uniform.nextDouble();
  }

  public static double nextUniform(double from, double to) {
    return uniform.nextDoubleFromTo(from, to);
  }
}

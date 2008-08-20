/**
 *
 */
package rhein;


import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.graph.Network;


/**
 * @author mlunzena
 *
 */
public class StewardScheduler {


  /**
   * Name of the Network to watch for river segments
   */
  private static String STEWARD_RIVER_NETWORK_NAME = "Rhein/StewardsRivers";


  /**
   *
   */
  private Network<Object> stewardRiverNet;


  /**
   *
   */
  private Vector<Steward> stewards;


  /**
   *
   */
  @SuppressWarnings("unchecked")
  @ScheduledMethod(start = 0)
  public void initialize() {

    stewardRiverNet = (Network<Object>) RepastEssentials
        .FindProjection(STEWARD_RIVER_NETWORK_NAME);

    // get initial segments
    initStewards();
  }


  /**
   * @param it
   * @return
   */
  private Vector<Steward> filterStewards(Iterable<?> it) {
    Vector<Steward> stewards = new Vector<Steward>();
    for (Object steward : it) {
      if (steward instanceof Steward) {
        stewards.add((Steward) steward);
      }
    }

    // sort them by name
    Collections.sort(stewards, new Comparator<Steward>() {

      @Override
      public int compare(Steward o1, Steward o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    return stewards;
  }



  /**
   * @return
   */
  public void initStewards() {
    stewards = filterStewards(stewardRiverNet.getNodes());
    System.out.println("initSteward: " + stewards);
  }


  /**
   *
   */
  @ScheduledMethod(start = 1, interval = 1, priority = 0)
  public void step() {
    System.out.format("[%.0f] %s\n", RepastEssentials.GetTickCount(), getClass().getSimpleName());
    for (Steward steward : stewards) {
      System.out.format(" I.  Steward '%s' (%d)\n", steward.getName(), steward.getBalance());
      steward.generatePossibleActions();
    }
    for (Steward steward : stewards) {
      System.out.format(" II. Steward '%s' (%d)\n", steward.getName(), steward.getBalance());
      steward.chooseActions();
    }
  }
}

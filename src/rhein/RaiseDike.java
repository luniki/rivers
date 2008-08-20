/**
 *
 */
package rhein;


import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

import com.google.common.base.Predicate;


/**
 * @author mlunzena
 *
 */
public class RaiseDike extends FloodProtection {


  /**
   *
   */
  public RaiseDike() {
  }


  public RaiseDike(Segment segment, Steward payer, int capacity) {
    super(segment, payer, capacity);
  }


  /*
   * (non-Javadoc)
   *
   * @see rhein.Command#execute()
   */
  @Override
  public void execute() {

    // TODO
    if (segment.getMaxDikeCapacity() < segment.getDikeCapacity() + capacity) {
      throw new RuntimeException();
    }

    payer.withdrawMoney(getCost());
    segment.addDikeCapacity(capacity);
  }


  public long getCost() {
    return segment.getLength() * 2 * getCostPerKilometer();
  }


  private int getCostPerKilometer() {
    Parameters p = RunEnvironment.getInstance().getParameters();

    return (int) (((Double) p.getValue("dikeBaseCost")) + capacity
        * ((Double) p.getValue("dikeCostPerCubicMeter")));
  }


  /*
   * (non-Javadoc)
   *
   * @see rhein.FloodProtection#getSubbasinCostEffectiveness()
   */
  @Override
  public double getSubbasinCostEffectiveness() {
    int subbasinLength = segment
        .getLengthOfDownstream(new Predicate<Segment>() {


          public boolean apply(Segment s) {
            return s.getSteward() == payer;
          }
        });
    return getCostEffectiveness() * segment.getLength() / subbasinLength;
  }


  /*
   * (non-Javadoc)
   *
   * @see rhein.FloodProtection#getWholeBasinCostEffectiveness()
   */
  @Override
  public double getWholeBasinCostEffectiveness() {
    return getCostEffectiveness() * segment.getLength()
        / segment.getLengthOfDownstream();
  }
}

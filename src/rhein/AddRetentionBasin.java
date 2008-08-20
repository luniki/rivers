/**
 *
 */
package rhein;

import com.google.common.base.Predicate;


/**
 * @author mlunzena
 *
 */
public class AddRetentionBasin extends FloodProtection {

// TODO was ist eigentlich mit der capacity aus Floodprotection? die interessiert hier nicht, oder was?

  private RetentionBasin retentionBasin;


  /**
   *
   */
  public AddRetentionBasin() {
  }


  public AddRetentionBasin(Segment segment, Steward payer, RetentionBasin retentionBasin) {
    super(segment, payer, retentionBasin.getCapacity());
    this.retentionBasin = retentionBasin;
  }


  /*
   * (non-Javadoc)
   *
   * @see rhein.Command#execute()
   */
  @Override
  public void execute() {

    payer.withdrawMoney(getCost());
    segment.addRetentionBasin(retentionBasin);
    segment.removePossibleRetention(retentionBasin);
  }


  public long getCost() {
    return retentionBasin.getCost();
  }

  /* (non-Javadoc)
   * @see rhein.FloodProtection#getSubbasinCostEffectiveness()
   */
  @Override
  public double getSubbasinCostEffectiveness() {

    int threatenedLength = segment.getLengthOfDownstream(new Predicate<Segment>() {
      public boolean apply(Segment s) {
        return s.getSteward() == payer && s.isThreatened();
      }
    });

    int subbasinLength = segment.getLengthOfDownstream(new Predicate<Segment>() {
      public boolean apply(Segment s) {
        return s.getSteward() == payer;
      }
    });

    return getCostEffectiveness() * threatenedLength / subbasinLength;
  }


  /* (non-Javadoc)
   * @see rhein.FloodProtection#getWholeBasinCostEffectiveness()
   */
  @Override
  public double getWholeBasinCostEffectiveness() {

    int threatenedLength = segment.getLengthOfDownstream(new Predicate<Segment>() {
      public boolean apply(Segment s) {
        return s.isThreatened();
      }
    });

    return getCostEffectiveness() * threatenedLength / segment.getLengthOfDownstream();
  }
}

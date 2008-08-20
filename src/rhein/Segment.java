package rhein;


import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * @author mlunzena
 *
 */
public class Segment extends AbstractSegment {

  private static final int NUMBER_OF_LAST_INFLOWS = 10;


  /**
   *
   */
  protected int dikeCapacity;


  /**
   *
   */
  protected int inflow;


  /**
   *
   */
  protected ArrayList<Integer> lastInflows;


  /**
   *
   */
  protected int length;


  /**
   *
   */
  protected int maxDikeCapacity;


  /**
   *
   */
  protected int minDischarge;


  /**
   *
   */
  protected boolean naturalDike;


  /**
   *
   */
  protected int overflow;


  protected Vector<FloodProtection> possibleActions = new Vector<FloodProtection>();


  /**
   *
   */
  protected Vector<RetentionBasin> possibleRetentionBasins = new Vector<RetentionBasin>();


  /**
   *
   */
  protected int retainable;


  /**
   *
   */
  protected int retained;


  /**
   *
   */
  protected double safety;


  /**
   *
   */
  public Segment() {
    this("Segment", 100, 1000, 400);
  }


  /**
   * Construct a new RiverSegment from its name and length.
   *
   * @param name
   * @param length
   * @param dikeCapacity
   * @param retainable
   */
  public Segment(String name, int length, int dikeCapacity, int retainable) {
    super(name);

    this.length = length;
    this.dikeCapacity = dikeCapacity;
    this.retainable = retainable;

    reset();

    this.lastInflows = new ArrayList<Integer>(0);

    this.minDischarge = 400;
    this.maxDikeCapacity = 20000;
    this.naturalDike = false;
    this.safety = 0.9;
  }


  public void addDikeCapacity(int amount) {
    if (amount + dikeCapacity > maxDikeCapacity) {
      throw new RuntimeException(
          "cannot increase dike capacity beyond maximum capacity");
    }
    int old = this.dikeCapacity;
    dikeCapacity += amount;
    changes.firePropertyChange("dikeCapacity", old, dikeCapacity);
  }


  public void addPossibleAction(FloodProtection action) {
    this.possibleActions.add(action);
  }


  public void addRetentionBasin(RetentionBasin retentionBasin) {
    int old = this.retainable;
    this.retainable += retentionBasin.getCapacity();
    changes.firePropertyChange("retainable", old, this.retainable);
  }


  public void clearPossibleActions() {
    this.possibleActions.clear();
  }


  /**
   *
   */
  public void clearPossibleRetentions() {
    possibleRetentionBasins.clear();
  }


  public void consume(Iterable<AbstractSegment> list) {

    reset();

    int inflow = sumInflowingWater(list);
    setInflow(inflow);
    addToLastInflows(inflow);

    int waterLeft = Math.max(minDischarge, inflow - retainable);

    setRetained(inflow - waterLeft);

    setOverflow(Math.max(0, waterLeft - dikeCapacity));

    setDischarge(waterLeft - (isNaturalDike() ? 0 : overflow));
  }


  /**
   *
   */
  public void generatePossibleRetentionBasin() {

    // TODO 3? wieso 3?
    if (naturalDike || possibleRetentionBasins.size() > 3) {
      return;
    }

    // TODO vielleicht lieber nicht über den generator?
    RetentionBasin retentionBasin = new RetentionBasinGenerator()
        .generateRetentionBasin();

    if (retentionBasin != null) {
      possibleRetentionBasins.add(retentionBasin);
    }
  }


  /**
   * @return
   */
  public int getDikeCapacity() {
    return dikeCapacity;
  }


  public static int getLengthOfSegments(Iterable<Segment> segments) {
    int length = 0;
    for (Segment s : segments) {
      length += s.getLength();
    }
    return length;
  }


  public int getLengthOfDownstream() {
    return getLengthOfSegments(getDownstreamSegments());
  }


  public int getLengthOfDownstream(Predicate<Segment> p) {
    return getLengthOfSegments(getDownstreamSegments(p));
  }


  public Iterable<Segment> getDownstreamSegments() {
    ArrayList<Segment> segments = new ArrayList<Segment>();
    getDownstreamSegments(segments);
    return segments;
  }


  public Iterable<Segment> getDownstreamSegments(Predicate<Segment> p) {
    return Iterables.filter(getDownstreamSegments(), p);
  }


  private void getDownstreamSegments(ArrayList<Segment> segments) {

    segments.add(this);

    try {
      Iterables.getOnlyElement(
          Iterables.filter(RheinHelper.getRiverNetwork().getSuccessors(this),
              Segment.class)).getDownstreamSegments(segments);
    } catch (NoSuchElementException e) {
      // nothing to do, just stop recursing
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Segment " + this
          + " has more than one successor.", e);
    }
  }


  /**
   * @return
   */
  public int getInflow() {
    return inflow;
  }


  /**
   * @return
   */
  public int getLength() {
    return length;
  }


  /**
   * @return the maxDikeCapacity
   */
  public int getMaxDikeCapacity() {
    return maxDikeCapacity;
  }


  /**
   * @return the minDischarge
   */
  public int getMinDischarge() {
    return minDischarge;
  }


  /**
   * @return
   */
  public int getOverflow() {
    return overflow;
  }


  /**
   * @return the possibleActions
   */
  public Vector<FloodProtection> getPossibleActions() {
    return possibleActions;
  }


  /**
   * @return the possibleRetentions
   */
  public Vector<RetentionBasin> getPossibleRetentionBasins() {
    return possibleRetentionBasins;
  }


  /**
   * @return
   */
  public int getRetainable() {
    return retainable;
  }


  /**
   * @return the retained
   */
  public int getRetained() {
    return retained;
  }


  /**
   * @return the safety
   */
  public double getSafety() {
    return safety;
  }


  /**
   * @return
   */
  public Steward getSteward() {
    Vector<Steward> stewards = new Vector<Steward>();
    for (Object o : RheinHelper.getStewardRiverNetwork().getAdjacent(this)) {
      if (o instanceof Steward) {
        stewards.add((Steward) o);
      }
    }
    if (stewards.size() > 1) {
      throw new RuntimeException("segment " + this
          + " has more than one steward " + stewards);
    }
    return stewards.firstElement();
  }


  /**
   * @return
   */
  public Vector<Segment> getTributaries() {
    Vector<Segment> tributaries = new Vector<Segment>();
    for (Object segment : RheinHelper.getRiverNetwork().getPredecessors(this)) {
      if (segment instanceof Segment) {
        tributaries.add((Segment) segment);
      }
    }
    return tributaries;
  }


  /**
   * @return
   */
  public Segment getUpstreamRetentionBasins() {

    // if a direct tributary can build a retention basin, return it
    for (Segment tributary : getTributaries()) {
      if (tributary.getPossibleRetentionBasins().size() > 0) {
        return tributary;
      }
    }

    // else let each tributary search upstream for one
    for (Segment tributary : getTributaries()) {
      Segment upstream = tributary.getUpstreamRetentionBasins();
      if (upstream != null) {
        return upstream;
      }
    }

    // none found
    return null;
  }


  /**
   * @return the naturalDike
   */
  public boolean isNaturalDike() {
    return naturalDike;
  }


  /**
   * @return
   */
  public boolean isThreatened() {
    return overflow > 0 || (getSimpleMovingAverageOfLastInflows() - retainable) >= dikeCapacity * safety;
  }


  /**
   * @param r
   * @return
   */
  public boolean removePossibleAction(FloodProtection f) {
    return possibleActions.remove(f);
  }


  /**
   * @param r
   * @return
   */
  public boolean removePossibleRetention(RetentionBasin r) {
    return possibleRetentionBasins.remove(r);
  }


  /**
   *
   */
  protected void reset() {
    retained = discharge = inflow = overflow = 0;
  }


  /**
   * @param dikeCapacity
   */
  public void setDikeCapacity(int dikeCapacity) {
    int old = this.dikeCapacity;
    this.dikeCapacity = dikeCapacity;
    changes.firePropertyChange("dikeCapacity", old, this.dikeCapacity);
  }


  /**
   * @param inflow
   *          the inflow to set
   */
  protected void setInflow(int inflow) {
    int old = this.inflow;
    this.inflow = inflow;
    changes.firePropertyChange("inflow", old, this.inflow);
  }


  /**
   * @param length
   */
  public void setLength(int length) {
    int old = this.length;
    this.length = length;
    changes.firePropertyChange("length", old, this.length);
  }


  /**
   * @param maxDikeCapacity
   *          the maxDikeCapacity to set
   */
  public void setMaxDikeCapacity(int maxDikeCapacity) {
    this.maxDikeCapacity = maxDikeCapacity;
  }


  /**
   * @param minDischarge
   *          the minDischarge to set
   */
  public void setMinDischarge(int minDischarge) {
    int old = this.minDischarge;
    this.minDischarge = minDischarge;
    changes.firePropertyChange("minDischarge", old, this.minDischarge);
  }


  /**
   * @param naturalDike
   *          the naturalDike to set
   */
  public void setNaturalDike(boolean naturalDike) {
    boolean old = this.naturalDike;
    this.naturalDike = naturalDike;
    changes.firePropertyChange("naturalDike", old, this.naturalDike);
  }


  /**
   * @param overflow
   *          the overflow to set
   */
  protected void setOverflow(int overflow) {
    int old = this.overflow;
    this.overflow = overflow;
    changes.firePropertyChange("overflow", old, this.overflow);
  }


  /**
   * @param retainable
   *          the retainable to set
   */
  public void setRetainable(int retainable) {
    int old = this.retainable;
    this.retainable = retainable;
    changes.firePropertyChange("retainable", old, this.retainable);
  }


  /**
   * @param retained
   *          the retained to set
   */
  protected void setRetained(int retained) {
    int old = this.retained;
    this.retained = retained;
    changes.firePropertyChange("retained", old, this.retained);
  }


  /**
   * @param safety
   *          the safety to set
   */
  public void setSafety(double safety) {
    this.safety = safety;
  }


  /**
   * @param list
   * @return
   */
  protected int sumInflowingWater(Iterable<AbstractSegment> list) {
    int sum = 0;
    for (AbstractSegment segment : list) {
      sum += segment.getDischarge();
    }
    return sum;
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("%s i%d/o%d/d%d/r%d th %s", name, inflow, overflow,
        discharge, retained, isThreatened() ? "y" : "n");
  }


  /**
   * @return the lastInflows
   */
  public ArrayList<Integer> getLastInflows() {
    return lastInflows;
  }

  /**
   * @return
   */
  public int getSimpleMovingAverageOfLastInflows() {
    float result = 0;
    for (Integer i: getLastInflows()) {
      result += i;
    }
    return Math.round(result / getLastInflows().size());
  }

  /**
   * @return the lastInflows
   */
  public void addToLastInflows(Integer inflow) {
    if (lastInflows.size() == NUMBER_OF_LAST_INFLOWS) {
      lastInflows.remove(0);
    }
    lastInflows.add(inflow);
  }


  /**
   * @param lastInflows the lastInflows to set
   */
  protected void setLastInflows(ArrayList<Integer> lastInflows) {
    this.lastInflows = lastInflows;
  }
}
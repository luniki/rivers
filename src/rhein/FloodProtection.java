/**
 *
 */
package rhein;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 * @author mlunzena
 *
 */
abstract public class FloodProtection {


  /**
   *
   */
  protected int capacity;


  /**
   *
   */
  protected final PropertyChangeSupport changes = new PropertyChangeSupport(
      this);


  /**
   *
   */
  protected Steward payer;


  /**
   *
   */
  protected Segment segment;


  /**
   *
   */
  public FloodProtection() {
  }


  /**
   * @param segment
   * @param payer
   */
  public FloodProtection(Segment segment, Steward payer, int capacity) {
    this.segment = segment;
    this.payer = payer;
    this.capacity = capacity;
  }


  public void addPropertyChangeListener(final PropertyChangeListener l) {
    this.changes.addPropertyChangeListener(l);
  }


  /**
   * Send this method to execute the command.
   */
  public abstract void execute();


  /**
   * @return the capacity
   */
  public int getCapacity() {
    return capacity;
  }


  /**
   * @return the capacity per cost
   */
  public double getCostEffectiveness() {
    return ((double) capacity) / ((double) getCost());
  }


  /**
   * @return
   */
  abstract public double getSubbasinCostEffectiveness();


  abstract public double getWholeBasinCostEffectiveness();


  /**
   * @return
   */
  abstract public long getCost();


  /**
   * @return the payer
   */
  public Steward getPayer() {
    return payer;
  }


  /**
   * @return the segment
   */
  public Segment getSegment() {
    return segment;
  }


  public void removePropertyChangeListener(final PropertyChangeListener l) {
    this.changes.removePropertyChangeListener(l);
  }


  /**
   * @param capacity
   *          the capacity to set
   */
  public void setCapacity(int capacity) {
    int old = this.capacity;
    this.capacity = capacity;
    changes.firePropertyChange("capacity", old, capacity);
  }


  /**
   * @param payer
   *          the payer to set
   */
  public void setPayer(Steward payer) {
    Steward old = this.payer;
    this.payer = payer;
    changes.firePropertyChange("payer", old, payer);
  }


  /**
   * @param segment
   *          the segment to set
   */
  public void setSegment(Segment segment) {
    Segment old = this.segment;
    this.segment = segment;
    changes.firePropertyChange("segment", old, segment);
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return this.getClass().getSimpleName() + "[capacity=" + capacity + ",cost="
        + getCost() + ",segment=" + segment.getName() + ", payer="
        + payer.getName() + "]";
  }
}
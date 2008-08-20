/**
 *
 */
package rhein;


/**
 * @author mlunzena
 *
 */
public class RetentionBasin {


  /**
   *
   */
  private int capacity;


  /**
   *
   */
  private double costPerCubicMeterPerSecond;


  /**
   *
   */
  public RetentionBasin() {
    this(100, 4 * 86400 / 1000);
  }


  /**
   * @param capacity
   * @param costPerCubicMeterPerSecond
   */
  public RetentionBasin(int capacity, double costPerCubicMeterPerSecond) {
    this.capacity = capacity;
    this.costPerCubicMeterPerSecond = costPerCubicMeterPerSecond;
  }


  /**
   * @return the capacity
   */
  public int getCapacity() {
    return capacity;
  }


  public long getCost() {
    return Math.round(capacity * this.costPerCubicMeterPerSecond);
  }

  /**
   * @return the costPerCubicMeterPerSecond
   */
  public double getCostPerCubicMeterPerSecond() {
    return costPerCubicMeterPerSecond;
  }


  /**
   * @param capacity the capacity to set
   */
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }


  /**
   * @param costPerCubicMeterPerSecond the costPerCubicMeterPerSecond to set
   */
  public void setCostPerCubicMeterPerSecond(double costPerCubicMeterPerSecond) {
    this.costPerCubicMeterPerSecond = costPerCubicMeterPerSecond;
  }
}

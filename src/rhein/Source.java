package rhein;




/**
 * @author mlunzena
 *
 */
public class Source extends AbstractSegment {


  /**
   *
   */
  protected int meanDischarge;


  /**
   *
   */
  protected int stdDevDischarge;


  /**
   *
   */
  public Source() {
    this("Source", 2000, 350);
  }


  /**
   * @param name
   * @param meanDischarge
   * @param stdDevDischarge
   */
  public Source(String name, int meanDischarge, int stdDevDischarge) {
    super(name);
    this.meanDischarge = meanDischarge;
    this.stdDevDischarge = stdDevDischarge;
  }


  /* (non-Javadoc)
   * @see repastriver.Segment#consume(java.lang.Iterable)
   */
  @Override
  public void consume(Iterable<AbstractSegment> list) {
    discharge = (int) RheinHelper.nextNormalDouble(meanDischarge, stdDevDischarge);
  }


  /**
   * @return
   */
  public int getMeanDischarge() {
    return meanDischarge;
  }


  /**
   * @param minDischarge
   */
  public void setMeanDischarge(int minDischarge) {
    this.meanDischarge = minDischarge;
  }


  /**
   * @return
   */
  public int getStdDevDischarge() {
    return stdDevDischarge;
  }


  /**
   * @param stdDevDischarge
   */
  public void setStdDevDischarge(int stdDevDischarge) {
    this.stdDevDischarge = stdDevDischarge;
  }

  /* (non-Javadoc)
   * @see repastriver.Segment#toString()
   */
  @Override
  public String toString() {
    return String.format("%s %d", getName(), discharge);
  }
}

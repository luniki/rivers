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
abstract public class AbstractSegment {


  /**
   *
   */
  protected final PropertyChangeSupport changes = new PropertyChangeSupport(
      this);


  /**
   *
   */
  protected int discharge;


  /**
   *
   */
  protected String name;


  /**
   *
   */
  public AbstractSegment() {
  }


  /**
   *
   */
  public AbstractSegment(String name) {
    this.name = name;
  }


  public void addPropertyChangeListener(final PropertyChangeListener l) {
    this.changes.addPropertyChangeListener(l);
  }


  /**
   * @param list
   */
  abstract public void consume(Iterable<AbstractSegment> list);


  /**
   * @return
   */
  public int getDischarge() {
    return discharge;
  }


  /**
   * @return
   */
  public String getName() {
    return name;
  }


  public void removePropertyChangeListener(final PropertyChangeListener l) {
    this.changes.removePropertyChangeListener(l);
  }


  /**
   * @param discharge
   *          the discharge to set
   */
  protected void setDischarge(int discharge) {
    this.discharge = discharge;
  }


  /**
   * @param name
   */
  public void setName(String name) {
    String old = this.name;
    this.name = name;
    changes.firePropertyChange("name", old, this.name);
  }
}
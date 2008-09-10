/**
 *
 */
package rhein;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Vector;

import org.drools.QueryResult;
import org.drools.QueryResults;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;

import repast.simphony.space.graph.Network;


/**
 * @author mlunzena
 *
 */
public class Steward {


  /**
   *
   */
  private long balance = 0;


  /**
   *
   */
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);


  /**
   *
   */
  private String name;


  /**
   *
   */
  private RuleBase ruleBase;


  /**
   *
   */
  private StatefulSession session;


  /**
   *
   */
  public Steward() {
    this("", 200000);
  }


  /**
   * Bean constructor.
   */
  public Steward(String name, long balance) {

    this.name = name;
    setBalance(balance);

    loadRuleFile();
  }


  public void addPropertyChangeListener(final PropertyChangeListener l) {
    this.changes.addPropertyChangeListener(l);
  }


  // @ScheduledMethod(start = 1, interval = 1, priority = -1, shuffle = false)
  public void chooseActions() {

    session = ruleBase.newStatefulSession();
    session.insert(this, true);

    for (Segment segment : getSegments()) {
      session.insert(segment, true);
    }

    session.startProcess("ACTION_SELECTION_RULEFLOW");
    session.fireAllRules();
    session.dispose();

    for (Segment s : getSegments()) {
      if (s.getPossibleActions().size() > 0) {
        FloodProtection construct = s.getPossibleActions().firstElement();
        construct.execute();
        System.out.format("  >>> constructed %s\n", construct);
        s.clearPossibleActions();
      }
    }
  }


  // @ScheduledMethod(start = 1, interval = 1, priority = 0, shuffle = false)
  public void generatePossibleActions() {


    session = ruleBase.newStatefulSession();
    session.insert(this, true);

    for (Segment segment : getSegments()) {
      session.insert(segment, true);
    }
    
    // add configuration variables

    session.startProcess("ACTION_GENERATION_RULEFLOW");
    session.fireAllRules();

    QueryResults results = session.getQueryResults("all floodprotections");

    for (Iterator<?> it = results.iterator(); it.hasNext();) {
      QueryResult result = (QueryResult) it.next();
      FloodProtection floodProtection = (FloodProtection) result
          .get("floodprotection");
      floodProtection.getSegment().addPossibleAction(floodProtection);
      System.out.format("  >>> set possible action %s\n", floodProtection);
    }

    session.dispose();
  }


  /**
   * @return
   */
  public long getBalance() {
    return balance;
  }


  /**
   * @return the name
   */
  public String getName() {
    return name;
  }


  /**
   * @return
   */
  public Vector<Segment> getSegments() {
    Network<Object> net = RheinHelper.getStewardRiverNetwork();

    Vector<Segment> segments = new Vector<Segment>();
    for (Object o : net.getAdjacent(this)) {
      if (o instanceof Segment) {
        segments.add((Segment) o);
      }
    }
    return segments;
  }


  /**
   *
   */
  private void loadRuleFile() {
    try {

      ClassLoader classLoader = Steward.class.getClassLoader();

      PackageBuilderConfiguration configuration = new PackageBuilderConfiguration(
          classLoader);
      PackageBuilder builder = new PackageBuilder(configuration);


      Reader rulesFile = new InputStreamReader(Steward.class
          .getResourceAsStream("../Steward.drl"));

      builder.addPackageFromDrl(rulesFile);


      Reader ruleFlowFile1 = new InputStreamReader(Steward.class
          .getResourceAsStream("../ActionGeneration.rfm"));
      Reader ruleFlowFile2 = new InputStreamReader(Steward.class
          .getResourceAsStream("../ActionSelection.rfm"));

      builder.addRuleFlow(ruleFlowFile1);
      builder.addRuleFlow(ruleFlowFile2);


      if (builder.hasErrors()) {
        System.out.println(builder.getErrors().toString());
        throw new RuntimeException("Unable to compile rule file.");
      }


      RuleBaseConfiguration ruleBaseConfiguration = new RuleBaseConfiguration(
          classLoader);

      ruleBase = RuleBaseFactory.newRuleBase(ruleBaseConfiguration);
      ruleBase.addPackage(builder.getPackage());


    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }


  public void removePropertyChangeListener(final PropertyChangeListener l) {
    this.changes.removePropertyChangeListener(l);
  }


  /**
   * @param amount
   */
  public void depositMoney(long amount) {
    this.balance += amount;
  }


  /**
   * @param balance
   */
  public void setBalance(long balance) {
    this.balance = balance;
  }


  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    String old = this.name;
    this.name = name;
    changes.firePropertyChange("name", old, name);
  }


  /**
   * @param amount
   */
  public void withdrawMoney(long amount) {
    if (balance < amount) {
      // TODO
      throw new RuntimeException("insufficient funds");
    }
    this.balance -= amount;
  }


  public String toString() {
    return "Steward[name=" + name + ", balance=" + balance + "]";
  }
}

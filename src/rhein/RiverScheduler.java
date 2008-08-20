package rhein;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;


/**
 * @author mlunzena
 *
 */
public class RiverScheduler {


  /**
   * The network projection where to search for river segments.
   */
  private Network<Object> riverNet;


  /**
   *
   */
  private Vector<AbstractSegment> segments;


  /**
   *
   */
  private Network<Object> stewardRiverNet;


  /**
   * @param it
   * @return
   */
  private Vector<AbstractSegment> filterSegments(Iterable<?> it) {
    Vector<AbstractSegment> segments = new Vector<AbstractSegment>();
    for (Object segment : it) {
      if (segment instanceof AbstractSegment) {
        segments.add((AbstractSegment) segment);
      }
    }

    // sort them by name
    Collections.sort(segments, new Comparator<AbstractSegment>() {


      @Override
      public int compare(AbstractSegment o1, AbstractSegment o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    return segments;
  }


  /**
   *
   */
  @ScheduledMethod(start = 0)
  public void initialize() {

    riverNet = RheinHelper.getRiverNetwork();

    stewardRiverNet = RheinHelper.getStewardRiverNetwork();

    // get initial segments
    segments = sortSegments();

    // listen to network
    riverNet.addProjectionListener(new ProjectionListener<Network<Object>>() {


      @Override
      public void projectionEventOccurred(ProjectionEvent<Network<Object>> evt) {
        if (evt.getType() == ProjectionEvent.Type.EDGE_ADDED
            || evt.getType() == ProjectionEvent.Type.EDGE_REMOVED) {
          segments = sortSegments();
        }
      }
    });
  }


  /**
   * @return
   */
  public Vector<AbstractSegment> sortSegments() {

    HashMap<AbstractSegment, Integer> d = new HashMap<AbstractSegment, Integer>();
    Stack<AbstractSegment> c = new Stack<AbstractSegment>();

    for (AbstractSegment agent : filterSegments(riverNet.getNodes())) {
      if (riverNet.getOutDegree(agent) > 1) {
        throw new RuntimeException("more than one outflow..");
      }
      d.put((AbstractSegment) agent, riverNet.getInDegree(agent));
      if (riverNet.getInDegree(agent) == 0) {
        c.add((AbstractSegment) agent);
      }
    }

    int steps = 0;
    Vector<AbstractSegment> segments = new Vector<AbstractSegment>();
    while (c.size() > 0) {
      AbstractSegment i = c.pop();
      segments.add(i);
      steps++;
      for (Object agent : riverNet.getSuccessors(i)) {
        Integer value = d.get(agent);
        d.put((AbstractSegment) agent, --value);
        if (value == 0) {
          c.add((AbstractSegment) agent);
        }
      }
    }

    if (d.size() != steps) {
      throw new RuntimeException("there is a cycle");
    }

    return segments;
  }


  /**
   *
   */
  @ScheduledMethod(start = 1, interval = 1, priority = 100)
  public void step() {

    System.out.format("[%.0f] %s\n", RepastEssentials.GetTickCount(), getClass().getSimpleName());

    for (AbstractSegment segment : segments) {

      feedSegment(segment);

      if (segment instanceof Segment) {

        // TODO sollte das in einer eigenen klasse stecken?
        payStewards(segment);

        ((Segment) segment).generatePossibleRetentionBasin();
      }

      System.out.format(" Segment '%s'\n", segment);

    }
  }


  /**
   * @param segment
   */
  private void payStewards(AbstractSegment segment) {
    int stewardPayment = (Integer) RunEnvironment.getInstance().getParameters().getValue("stewardPayment");
    Steward steward = null;
    for (RepastEdge<?> edge : stewardRiverNet.getEdges(segment)) {
      if (edge.getSource() instanceof Steward) {
        steward = (Steward) edge.getSource();
      } else if (edge.getTarget() instanceof Steward) {
        steward = (Steward) edge.getTarget();
      } else {
        continue;
      }
      steward.depositMoney(stewardPayment);
    }
  }


  /**
   * @param segment
   */
  private void feedSegment(AbstractSegment segment) {
    segment.consume(filterSegments(riverNet.getPredecessors(segment)));
  }
}

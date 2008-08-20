package rhein;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import repast.simphony.visualization.visualization2D.style.Style2D;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.nodes.PComposite;


public class CStyle implements Style2D<Segment> {


  private HashMap<Segment, PComposite> composites = new HashMap<Segment, PComposite>();


  public CStyle() {
  }


  public CStyle(Paint p) {
  }


  // TODO da muss noch mehr hin
  public Rectangle2D getBounds(Segment object) {

    double inflow = object.getInflow() / 200.0;
    double discharge = object.getDischarge() / 200.0;
    double retained  = object.getRetained() / 200.0;
    double overflow = object.getOverflow() / 200.0;

    double width = object.getDikeCapacity() / 200.0;

    composites.get(object).getChild(0).setBounds(
        new Rectangle2D.Double(0, 0, width, discharge));

    composites.get(object).getChild(1).setBounds(
        new Rectangle2D.Double(0, discharge, width, retained));

    composites.get(object).getChild(2).setBounds(
        new Rectangle2D.Double(0, discharge + retained, width, overflow));

    PText label = ((PText) composites.get(object).getChild(3));
    label.setText(object.toString());
    label.setOffset(width + 5, 0);

    return new Rectangle2D.Double(0, 0, width, inflow);
  }


  /*
   * (non-Javadoc)
   *
   * @see repast.simphony.visualization.visualization2D.style.Style2D#getLabel(java.lang.Object)
   */
  @Override
  public PText getLabel(Segment object) {
    return null;
  }


  public Paint getPaint(Segment object) {
    return Color.WHITE;
  }


  public PNode getPNode(Segment object, PNode node) {

    PComposite c = new PComposite();
    c.setPaint(Color.WHITE);

    c.setBounds(new Rectangle2D.Float(0, 0, 100, 100));
    c.setOffset(0, 0);

    composites.put(object, c);

    PPath discharge = new PPath(new Rectangle2D.Float(-5, -5, 10, 10));
    discharge.setBounds(new Rectangle2D.Float(-5, -5, 10, 10));
    discharge.setPaint(Color.BLUE);
    discharge.setStrokePaint(null);
    c.addChild(0, discharge);

    PPath retained = new PPath(new Rectangle2D.Float(0, 0, 5, 5));
    retained.setBounds(new Rectangle2D.Float(0, 0, 5, 5));
    retained.setPaint(Color.GREEN);
//    retained.setTransparency(0.5f);
    retained.setStrokePaint(null);
    c.addChild(1, retained);

    PPath overflow = new PPath(new Rectangle2D.Float(0, 0, 5, 5));
    overflow.setBounds(new Rectangle2D.Float(0, 0, 5, 5));
    overflow.setPaint(Color.RED);
//    overflow.setTransparency(0.5f);
    overflow.setStrokePaint(null);
    c.addChild(2, overflow);

    PText label = new PText(object.toString());
    label.setOffset(105, 0);
    label.transformBy(AffineTransform.getScaleInstance(1, -1));
    c.addChild(3, label);

    return c;
  }


  public double getRotation(Segment object) {
    return 0.0;
  }


  public Stroke getStroke(Segment object) {
    return new BasicStroke(1);
  }


  public Paint getStrokePaint(Segment object) {
    return Color.BLACK;
  }


  public boolean isScaled(Segment object) {
    return true;
  }


  public void setBounds(Rectangle2D bounds) {
  }


  public void setPaint(Paint p) {
  }


  public void setRotation(double rot) {
  }


  public void setStroke(Stroke stroke) {
  }
}

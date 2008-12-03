package rhein;


import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;


/**
 * @author mlunzena
 *
 */
public class StrategyContext implements ContextBuilder<Object> {


  /**
   * @see repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context.Context)
   */
  @SuppressWarnings("unchecked")
  @Override
  public Context build(Context<Object> context) {

	Parameters p = RunEnvironment.getInstance().getParameters();

    // get strategy
	String strategy = (String)p.getValue("contextBuilder");

	ContextBuilder<Object> builder = strategy.equals("rhein") ? new RheinContext() : new RandomContext();
	
	return builder.build(context);
  }

}

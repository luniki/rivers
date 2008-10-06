package rhein;


import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;


/**
 * @author mlunzena
 *
 */
public class RandomContext implements ContextBuilder<Object> {


  /**
   * @see repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context.Context)
   */
  @Override
  public Context<Object> build(Context<Object> context) {

	Parameters p = RunEnvironment.getInstance().getParameters();
	
	int seed = (Integer)p.getValue("rheinHelperSeed");
	
    RheinHelper.init(seed);

    Network<Object> stewardsRiversNet = NetworkFactoryFinder
        .createNetworkFactory(null).createNetwork("StewardsRivers", context,
            true);

    Network<Object> riversNet = NetworkFactoryFinder.createNetworkFactory(null)
        .createNetwork("Rivers", context, true);


    Network<Object> stewardsNet = NetworkFactoryFinder.createNetworkFactory(
        null).createNetwork("Stewards", context, true);

    Grid<Object> grid = GridFactoryFinder.createGridFactory(null).createGrid(
        "Grid",
        context,
        GridBuilderParameters.singleOccupancy2D(new RandomGridAdder<Object>(),
            new StickyBorders(), 100, 100));


    // //////////////////////////////
    //
    // RIVERS & SEGMENTS
    //
    // //////////////////////////////

    int RIVERS = 5;
    int SEGMENTS = 5;

    Steward stewards[][] = new Steward[RIVERS][SEGMENTS];
    for (int i = 0; i < RIVERS; i++) {
      for (int j = 0; j < SEGMENTS; j++) {
        stewards[i][j] = new Steward("Steward " + i + "-" + j, (Integer)p.getValue("stewardStartBalance"));
        context.add(stewards[i][j]);

        grid.moveTo(stewards[i][j], 99, i * RIVERS + j);

        for (int k = 0; k < j; k++) {
          stewardsNet.addEdge(stewards[i][j], stewards[i][k]);
        }
      }
    }

    for (int i = 0; i < RIVERS; i++) {
      for (int j = 0; j < SEGMENTS; j++) {
        int river = RheinHelper.nextIntFromTo(0, RIVERS - 1);
        int segment = RheinHelper.nextIntFromTo(0, SEGMENTS - 1);
        if (i != river) {
          if (!stewardsNet.isAdjacent(stewards[i][j], stewards[river][segment])) {
            stewardsNet.addEdge(stewards[i][j], stewards[river][segment]);
          }
        }
      }
    }

    Segment last;
    for (int i = 0; i < RIVERS; i++) {
      last = null;
      for (int j = 0; j < SEGMENTS; j++) {
        int mean = RheinHelper.nextIntFromTo(1500, 4000);
        int stddev = RheinHelper.nextIntFromTo(250, 650);
        Source source = new Source("Souce " + i + "-" + j, mean, stddev);

        int length = RheinHelper.nextIntFromTo(50, 250);
        int capacity = RheinHelper.nextIntFromTo(5, 15) * 1000;

        Segment segment = new Segment("Segment " + i + "-" + j, length,
            capacity, 0);
        segment.generatePossibleRetentionBasin();
        segment.generatePossibleRetentionBasin();
        segment.generatePossibleRetentionBasin();
        
        context.add(source);
        context.add(segment);

        grid.moveTo(source,  i * 10, j * 10 + i);
        grid.moveTo(segment, i * 10 + 5, j * 10 + 5 + i);


        riversNet.addEdge(source, segment);
        if (last != null) {
          riversNet.addEdge(last, segment);
        }
        last = segment;
        stewardsRiversNet.addEdge(stewards[i][j], segment);
      }
    }

    // SCHEDULERS
    RiverScheduler riverScheduler = new RiverScheduler();
    context.add(riverScheduler);

    StewardScheduler stewardScheduler = new StewardScheduler();
    context.add(stewardScheduler);

    return context;
  }

}

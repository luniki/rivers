package rhein;


import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StickyBorders;


/**
 * @author mlunzena
 *
 */
public class RheinContext implements ContextBuilder<Object> {


  /**
   * @see repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context.Context)
   */
  @Override
  public Context<Object> build(Context<Object> context) {

    RheinHelper.init();

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
            new StickyBorders(), 40, 40));

    // //////////////////////////////
    //
    // RIVERS
    //
    // //////////////////////////////

    Source basel = new Source("Rhine (Basel)", 3817, 636);
    Source neckar = new Source("Neckar", 2017, 336);
    Source main = new Source("Main", 1507, 251);
    Source lahn_nahe = new Source("Lahn/Nahe", 1380, 230);
    Source mosel = new Source("Mosel", 3127, 521);
    Source lippe_ruhr_sieg = new Source("Lippe/Ruhr/Sieg", 1732, 289);

    context.add(basel);
    context.add(neckar);
    context.add(main);
    context.add(lahn_nahe);
    context.add(mosel);
    context.add(lippe_ruhr_sieg);

    grid.moveTo(basel, 0, 35);
    grid.moveTo(neckar, 0, 30);
    grid.moveTo(main, 0, 25);
    grid.moveTo(lahn_nahe, 0, 20);
    grid.moveTo(mosel, 0, 15);
    grid.moveTo(lippe_ruhr_sieg, 0, 10);

    Segment upper_rhine_1 = new Segment("Oberrhein (Basel)", 256, 5000, 756);
    Segment upper_rhine_2 = new Segment("Oberrhein (Neckar)", 50, 6000, 0);
    Segment upper_rhine_3 = new Segment("Oberrhein (Main)", 66, 7200, 0);

    Segment lower_rhine_1 = new Segment("Unterrhein", 54, 8000, 0);
    lower_rhine_1.setMaxDikeCapacity(8000);
    lower_rhine_1.setNaturalDike(true);
    Segment lower_rhine_2 = new Segment("Unterrhein (Lahn/Mosel)", 110, 10000, 0);
    lower_rhine_2.setMaxDikeCapacity(10000);
    lower_rhine_2.setNaturalDike(true);
    Segment lower_rhine_3 = new Segment("Unterrhein (Sieg/Ruhr/Lippe)", 142, 13300, 0);

    Segment rijn = new Segment("Rijn", 148, 15000, 0);

    context.add(upper_rhine_1);
    context.add(upper_rhine_2);
    context.add(upper_rhine_3);
    context.add(lower_rhine_1);
    context.add(lower_rhine_2);
    context.add(lower_rhine_3);
    context.add(rijn);

    grid.moveTo(upper_rhine_1, 10, 39);
    grid.moveTo(upper_rhine_2, 10, 32);
    grid.moveTo(upper_rhine_3, 10, 25);
    grid.moveTo(lower_rhine_1, 10, 21);
    grid.moveTo(lower_rhine_2, 10, 14);
    grid.moveTo(lower_rhine_3, 10,  7);
    grid.moveTo(rijn,          10,  0);

    riversNet.addEdge(upper_rhine_1, upper_rhine_2);
    riversNet.addEdge(upper_rhine_2, upper_rhine_3);
    riversNet.addEdge(upper_rhine_3, lower_rhine_1);

    riversNet.addEdge(lower_rhine_1, lower_rhine_2);
    riversNet.addEdge(lower_rhine_2, lower_rhine_3);
    riversNet.addEdge(lower_rhine_3, rijn);


    riversNet.addEdge(basel, upper_rhine_1);
    riversNet.addEdge(neckar, upper_rhine_2);
    riversNet.addEdge(main, upper_rhine_3);

    riversNet.addEdge(lahn_nahe, lower_rhine_2);
    riversNet.addEdge(mosel, lower_rhine_2);
    riversNet.addEdge(lippe_ruhr_sieg, lower_rhine_3);

    RiverScheduler riverScheduler = new RiverScheduler();
    context.add(riverScheduler);

    grid.moveTo(riverScheduler, 39, 0);


    // //////////////////////////////
    //
    // STEWARDS
    //
    // //////////////////////////////

    Steward s1 = new Steward("upper_rhine", 200000);
    Steward s2 = new Steward("lower_rhine", 200000);
    Steward s3 = new Steward("rijn", 200000);

    context.add(s1);
    context.add(s2);
    context.add(s3);

    grid.moveTo(s1, 35, 39);
    grid.moveTo(s2, 35, 21);
    grid.moveTo(s3, 35,  0);

    stewardsRiversNet.addEdge(s1, upper_rhine_1);
    stewardsRiversNet.addEdge(s1, upper_rhine_2);
    stewardsRiversNet.addEdge(s1, upper_rhine_3);
    stewardsRiversNet.addEdge(s2, lower_rhine_1);
    stewardsRiversNet.addEdge(s2, lower_rhine_2);
    stewardsRiversNet.addEdge(s2, lower_rhine_3);
    stewardsRiversNet.addEdge(s3, rijn);

    StewardScheduler stewardScheduler = new StewardScheduler();
    context.add(stewardScheduler);

    grid.moveTo(stewardScheduler, 39, 1);

    return context;
  }

}

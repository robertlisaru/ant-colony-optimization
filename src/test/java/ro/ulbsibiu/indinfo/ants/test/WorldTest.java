package ro.ulbsibiu.indinfo.ants.test;

import org.junit.Test;
import ro.ulbsibiu.indinfo.ants.World;

import static org.junit.Assert.assertTrue;

public class WorldTest {

    final int numCities = 6;
    final int[][] distances = {
            {0, 10, 15, 15, 15, 10},
            {10, 0, 10, 15, 15, 15},
            {15, 10, 0, 10, 15, 15},
            {15, 15, 10, 0, 10, 15},
            {15, 15, 15, 10, 0, 10},
            {10, 15, 15, 15, 10, 0}
    };
    private final int numAnts = 15;
    private final double initialPheromoneIntensity = 0.001;
    private final double evaporationRate = 0.5;
    private final double pheromoneIncrease = 100;
    private final double pheromoneInfluence = 1;
    private final double visibilityInfluence = 1;
    private final double bestPathPheromoneIncreaseFactor = 5;

    @Test
    public void worldTest() {
        World world = new World(numCities, numAnts, initialPheromoneIntensity,
                distances, evaporationRate, pheromoneIncrease, pheromoneInfluence,
                visibilityInfluence, bestPathPheromoneIncreaseFactor);

        for (int iteration = 0; iteration < 10; iteration++) {
            int minDistance = world.iterate();
            assertTrue(minDistance >= 50 && minDistance <= 75);
        }
    }
}

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
    private final double evaporationPercent = 0.5;
    private final double pheromoneIncrease = 100;
    private final double pheromoneExponent = 1;
    private final double visibilityExponent = 1;
    private final double bestPathPheromoneIncreaseFactor = 5;

    @Test
    public void worldTest() {
        World world = new World(numCities, numAnts,
                distances, evaporationPercent, pheromoneIncrease, pheromoneExponent,
                visibilityExponent, bestPathPheromoneIncreaseFactor);

        for (int iteration = 0; iteration < 10; iteration++) {
            int minDistance = world.iterate();
            world.resetAnts();
            assertTrue(minDistance >= 50 && minDistance <= 75);
        }
    }
}

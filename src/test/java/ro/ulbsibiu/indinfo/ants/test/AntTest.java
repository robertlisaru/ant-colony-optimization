package ro.ulbsibiu.indinfo.ants.test;

import org.junit.Test;
import ro.ulbsibiu.indinfo.ants.Ant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AntTest {
    final int numCities = 3;

    final int[][] distances = {
            {0, 10, 20},
            {10, 0, 30},
            {20, 30, 0},
    };

    final double[][] pheromones = {
            {0.1, 0.1, 0.1},
            {0.1, 0.1, 0.1},
            {0.1, 0.1, 0.1}
    };

    @Test
    public void checkBooleanArrayDefaultInitialization() {
        boolean[] isVisited = new boolean[10];
        for (int i = 0; i < isVisited.length; i++) {
            assertFalse(isVisited[i]);
        }
    }

    @Test
    public void antTest() {
        Ant ant = new Ant(pheromones, distances, numCities, 1, 1);
        int[] path = ant.getPath();
        assertTrue(0 <= path[0] && path[0] < numCities);

        for (int testIteration = 0; testIteration < 3; testIteration++) {
            ant.runTour();
            path = ant.getPath();
            assertEquals(numCities, path.length);
            int pathDistance = ant.getPathDistance();
            int expectedPathDistance = 0;
            for (int i = 0; i < path.length - 1; i++) {
                expectedPathDistance += distances[path[i]][path[i + 1]];
                assertTrue(0 <= path[i] && path[i] < numCities);
            }
            assertEquals(expectedPathDistance, pathDistance);
            ant.reset();
        }

    }
}

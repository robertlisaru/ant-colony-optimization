package ro.ulbsibiu.indinfo.ants;

import java.util.Arrays;

public class World {

    private final static double initialPheromoneIntensity = 0.001;
    private final int numCities;
    private final int numAnts;
    private final Ant[] ants;
    private final double[][] pheromoneMap;
    private final double evaporationProcent;
    private final double pheromoneIncrease;
    private final double minPathPheromoneIncreaseFactor;
    private int[] minPath = null;

    public World(final int numCities, final int numAnts, int[][] distances,
                 double evaporationPercent, double pheromoneIncrease, double pheromoneExponent, double visibilityExponent,
                 double minPathPheromoneIncreaseFactor) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.evaporationProcent = evaporationPercent;
        this.pheromoneIncrease = pheromoneIncrease;
        this.minPathPheromoneIncreaseFactor = minPathPheromoneIncreaseFactor;

        pheromoneMap = new double[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < i; j++) {
                pheromoneMap[i][j] = initialPheromoneIntensity;
                pheromoneMap[j][i] = initialPheromoneIntensity;
            }
        }

        ants = new Ant[numAnts];
        for (int i = 0; i < numAnts; i++) {
            ants[i] = new Ant(pheromoneMap, distances, numCities, pheromoneExponent, visibilityExponent);
        }
    }

    public int iterate() {
        int minDistance = Integer.MAX_VALUE;
        for (int k = 0; k < numAnts; k++) {
            ants[k].runTour();
            int pathDistance = ants[k].getPathDistance();
            if (pathDistance < minDistance) {
                minDistance = pathDistance;
                minPath = ants[k].getPath();
            }
        }

        //region Update pheromoneMap
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromoneMap[i][j] *= (1.0 - evaporationProcent);
                pheromoneMap[j][i] *= (1.0 - evaporationProcent);
            }
        }
        for (int k = 0; k < numAnts; k++) {
            int[] path = ants[k].getPath();
            for (int i = 0; i < path.length - 1; i++) {
                pheromoneMap[path[i]][path[i + 1]] += (pheromoneIncrease / ants[k].getPathDistance());
                pheromoneMap[path[i + 1]][path[i]] += (pheromoneIncrease / ants[k].getPathDistance());
            }
        }
        for (int i = 0; i < minPath.length - 1; i++) {
            pheromoneMap[minPath[i]][minPath[i + 1]] += (pheromoneIncrease / minDistance)
                    * minPathPheromoneIncreaseFactor;
            pheromoneMap[minPath[i + 1]][minPath[i]] += (pheromoneIncrease / minDistance)
                    * minPathPheromoneIncreaseFactor;
        }
        //endregion

        return minDistance;
    }

    public int[] getCopyOfMinPath() {
        return Arrays.copyOf(minPath, numCities);
    }

    public void resetAnts() {
        for (int k = 0; k < numAnts; k++) {
            ants[k].reset();
        }
    }

    public double[][] getPheromoneMap() {
        return pheromoneMap;
    }
}

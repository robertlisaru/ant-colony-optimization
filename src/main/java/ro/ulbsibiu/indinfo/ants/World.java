package ro.ulbsibiu.indinfo.ants;

import static ro.ulbsibiu.indinfo.ants.Util.printArray;

public class World {

    private final int numCities;
    private final int numAnts;
    private final Ant[] ants;
    private final double[][] pheromoneMap;
    private final double evaporationProcent;
    private final double pheromoneIncrease;
    private final double bestPathPheromoneIncreaseFactor;

    public World(final int numCities, final int numAnts, final double initialPheromoneIntensity, int[][] distances,
                 double evaporationPercent, double pheromoneIncrease, double pheromoneExponent, double visibilityExponent,
                 double bestPathPheromoneIncreaseFactor) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.evaporationProcent = evaporationPercent;
        this.pheromoneIncrease = pheromoneIncrease;
        this.bestPathPheromoneIncreaseFactor = bestPathPheromoneIncreaseFactor;

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
        int[] bestPath = null;
        for (int k = 0; k < numAnts; k++) {
            ants[k].runTour();
            int pathDistance = ants[k].getPathDistance();
            if (pathDistance < minDistance) {
                minDistance = pathDistance;
                bestPath = ants[k].getPath();
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
            System.out.print("ant " + k + ": ");
            printArray(path, path.length);
            System.out.print(" distance:" + ants[k].getPathDistance() + "\n");
            for (int i = 0; i < path.length - 1; i++) {
                pheromoneMap[path[i]][path[i + 1]] += (pheromoneIncrease / ants[k].getPathDistance());
                pheromoneMap[path[i + 1]][path[i]] += (pheromoneIncrease / ants[k].getPathDistance());
            }
        }
        for (int i = 0; i < bestPath.length - 1; i++) {
            pheromoneMap[bestPath[i]][bestPath[i + 1]] += (pheromoneIncrease / minDistance)
                    * bestPathPheromoneIncreaseFactor;
            pheromoneMap[bestPath[i + 1]][bestPath[i]] += (pheromoneIncrease / minDistance)
                    * bestPathPheromoneIncreaseFactor;
        }
        //endregion

        for (int k = 0; k < numAnts; k++) {
            ants[k].reset();
        }
        System.out.println("Min distance: " + minDistance);
        Util.printMatrix(pheromoneMap, numCities, numCities);
        return minDistance;
    }

    public double[][] getPheromoneMap() {
        return pheromoneMap;
    }
}

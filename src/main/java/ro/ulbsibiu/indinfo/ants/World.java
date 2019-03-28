package ro.ulbsibiu.indinfo.ants;

import static ro.ulbsibiu.indinfo.ants.Util.printArray;

public class World {

    private final int numCities;
    private final int numAnts;
    private final double initialPheromoneIntensity;
    private final int[][] distances;
    private final Ant[] ants;
    private final double[][] pheromoneMap;
    private final double evaporationRate;
    private final double pheromoneIncrease;
    private final double pheromoneInfluence;
    private final double visibilityInfluence;
    private final double bestPathPheromoneIncreaseFactor;

    public World(final int numCities, final int numAnts, final double initialPheromoneIntensity, int[][] distances, double evaporationRate, double pheromoneIncrease, double pheromoneInfluence, double visibilityInfluence, double bestPathPheromoneIncreaseFactor) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.initialPheromoneIntensity = initialPheromoneIntensity;
        this.distances = distances;
        this.evaporationRate = evaporationRate;
        this.pheromoneIncrease = pheromoneIncrease;
        this.pheromoneInfluence = pheromoneInfluence;
        this.visibilityInfluence = visibilityInfluence;
        this.bestPathPheromoneIncreaseFactor = bestPathPheromoneIncreaseFactor;

        pheromoneMap = new double[numCities][numCities];
        ants = new Ant[numAnts];
        for (int i = 0; i < numAnts; i++) {
            ants[i] = new Ant(pheromoneMap, this.distances, numCities, pheromoneInfluence, visibilityInfluence);
        }

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < i; j++) {
                pheromoneMap[i][j] = this.initialPheromoneIntensity;
                pheromoneMap[j][i] = this.initialPheromoneIntensity;
            }
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
                pheromoneMap[i][j] *= (1.0 - evaporationRate);
                pheromoneMap[j][i] *= (1.0 - evaporationRate);
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
            pheromoneMap[bestPath[i]][bestPath[i + 1]] += (pheromoneIncrease / minDistance) * bestPathPheromoneIncreaseFactor;
            pheromoneMap[bestPath[i + 1]][bestPath[i]] += (pheromoneIncrease / minDistance) * bestPathPheromoneIncreaseFactor;
        }
        //endregion

        for (int k = 0; k < numAnts; k++) {
            ants[k].reset();
        }
        System.out.println("Min distance: " + minDistance);
        Util.printMatrix(pheromoneMap, numCities, numCities);
        return minDistance;
    }

}

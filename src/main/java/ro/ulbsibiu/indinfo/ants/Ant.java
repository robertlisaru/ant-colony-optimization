package ro.ulbsibiu.indinfo.ants;

import java.util.Random;

import static java.lang.Math.pow;

public class Ant {
    private static final Random random = new Random(System.currentTimeMillis());
    private final double[][] pheromoneMap;
    private final int[][] distances;
    private final int numCities;
    private final int[] path;
    private final double[] cityScores; //re-written at each city
    private final double pheromoneInfluence;
    private final double visibilityInfluence;
    private int pathCityCount = 0;
    private int pathDistance = 0;
    private boolean[] isVisited;

    public Ant(final double[][] pheromoneMap, final int[][] distances, final int numCities
            , double pheromoneInfluence, double visibilityInfluence) {
        this.pheromoneMap = pheromoneMap;
        this.distances = distances;
        this.numCities = numCities;
        this.pheromoneInfluence = pheromoneInfluence;
        this.visibilityInfluence = visibilityInfluence;

        path = new int[numCities];
        isVisited = new boolean[numCities];
        cityScores = new double[numCities];

        int startingCity = random.nextInt(numCities);
        path[pathCityCount++] = startingCity;
        isVisited[startingCity] = true;
    }

    private int chooseNextCity() {
        int currentCity = path[pathCityCount - 1];
        double totalScore = 0.0;

        //region compute scores for next city candidates
        for (int city = 0; city < numCities; city++) {
            if (isVisited[city]) {
                cityScores[city] = 0.0;
            } else {
                double visibility = 1.0 / distances[currentCity][city];
                cityScores[city] = pow(pheromoneMap[currentCity][city], pheromoneInfluence)
                        * pow(visibility, visibilityInfluence);
                totalScore += cityScores[city];
            }
        }
        //endregion

        double roulette = random.nextDouble();
        double cumulativeSum = 0.0;
        for (int city = 0; city < numCities; city++) {
            double probability = cityScores[city] / totalScore;
            cumulativeSum += probability;
            if (roulette < cumulativeSum) {
                return city;
            }
        }

        return -1;
    }

    private void walkToCity(int city) {
        int currentCity = path[pathCityCount - 1];
        pathDistance += distances[currentCity][city];
        path[pathCityCount++] = city;
        isVisited[city] = true;
    }

    public void runTour() {
        while (pathCityCount < numCities) {
            int nextCity = chooseNextCity();
            walkToCity(nextCity);
        }
    }

    public int[] getPath() {
        return path;
    }

    public int getPathDistance() {
        return pathDistance;
    }

    public void reset() {
        pathCityCount = 0;
        pathDistance = 0;
        path[pathCityCount++] = random.nextInt(numCities);
        isVisited = new boolean[numCities];
        isVisited[path[0]] = true;
    }
}

package ro.ulbsibiu.indinfo.ants;

import java.util.Random;

public class Ant {
    private double[][] pheromones;
    private int[][] distances;
    private int numCities;
    private int[] path;
    private int pathCityCount = 0;
    private int pathDistance = 0;
    private boolean[] isVisited;
    private double[] cityScores; //re-written at each city
    private Random random;

    public Ant(double[][] pheromones, int[][] distances, int numCities) {
        this.pheromones = pheromones;
        this.distances = distances;
        this.numCities = numCities;

        path = new int[numCities];
        isVisited = new boolean[numCities];
        cityScores = new double[numCities];

        random = new Random(System.currentTimeMillis());
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
                cityScores[city] = pheromones[currentCity][city] * visibility;
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
        path[pathCityCount++] = random.nextInt(numCities);
        isVisited = new boolean[numCities];
        isVisited[path[0]] = true;
    }
}

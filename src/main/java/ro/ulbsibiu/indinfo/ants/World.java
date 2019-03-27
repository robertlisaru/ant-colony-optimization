package ro.ulbsibiu.indinfo.ants;

import java.util.LinkedList;

public class World {

    private final int numCities;
    private final int numAnts;
    private final double initialPheromoneIntensity;
    private int[][] distances;
    private double[][] feromones;

    public World(int numCities, int numAnts, double initialPheromoneIntensity) {
        this.numCities = numCities;
        this.numAnts = numAnts;
        this.initialPheromoneIntensity = initialPheromoneIntensity;

        distances = new int[numCities][numCities];
        feromones = new double[numCities][numCities];

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < i; j++) {
                feromones[i][j] = initialPheromoneIntensity;
                feromones[j][i] = initialPheromoneIntensity;
            }
        }
    }


    private class Ant {

        private final LinkedList<Integer> visited = new LinkedList<>();
        private final LinkedList<Integer> unvisited = new LinkedList<>();

        public Ant() {
            for (int i = 0; i < numCities; i++) {
                unvisited.add(i);
            }
            int startingCity = Util.getRandomInt(0, numCities);
            unvisited.remove(new Integer(startingCity));
            visited.add(startingCity);
        }

        public int getCurrentCity() {
            return visited.getLast();
        }

        public int computeDistance() {
            int distance = 0;
            for (int i = 0; i < visited.size() - 1; i++) {
                distance += distances[visited.get(i)][visited.get(i + 1)];
            }
            return distance;
        }
    }
}

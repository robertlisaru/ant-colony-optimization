package ro.ulbsibiu.indinfo.ants;

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


}

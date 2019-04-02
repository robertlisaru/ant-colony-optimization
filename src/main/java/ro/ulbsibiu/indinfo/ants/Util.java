package ro.ulbsibiu.indinfo.ants;

import java.awt.*;
import java.util.Random;

public class Util {

    private static Random rand = new Random(System.currentTimeMillis());

    public static int getRandomInt(int lowerLimit, int higherLimitExclusive) {
        return lowerLimit + rand.nextInt(higherLimitExclusive - lowerLimit);
    }

    public static void printMatrix(double[][] matrix, int numLines, int numColumns) {
        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < numColumns; j++) {
                System.out.print(String.format("%.2f", matrix[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printArray(int[] array, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + " ");
        }
    }

    public static int distanceInt(Point a, Point b) {
        return (Long.valueOf(Math.round(Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))))).intValue();
    }

    public static double maxPheromone(double[][] pheromoneMap, int numCities) {
        double max = Double.MIN_VALUE;
        for (int i = 1; i < numCities; i++) {
            for (int j = 0; j < i; j++) {
                if (pheromoneMap[i][j] > max) {
                    max = pheromoneMap[i][j];
                }
            }
        }
        return max;
    }
}

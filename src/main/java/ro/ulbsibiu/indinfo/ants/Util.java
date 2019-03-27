package ro.ulbsibiu.indinfo.ants;

import java.util.Random;

public class Util {

    private static Random rand = new Random(System.currentTimeMillis());

    public static int getRandomInt(int lowerLimit, int higherLimitExclusive) {
        return lowerLimit + rand.nextInt(higherLimitExclusive - lowerLimit);
    }

    public static void printMatrix(int[][] matrix, int numLines, int numColumns) {
        for (int i = 0; i < numLines; i++) {
            for (int j = 0; j < numColumns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printArray(int[] array, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }


}
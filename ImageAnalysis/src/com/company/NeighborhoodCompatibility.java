package com.company;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class NeighborhoodCompatibility {

    private static final int NEIGHBORHOOD_SIZE = 50;
    private static final double COHESION = 0.7;
    private static final double ACCEPTABLE = COHESION * (double) NEIGHBORHOOD_SIZE;
    private static final int R1 = 30;
    private static final int R2 = 100;
    private static final int NUMBER_OF_BEST_PAIRS = 150;

    public static void setNearestNeighbor(ImageProperties imageA, ImageProperties imageB) {
        imageA.getKeyPoints().forEach(keyPointA -> {
            ArrayList<Pair> neighborList = new ArrayList<>();
            final double[] distance = {0};

            imageB.getKeyPoints().forEach(keyPointB -> {
                for (int i = 0; i < keyPointA.getFeatures().length; i++) {
                    distance[0] += Math.pow(keyPointA.getFeatures()[i] - keyPointB.getFeatures()[i], 2);
                    //distance[0] += Math.abs(keyPointA.getFeatures()[i] - keyPointB.getFeatures()[i]);
                }

                neighborList.add(new Pair(keyPointA, keyPointB).setDistance(Math.sqrt(distance[0])));
                //neighborList.add(new Pair(keyPointA, keyPointB).setDistance(distance[0]));
                distance[0] = 0;
            });
            neighborList.sort(Comparator.comparing(Pair::getDistance));
            keyPointA.setNearestNeighbor(neighborList.get(0).getKeyPointB());
        });
    }

    public static ArrayList<Pair> getPairNeighbor(ImageProperties imageProperties) {
        ArrayList<Pair> pairs = new ArrayList<>();
        imageProperties.getKeyPoints().forEach(keyPoint -> {
            if (keyPoint.getNearestNeighbor().getNearestNeighbor().equals(keyPoint)) {
                pairs.add(new Pair(keyPoint, keyPoint.getNearestNeighbor()));
            }
        });
        return pairs;
    }

    public static void setNeighborhood(ArrayList<Pair> pairs) {

        ArrayList<KeyPoint> keyPointsA = new ArrayList<>();
        ArrayList<KeyPoint> keyPointsB = new ArrayList<>();

        pairs.forEach(pair -> {
            keyPointsA.add(pair.getKeyPointA());
            keyPointsB.add(pair.getKeyPointB());
        });

        setNeighborhoodHelper(keyPointsA);
        setNeighborhoodHelper(keyPointsB);
    }

    private static void setNeighborhoodHelper(ArrayList<KeyPoint> keyPoints) {

        keyPoints.forEach(keyPoint -> {
            ArrayList<KeyPoint> neighborhood = new ArrayList<>();
            ArrayList<Pair> distanceBetweenPoints = new ArrayList<>();

            keyPoints.forEach(innerKeyPoint -> {
                double distance = getPhisicalDistance(keyPoint, innerKeyPoint);
                if (distance > 0) {
                    distanceBetweenPoints.add(new Pair(keyPoint, innerKeyPoint).setDistance(distance));
                }
            });

            distanceBetweenPoints.sort(Comparator.comparing(Pair::getDistance));
            for (int currentNeighborhoodIndex = 0; currentNeighborhoodIndex < NEIGHBORHOOD_SIZE; currentNeighborhoodIndex++) {
                neighborhood.add(distanceBetweenPoints.get(currentNeighborhoodIndex).getKeyPointB());
            }
            keyPoint.setNeighborhood(neighborhood);
        });
    }

    private static double getPhisicalDistance(KeyPoint keyPointFirst, KeyPoint keyPointSecond) {
        return Math.sqrt(Math.pow(keyPointFirst.getCoordinateX() - keyPointSecond.getCoordinateX(), 2) +
                Math.pow(keyPointFirst.getCoordinateY() - keyPointSecond.getCoordinateY(), 2));
    }

    public static ArrayList<Pair> getCohesiveNeighborhood(ArrayList<Pair> pairs) {

        ArrayList<Pair> result = new ArrayList<>();
        pairs.forEach(pair -> {
            final int[] counter = {0};
            pair.getKeyPointA().getNeighborhood().forEach(keyPoint -> {
                if (pair.getKeyPointB().getNeighborhood().contains(keyPoint.getNearestNeighbor())) {
                    counter[0]++;
                }
            });

            if (counter[0] >= ACCEPTABLE) {
                result.add(pair);
            }
        });
        return result;
    }

    public static ArrayList<Pair> getPhisicalDistanceNeighborhood(ArrayList<Pair> pairs) {

        ArrayList<Pair> result = new ArrayList<>();
        Random random = new Random();


        pairs.forEach(pair -> {

            Pair randomPair = pairs.get(random.nextInt(pairs.size()));
            double phisicalDistanceA = getPhisicalDistance(pair.getKeyPointA(), randomPair.getKeyPointA());
            double phisicalDistanceB = getPhisicalDistance(pair.getKeyPointB(), randomPair.getKeyPointB());

            if(R1 < phisicalDistanceA && phisicalDistanceA < R2
                    && R1 < phisicalDistanceB && phisicalDistanceB < R2){
                result.add(pair);
                result.add(randomPair);
            }
        });


        return result;
    }

    public static ArrayList<Pair> getMostCohesivePairs(ArrayList<Pair> pairs){

        ArrayList<Pair> result = new ArrayList<>();
        ArrayList<Pair> sortedPairs = pairs;
        sortedPairs.sort(Comparator.comparing(Pair::getDistance));

        for(int actualPairIndex = 0; actualPairIndex < NUMBER_OF_BEST_PAIRS && actualPairIndex < pairs.size(); actualPairIndex++){
            result.add(sortedPairs.get(actualPairIndex));
        }

        return result;
    }
}

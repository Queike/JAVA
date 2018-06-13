package com.company;

import Jama.Matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

//        Image imgA = FileReader.readImage("src/drawable/image_2a.png");
//        File siftA = FileReader.readSift("src/drawable/image_2a.png.haraff.sift");
//        Image imgB = FileReader.readImage("src/drawable/image_2b.png");
//        File siftB = FileReader.readSift("src/drawable/image_2b.png.haraff.sift");

//        Image imgA = FileReader.readImage("src/drawable/image_p1_a.png");
//        File siftA = FileReader.readSift("src/drawable/image_p1_a.png.haraff.sift");
//        Image imgB = FileReader.readImage("src/drawable/image_p1_b.png");
//        File siftB = FileReader.readSift("src/drawable/image_p1_b.png.haraff.sift");

//        Image imgA = FileReader.readImage("src/drawable/image_p2_a.png");
//        File siftA = FileReader.readSift("src/drawable/image_p2_a.png.haraff.sift");
//        Image imgB = FileReader.readImage("src/drawable/image_p2_b.png");
//        File siftB = FileReader.readSift("src/drawable/image_p2_b.png.haraff.sift");

//        Image imgA = FileReader.readImage("src/drawable/image_p3_a.png");
//        File siftA = FileReader.readSift("src/drawable/image_p3_a.png.haraff.sift");
//        Image imgB = FileReader.readImage("src/drawable/image_p3_b.png");
//        File siftB = FileReader.readSift("src/drawable/image_p3_b.png.haraff.sift");

        Image imgA = FileReader.readImage("src/drawable/imageC.png");
        File siftA = FileReader.readSift("src/drawable/imageC.png.haraff.sift");
        Image imgB = FileReader.readImage("src/drawable/imageA.png");
        File siftB = FileReader.readSift("src/drawable/imageA.png.haraff.sift");

//        Image imgA = FileReader.readImage("src/drawable/kuchnia1.png");
//        File siftA = FileReader.readSift("src/drawable/kuchnia1.png.haraff.sift");
//        Image imgB = FileReader.readImage("src/drawable/kuchnia2.png");
//        File siftB = FileReader.readSift("src/drawable/kuchnia2.png.haraff.sift");


        ImageProperties imagePropertiesA = new ImageProperties(imgA, siftA);
        ImageProperties imagePropertiesB = new ImageProperties(imgB, siftB);

//        ArrayList<KeyPoint> keyPoints = imagePropertiesA.getKeyPoints();
//        for (KeyPoint k : keyPoints) {
//            System.out.println(k.toString());
//        }

        NeighborhoodCompatibility.setNearestNeighbor(imagePropertiesA, imagePropertiesB);
        NeighborhoodCompatibility.setNearestNeighbor(imagePropertiesB, imagePropertiesA);

        ArrayList<Pair> pairs = NeighborhoodCompatibility.getPairNeighbor(imagePropertiesA);

//        NeighborhoodCompatibility.setNeighborhood(pairs);

//        ArrayList<Pair> finalPairs = NeighborhoodCompatibility.getCohesiveNeighborhood(pairs);
//        ArrayList<Pair> finalPairs = NeighborhoodCompatibility.getPhisicalDistanceNeighborhood(pairs);
//        ArrayList<Pair> finalPairs = NeighborhoodCompatibility.getMostCohesivePairs(pairs);
//        ArrayList<Pair> finalPairs = new ArrayList<>(pairs);    // no selection pairs


//        System.out.println("Number of pairs: " + finalPairs.size());

//        EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, finalPairs, null, false));




        // ___________________________ RANSAC ______________________________________________


        Ransac ransac = new Ransac(1000000, 30, 5, 650, pairs);
        Matrix matrix = ransac.ransacAlgorithm(true);

        ArrayList<Pair> best = ransac.getMatchingPairs(matrix);
        System.out.println("Number of pairs: " + best.size());

//        EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, pairs, best, true));
        EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, best, null, false));
    }
}

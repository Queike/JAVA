import java.util.*;

public class Population {

    // change individuals to solutions !!!!!!!!!!

    private final int NUMBER_OF_CROSSING_TYPES = 3;

    private int populationSize;
    private static int locationsNumber;
    public static int[][] actualGeneration;
    private QualityCounter qualityCounter;
    private int percentageProbabilityOfMutation;
    private int percentageProbabilityOfCrossing;
    private Random generator;

    public Population(int populationSize, int locationsNumber, QualityCounter qualityCounter, int percentageProbabilityOfMutation, int percentageProbabilityOfCrossing){
        this.populationSize = populationSize;
        this.locationsNumber = locationsNumber;
        this.qualityCounter = qualityCounter;
        this.percentageProbabilityOfMutation = percentageProbabilityOfMutation;
        this.percentageProbabilityOfCrossing = percentageProbabilityOfCrossing;
        generator = new Random();
        actualGeneration = generateRandomlyFirstGeneration();
    }

    public int getPercentageProbabilityOfMutation() {
        return percentageProbabilityOfMutation;
    }

    public int getPercentageProbabilityOfCrossing(){
        return percentageProbabilityOfCrossing;
    }

    private int[][] generateRandomlyFirstGeneration(){
        int[][] generation = new int[populationSize][locationsNumber];

        for(int actualIndividualNumber = 0; actualIndividualNumber < populationSize; actualIndividualNumber++){
            for(int actualLocationNumber = 0; actualLocationNumber < locationsNumber; actualLocationNumber++){
                generation[actualIndividualNumber] = generateVector();
            }
        }
        return generation;
    }

    private int[] generateVector(){

        ArrayList<Integer> factories = new ArrayList<>();
        Random generator = new Random();
        int generatedNumber;

        for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++)
        {
            factories.add(actualFactoryNumber);
        }

        int [] vector = new int[locationsNumber];

        int position = 0;

        while(factories.size() > 0){
            generatedNumber = generator.nextInt(factories.size());
            vector[position] = factories.get(generatedNumber);
            factories.remove(generatedNumber);
            position++;
        }

        return vector;
    }


    public static int[] swapMutation(int[] solution){
        Random generator = new Random();

        int indexOfFirstGenToSwap = generator.nextInt(locationsNumber);
        int indexOfSecondGenToSwap = generator.nextInt(locationsNumber);
        while(indexOfFirstGenToSwap == indexOfSecondGenToSwap){
            indexOfSecondGenToSwap = generator.nextInt(locationsNumber);
        }

        int savedGen = solution[indexOfFirstGenToSwap];
        solution[indexOfFirstGenToSwap] = solution[indexOfSecondGenToSwap];
        solution[indexOfSecondGenToSwap] = savedGen;

        return solution;
    }

    public ArrayList<int[]> mutate(ArrayList<int[]> population){
        for(int indexOfCurrentSolution = 0; indexOfCurrentSolution < populationSize; indexOfCurrentSolution++){
            if(willBeMutated()){
                population.set(indexOfCurrentSolution, swapMutation(population.get(indexOfCurrentSolution)));
            }
        }
        int[][] src = convertArrayListOfArraysToTwoDimArray(population);
        System.arraycopy( src, 0, actualGeneration, 0, src.length );
        return population;
    }



    public ArrayList<Integer> repairChild(ArrayList<Integer> child){

        ArrayList<Integer> factories = new ArrayList<>();
        ArrayList<Integer> unusedFactories = new ArrayList<>();
        ArrayList<Integer> repairedChild = new ArrayList<>();

        for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++) {
            factories.add(actualFactoryNumber);
        }

        for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++){
            if(!child.contains(factories.get(actualFactoryNumber))){
                unusedFactories.add(factories.get(actualFactoryNumber));
            }
        }

        for(int actualFactoryNumber = 0; actualFactoryNumber < locationsNumber; actualFactoryNumber++){
            if(!repairedChild.contains(child.get(actualFactoryNumber))){
                repairedChild.add(child.get(actualFactoryNumber));
            } else{
                repairedChild.add(unusedFactories.get(0));
                unusedFactories.remove(0);
            }
        }
        return repairedChild;
    }


    public int[] cross(int[] firstParent, int[] secondParent){
        int crossingType = generator.nextInt(NUMBER_OF_CROSSING_TYPES);
        int[] child = new int[locationsNumber];
        int pointOfCrossing;

        switch (crossingType){
            case 0:
                System.arraycopy(firstParent, 0, child, 0, locationsNumber / 2);

                System.arraycopy(secondParent, locationsNumber / 2, child, locationsNumber / 2, locationsNumber - locationsNumber / 2);
                break;

            case 1:
                System.arraycopy(secondParent, 0, child, 0, locationsNumber / 2);

                System.arraycopy(firstParent, locationsNumber / 2, child, locationsNumber / 2, locationsNumber - locationsNumber / 2);
                break;

            case 2:
                pointOfCrossing = generator.nextInt(locationsNumber);

                System.arraycopy(secondParent, 0, child, 0, pointOfCrossing);

                System.arraycopy(firstParent, pointOfCrossing, child, pointOfCrossing, locationsNumber - pointOfCrossing);
                break;

            case 3:
                pointOfCrossing = generator.nextInt(locationsNumber);

                System.arraycopy(firstParent, 0, child, 0, pointOfCrossing);

                System.arraycopy(secondParent, pointOfCrossing, child, pointOfCrossing, locationsNumber - pointOfCrossing);
                break;
        }

        ArrayList<Integer> childAL = new ArrayList<Integer>(Arrays.asList(Arrays.stream( child ).boxed().toArray( Integer[]::new )));
        ArrayList<Integer> repairedChild = repairChild(childAL);
        child = convertArrayListToArray(repairedChild);
        return child;
    }

    public int[][] makeNextGeneration(int[][] thisGeneration){
        int[][] nextGeneration = new int[populationSize][locationsNumber];
        int[] bestSolutionFromThisGeneraion = qualityCounter.findBestIndividual(thisGeneration);

        nextGeneration[0] = bestSolutionFromThisGeneraion;

        for(int actualSolution = 1; actualSolution < populationSize; actualSolution++){
            if(willBeCrossed())
                nextGeneration[actualSolution] = cross(thisGeneration[actualSolution], thisGeneration[generator.nextInt(populationSize)]);
            else
                nextGeneration[actualSolution] = thisGeneration[actualSolution];

            if(willBeMutated())
                nextGeneration[actualSolution] = swapMutation(nextGeneration[actualSolution]);

        }

        actualGeneration = nextGeneration;
        return nextGeneration;
    }

//    public static int[] swapMutation(int[] parent){
//        int[] parentClone = parent.clone();
//        Random generator = new Random();
//
//        int factoryLocation1 = generator.nextInt(locationsNumber);
//        int factoryLocation2 = generator.nextInt(locationsNumber);
//
//        while(factoryLocation1 == factoryLocation2)
//            factoryLocation2 = generator.nextInt(locationsNumber);
//
//        int temp = parentClone[factoryLocation1];
//        parentClone[factoryLocation1] = parentClone[factoryLocation2];
//        parentClone[factoryLocation2] = temp;
//
//        return parentClone;
//    }

//    public static int[][] swapMutation(int[][] population){
////
////        Random generator = new Random();
////        int selectedSolution;
////        int firstFactoryToMutation;
////        int secondFactoryToMutation = -1;
////
////        for(int mutationLoop = 0; mutationLoop < population.length * PERCENTAGE_OF_INDIVIDUALS_TO_MUTATION; mutationLoop++){
////            selectedSolution = generator.nextInt(population.length);
////            firstFactoryToMutation = generator.nextInt(locationsNumber);
////            while (secondFactoryToMutation == firstFactoryToMutation || secondFactoryToMutation == -1){
////                secondFactoryToMutation = generator.nextInt(locationsNumber);
////            }
////
////            int savedFactory = population[selectedSolution][firstFactoryToMutation];
////            population[selectedSolution][firstFactoryToMutation] = population[selectedSolution][secondFactoryToMutation];
////            population[secondFactoryToMutation][secondFactoryToMutation] = savedFactory;
////
////        }
////
////        actualGeneration = population;
////        return population;
////    }


//    public int[][] cross(int[][] individulasToCross){
//        int[][] solutionsAfterCrossing = new int[populationSize][locationsNumber];
//        Random generator = new Random();
//
//        int actualSolutionNumber = 0;
//
//        while(actualSolutionNumber < populationSize){
//            int[] firstParent = new int[locationsNumber];
//            int[] secondParent = new int[locationsNumber];
//
//            firstParent = individulasToCross[generator.nextInt(individulasToCross.length)];
//            solutionsAfterCrossing[actualSolutionNumber] = firstParent;
//            actualSolutionNumber++;
//            secondParent = individulasToCross[generator.nextInt(individulasToCross.length)];
//            solutionsAfterCrossing[actualSolutionNumber] = secondParent;
//            actualSolutionNumber++;
//
//            ArrayList<Integer> firstChild = new ArrayList<>();
//            ArrayList<Integer> secondChild = new ArrayList<>();
//
//            for(int actualGenNumber = 0; actualGenNumber < locationsNumber/2; actualGenNumber++){
//                firstChild.add(firstParent[actualGenNumber]);
//                secondChild.add(secondParent[actualGenNumber]);
//            }
//
//            for(int actualGenNumber = locationsNumber/2; actualGenNumber < locationsNumber; actualGenNumber++){
//                firstChild.add(secondParent[actualGenNumber]);
//                secondChild.add(firstParent[actualGenNumber]);
//            }
//
//            firstChild = repairChild(firstChild);
//            secondChild = repairChild(secondChild);
//
//
//            solutionsAfterCrossing[actualSolutionNumber] = firstChild.stream().mapToInt(i -> i).toArray();
//            actualSolutionNumber++;
//            solutionsAfterCrossing[actualSolutionNumber] = secondChild.stream().mapToInt(i -> i).toArray();
//            actualSolutionNumber++;
//
//        }
//
//        actualGeneration = solutionsAfterCrossing;
//        return solutionsAfterCrossing;
//    }

//    public ArrayList<ArrayList> cross(int[] firstParent, int[] secondParent){
//
//        Random generator = new Random();
//
//        ArrayList<Integer> firstChild = new ArrayList<>();
//        ArrayList<Integer> secondChild = new ArrayList<>();
//
//        for(int actualGenNumber = 0; actualGenNumber < locationsNumber/2; actualGenNumber++){
//            firstChild.add(firstParent[actualGenNumber]);
//            secondChild.add(secondParent[actualGenNumber]);
//        }
//
//        for(int actualGenNumber = locationsNumber/2; actualGenNumber < locationsNumber; actualGenNumber++){
//            firstChild.add(secondParent[actualGenNumber]);
//            secondChild.add(firstParent[actualGenNumber]);
//        }
//
//        firstChild = repairChild(firstChild);
//        secondChild = repairChild(secondChild);
//
//        ArrayList<ArrayList> children = new ArrayList<>();
//        children.add(firstChild);
//        children.add(secondChild);
//        return children;
//    }

//    public ArrayList<ArrayList> cross(ArrayList<Integer> firstParent, ArrayList<Integer> secondParent){
//
//        Random generator = new Random();
//
//        ArrayList<Integer> firstChild = new ArrayList<>();
//        ArrayList<Integer> secondChild = new ArrayList<>();
//
//        for(int actualGenNumber = 0; actualGenNumber < locationsNumber/2; actualGenNumber++){
//            firstChild.add(firstParent.get(actualGenNumber));
//            secondChild.add(secondParent.get(actualGenNumber));
//        }
//
//        for(int actualGenNumber = locationsNumber/2; actualGenNumber < locationsNumber; actualGenNumber++){
//            firstChild.add(secondParent.get(actualGenNumber));
//            secondChild.add(firstParent.get(actualGenNumber));
//        }
//
//        firstChild = repairChild(firstChild);
//        secondChild = repairChild(secondChild);
//
//        ArrayList<ArrayList> children = new ArrayList<>();
//        children.add(firstChild);
//        children.add(secondChild);
//        return children;
//    }


//    public int[][] selectIndividualsWithRoulette(){
//        int totalSum = 0;
//        int[][] chosenIndividuals = new int[PERCENTAGE_OF_INDIVIDUALS_TO_CROSS * populationSize / 100][locationsNumber];
//        ArrayList<Integer> indexesOfChosenIndividuals = new ArrayList<>();
//
//        System.out.println("Pierwszy znacznik w ruletce");
//
//        for(int actualIndividual = 0; actualIndividual < populationSize; actualIndividual++){
//            totalSum += qualityCounter.count(actualGeneration[actualIndividual]);
//            System.out.println("Drugi znacznik w ruletce");
//        }
//
//        Random generator = new Random();
//        System.out.println("Trzeci znacznik w ruletce");
//
//        for(int actualChooseOfIndividual = 0; actualChooseOfIndividual < chosenIndividuals.length; actualChooseOfIndividual++){ // ---------------------------------------
//            int randomPartialSum = generator.nextInt(totalSum);
//            int partialSum = 0;
//            System.out.println("Czwarty znacznik w ruletce");
//
//            for(int actualIndividual = 0; actualIndividual < populationSize; actualIndividual++){
//                System.out.println("Piaty znacznik w ruletce");
//                partialSum += qualityCounter.count(actualGeneration[actualIndividual]);
//                System.out.println("PARTIAL SUM = " + partialSum);
//                System.out.println("RANDOM PARTIAL SUM = " + randomPartialSum);
//                if(partialSum > randomPartialSum){
//                    System.out.println("indexesOfChosenIndividuals : " + indexesOfChosenIndividuals);
//                    System.out.println("actualIndividual : " + actualIndividual);
//                    if(!indexesOfChosenIndividuals.contains(actualIndividual)){
//                        System.out.println("Szosty znacznik w ruletce");
//                        chosenIndividuals[actualChooseOfIndividual] = actualGeneration[actualIndividual];
//                        indexesOfChosenIndividuals.add(actualIndividual);
//                        //actualIndividual++;                                                                                   // --------------------------------------------
//                    }
//
//                }
//
//            }
//        }
//        System.out.println("Wypisuje wybrane osobniki z metody ruletki ");
//        printPopulation(chosenIndividuals);
//        return chosenIndividuals;
//    }

//    public ArrayList<int[]> createNewGenerationWithBestAndCross(){
//
//        int percentOfBestToNewGeneration = 50;
//        int firstIndexToPair;
//        int secondIndexToPair;
//        Random generator = new Random();
//
//        ArrayList<int[]> currentPopulation = twoDArrayToArrayListOfArrays(sortActualGenerationDesc());
//        ArrayList<int[]> newPopulation = new ArrayList<>();
//
//
//        for(int i = 0; i < percentOfBestToNewGeneration * populationSize / 100; i++){
//            newPopulation.add(currentPopulation.get(i));
//        }
//
//        while(newPopulation.size() < populationSize){
//            firstIndexToPair = generator.nextInt(percentOfBestToNewGeneration * populationSize / 100);
//            secondIndexToPair = generator.nextInt(percentOfBestToNewGeneration * populationSize / 100);
//            while(firstIndexToPair == secondIndexToPair){
//                secondIndexToPair = generator.nextInt(percentOfBestToNewGeneration * populationSize / 100);
//            }
//
//            ArrayList<ArrayList> children = new ArrayList<ArrayList>();
//            children = cross(newPopulation.get(firstIndexToPair), newPopulation.get(secondIndexToPair));
//            newPopulation.add(convertArrayListToArray(children.get(0)));
//            newPopulation.add(convertArrayListToArray(children.get(1)));
//        }
//
//        actualGeneration = convertArrayListOfArraysToTwoDimArray(newPopulation);
//        return newPopulation;
//    }


//    public ArrayList<int[]> createNewGenerationWithRouletteAndCross(){
//        ArrayList<int[]> currentPopulation = twoDArrayToArrayListOfArrays(actualGeneration);
//        ArrayList<int[]> newPopulation = new ArrayList<>();
//
//        Random generator = new Random();
//        int firstIndexToPair;
//        int secondIndexToPair;
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//
//        int[] bestIndividual = qualityCounter.findBestIndividual(actualGeneration);
//        int indexOfBestIndividual = currentPopulation.indexOf(bestIndividual);
//        newPopulation.add(currentPopulation.get(indexOfBestIndividual));
//        currentPopulation.remove(indexOfBestIndividual);
//
//        while (currentPopulation.size() > 1){
//            firstIndexToPair = generator.nextInt(currentPopulation.size());
//            secondIndexToPair = generator.nextInt(currentPopulation.size());
//            if(firstIndexToPair == secondIndexToPair){
//                secondIndexToPair = generator.nextInt(currentPopulation.size());
//            }
//
//            if(willBeCrossed()){
//                ArrayList<ArrayList> children = new ArrayList<ArrayList>();
//                children = cross(currentPopulation.get(firstIndexToPair), currentPopulation.get(secondIndexToPair));
//                newPopulation.add(convertArrayListToArray(children.get(0)));
//                newPopulation.add(convertArrayListToArray(children.get(1)));
//                if(secondIndexToPair == 0)
//                    currentPopulation.remove(secondIndexToPair);
//                else
//                    currentPopulation.remove(secondIndexToPair - 1);
//            }
//            else{
//                newPopulation.add(currentPopulation.get(firstIndexToPair));
//                newPopulation.add(currentPopulation.get(secondIndexToPair));
//                currentPopulation.remove(firstIndexToPair);
//                if(secondIndexToPair == 0)
//                    currentPopulation.remove(secondIndexToPair);
//                else
//                    currentPopulation.remove(secondIndexToPair - 1);
//            }
//        }
//
//        if(currentPopulation.size() > 0){
//            newPopulation.add(currentPopulation.get(0));
//            currentPopulation.remove(0);
//        }
//
//        return newPopulation;
//    }

//    public int[][] sortActualGenerationDesc(){
//        ArrayList<int[]> sortedGeneration = new ArrayList<>();
//        QualityCounter qualityCounter = new QualityCounter(locationsNumber, distanceMatrix, flowMatrix);
//        sortedGeneration.add(actualGeneration[0]);
//
//        for(int index = 1; index < actualGeneration.length; index++){
//            int cost = qualityCounter.count(actualGeneration[index]);
//            int currentElementCost = qualityCounter.count(actualGeneration[index]);
//
//            int t = sortedGeneration.size();
//
//            for (int j = 0; j < t; j++) {
//                if(qualityCounter.count(sortedGeneration.get(j)) > currentElementCost){
//                    sortedGeneration.add(j, actualGeneration[index]);
//                    break;
//                }
//                if(j == sortedGeneration.size() - 1)
//                    sortedGeneration.add(0, actualGeneration[index]);
//            }
//        }
//
//
//        actualGeneration = convertArrayListOfArraysToTwoDimArray(sortedGeneration);
//
//        return actualGeneration;
//    }



//    public List<Integer[]> twoDArrayToList(Integer[][] twoDArray) {
//        List<Integer[]> list = new ArrayList<>();
//        for (Integer[] array : twoDArray) {
//            list.addAll(Arrays.asList(array));
//        }
//        return list;
//    }


    private static int[] convertArrayListToArray(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next();
        }
        return ret;
    }

    private boolean willBeCrossed(){
        Random generator = new Random();
        int randomPoint = generator.nextInt(100) + 1;
        return randomPoint < percentageProbabilityOfCrossing;
    }

    private boolean willBeMutated(){
        Random generator = new Random();
        int randomPoint = generator.nextInt(100) + 1;
        return randomPoint < percentageProbabilityOfMutation;
    }


    public ArrayList<int[]> twoDArrayToArrayListOfArrays(int[][] twoDArray){

        return new ArrayList<>(Arrays.asList(twoDArray));
    }


    private void printVector(int[] vector){
        for (int aVector : vector) {
            System.out.print(aVector + " ");
        }
        System.out.println();
    }

    public void printPopulation(int[][] population){
        System.out.println("__THE POPULATION__");
        for (int[] aPopulation : population) {
            printVector(aPopulation);
        }
    }

    private int[][] convertArrayListOfArraysToTwoDimArray(ArrayList<int[]> arrayList){
        int[][] array = new int[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            int[] row = arrayList.get(i);
            array[i] = row;
        }
        return array;
    }
}

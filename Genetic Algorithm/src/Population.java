import java.util.*;

public class Population {

    private final int NUMBER_OF_CROSSING_TYPES = 3;

    private int populationSize;
    private static int locationsNumber;
    public static ArrayList<Solution> actualGeneration;
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

    private ArrayList<Solution> generateRandomlyFirstGeneration(){
        ArrayList<Solution> generation = new ArrayList<>(locationsNumber);

        for(int actualSolutionIndex = 0; actualSolutionIndex < populationSize; actualSolutionIndex++){
            generation.add(generateVector());
        }
        return generation;
    }

    private Solution generateVector(){

        Solution solution = new Solution();
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

        solution.setVector(vector);
        return solution;
    }


    public static Solution swapMutation(Solution solution){
        Random generator = new Random();

        int indexOfFirstGenToSwap = generator.nextInt(locationsNumber);
        int indexOfSecondGenToSwap = generator.nextInt(locationsNumber);
        while(indexOfFirstGenToSwap == indexOfSecondGenToSwap){
            indexOfSecondGenToSwap = generator.nextInt(locationsNumber);
        }

        int savedGen = solution.getVector()[indexOfFirstGenToSwap];
        solution.modifyVector(indexOfFirstGenToSwap, solution.getVector()[indexOfSecondGenToSwap]);
        solution.modifyVector(indexOfSecondGenToSwap, savedGen);

        return solution;
    }

//    public ArrayList<int[]> mutate(ArrayList<int[]> population){
//        for(int indexOfCurrentSolution = 0; indexOfCurrentSolution < populationSize; indexOfCurrentSolution++){
//            if(willBeMutated()){
//                population.set(indexOfCurrentSolution, swapMutation(population.get(indexOfCurrentSolution)));
//            }
//        }
//        int[][] src = convertArrayListOfArraysToTwoDimArray(population);
//        System.arraycopy( src, 0, actualGeneration, 0, src.length );
//        return population;
//    }



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


    public Solution singleCross(Solution firstParent, Solution secondParent){
        int crossingType = generator.nextInt(NUMBER_OF_CROSSING_TYPES);
        Solution child = new Solution();
        int[] childVector = new int[locationsNumber];
        int pointOfCrossing;

        switch (crossingType){
            case 0:
                for(int actualIndex = 0; actualIndex < locationsNumber / 2; actualIndex++){
                    childVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                for(int actualIndex = locationsNumber / 2; actualIndex < locationsNumber; actualIndex++){
                    childVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                break;

            case 1:
                for(int actualIndex = 0; actualIndex < locationsNumber / 2; actualIndex++){
                    childVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                for(int actualIndex = locationsNumber / 2; actualIndex < locationsNumber; actualIndex++){
                    childVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                break;

            case 2:
                pointOfCrossing = generator.nextInt(locationsNumber);

                for(int actualIndex = 0; actualIndex < pointOfCrossing; actualIndex++){
                    childVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                for(int actualIndex = pointOfCrossing; actualIndex < locationsNumber; actualIndex++){
                    childVector[actualIndex] = firstParent.getVector()[actualIndex];
                }
                break;

            case 3:
                pointOfCrossing = generator.nextInt(locationsNumber);

                for(int actualIndex = 0; actualIndex < pointOfCrossing; actualIndex++){
                    childVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                for(int actualIndex = pointOfCrossing; actualIndex < locationsNumber; actualIndex++){
                    childVector[actualIndex] = secondParent.getVector()[actualIndex];
                }
                break;
        }

        ArrayList<Integer> childAL = new ArrayList<Integer>(Arrays.asList(Arrays.stream( childVector ).boxed().toArray( Integer[]::new )));
        ArrayList<Integer> repairedChildVector = repairChild(childAL);
        child.setVector(convertArrayListToArray(repairedChildVector));

        return child;
    }

    public ArrayList<Solution> makeNextGeneration(ArrayList<Solution> thisGeneration){
        ArrayList<Solution> nextGeneration = new ArrayList<>(locationsNumber);
        Solution bestSolutionFromThisGeneration = new Solution();
        bestSolutionFromThisGeneration.setVector(qualityCounter.findBestSolution(thisGeneration).getVector());

        nextGeneration.add(bestSolutionFromThisGeneration);

        for(int actualSolutionIndex = 1; actualSolutionIndex < populationSize; actualSolutionIndex++){


            if(willBeCrossed())
                nextGeneration.add(singleCross(thisGeneration.get(actualSolutionIndex), thisGeneration.get(generator.nextInt(populationSize))));
            else
                nextGeneration.add(thisGeneration.get(actualSolutionIndex));

            if(willBeMutated())
                nextGeneration.set(actualSolutionIndex, swapMutation(nextGeneration.get(actualSolutionIndex)));

        }

        actualGeneration = nextGeneration;
        return nextGeneration;
    }

    // TODO: switch best solution to next Generation

    public ArrayList<Solution> makeNextGenerationWithTournament(ArrayList<Solution> thisGeneration, int tournamentSize){
        Solution firstParent = new Solution();
        Solution secondParent = new Solution();
        Random generator = new Random();
        ArrayList<Solution> solutionsInTournament = new ArrayList<>();

        Solution bestSolutionFromCurrentGeneration = qualityCounter.findBestSolution(thisGeneration);
        ArrayList<Solution> newGeneration = new ArrayList<>();

        newGeneration.add(bestSolutionFromCurrentGeneration);
        thisGeneration.remove(bestSolutionFromCurrentGeneration);

        while(newGeneration.size() < populationSize){

            firstParent = playTournament(tournamentSize, thisGeneration);
            secondParent = playTournament(tournamentSize, thisGeneration);

            printVector(firstParent.getVector());
            printVector(secondParent.getVector());

            if(willBeCrossed()){
                ArrayList<Solution> children = cross(firstParent, secondParent);
                newGeneration.add(children.get(0));
                newGeneration.add(children.get(1));
            } else {
                newGeneration.add(firstParent);
                newGeneration.add(secondParent);
            }

            if(willBeMutated()){
                newGeneration.set(newGeneration.size() - 2, swapMutation(newGeneration.get(newGeneration.size() - 2)));
            }

            if(willBeMutated()){
                newGeneration.set(newGeneration.size() - 1, swapMutation(newGeneration.get(newGeneration.size() - 1)));
            }

        }

        actualGeneration = newGeneration;

        return newGeneration;

    }


    public Solution getWinnerFromTournament(ArrayList<Solution> solutionsInTournament){
        Solution winner = new Solution();

        for(Solution solution : solutionsInTournament){
            if(solution.getCost() < winner.getCost() || winner.getCost() == 0){
                winner = solution;
            }
        }

        return winner;
    }

    public Solution playTournament(int tournamentSize, ArrayList<Solution> thisGeneration){
        ArrayList<Solution> solutionsInTournament = new ArrayList<>();

        while (solutionsInTournament.size() < tournamentSize){
            int randomSolutionIndex = generator.nextInt(populationSize);
            if(!solutionsInTournament.contains(thisGeneration.get(randomSolutionIndex)))
                solutionsInTournament.add(thisGeneration.get(randomSolutionIndex));
        }
        System.out.print("Solutions in tournament ");
        printVector(solutionsInTournament.get(0).getVector());

        Solution winner = getWinnerFromTournament(solutionsInTournament);

        return winner;
    }

    public ArrayList<Solution> makeNextGenerationWithRoulette(ArrayList<Solution> thisGeneration){
        int totalCost;
        int randomPartialSum;
        Solution firstParent = new Solution();
        Solution secondParent = new Solution();
        Random generator = new Random();
        Solution bestSolutionFromCurrentGeneration = qualityCounter.findBestSolution(thisGeneration);
        ArrayList<Solution> newGeneration = new ArrayList<>();

        newGeneration.add(bestSolutionFromCurrentGeneration);
        thisGeneration.remove(bestSolutionFromCurrentGeneration);

        while (thisGeneration.size() > 1){
            totalCost = getGenerationCost(thisGeneration);
            randomPartialSum = generator.nextInt(totalCost);
            firstParent = getSolutionWithPartialSum(randomPartialSum, thisGeneration);
            thisGeneration.remove(firstParent);

            totalCost = getGenerationCost(thisGeneration);
            randomPartialSum = generator.nextInt(totalCost);
            secondParent = getSolutionWithPartialSum(randomPartialSum, thisGeneration);
            thisGeneration.remove(secondParent);

            if(willBeCrossed()){
                ArrayList<Solution> children = cross(firstParent, secondParent);
                newGeneration.add(children.get(0));
                newGeneration.add(children.get(1));
            } else {
                newGeneration.add(firstParent);
                newGeneration.add(secondParent);
            }

            if(willBeMutated()){
                newGeneration.set(newGeneration.size() - 2, swapMutation(newGeneration.get(newGeneration.size() - 2)));
            }

            if(willBeMutated()){
                newGeneration.set(newGeneration.size() - 1, swapMutation(newGeneration.get(newGeneration.size() - 1)));
            }
        }

        while (thisGeneration.size() > 0){
            firstParent = thisGeneration.get(0);
            thisGeneration.remove(firstParent);
            newGeneration.add(firstParent);

            if(willBeMutated()){
                newGeneration.set(newGeneration.size() - 1, swapMutation(newGeneration.get(newGeneration.size() - 1)));
            }
        }

        actualGeneration = newGeneration;
        return newGeneration;

    }

    public int getGenerationCost(ArrayList<Solution> generation){
        int totalCost = 0;

        for(Solution solution : generation){
            if(solution.getCost() != -1)
                totalCost += solution.getCost();
            else
                totalCost += qualityCounter.count(solution);
        }

        return totalCost;
    }

    public Solution getSolutionWithPartialSum(int presetPartialSum, ArrayList<Solution> generation){
        int partialSum = 0;
        Solution resultSolution = new Solution();

        for(Solution solution : generation){
            if(solution.getCost() != -1)
                partialSum += solution.getCost();
            else
                partialSum += qualityCounter.count(solution);

            if(presetPartialSum < partialSum)
                resultSolution = solution;
        }

        return resultSolution;
    }


    public ArrayList<Solution> cross(Solution firstParent, Solution secondParent){
        int crossingType = generator.nextInt(NUMBER_OF_CROSSING_TYPES);
        Solution firstChild = new Solution();
        Solution secondChild = new Solution();
        int[] firstChildVector = new int[locationsNumber];
        int[] secondChildVector = new int[locationsNumber];
        int pointOfCrossing;

        switch (crossingType){
            case 0:
                for(int actualIndex = 0; actualIndex < locationsNumber / 2; actualIndex++){
                    firstChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                for(int actualIndex = locationsNumber / 2; actualIndex < locationsNumber; actualIndex++){
                    firstChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                break;

            case 1:
                for(int actualIndex = 0; actualIndex < locationsNumber / 2; actualIndex++){
                    firstChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                for(int actualIndex = locationsNumber / 2; actualIndex < locationsNumber; actualIndex++){
                    firstChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                break;

            case 2:
                pointOfCrossing = generator.nextInt(locationsNumber);

                for(int actualIndex = 0; actualIndex < pointOfCrossing; actualIndex++){
                    firstChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                }

                for(int actualIndex = pointOfCrossing; actualIndex < locationsNumber; actualIndex++){
                    firstChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                }
                break;

            case 3:
                pointOfCrossing = generator.nextInt(locationsNumber);

                for(int actualIndex = 0; actualIndex < pointOfCrossing; actualIndex++){
                    firstChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                }

                for(int actualIndex = pointOfCrossing; actualIndex < locationsNumber; actualIndex++){
                    firstChildVector[actualIndex] = firstParent.getVector()[actualIndex];
                    secondChildVector[actualIndex] = secondParent.getVector()[actualIndex];
                }
                break;
        }

        ArrayList<Integer> firstChildAL = new ArrayList<Integer>(Arrays.asList(Arrays.stream( firstChildVector ).boxed().toArray( Integer[]::new )));
        ArrayList<Integer> secondChildAL = new ArrayList<Integer>(Arrays.asList(Arrays.stream( secondChildVector ).boxed().toArray( Integer[]::new )));
        ArrayList<Integer> repairedFirstChildVector = repairChild(firstChildAL);
        ArrayList<Integer> repairedSecondChildVector = repairChild(secondChildAL);

        firstChild.setVector(convertArrayListToArray(repairedFirstChildVector));
        secondChild.setVector(convertArrayListToArray(repairedSecondChildVector));

        ArrayList<Solution> children = new ArrayList<>();
        children.add(firstChild);
        children.add(secondChild);

        return children;
    }


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


    // change individuals to solutions !!!!!!!!!!

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

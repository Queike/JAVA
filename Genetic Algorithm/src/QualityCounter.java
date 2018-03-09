import java.util.ArrayList;

public class QualityCounter {

    private static int N;
    private static int[][] distanceMatrix;
    private static int[][] flowMatrix;

    QualityCounter(int N, int[][] distanceMatrix, int[][] flowMatrix){
        QualityCounter.N = N;
        QualityCounter.distanceMatrix = distanceMatrix;
        QualityCounter.flowMatrix = flowMatrix;
    }

    public int count(Solution solution){
        int solutionValue = 0;
        for (int actualLocationNumber = 0; actualLocationNumber < N; actualLocationNumber++){
            for (int actualFactoryNumber = 0; actualFactoryNumber < N; actualFactoryNumber++){
                solutionValue += (distanceMatrix[actualLocationNumber][actualFactoryNumber]*flowMatrix[solution.getVector()[actualLocationNumber]][solution.getVector()[actualFactoryNumber]]);
            }
        }
        solution.setCost(solutionValue);
        return solutionValue;
    }


    public Solution findBestSolution(ArrayList<Solution> generation){
        int bestSolutionValue = 0;
        Solution bestSolution = generation.get(0);

        for (Solution solution : generation){
            if(solution.getCost() == -1)
                solution.setCost(count(solution));

            if(solution.getCost() < bestSolutionValue || bestSolutionValue == 0){
                bestSolutionValue = solution.getCost();
                bestSolution = solution;
            }
        }

        return bestSolution;
    }

    public Solution findWorstSolution(ArrayList<Solution> generation){
        int worstSolutionValue = 0;
        Solution worstSolution = generation.get(0);

        for (Solution solution : generation){
            if(solution.getCost() == -1)
                solution.setCost(count(solution));

            if(solution.getCost() > worstSolutionValue || worstSolutionValue == 0){
                worstSolutionValue = solution.getCost();
                worstSolution = solution;
            }
        }

        return worstSolution;
    }

}

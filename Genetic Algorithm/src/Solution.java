import java.util.ArrayList;
import java.util.Random;

public class Solution {

    private int[] solutionVector;
    private int cost;

    protected Solution clone() {
        Solution solution = new Solution();
        solution.setCost(this.getCost());
        solution.setVector(this.getVector().clone());

        return solution;
    }

    public Solution(){
        cost = -1;
    }

    public Solution(int[] solutionVector){
        this.solutionVector = solutionVector;
        cost = -1;
    }

    public void setCost(int cost){
        this.cost = cost;
    }

    public int getCost(){
        return cost;
    }

    public void setVector(int[] solutionVector){
        this.solutionVector = solutionVector;
    }

    public int[] getVector(){
        return solutionVector;
    }

    public void modifyVector(int position, int newValue){
        solutionVector[position] = newValue;
    }


    private int[] generateSolution(int locationsNumber){

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
}

public class Solution {

    private int[] solutionVector;
    private int cost;

    public Solution(int[] solutionVector){
        this.solutionVector = solutionVector;
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

}

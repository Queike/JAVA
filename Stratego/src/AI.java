import java.util.ArrayList;
import java.util.Random;

public class AI {

    Random random;

    AI(){
        random = new Random();
    }


    public SingleMove makeRandomMove(ArrayList<SingleMove> availableMoves){
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}

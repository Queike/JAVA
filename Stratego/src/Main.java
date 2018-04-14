public class Main {

    public static void main(String[] args) throws InterruptedException {

        Gui gui = new Gui();
        gui.setup();
        Game game = new Game(gui.getGameBoardSizeChosenByUser());
        runAppropriateGame(gui, game);

    }

    private static void runAppropriateGame(Gui gui, Game game) throws InterruptedException {
        int gameMode = gui.getGameModeChosenByUser();

        if(gameMode == 1){
            game.playPvP();
        }
        else if(gameMode == 2){
            game.playAvP();
        }
        else if(gameMode == 3){
            game.playAvA();
        }
        else{
            gui.showBadGameModeMessage();
        }
    }
}

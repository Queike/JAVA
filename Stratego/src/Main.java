public class Main {

    public static void main(String[] args) {

        Gui gui = new Gui();
        gui.setup();
        Game game = new Game(gui.getGameBoardSizeChosenByUser());
        game.play();

    }
}

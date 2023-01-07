package dtu.matador.game;

public class GameState {
    GameController controller;
    private static GameState gameStateObject;
    private GameState() {
        controller = new GameController();
    }

    public static GameState getInstance() {
        if (gameStateObject == null) {
            gameStateObject = new GameState();
        }
        else {System.out.println("Gamestate instance already initialized..."); }
        return gameStateObject;
    }

    public void menu() {
        controller.setBoard("fieldSpaces");
        controller.addPlayer();


    }

}

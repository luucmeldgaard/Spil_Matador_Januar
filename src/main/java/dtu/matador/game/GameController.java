package dtu.matador.game;

public class GameController {

    FieldController board;
    GUIController gui;

    public static void main(String[] args) {
        GameState currentGameState;
        currentGameState = GameState.getInstance();
        currentGameState.menu();

    }

    public void setBoard(String selectedBoard) {
        board = new FieldController(selectedBoard);
        board.setupFields();
        gui = GUIController.getInstance(board.getFieldMap());
    }



}
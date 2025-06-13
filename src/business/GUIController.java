package business;

import presentation.GUI;

public class GUIController {
    // Atributos
    private GUI gui;
    private GameController gameController;

    // Constructor
    public GUIController(GUI gui) {
        this.gui = gui;
    }

    // Métodos públicos
    public void getControl() {
        gui.getbAmerica().setOnAction(event -> startGame(0));
        gui.getbEuropa().setOnAction(event -> startGame(1));
        gui.getbAsia().setOnAction(event -> startGame(2));
    }

    public void killPlayer() {
        actualizarVista();
    }

    public void killInfection() {
        actualizarVista();
    }

    private void startGame(int seed) {
        setGameMap(seed);
        setGameControls();
    }

    private void actualizarVista() {
        gui.updateTable(gameController.getEvents());
        gui.setGameMap(gameController.getBoard().getBoard());
    }

    private void setGameMap(int seed) {
        gameController = new GameController(seed);
        gui.setGameMap(gameController.getBoard().getBoard());
        gui.updateTable(gameController.getEvents());
    }

    private void setGameControls() {
        gui.setPaneButtonsGame();

        gui.getbVer().setOnAction(event -> {
            gameController.moveTurn();
            actualizarVista();
        });

        gui.getbkillInfected().setOnAction(event -> {
            actualizarVista();
        });

        gui.getbKillPlayer().setOnAction(event -> {
            actualizarVista();
        });
    }
}
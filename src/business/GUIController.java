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
        gameController.killPlayer();
        actualizarVista();
    }

    public void killInfection() {
        gameController.killInfection();
        actualizarVista();
    }

    private void startGame(int seed) {
        setGameMap(seed);
        setGameControls();
    }

    private void actualizarVista() {
        gui.updateTable(gameController.getEvents());
        // Siempre usar la referencia actual del tablero
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
            gameController.killInfection();
            actualizarVista();
        });

        gui.getbKillPlayer().setOnAction(event -> {
            gameController.killPlayer();
            actualizarVista();
        });
    }
}
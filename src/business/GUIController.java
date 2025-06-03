package business;


import presentation.GUI;

public class GUIController {
    private GUI gui;
    private GameController gameController;
    private HandleEvent handleEvent = new HandleEvent();

    public GUIController(GUI gui) {
    this.gui = gui;
    }
    
    public void getControl() {
        gui.getbAmerica().setOnAction(event -> startGame(0));
        gui.getbEuropa().setOnAction(event -> startGame(1));
        gui.getbAsia().setOnAction(event -> startGame(2));
    }
    
    private void startGame(int seed) {
        setGameMap(seed);
        setGameControls();
    }
    
    public void setGameMap(int seed) {
        gameController = new GameController(seed, handleEvent);
        gui.setGameMap(gameController.getBoard().getBoard());
        gui.updateTable(gameController.getEvents());
    }
    
    public void setGameControls() {
        gui.setPaneButtonsGame();
    
        gui.getbVer().setOnAction(event -> {
            gameController.moveTurn();
            actualizarVista();
        });
    
        gui.getbkillInfected().setOnAction(event -> {
            gameController.killBill(0);;
            actualizarVista();
        });
    
        gui.getbKillPlayer().setOnAction(event -> {
            gameController.killBill(1);
            actualizarVista();
        });
    }
    
    private void actualizarVista() {
        gui.updateTable(gameController.getEvents());
        gui.setGameMap(gameController.getBoard().getBoard());
    }
    
    public void killPlayer() {
        gameController.getBoard().getPlayerList().forEach(player -> {
            if (player.getState() == 'H') {
                gameController.killPlayer(player);
            }
        });
        actualizarVista();
    }
    
    public void killInfection() {
        gameController.getBoard().getPlayerList().forEach(player -> {
            if (player.getState() == 'I') {
                gameController.killPlayer(player);
            }
        });
        actualizarVista();
    }
    
}

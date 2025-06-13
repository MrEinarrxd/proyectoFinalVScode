package business;

import java.util.ArrayList;

import domain.Continent;
import domain.Event;
import domain.Item;
import domain.Player;

public class GameController {
    // Atributos
    private Continent board;
    private HandleEvent handleEvent;
    private ArrayList<Event> eventHistory = new ArrayList<>();

    private int round = 0;

    // Constructor
    public GameController(int seed) {
        board = new Continent(seed);
        this.handleEvent = new HandleEvent();

    }

    // Métodos públicos
    public void moveTurn() {
        round++;
        if (round % 5 == 0) {
            board.addPotion();
        }
        movePlayers();
        eventHistory.addAll(handleEvent.getEvents(board));
        board.updateBoard(null, null);
    }

	public void endGame() {

		System.out.println("El juego ha terminado.");
	}

    public Continent getBoard() {
        return board;
    }

    public void killPlayer(Player player) {
        getBoard().getPlayerList().remove(player);
    }

    public void deleteItem(Item item) {
        board.getItemList().remove(item);
    }

    public ArrayList<Event> getEvents() {
        return eventHistory;
    }

    // Métodos de movimiento
    public void movePlayers() {
        for (Player player : board.getPlayerList()) {
            int tryCount = 4;
            boolean moved = false;
            while (tryCount > 0 && !moved) {
                int[] newPos = player.move();
                int[] validPos = validPosition(newPos, board.getBoard());
                // Validar que la celda no esté ocupada por un edificio
                if (!board.getBoard()[validPos[0]][validPos[1]].equals("B")) {
                    player.setCoordinates(validPos);
                    moved = true;
                }
                tryCount--;
            }
            // Si no se pudo mover, el jugador se queda en su posición actual
        }
    }

    public int[] validPosition(int[] pos, String[][] board) {
        int x = (pos[0] + board.length) % board.length;
        int y = (pos[1] + board[0].length) % board[0].length;
        return new int[] { x, y };
    }
}
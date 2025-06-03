package business;

import java.util.ArrayList;

import domain.Board;
import domain.Event;
import domain.Item;
import domain.Player;

public class GameController {
	private Board board;
	private HandleEvent handleEvent;
	private ArrayList<Event> eventHistory = new ArrayList<>();

	
	public GameController(int seed, HandleEvent handleEvent) {
		board = new Board(seed);
		this.handleEvent = handleEvent;
		board.initializeBoard();
	}
	
	public void moveTurn() {
		movePlayers();
		eventHistory.addAll(handleEvent.getEvents(board));

	}
	public Board getBoard() {
		return board;
	}


	public void killBill(int target) {
		ArrayList<Player> players = board.getPlayerList();
		for (Player player : players) {
			if (target == 0 && player.getState() == 'I') {
				killPlayer(player);
			} else if (target == 1 && player.getState() == 'H') {
				killPlayer(player);
			}
		}
	}

    
    public void killPlayer(Player player) {
		getBoard().getPlayerList().remove(player);
    }


	public ArrayList<Player> movePlayers() {
		ArrayList<Player> players = board.getPlayerList();
		ArrayList<Player> newCoordinates = new ArrayList<>();

		for (Player player : players) {
			// Limpia la posición anterior
			board.cleanPosition(player.getCoordenates(), player.getState());

			// Intenta mover el jugador
			int[] originalPos = player.getCoordenates().clone();
			player.move();
			int[] validPos = board.validPosition(player.getCoordenates());

			// Si la nueva posición tiene un edificio, no mover
			if (board.getOnBoard(validPos).contains("E")) {
				player.setCoordenates(originalPos); // Vuelve a la posición original
			} else {
				player.setCoordenates(validPos);
			}

			// Actualiza el tablero con el nuevo estado
			board.setOnBoard(player.getCoordenates(), String.valueOf(player.getState()));
			newCoordinates.add(player);
		}

		return newCoordinates;
	}


	public void deleteItem(Item item) {
		board.getItemList().remove(item);
	}

	public ArrayList<Event> getEvents() {
		return eventHistory;
	}
}
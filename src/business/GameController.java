package business;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.reflect.TypeToken;

import data.FilesJson;
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

		Type continentType = new TypeToken<Continent>(){}.getType();
		FilesJson<Continent> filesJson = new FilesJson<>(continentType);
		String fileName = "Transmisión Inicial - " + board.getContinentName() + ".json";
		ArrayList<Continent> initialList = new ArrayList<>();
		initialList.add(board);
		filesJson.writeList(fileName, initialList);

    }

    // Métodos públicos
    public void moveTurn() {
        round++;
        if (round % 5 == 0) {
            board.addPotion();
        }
        movePlayers();
        eventHistory.addAll(handleEvent.getEvents(board));
        board = handleEvent.getResultBoard(board);

        if (isGameOver()) {
            endGame();
            System.out.println("El juego ha terminado. No hay más jugadores.");
        } else {
            System.out.println("Ronda " + round + " completada. Jugadores restantes: " + board.getHumans() + " humanos, " + board.getInfected() + " infectados.");
        }
    }

    private boolean isGameOver() {
        int humans = board.getHumans();
        int infected = board.getInfected();
        return (humans == 0 || infected == 0);
    }

    public void endGame() {
        // Guardar estado final del continente
        Type continentType = new TypeToken<Continent>(){}.getType();
        FilesJson<Continent> filesJsonContinent = new FilesJson<>(continentType);
        String fileNameFinal = "Transmisión Final - " + board.getContinentName() + ".json";
        ArrayList<Continent> finalList = new ArrayList<>();
        finalList.add(board);
        filesJsonContinent.writeList(fileNameFinal, finalList);

        // Guardar acontecimientos
        Type eventType = new TypeToken<Event>(){}.getType();
        FilesJson<Event> filesJsonEvent = new FilesJson<>(eventType);
        String fileNameEvents = "Acontecimientos - " + board.getContinentName() + ".json";
        filesJsonEvent.writeList(fileNameEvents, eventHistory);

        System.out.println("El juego ha terminado. Estado final y acontecimientos guardados.");
    }

    public void killPlayer() {
        board.getPlayerList().removeIf(player -> player.getState() == 'H');
        // Actualiza el tablero con las listas actuales
        board.updateBoard(board.getPlayerList(), board.getItemList());
        System.out.println("Humanos exterminados: " + board.getHumans());
        endGame();
    }
    
    public void killInfection() {
        board.getPlayerList().removeIf(player -> player.getState() == 'I');
        // Actualiza el tablero con las listas actuales
        board.updateBoard(board.getPlayerList(), board.getItemList());
        System.out.println("Infectados exterminados: " + board.getInfected());
        endGame();
    }

    public Continent getBoard() {
        return board;
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
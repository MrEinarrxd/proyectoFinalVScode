package domain;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private String[][] board;
    private ArrayList<Player> playerList;
    private ArrayList<Item> itemList;

    public Board(int seed) {
        setBoardCase(seed);
    }
    public String[][] getBoard() {
        return board;
    }

    private int[] getRandomPosition() {
        int x = (int) (Math.random() * board.length);
        int y = (int) (Math.random() * board[0].length);
        return new int[] { x, y };
    }

    public void updateBoard(ArrayList<Player> players, ArrayList<Item> items) {
        for (Player player : players) {
            int[] pos = player.getCoordenates();
            this.board[pos[0]][pos[1]] = String.valueOf(player.getState());
        }
        for (Item item : items) {
            int[] pos = item.getCoordinates();
            this.board[pos[0]][pos[1]] = String.valueOf(item.getType());
        }
    }

    public void setOnBoard(int[] pos, String type) {
        pos = validPosition(pos);
        this.board[pos[0]][pos[1]] += type;
    }

    public void cleanPosition(int[] pos, char type) {
        pos = validPosition(pos);
        String currentCell = this.board[pos[0]][pos[1]];
        if (currentCell.contains(String.valueOf(type))) {
            this.board[pos[0]][pos[1]] = currentCell.replace(String.valueOf(type), "");
        }
    }

    public void initializeBoard() {
        for (Player player : playerList) {
            board[player.getCoordenates()[0]][player.getCoordenates()[1]] = String.valueOf(player.getState());
        }
    }

    public int[] validPosition(int[] pos) {
        int x = ((pos[0] % board.length) + board.length) % board.length;
        int y = ((pos[1] % board[0].length) + board[0].length) % board[0].length;
        return new int[] { x, y };
    }


    public String getOnBoard(int[] pos) {
        int[] validPos = validPosition(pos);
        return board[validPos[0]][validPos[1]];
    }


    public boolean validCoordinates(int[] pos, int caseType) {
        String cell = getOnBoard(pos);
        boolean valid = true;
        if (caseType == 0) { // movimiento de jugadores
            if (cell.contains("E")) valid = false;
        } else if (caseType == 1) { // creación de edificios
            if (!cell.equals(" ")) valid = false;
        }
        return valid;
    }

    public ArrayList<Player> getPlayersOnPosition(int[] pos) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getCoordenates()[0] == pos[0] && player.getCoordenates()[1] == pos[1]) {
                players.add(player);
            }
        }
        return players;
    }

    public void removePlayer(Player player) {
        playerList.remove(player);
        cleanPosition(player.getCoordenates(), player.getState());
    }
    
    private void setBoardCase(int seed) {
        int rows = 0;
        int cols = 0;
        int numPlayers = 0;
        int numpotsions = 0;
        int numBuilds = 0;
        int numInfected = 0;
        switch (seed) {
            case 0:
                rows = 10;
                cols = 10;
                numInfected = 10;
                numPlayers = 10 + numInfected;
                numpotsions = 5;
                numBuilds = 10 + numpotsions;
                break;
            case 1:
                rows = 15;
                cols = 15;
                numInfected = 15;
                numPlayers = 15 + numInfected;
                numpotsions = 10;
                numBuilds = 15 + numpotsions;
                break;
            case 2:
                rows = 20;
                cols = 20;
                numInfected = 20;
                numPlayers = 20 + numInfected;
                numpotsions = 15;
                numBuilds = 20 + numpotsions;
                break;
            default:
                rows = 10;
                cols = 10;
                numInfected = 10;
                numPlayers = 10 + numInfected;
                numpotsions = 5;
                numBuilds = 10 + numpotsions;
                break;
        }

        this.board = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = " ";
            }
        }

        this.playerList = new ArrayList<Player>(numPlayers);
        int playerId = 1;
        while (playerList.size() < numPlayers) {
            int[] position = getRandomPosition();
            do {
                position = getRandomPosition();
            } while (!validCoordinates(position,0));
            playerList.add(new Player("Player"+playerId, position));
                playerId++;
        }

        while (playerList.size() < numInfected + numPlayers) {
            int[] position = getRandomPosition();
            do {
                position = getRandomPosition();
            } while (!validCoordinates(position, 0));
            playerList.add(new Player("Infected"+playerId, position));
            playerList.getLast().setState('I');
            playerId++;
        }
    
        this.itemList = new ArrayList<Item>(numBuilds);
        while (itemList.size() < numBuilds) {
            int[] position = getRandomPosition();
         
            if (validCoordinates(position, 3)) {
                itemList.add(new Item(position, String.valueOf('E')));
                setOnBoard(position, "E");
            }
        }
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public Item getItem(int[] pos) {
        for (Item item : itemList) {
            if (item.getCoordinates()[0] == pos[0] && item.getCoordinates()[1] == pos[1]) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
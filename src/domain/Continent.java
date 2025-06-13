package domain;

import java.util.ArrayList;

public class Continent {
    private String continentName;
    private String continentChannel;
    private String continentSize;
    private int builds;
    private int infected;
    private int humans;
    private int potions;
    // Atributos
    private String[][] board;
    private ArrayList<Player> playerList;
    private ArrayList<Item> itemList;
    private int idPlayer = 0;
    private int idItem = 0;

    // Constructor
    public Continent(int seed) {
        this.playerList = new ArrayList<>();
        this.itemList = new ArrayList<>();

        switch (seed) {
            case 0: // Africa
                this.board = new String[10][10];
                this.continentSize = "10x10";
                this.continentName = "Africa";
                this.continentChannel = "18";
                builds = 10;
                infected = 10;
                humans = 10;
                potions = 5;
                initializeBoard(builds, infected, humans);
                updateBoard(playerList, itemList);
                break;
            case 1: // America
                this.board = new String[15][15];
                this.continentSize = "15x15";
                this.continentName = "America";
                this.continentChannel = "20";
                builds = 15;
                infected = 15;
                humans = 15;
                potions = 10;
                initializeBoard(builds, infected, humans);
                updateBoard(playerList, itemList);
                break;
            case 2: // Asia
                this.board = new String[20][20];
                this.continentSize = "20x20";
                this.continentName = "Asia";
                this.continentChannel = "25";
                builds = 20;
                infected = 20;
                humans = 20;
                potions = 15;
                initializeBoard(builds, infected, humans);
                updateBoard(playerList, itemList);
                break;
            default:
                break;
        }
    }

    // Métodos públicos de acceso (getters)
    public String[][] getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public Item getItem(int id) {
        for (Item item : itemList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayersOnPosition(int[] pos) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : playerList) {
            if (player.getCoordinates()[0] == pos[0] && player.getCoordinates()[1] == pos[1]) {
                players.add(player);
            }
        }
        return players;
    }

    public String getContinentName() {
        return continentName;
    }

    public String getContinentChannel() {
        return continentChannel;
    }

    public String getContinentSize() {
        return continentSize;
    }

    public int getBuilds() {
        return builds;
    }

    public int getInfected() {
        int updateInfectedCount = 0;
        for (Player player : playerList) {
            if (player.getState() == 'I') {
                updateInfectedCount++;
            }
        }
        this.infected = updateInfectedCount;
        return infected;
    }

    public int getHumans() {
        int updateHumansCount = 0;
        for (Player player : playerList) {
            if (player.getState() == 'H') {
                updateHumansCount++;
            }
        }
        this.humans = updateHumansCount;
        return humans;
    }

    public int getPotions() {
        int updatePotionsCount = 0;
        for (Item item : itemList) {
            if (item.getType().equals("P")) {
                updatePotionsCount++;
            }
        }
        this.potions = updatePotionsCount;
        return potions;
    }

    public int getIdPlayer() {
        return idPlayer;
    }

    public int getIdItem() {
        return idItem;
    }

    // Métodos públicos de modificación
    public void setOnBoard(int[] pos, String type) {
        this.board[pos[0]][pos[1]] = type;
    }

    public void updateBoard(ArrayList<Player> players, ArrayList<Item> items) {
        // Limpia el tablero antes de actualizar
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = " ";
            }
        }
        // Si los parámetros no son null, actualiza las listas internas
        if (players != null) {
            playerList = players;
        }
        if (items != null) {
            itemList = items;
        }
        // Escribe en el tablero usando las listas internas actuales
        for (Player player : playerList) {
            int[] pos = player.getCoordinates();
            setOnBoard(pos, String.valueOf(player.getState()));
        }
        for (Item item : itemList) {
            int[] pos = item.getCoordinates();
            setOnBoard(pos, item.getType());
        }
    }

    public boolean addPotion() {
        if (potions > 0) {
            int[] pos = getEmptyPosition();
            Item item = new Item(idItem++, pos, "P");
            itemList.add(item);
            setOnBoard(pos, "P");
            potions--;
            return true;
        }
        return false;
    }

    public void killBill(int target) { //por trabajar
        ArrayList<Player> players = getPlayerList();
        for (Player player : players) {
            if (target == 0 && player.getState() == 'I') {
                //killPlayer(player);
            } else if (target == 1 && player.getState() == 'H') {
                //killPlayer(player);
            }
        }
    }

    // Métodos privados de inicialización
    private void initializeBoard(int builds, int infected, int humans) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = " ";
            }
        }
        startPlayers(infected, humans);
        startItems(potions, builds);
    }

    private void startPlayers(int infected, int humans) {
        for (int i = 0; i < infected; i++) {
            int[] pos = getEmptyPosition();
            Player player = new Player(idPlayer++, pos, 'I');
            playerList.add(player);
        }
        for (int i = 0; i < humans; i++) {
            int[] pos = getEmptyPosition();
            Player player = new Player(idPlayer++, pos, 'H');
            playerList.add(player);
        }
    }

    private void startItems(int potions, int builds) {
        for (int i = 0; i < builds; i++) {
            int[] pos = getEmptyPosition();
            Item item = new Item(idItem++, pos, "B");
            itemList.add(item);
        }
    }

    private int[] getEmptyPosition() {
        int x, y;
        do {
            x = (int) (Math.random() * board.length);
            y = (int) (Math.random() * board[0].length);
        } while (!board[x][y].equals(" "));
        return new int[] { x, y };
    }
}
package business;

import java.util.ArrayList;

import domain.Continent;
import domain.Event;
import domain.Item;
import domain.Player;

public class HandleEvent {
    // Atributos
    private int id;

    // Constructor
    public HandleEvent() {
        this.id = 0;
    }

    // Agrupa jugadores por posición
    public ArrayList<ArrayList<Player>> agroupByPosition(ArrayList<Player> players) {
        ArrayList<ArrayList<Player>> agrouped = new ArrayList<>();
        ArrayList<int[]> locations = new ArrayList<>();

        for (Player player : players) {
            int[] pos = player.getCoordinates();
            boolean found = false;

            for (int i = 0; i < locations.size(); i++) {
                int[] posExistente = locations.get(i);
                if (pos[0] == posExistente[0] && pos[1] == posExistente[1]) {
                    agrouped.get(i).add(player);
                    found = true;
                    break;
                }
            }
            if (!found) {
                locations.add(pos);
                ArrayList<Player> nuevoGrupo = new ArrayList<>();
                nuevoGrupo.add(player);
                agrouped.add(nuevoGrupo);
            }
        }
        return agrouped;
    }

    // Maneja los jugadores según el tipo de evento
    public ArrayList<Player> handlePlayers(ArrayList<Player> players, int caseType, ArrayList<Item> items) {
        ArrayList<Player> handledPlayers = new ArrayList<>(players);
        switch (caseType) {
            case 1: // Virus vs Humano: Infección
                for (Player p : handledPlayers) {
                    if (p.getState() == 'H') {
                        p.setState('I');
                        System.out.println("Humano infectado por Virus");
                    }
                }
                break;
            case 2: // Virus vs Virus: Saludo
                System.out.println("Virus saluda a virus");
                break;
            case 3: // Virus vs Virus vs Humano: Pelea
                ArrayList<Player> toRemove3 = new ArrayList<>();
                for (Player p : handledPlayers) {
                    if (p.getState() == 'H') {
                        toRemove3.add(p);
                    }
                }
                System.out.println("Humano muere en pelea entre Virus");
                handledPlayers.removeAll(toRemove3);
                break;
            case 4: // Humano vs Humano vs Virus: Pelea
                ArrayList<Player> toRemove4 = new ArrayList<>();
                for (Player p : handledPlayers) {
                    if (p.getState() == 'I') {
                        toRemove4.add(p);
                    }
                }
                System.out.println("Virus muere en pelea entre Humanos");
                handledPlayers.removeAll(toRemove4);
                break;
            case 5: // Humano vs Humano: Saludo
                System.out.println("Humanos se saludan");
                break;
            case 6: // Virus vs Antídoto: Curación
                for (Player p : handledPlayers) {
                    if (p.getState() == 'I') {
                        p.setState('H');
                        handleItems(items, p.getCoordinates());
                    }
                }
                System.out.println("Virus se cura con antídoto y pasa a Humano");
                break;
            case 7: // Humano vs Antídoto: Tomar
                for (Player p : handledPlayers) {
                    if (p.getState() == 'H') {
                        System.out.println("Humano toma antídoto y se cura");
                        handleItems(items, p.getCoordinates());

                    }
                }
                break;
            default:
                break;
        }
        return handledPlayers;
    }

    // Maneja los ítems según el tipo de evento
    public ArrayList<Item> handleItems(ArrayList<Item> items, int[] coordinates) {
        ArrayList<Item> handledItems = new ArrayList<>(items);
        switch (id) {
            case 6: // Virus vs Antídoto: Curación
                for (Item item : handledItems) {
                    if (item.getCoordinates() == coordinates && item.getType().equals("P")) {
                        System.out.println("Antídoto usado, eliminando del tablero");
                        handledItems.remove(item);
                        break; // Solo se elimina un antídoto por evento
                    }
                }
                break;
            case 7: // Humano vs Antídoto: Tomar
                for (Item item : handledItems) {
                    if (item.getCoordinates() == coordinates && item.getType().equals("P")) {
                        System.out.println("Antídoto usado, eliminando del tablero");
                        handledItems.remove(item);
                        break; // Solo se elimina un antídoto por evento
                    }
                }
                break;
            default:
                System.out.println("No se maneja ítem en este caso");
        }
        return handledItems;
    }

    // Determina el tipo de evento según los jugadores e ítems presentes
    private int getCase(ArrayList<Player> players, ArrayList<Item> items) {
        int virusCount = 0;
        int humanCount = 0;
        boolean hasAntidote = false;
    
        for (Player p : players) {
            if (p.getState() == 'I') virusCount++;
            if (p.getState() == 'H') humanCount++;

            for (Item item : items) {
                if (item.getType().equals("P") && item.getCoordinates()[0] == p.getCoordinates()[0] && item.getCoordinates()[1] == p.getCoordinates()[1]) {
                    hasAntidote = true;
                }
            }
        }
    
        // 1. Virus vs Humano: Infección
        if (virusCount == 1 && humanCount == 1 && !hasAntidote) return 1;
        // 2. Virus vs Virus: Saludo
        if (virusCount > 1 && humanCount == 0 && !hasAntidote) return 2;
        // 3. Virus vs Virus vs Humano: Muere Humano
        if (virusCount > 1 && humanCount == 1 && !hasAntidote) return 3;
        // 4. Humano vs Humano vs Virus: Muere Virus
        if (humanCount > 1 && virusCount == 1 && !hasAntidote) return 4;
        // 5. Humano vs Humano: Saludo
        if (humanCount > 1 && virusCount == 0 && !hasAntidote) return 5;
        // 6. Virus vs Antídoto: Curación
        if (virusCount >= 1 && humanCount == 0 && hasAntidote) return 6;
        // 7. Humano vs Antídoto: Tomar
        if (humanCount >= 1 && virusCount == 0 && hasAntidote) return 7;
    
        // Si no hay colisión relevante
        return 0;
    }

    // Crea un evento según los jugadores e ítems presentes
    public Event createEvent(ArrayList<Player> players, ArrayList<Item> items, int[] coordinates) {
        String collision = "";
        String eventEfect = "";
        String eventResult = "";
        ArrayList<Player> handledPlayers = new ArrayList<>();
        ArrayList<Item> handledItems = new ArrayList<>();
    
        int caseType = getCase(players, items);
    
        switch (caseType) {
            case 1:
                collision = "Virus vs Humano";
                eventEfect = "Infección";
                eventResult = "Humano se transforma en Virus";
                handledPlayers = handlePlayers(players, 1, items);
                break;
            case 2:
                collision = "Virus vs Virus";
                eventEfect = "Saludo";
                eventResult = "Continúan";
                handledPlayers = handlePlayers(players, 2, items);
                break;
            case 3:
                collision = "Virus vs Virus vs Humano";
                eventEfect = "Pelea";
                eventResult = "Muere Humano";
                handledPlayers = handlePlayers(players, 3, items);
                break;
            case 4:
                collision = "Humano vs Humano vs Virus";
                eventEfect = "Pelea";
                eventResult = "Muere Virus";
                handledPlayers = handlePlayers(players, 4, items);
                break;
            case 5:
                collision = "Humano vs Humano";
                eventEfect = "Saludo";
                eventResult = "Continúan";
                handledPlayers = handlePlayers(players, 5, items);
                break;
            case 6:
                collision = "Virus vs Antídoto";
                eventEfect = "Curación";
                eventResult = "Virus se cura y pasa a Humano";
                handledPlayers = handlePlayers(players, 6, items);
                break;
            case 7:
                collision = "Humano vs Antídoto";
                eventEfect = "Tomar";
                eventResult = "Continúa el Humano";
                handledPlayers = handlePlayers(players, 7, items);
                break;
            default:
                System.out.println("Colision no contemplado en la tabla: Jugadores=" + players.size() + ", Items=" + items.size());
                return null; // No hay evento relevante  y retorna un evento nulo
        }

        return new Event(id++, coordinates, collision, eventEfect, eventResult, handledPlayers, handledItems);
    }

    // Obtiene todos los eventos del tablero
    public ArrayList<Event> getEvents(Continent board) {
        ArrayList<Player> players = board.getPlayerList();
        ArrayList<Item> items = board.getItemList();
        ArrayList<ArrayList<Player>> groupedPlayers = agroupByPosition(players);
        ArrayList<Event> events = new ArrayList<>();
    
        for (ArrayList<Player> group : groupedPlayers) {
            int[] pos = group.get(0).getCoordinates();
            ArrayList<Item> itemsEnPos = new ArrayList<>();
            for (Item item : items) {
                if (item.getCoordinates()[0] == pos[0] && item.getCoordinates()[1] == pos[1]) {
                    itemsEnPos.add(item);
                }
            }
    
            int caseType = getCase(group, itemsEnPos);
            if (caseType > 0) {
                Event event = createEvent(group, itemsEnPos, pos);
                events.add(event);
            }
        }
    
        return events;
    }

    // Actualiza el tablero según los eventos
    public Continent getResultBoard(Continent board) {
        ArrayList<Player> players = board.getPlayerList();
        ArrayList<Item> items = board.getItemList();
        ArrayList<ArrayList<Player>> groupedPlayers = agroupByPosition(players);

        ArrayList<Player> updatedPlayers = new ArrayList<>(players);
        ArrayList<Item> updatedItems = new ArrayList<>(items);

        for (ArrayList<Player> group : groupedPlayers) {
            int[] pos = group.get(0).getCoordinates();
            ArrayList<Item> itemsEnPos = new ArrayList<>();
            for (Item item : updatedItems) {
                if (item.getCoordinates()[0] == pos[0] && item.getCoordinates()[1] == pos[1]) {
                    itemsEnPos.add(item);
                }
            }

            int caseType = getCase(group, itemsEnPos);

            if (caseType != -1) {
                ArrayList<Player> handledPlayers = handlePlayers(new ArrayList<>(group), caseType, updatedItems);

                for (Player p : group) {
                    updatedPlayers.remove(p);
                }
                for (Player p : handledPlayers) {
                    if (!updatedPlayers.contains(p)) {
                        updatedPlayers.add(p);
                    }
                }

                ArrayList<Item> itemsToRemove = new ArrayList<>();
                if (caseType == 6 || caseType == 7) {
                    for (Item item : itemsEnPos) {
                        if (item.getType().equals("P")) {
                            itemsToRemove.add(item);
                        }
                    }
                }
                updatedItems.removeAll(itemsToRemove);
            } else {
                System.out.println("Evento no contemplado en la tabla: Jugadores=" + group.size() + ", Items=" + itemsEnPos.size());
            }
        }

        board.updateBoard(updatedPlayers, updatedItems);
        return board;
    }
}

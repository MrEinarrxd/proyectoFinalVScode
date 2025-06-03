package business;

import java.util.ArrayList;

import domain.Board;
import domain.Event;
import domain.Item;
import domain.Player;

public class HandleEvent {
    private int id;

    public HandleEvent() {
        this.id = 0;
    }

    public Board getResultBoard(Board board) {
        ArrayList<Player> players = board.getPlayerList();
        ArrayList<Item> items = board.getItemList();
        ArrayList<ArrayList<Player>> groupedPlayers = agruparPorPosicion(players);

        // Copias para modificar sin afectar la iteración original
        ArrayList<Player> updatedPlayers = new ArrayList<>(players);
        ArrayList<Item> updatedItems = new ArrayList<>(items);

        for (ArrayList<Player> group : groupedPlayers) {
            // Encuentra los ítems en la misma posición que el grupo
            int[] pos = group.get(0).getCoordenates();
            ArrayList<Item> itemsEnPos = new ArrayList<>();
            for (Item item : updatedItems) {
                if (item.getCoordenates()[0] == pos[0] && item.getCoordenates()[1] == pos[1]) {
                    itemsEnPos.add(item);
                }
            }

            int caseType = getCase(group, itemsEnPos);

            // Aplica cambios a los jugadores del grupo
            ArrayList<Player> handledPlayers = handlePlayers(new ArrayList<>(group), caseType);

            // Elimina todos los jugadores del grupo original de updatedPlayers
            for (Player p : group) {
                updatedPlayers.remove(p);
            }
            // Solo agrega los jugadores que siguen existiendo (no duplicados)
            for (Player p : handledPlayers) {
                if (!updatedPlayers.contains(p)) {
                    updatedPlayers.add(p);
                }
            }

            // Aplica cambios a los ítems en la posición
            ArrayList<Item> itemsToRemove = new ArrayList<>();
            if (caseType == 6 || caseType == 7) {
                for (Item item : itemsEnPos) {
                    if (item.getType().equals("C")) {
                        itemsToRemove.add(item);
                    }
                }
            }
            updatedItems.removeAll(itemsToRemove);
        }

        board.updateBoard(updatedPlayers, updatedItems);
        return board;
    }

    public ArrayList<Event> getEvents(Board board) {
        ArrayList<Player> players = board.getPlayerList();
        ArrayList<Item> items = board.getItemList();
        ArrayList<ArrayList<Player>> groupedPlayers = agruparPorPosicion(players);
        ArrayList<Event> events = new ArrayList<>();

        for (ArrayList<Player> group : groupedPlayers) {
            // Filtrar ítems en la misma posición que el grupo
            int[] pos = group.get(0).getCoordenates();
            ArrayList<Item> itemsEnPos = new ArrayList<>();
            for (Item item : items) {
                if (item.getCoordenates()[0] == pos[0] && item.getCoordenates()[1] == pos[1]) {
                    itemsEnPos.add(item);
                }
            }

            int caseType = getCase(group, itemsEnPos);
            if (caseType != 0) {
                Event event = createEvent(group, itemsEnPos, pos);
                events.add(event);
            }
        }

        return events;
    }

    public ArrayList<ArrayList<Player>> agruparPorPosicion(ArrayList<Player> players) {
        ArrayList<ArrayList<Player>> agrupados = new ArrayList<>();
        ArrayList<int[]> posiciones = new ArrayList<>();
    
        for (Player player : players) {
            int[] pos = player.getCoordenates();
            boolean found = false;
            for (int i = 0; i < posiciones.size(); i++) {
                int[] posExistente = posiciones.get(i);
                if (pos[0] == posExistente[0] && pos[1] == posExistente[1]) {
                    agrupados.get(i).add(player);
                    found = true;
                    break;
                }
            }
            if (!found) {
                posiciones.add(pos);
                ArrayList<Player> nuevoGrupo = new ArrayList<>();
                nuevoGrupo.add(player);
                agrupados.add(nuevoGrupo);
            }
        }
        return agrupados;
    }

    public Event createEvent(ArrayList<Player> players, ArrayList<Item> items, int[] coordinates) {
    String collision = "";
    String eventEfect = "";
    String eventResult = "";
    ArrayList<Player> handledPlayers;
    ArrayList<Item> handledItems;

    // Aquí se define el evento según el caso
    switch (getCase(players, items)) {
        case 1: // Virus vs Humano: Infección
            collision = "Virus vs Humano";
            eventEfect = "Infección";
            eventResult = "Humano se transforma en Virus";
            handledPlayers = handlePlayers(players, 1);
            handledItems = handleItems(items, 1);
            break;
        case 2: // Virus vs Virus: Saludo
            collision = "Virus vs Virus";
            eventEfect = "Saludo";
            eventResult = "Continúan";
            handledPlayers = handlePlayers(players, 2);
            handledItems = handleItems(items, 2);
            break;
        case 3: // Virus vs Virus vs Humano: Pelea
            collision = "Virus vs Virus vs Humano";
            eventEfect = "Pelea";
            eventResult = "Muere Humano";
            handledPlayers = handlePlayers(players, 3);
            handledItems = handleItems(items, 3);
            break;
        case 4: // Humano vs Humano vs Virus: Pelea
            collision = "Humano vs Humano vs Virus";
            eventEfect = "Pelea";
            eventResult = "Muere Virus";
            handledPlayers = handlePlayers(players, 4);
            handledItems = handleItems(items, 4);
            break;
        case 5: // Humano vs Humano: Saludo
            collision = "Humano vs Humano";
            eventEfect = "Saludo";
            eventResult = "Continúan";
            handledPlayers = handlePlayers(players, 5);
            handledItems = handleItems(items, 5);
            break;
        case 6: // Virus vs Antídoto: Curación
            collision = "Virus vs Antídoto";
            eventEfect = "Curación";
            eventResult = "Virus se cura y pasa a Humano";
            handledPlayers = handlePlayers(players, 6);
            handledItems = handleItems(items, 6);
            break;
        case 7: // Humano vs Antídoto: Tomar
            collision = "Humano vs Antídoto";
            eventEfect = "Tomar";
            eventResult = "Continúa el Humano";
            handledPlayers = handlePlayers(players, 7);
            handledItems = handleItems(items, 7);
            break;
        default:
            collision = "No hay interacción relevante";
            eventEfect = "Ninguno";
            eventResult = "Continúan sin cambios";
            handledPlayers = new ArrayList<>(players);
            handledItems = new ArrayList<>(items);
            break;
    }

    return new Event(id++, coordinates, collision, eventEfect, eventResult, handledPlayers, handledItems);
    }

    public ArrayList<Player> handlePlayers(ArrayList<Player> players, int caseType) {
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
                    }
                }
                System.out.println("Virus se cura con antídoto y pasa a Humano");
                break;
            case 7: // Humano vs Antídoto: Tomar
                for (Player p : handledPlayers) {
                    if (p.getState() == 'H') {
                        System.out.println("Humano toma antídoto y se cura");
                    }
                }
                // No se cambia el estado del Humano, solo se registra que tomó el antídoto
                System.out.println("Humano toma antídoto");
                break;
            default:
                break;
        }
        return handledPlayers;
    }

    public ArrayList<Item> handleItems(ArrayList<Item> items, int caseType) {
        ArrayList<Item> handledItems = new ArrayList<>(items);
        switch (caseType) {
            case 6: // Virus vs Antídoto: Curación
            case 7: // Humano vs Antídoto: Tomar
                ArrayList<Item> toRemove = new ArrayList<>();
                for (Item item : handledItems) {
                    if (item.getType().equals("C")) {
                        toRemove.add(item);
                    }
                }
                handledItems.removeAll(toRemove);
                break;
            default:
                break;
        }
        return handledItems;
    }

    private int getCase(ArrayList<Player> players, ArrayList<Item> items) {
        int caseType = 0;
        boolean hasVirus = false;
        boolean hasHuman = false;
        boolean hasAntidote = false;
        int virusCount = 0;
        int humanCount = 0;

        for (Player player : players) {
            if (player.getState() == 'I') {
                hasVirus = true;
                virusCount++;
            } else if (player.getState() == 'H') {
                hasHuman = true;
                humanCount++;
            }
        }
        for (Item item : items) {
            if (item.getType().equals("C")) {
                hasAntidote = true;
            }
        }
        if (hasVirus && hasHuman && virusCount == 1 && humanCount == 1) {
            caseType = 1; // Virus vs Humano
        } else if (hasVirus && !hasHuman && virusCount > 1) {
            caseType = 2; // Virus vs Virus
        } else if (hasVirus && hasHuman && virusCount > 1 && humanCount == 1) {
            caseType = 3; // Virus vs Virus vs Humano
        } else if (hasHuman && !hasVirus && humanCount > 1) {
            caseType = 5; // Humano vs Humano
        } else if (hasVirus && hasAntidote) {
            caseType = 6; // Virus vs Antídoto
        } else if (hasHuman && hasAntidote) {
            caseType = 7; // Humano vs Antídoto
        }
        // caseType = 0: No hay interacción relevante
        return caseType;
    }

}

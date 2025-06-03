package domain;

import java.util.ArrayList;

public class Event {
    private int id;
    private int[] coordinates;
    private String collition;
    private String eventEfect;
    private String eventResult;
    private ArrayList<Player> players;
    private ArrayList<Item> items;

    // Agrega campos para la tabla
    private String date;
    private String type;
    private String description;

    public Event(int id, int[] coordinates, String collition, String eventEfect, String eventResult, ArrayList<Player> players, ArrayList<Item> items) {
        this.id = id;
        this.coordinates = coordinates;
        this.collition = collition;
        this.eventEfect = eventEfect;
        this.eventResult = eventResult;
        this.players = players;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    // Cambiar el nombre del getter para que coincida con el campo y PropertyValueFactory
    public String getCollition() {
        return collition;
    }

    public String getEventEfect() {
        return eventEfect;
    }
    public String getEventResult() {
        return eventResult;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public String getEventToString(){
        return collition + "-" + eventEfect + "-" + eventResult;
    }

}
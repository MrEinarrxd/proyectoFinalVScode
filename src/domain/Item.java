package domain;

public class Item {
	private int[] coordenates = new int[2];
	private String type = " ";
	
	public Item(int[] coordenates, String type) {
		this.coordenates = coordenates;
		this.type = type;
	}
	public int[] getCoordenates() {
		return coordenates;
	}
	public void setCoordenates(int[] coordenates) {
		this.coordenates = coordenates;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return "Item [coordenates=" + coordenates[0] + "," + coordenates[1] + ", type=" + type + "]";
	}

	public int[] getCoordinates() {
		return coordenates;
	}
}

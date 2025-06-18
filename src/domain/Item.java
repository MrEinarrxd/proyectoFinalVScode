package domain;

public class Item {
	private int id;
	private int[] coordenates = new int[2];
	private String type = " ";

	public Item(int id, int[] coordenates, String type) {
		this.id = id;
		this.coordenates = coordenates;
		this.type = type;
	}

	public void setCoordinates(int[] coordenates) {
		this.coordenates = coordenates;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int[] getCoordinates() {
		return coordenates;
	}
}
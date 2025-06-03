package domain;

public class Player {
	private String id;
	private char state = 'H';
	private int[] coordenates = new int[2];

	public Player(String id, int[] coordenates) {
		this.id = id;
		this.coordenates = coordenates;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	public int[] getCoordenates() {
		return coordenates;
	}


	public void setCoordenates(int[] coordenates) {
		this.coordenates = coordenates;
	}

	public void move() {
		int dir = (int) (Math.random() * 4);
		switch (dir) {
			case 0: // derecha
				coordenates[1] += 1;
				break;
			case 1: // izquierda
				coordenates[1] -= 1;
				break;
			case 2: // arriba
				coordenates[0] -= 1;
				break;
			case 3: // abajo
				coordenates[0] += 1;
				break;
		}
	}
}
